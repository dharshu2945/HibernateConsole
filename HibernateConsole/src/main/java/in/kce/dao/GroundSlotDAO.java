package in.kce.dao;

import in.kce.entity.GroundSlot;
import in.kce.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;
public class GroundSlotDAO {
    public GroundSlot findBooking(String bookingID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(GroundSlot.class, bookingID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getNextBookingId(Session session) {
        Number n = (Number) session.createNativeQuery("select BOOKING_SEQ.nextval from dual").getSingleResult();
        long val = n.longValue();
        return String.format("GB%04d", val);
    }
    public boolean insertBooking(GroundSlot slot) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
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
    public boolean updateBookingStatusAndCancellation(String bookingID, String newStatus, String cancellationReason) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            GroundSlot slot = session.get(GroundSlot.class, bookingID);
            if (slot == null) return false;
            slot.setBookingStatus(newStatus);
            slot.setCancellationReason(cancellationReason);
            session.flush();
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
    public List<GroundSlot> viewBookingsByDateAndStatus(java.sql.Date bookingDate, String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from GroundSlot where bookingDate = :date and bookingStatus = :status",
                            GroundSlot.class
                    )
                    .setParameter("date", bookingDate)
                    .setParameter("status", status)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }
    public List<GroundSlot> findConfirmedBookingsForDateAndSlot(Date bookingDate, String timeSlot) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from GroundSlot where bookingDate = :date and timeSlot = :slot and bookingStatus = 'CONFIRMED'",
                            GroundSlot.class
                    )
                    .setParameter("date", bookingDate)
                    .setParameter("slot", timeSlot)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }
}