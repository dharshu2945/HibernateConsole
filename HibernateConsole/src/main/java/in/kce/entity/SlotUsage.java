package in.kce.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Slot_usage")
public class SlotUsage {
    @Id
    @SequenceGenerator(name = "usage_seq", sequenceName = "SLOTUSAGE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usage_seq")
    @Column(name="USAGEID", nullable=false)
    private int usageID;
    @Column(name = "BOOKINGID", nullable = false, length = 20)
    private String bookingID;
    @Column(name = "ACTUALSPORTPLAYED", nullable = false, length = 30)
    private String actualSportPlayed;
    @Column(name = "ACTUALPLAYERSCOUNT", nullable = false)
    private int actualPlayersCount;
    @Column(name = "ACTUALSTARTTIME", nullable = false)
    private Timestamp actualStartTime;
    @Column(name = "ACTUALENDTIME", nullable = false)
    private Timestamp actualEndTime;
    @Column(name = "OVERTIMEMINUTES", nullable = false)
    private int overtimeMinutes;
    @Column(name = "OVERTIMECHARGEAMOUNT", nullable = false)
    private double overtimeChargeAmount;
    @Column(name = "GROUNDCONDITIONRATING", nullable = false, length = 20)
    private String groundConditionRating;
    @Column(name = "CLEANLINESSRATING", nullable = false, length = 20)
    private String cleanlinessRating;
    @Column(name = "CARETAKERREMARKS")
    private String caretakerRemarks;
    @Column(name = "USAGESTATUS", nullable = false, length = 20)
    private String usageStatus;
	public int getUsageID() {
		return usageID;
	}
	public void setUsageID(int usageID) {
		this.usageID = usageID;
	}
	public String getBookingID() {
		return bookingID;
	}
	public void setBookingID(String bookingID) {
		this.bookingID = bookingID;
	}
	public String getActualSportPlayed() {
		return actualSportPlayed;
	}
	public void setActualSportPlayed(String actualSportPlayed) {
		this.actualSportPlayed = actualSportPlayed;
	}
	public int getActualPlayersCount() {
		return actualPlayersCount;
	}
	public void setActualPlayersCount(int actualPlayersCount) {
		this.actualPlayersCount = actualPlayersCount;
	}
	public java.sql.Timestamp getActualStartTime() {
		return actualStartTime;
	}
	public void setActualStartTime(java.sql.Timestamp actualStartTime) {
		this.actualStartTime = actualStartTime;
	}
	public java.sql.Timestamp getActualEndTime() {
		return actualEndTime;
	}
	public void setActualEndTime(java.sql.Timestamp actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	public int getOvertimeMinutes() {
		return overtimeMinutes;
	}
	public void setOvertimeMinutes(int overtimeMinutes) {
		this.overtimeMinutes = overtimeMinutes;
	}
	public double getOvertimeChargeAmount() {
		return overtimeChargeAmount;
	}
	public void setOvertimeChargeAmount(double overtimeChargeAmount) {
		this.overtimeChargeAmount = overtimeChargeAmount;
	}
	public String getGroundConditionRating() {
		return groundConditionRating;
	}
	public void setGroundConditionRating(String groundConditionRating) {
		this.groundConditionRating = groundConditionRating;
	}
	public String getCleanlinessRating() {
		return cleanlinessRating;
	}
	public void setCleanlinessRating(String cleanlinessRating) {
		this.cleanlinessRating = cleanlinessRating;
	}
	public String getCaretakerRemarks() {
		return caretakerRemarks;
	}
	public void setCaretakerRemarks(String caretakerRemarks) {
		this.caretakerRemarks = caretakerRemarks;
	}
	public String getUsageStatus() {
		return usageStatus;
	}
	public void setUsageStatus(String usageStatus) {
		this.usageStatus = usageStatus;
	}
	@Override
	public String toString() {
		return "SlotUsage [usageID=" + usageID + ", bookingID=" + bookingID + ", actualSportPlayed=" + actualSportPlayed
				+ ", actualPlayersCount=" + actualPlayersCount + ", actualStartTime=" + actualStartTime
				+ ", actualEndTime=" + actualEndTime + ", overtimeMinutes=" + overtimeMinutes
				+ ", overtimeChargeAmount=" + overtimeChargeAmount + ", groundConditionRating=" + groundConditionRating
				+ ", cleanlinessRating=" + cleanlinessRating + ", caretakerRemarks=" + caretakerRemarks
				+ ", usageStatus=" + usageStatus + "]";
	}

}
