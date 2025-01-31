package src;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * The {@code Schedule} class represents a single time slot in a doctor's schedule.
 * Each schedule object includes a date, time, availability status, and an optional appointment.
 */

public class Schedule {
	private Calendar calendar = Calendar.getInstance();
	private Date dateTime;
	private boolean available;
	private Appointment appointment;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // display time from Date data in the format 12:00
	private SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM YYYY"); // display date from Date data in the format "Friday, 01 Nov 2024"

	/**
	 * Constructs a {@code Schedule} object with a specific day offset and time slot.
	 *
	 * @param inDay  The number of days from today for the schedule.
	 * @param inTime The time slot index starting from 0 (e.g., 0 for 9 AM, 1 for 10 AM, etc.).
	 */

	public Schedule (int inDay, int inTime) {
		calendar.add(Calendar.DATE, inDay);					//Today + inDay
		calendar.set(Calendar.HOUR_OF_DAY, (9+inTime));		//First apt at 9 am (one hour slot) + Nth appointment
		calendar.set(Calendar.MINUTE, 0);					//Ensure hour ends with :00
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		dateTime = calendar.getTime();						//Set dateTime for apt as the above
		available = true;									//default value
	}
	
	public void changeAvail(boolean avail) {
		available = avail;
	}
	
	public String getDate() {
		return dateFormat.format(dateTime.getTime());
	}
	public String getTime() {
		return timeFormat.format(dateTime.getTime());
	}
	
	public Date getDateTime() {
		return dateTime;
	}
	
	public boolean getAvail() {
		return available;
	}
	
	public void addAppointment(Appointment inApt) {
		appointment = inApt;
		available = false;
		System.out.println("Availability updated. Appointment added in schedule.");
	}
	
	public void removeAppointment(Appointment inApt) {
		appointment = null;
		available = true;
		System.out.println("Availability updated. Appointment removed in schedule.");
	}
	
	public Appointment getAppointment() {
		return appointment;
	}
	
}
