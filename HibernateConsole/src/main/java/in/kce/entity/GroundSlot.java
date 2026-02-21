package in.kce.entity;
import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "Ground_slot")
public class GroundSlot {
    @Id
    @Column(name = "BOOKINGID", nullable = false, length = 20)
    private String bookingID;   
    @Column(name = "BOOKINGDATE", nullable = false)
    private Date bookingDate;
    @Column(name = "TIMESLOT", nullable = false, length = 50)
    private String timeSlot;
    @Column(name = "TEAMNAME", nullable = false, length = 100)
    private String teamName;
    @Column(name = "CONTACTPERSONNAME", nullable = false, length = 100)
    private String contactPersonName;
    @Column(name = "CONTACTMOBILE", nullable = false, length = 20)
    private String contactMobile;
    @Column(name = "USERTYPE", nullable = false, length = 30)
    private String userType;
    @Column(name = "PLANNEDSPORT", nullable = false, length = 30)
    private String plannedSport;
    @Column(name = "ESTIMATEDPLAYERSCOUNT", nullable = false)
    private int estimatedPlayersCount;
    @Column(name = "BASESLOTFEE", nullable = false)
    private double baseSlotFee;
    @Column(name = "CALCULATEDBOOKINGAMOUNT", nullable = false)
    private double calculatedBookingAmount;
    @Column(name = "BOOKINGSTATUS", nullable = false, length = 20)
    private String bookingStatus;
    @Column(name = "CREATEDTIMESTAMP", nullable = false)
    private Timestamp createdTimestamp;
    @Column(name = "CANCELLATIONREASON") 
    private String cancellationReason;
	public String getBookingID() {
		return bookingID;
	}
	public void setBookingID(String bookingID) {
		this.bookingID = bookingID;
	}
	public java.sql.Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(java.sql.Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeam_name(String teamName) {
		this.teamName = teamName;
	}
	public String getContactPersonName() {
		return contactPersonName;
	}
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	public String getContactMobile() {
		return contactMobile;
	}
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getPlannedSport() {
		return plannedSport;
	}
	public void setPlannedSport(String plannedSport) {
		this.plannedSport = plannedSport;
	}
	public int getEstimatedPlayersCount() {
		return estimatedPlayersCount;
	}
	public void setEstimatedPlayersCount(int estimatedPlayersCount) {
		this.estimatedPlayersCount = estimatedPlayersCount;
	}
	public double getBaseSlotFee() {
		return baseSlotFee;
	}
	public void setBaseSlotFee(double baseSlotFee) {
		this.baseSlotFee = baseSlotFee;
	}
	public double getCalculatedBookingAmount() {
		return calculatedBookingAmount;
	}
	public void setCalculatedBookingAmount(double calculatedBookingAmount) {
		this.calculatedBookingAmount = calculatedBookingAmount;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public java.sql.Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(java.sql.Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public String getCancellationReason() {
		return cancellationReason;
	}
	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}
	@Override
	public String toString() {
		return "GroundSlot [bookingID=" + bookingID + ", bookingDate=" + bookingDate + ", timeSlot=" + timeSlot
				+ ", teamName=" + teamName + ", contactPersonName=" + contactPersonName + ", contactMobile="
				+ contactMobile + ", userType=" + userType + ", plannedSport=" + plannedSport
				+ ", estimatedPlayersCount=" + estimatedPlayersCount + ", baseSlotFee=" + baseSlotFee
				+ ", calculatedBookingAmount=" + calculatedBookingAmount + ", bookingStatus=" + bookingStatus
				+ ", createdTimestamp=" + createdTimestamp + ", cancellationReason=" + cancellationReason + "]";
	}
	

}
