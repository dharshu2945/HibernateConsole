package in.kce.service;

import in.kce.dao.GroundSlotDAO;
import in.kce.dao.SlotUsageDAO;
import in.kce.entity.GroundSlot;
import in.kce.entity.SlotUsage;
import in.kce.util.HibernateUtil;
import in.kce.util.SlotOverlapException;
import in.kce.util.UsageAlreadyExistsException;
import in.kce.util.ValidationException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.sql.Timestamp;
import java.util.List;
public class GroundService {
    private final GroundSlotDAO d = new GroundSlotDAO();
    private final SlotUsageDAO s = new SlotUsageDAO();
    public GroundSlot findBooking(String bookingID) {
        if (bookingID == null || bookingID.isEmpty()) return null;
        return d.findBooking(bookingID);
    }
    public List<GroundSlot> viewBookingsByDateAndStatus(java.sql.Date bookingDate, String status) {
        List<GroundSlot> list = d.viewBookingsByDateAndStatus(bookingDate, status);
        return (list == null) ? java.util.Collections.emptyList() : list;
    }
    public boolean registerNewBooking(GroundSlot slot,
                                      double residentMultiplier,
                                      double guestMultiplier,
                                      double externalMultiplier) throws ValidationException {
        if (slot == null ||
                slot.getTimeSlot() == null || slot.getTimeSlot().isEmpty() ||
                slot.getTeamName() == null || slot.getTeamName().isEmpty() ||
                slot.getContactPersonName() == null || slot.getContactPersonName().isEmpty() ||
                slot.getContactMobile() == null || slot.getContactMobile().isEmpty() ||
                slot.getUserType() == null || slot.getUserType().isEmpty() ||
                slot.getPlannedSport() == null || slot.getPlannedSport().isEmpty() ||
                slot.getBookingDate() == null) {
            throw new ValidationException();
        }
        if (slot.getEstimatedPlayersCount() <= 0) throw new ValidationException();
        if (slot.getBaseSlotFee() < 0) throw new ValidationException();
        double multiplier;
        switch (slot.getUserType()) {
            case "RESIDENT": multiplier = residentMultiplier; break;
            case "GUEST_OF_RESIDENT": multiplier = guestMultiplier; break;
            case "EXTERNAL_TEAM": multiplier = externalMultiplier; break;
            default: throw new ValidationException();
        }
        slot.setCalculatedBookingAmount(slot.getBaseSlotFee() * multiplier);
        if (slot.getBookingStatus() == null || slot.getBookingStatus().isEmpty()) {
            slot.setBookingStatus("PENDING");
        }
        if (slot.getCreatedTimestamp() == null) {
            slot.setCreatedTimestamp(new Timestamp(System.currentTimeMillis())); // ✅ auto set
        }
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            if (slot.getBookingID() == null || slot.getBookingID().isEmpty()) {
                slot.setBookingID(d.getNextBookingId(session)); // ✅ GBxxxx
            }
            session.persist(slot);
            session.flush();
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
    public boolean confirmBooking(String bookingID) throws ValidationException, SlotOverlapException {
        if (bookingID == null || bookingID.isEmpty()) throw new ValidationException();
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            GroundSlot booking = session.get(GroundSlot.class, bookingID);
            if (booking == null) return false;
            String status = booking.getBookingStatus();
            if ("CONFIRMED".equalsIgnoreCase(status)) {
                tx.commit();
                return true;
            }
            if (!"PENDING".equalsIgnoreCase(status)) throw new ValidationException();
            List<GroundSlot> overlapping = session.createQuery(
                            "from GroundSlot gs where gs.bookingDate = :date and gs.timeSlot = :slot and gs.bookingStatus = 'CONFIRMED'",
                            GroundSlot.class
                    )
                    .setParameter("date", booking.getBookingDate())
                    .setParameter("slot", booking.getTimeSlot())
                    .getResultList();
            if (!overlapping.isEmpty()) throw new SlotOverlapException();
            booking.setBookingStatus("CONFIRMED");
            booking.setCancellationReason(null);
            session.flush();
            tx.commit();
            return true;
        } catch (SlotOverlapException | ValidationException e) {
            if (tx != null) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
    public boolean cancelBooking(String bookingID, String reason) throws ValidationException {
        if (bookingID == null || bookingID.isEmpty() || reason == null || reason.isEmpty()) throw new ValidationException();
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            GroundSlot booking = session.get(GroundSlot.class, bookingID);
            if (booking == null) return false;
            String status = booking.getBookingStatus();
            if ("CANCELLED".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status)) throw new ValidationException();
            booking.setBookingStatus("CANCELLED");
            booking.setCancellationReason(reason);
            session.flush();
            tx.commit();
            return true;
        } catch (ValidationException e) {
            if (tx != null) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
    public boolean recordSlotUsage(String bookingID, String actualSport, int actualPlayers,
                                   java.sql.Timestamp startTime, java.sql.Timestamp endTime,
                                   int overtimeMinutes, double overtimeRatePerMinutes,
                                   String groundCondition, String cleanliness, String remarks)
            throws ValidationException, UsageAlreadyExistsException {
        if (bookingID == null || bookingID.isEmpty() ||
                actualSport == null || actualSport.isEmpty() ||
                startTime == null || endTime == null || endTime.before(startTime) ||
                groundCondition == null || groundCondition.isEmpty() ||
                cleanliness == null || cleanliness.isEmpty()) {
            throw new ValidationException();
        }
        if (actualPlayers < 0 || overtimeMinutes < 0 || overtimeRatePerMinutes < 0) throw new ValidationException();
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            GroundSlot booking = session.get(GroundSlot.class, bookingID);
            if (booking == null) return false;
            if (!"CONFIRMED".equalsIgnoreCase(booking.getBookingStatus())) throw new ValidationException();
            SlotUsage existingUsage = session.createQuery(
                            "from SlotUsage where bookingID = :bid",
                            SlotUsage.class
                    ).setParameter("bid", bookingID)
                    .uniqueResult();
            if (existingUsage != null &&
                    ("SUBMITTED".equalsIgnoreCase(existingUsage.getUsageStatus()) ||
                            "ADJUSTED".equalsIgnoreCase(existingUsage.getUsageStatus()))) {
                throw new UsageAlreadyExistsException();
            }
            double overtimeChargeAmount = overtimeMinutes * overtimeRatePerMinutes;
            SlotUsage usage = new SlotUsage();
            usage.setBookingID(bookingID);
            usage.setActualSportPlayed(actualSport);
            usage.setActualPlayersCount(actualPlayers);
            usage.setActualStartTime(startTime);
            usage.setActualEndTime(endTime);
            usage.setUsageStatus("SUBMITTED");
            usage.setOvertimeMinutes(overtimeMinutes);
            usage.setOvertimeChargeAmount(overtimeChargeAmount);
            usage.setGroundConditionRating(groundCondition);
            usage.setCleanlinessRating(cleanliness);
            usage.setCaretakerRemarks(remarks);
            session.persist(usage);
            booking.setBookingStatus("COMPLETED");
            session.flush();
            tx.commit();
            return true;
        } catch (UsageAlreadyExistsException | ValidationException e) {
            if (tx != null) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
    public boolean adjustSlotUsage(int usageID, int newActualPlayers, int newOvertimeMinutes,
                                   double newOvertimeRatePerMinute, String newGroundCondition,
                                   String newCleanliness, String newRemarks) throws ValidationException {
        if (usageID <= 0 || newActualPlayers <= 0 || newOvertimeMinutes < 0 || newOvertimeRatePerMinute < 0)
            throw new ValidationException();
        if (newGroundCondition == null || newGroundCondition.isEmpty() ||
                newCleanliness == null || newCleanliness.isEmpty())
            throw new ValidationException();
        SlotUsage usage = s.findSlotUsageByUsageID(usageID);
        if (usage == null) 
        	return false;
        double overtimeChargeAmount = newOvertimeMinutes * newOvertimeRatePerMinute;
        usage.setActualPlayersCount(newActualPlayers);
        usage.setOvertimeMinutes(newOvertimeMinutes);
        usage.setOvertimeChargeAmount(overtimeChargeAmount);
        usage.setGroundConditionRating(newGroundCondition);
        usage.setCleanlinessRating(newCleanliness);
        usage.setCaretakerRemarks(newRemarks);
        usage.setUsageStatus("ADJUSTED");
        return s.updateSlotUsage(usage);
    }
    public List<SlotUsage> listUsageByDate(java.sql.Date bookingDate) {
        List<SlotUsage> list = s.findUsageByDate(bookingDate);
        return (list == null) ? java.util.Collections.emptyList() : list;
    }
    public List<SlotUsage> listUsageBySport(String sportName) {
        List<SlotUsage> list = s.findUsageBySport(sportName);
        return (list == null) ? java.util.Collections.emptyList() : list;
    }
}