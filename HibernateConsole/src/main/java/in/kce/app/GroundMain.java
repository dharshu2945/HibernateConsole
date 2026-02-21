package in.kce.app;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Scanner;
import in.kce.entity.GroundSlot;
import in.kce.service.GroundService;
import in.kce.util.ValidationException;
import in.kce.util.SlotOverlapException;
public class GroundMain {
    private static GroundService service = new GroundService();
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("--- Community Sports Ground Console ---");
        try {
            GroundSlot slot = new GroundSlot();
            slot.setBookingID("GB2014");
            slot.setBookingDate(new Date(System.currentTimeMillis()));
            slot.setTimeSlot("06:00-08:00");
            slot.setTeam_name("Sunrise FC");
            slot.setContactPersonName("Meenakshi Rao");
            slot.setContactMobile("9998887771");
            slot.setUserType("RESIDENT");
            slot.setPlannedSport("FOOTBALL");
            slot.setEstimatedPlayersCount(14);
            slot.setBaseSlotFee(300.0);
            slot.setBookingStatus("CONFIRMED");
            slot.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
            boolean ok = service.registerNewBooking(slot, 1.0, 1.3, 2.0);
            System.out.println(ok ? "BOOKING REGISTERED" 
                                  : "BOOKING REGISTRATION FAILED");
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.toString());
        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }
        try {
            boolean ok = service.confirmBooking("GB2004");
            System.out.println(ok ? "BOOKING CONFIRMATION FAILED"
                                  : "BOOKING CONFIRMED");
        } catch (SlotOverlapException e) {
            System.out.println("Overlap Error: " + e.toString());
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.toString());
        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }
        sc.close();
    }
}