package in.kce.dao;

import in.kce.entity.SlotUsage;
import in.kce.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
public class SlotUsageDAO {
    public boolean insertSlotUsage(SlotUsage usage) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(usage);
            session.flush();
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
    public SlotUsage findSlotUsageByUsageID(int usageID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(SlotUsage.class, usageID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean updateSlotUsage(SlotUsage usage) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(usage);
            session.flush();
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
    public List<SlotUsage> findUsageByDate(java.sql.Date bookingDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select su from SlotUsage su, GroundSlot gs " +
                                    "where su.bookingID = gs.bookingID and gs.bookingDate = :date",
                            SlotUsage.class
                    )
                    .setParameter("date", bookingDate)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }
    public List<SlotUsage> findUsageBySport(String sportName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from SlotUsage where actualSportPlayed = :sport",
                            SlotUsage.class
                    )
                    .setParameter("sport", sportName)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }
    public List<SlotUsage> findUsageByTeam(String teamName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select su from SlotUsage su, GroundSlot gs " +
                                    "where su.bookingID = gs.bookingID and gs.teamName = :team",
                            SlotUsage.class
                    )
                    .setParameter("team", teamName)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }
}