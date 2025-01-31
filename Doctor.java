package src;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * The {@code Doctor} class extends {@code Staff} and manages schedules, appointments,
 * and availability for a doctor.
 */


class Doctor extends Staff{
	private final int MAX_APT = 7;		//Max no of appointment per day
	Scanner sc = new Scanner(System.in);
	private Schedule[][] schedule = new Schedule[7][MAX_APT];

	/**
	 * Constructs a {@code Doctor} object with user details and initializes the schedule.
	 *
	 * @param userID   Unique ID of the doctor.
	 * @param name     Name of the doctor.
	 * @param role     Role of the doctor.
	 * @param gender   Gender of the doctor.
	 * @param age      Age of the doctor.
	 * @param password Password for the doctor.
	 */

	public Doctor(String userID, String name, String role, String gender, int age, String password) {
		super(userID, name, role, gender, age, password);
	        
	    for (int i=0; i<7; i++) {
	    	for (int j = 0; j<7; j++) {
				schedule[i][j] = new Schedule(i, j);
			}
	    }
	}
	
	public Schedule[][] getSchedule(){
		return schedule;
	}
	
	public void setAvailability() {
		int dChoice, tChoice, avail;
		Scanner sc = new Scanner(System.in);
		do {
			for (int i=0; i<7; i++) {
				System.out.format("%d. %s\n", i+1, schedule[i][1].getDate());
			}
			System.out.println("8. Back");
			
			while (true) {
				System.out.print("Choose date: ");
				if (sc.hasNextInt()) { // Check if input is an integer
					dChoice = sc.nextInt();
					break; // Exit the validation loop
				} else {
					System.out.println("Invalid input! Please enter a number.");
					sc.next(); // Clear the invalid input
				}
			}
			
			if (dChoice > 0 && dChoice < 8) {
				do {
					System.out.format("%s\n", schedule[dChoice-1][1].getDate());
					for(int j=0; j<MAX_APT; j++) {
						System.out.format("%d. %s\n", j+1, schedule[dChoice-1][j].getTime());
						}
					System.out.println((MAX_APT+1) + ". Back");
					
					while (true) {
						System.out.print("Choose time: ");
						if (sc.hasNextInt()) { // Check if input is an integer
							tChoice = sc.nextInt();
							break; // Exit the validation loop
						} else {
							System.out.println("Invalid input! Please enter a number.");
							sc.next(); // Clear the invalid input
						}
					}
					
					if (tChoice > 0 && tChoice < MAX_APT+1) {
						do {
							System.out.println("Indicate availability:");
							System.out.println("1. Available");
							System.out.println("2. Not available");
							System.out.println("3. Back");
							while (true) {
								if (sc.hasNextInt()) { // Check if input is an integer
									avail = sc.nextInt();
									break; // Exit the validation loop
								} else {
									System.out.println("Invalid input! Please enter a number.");
									sc.next(); // Clear the invalid input
								}
							}
						
							switch(avail) {
							case 1:
								schedule[dChoice-1][tChoice-1].changeAvail(true);
								System.out.format("Available on %s %s\n", schedule[dChoice-1][tChoice-1].getDate(), schedule[dChoice-1][tChoice-1].getTime());
								break;
							case 2:
								schedule[dChoice-1][tChoice-1].changeAvail(false);
								System.out.format("Unvailable on %s %s\n", schedule[dChoice-1][tChoice-1].getDate(), schedule[dChoice-1][tChoice-1].getTime());
								break;
							case 3:
								break;
							default:
								System.out.println("Invalid choice! Please enter again.");
								break;
							}
						} while (avail > 3 || avail < 1);
					}
					else if(tChoice > MAX_APT+1 || tChoice < 1){
						System.out.println("Invalid time! Please enter again.");
					}
					
				} while (tChoice != MAX_APT+1);
			}
				
			else if (dChoice > 8 || dChoice < 1){
					System.out.println("Date invalid! Please enter again.");
			}
		} while (dChoice != 8); 
	}
	
	
	public void getAllAvailability(boolean docAccess){
		int dChoice = 0;
		do {
			for (int i=0; i<7; i++) {
				System.out.format("%d. %s\n", i+1, schedule[i][1].getDate());
			}
			System.out.println("8. Back");
			while (true) {
				System.out.print("Choose date: ");
				if (sc.hasNextInt()) { // Check if input is an integer
					dChoice = sc.nextInt();
					break; // Exit the validation loop
				} else {
					System.out.println("Invalid input! Please enter a number.");
					sc.next(); // Clear the invalid input
				}
			}
			
			if (dChoice < 8 && dChoice > 0) {
				if (docAccess) {
					System.out.format("%s\n", schedule[dChoice-1][1].getDate());
					for(int j=0; j<MAX_APT; j++) {
						System.out.format("%d. %s : ", j+1, schedule[dChoice-1][j].getTime());
						if (!schedule[dChoice-1][j].getAvail()) {
							if (schedule[dChoice-1][j].getAppointment() != null) {
								System.out.println(schedule[dChoice-1][j].getAppointment().getID() + " " + schedule[dChoice-1][j].getAppointment().getPatient());
							}
							else System.out.println("Unvailable");
						}
						else System.out.println("Available");
					}	
				}
				else {
					System.out.format("%s\n", schedule[dChoice-1][1].getDate());
					for(int j=0; j<MAX_APT; j++) {
						System.out.format("%d. %s : ", j+1, schedule[dChoice-1][j].getTime());
						if (schedule[dChoice-1][j].getAvail()) {
							System.out.println("Available");
						}
						else System.out.println("Unavailable");
					}
				}
				System.out.println();
			}
				
			else if (dChoice > 8|| dChoice < 1){
				System.out.println("Date invalid! Please enter again.");
			}
		} while (dChoice != 8 );
			
	}
	
	public void getAvailSlots() {
		int num = 0;
		System.out.println("Available slots: ");
		for (int i = 0; i<7; i++) {
			System.out.println(schedule[i][1].getDate() + ": ");
			for (int j = 0; j<MAX_APT; j++) {
				if (schedule[i][j].getAvail()) {
					num++;
					System.out.println(num + ". " + schedule[i][j].getTime());
				}
			}
		}
	}
//rescheduling appointments
public boolean rescheduleAppointmentByID(Patient patient, String appointmentID, Date newDateTime) {
  // Step 1: Locate the appointment by ID
  for (int i = 0; i < 7; i++) {
      for (int j = 0; j < MAX_APT; j++) {
          Appointment appointment = schedule[i][j].getAppointment();
          if (appointment != null && appointment.getID().equals(appointmentID) && appointment.getPatient().equals(patient)) {
              
              // Step 2: Check if the new slot is available
              for (int x = 0; x < 7; x++) {
                  for (int y = 0; y < MAX_APT; y++) {
                      if (schedule[x][y].getDateTime().equals(newDateTime) && schedule[x][y].getAvail()) {
                          // Move the appointment to the new slot
                          schedule[x][y].addAppointment(appointment);
                          schedule[i][j].changeAvail(true); // Set old slot as available
                          schedule[i][j].addAppointment(null); // Clear old appointment
                          return true; // Success
                      }
                  }
              }
              return false; // New slot not available
          }
      }
  }
  return false; // Appointment not found
}



public void listAppointmentsForPatient(Patient patient) {
  System.out.println("Upcoming appointments for " + patient.getName() + ":");

  boolean found = false;
  for (int i = 0; i < 7; i++) {
      for (int j = 0; j < MAX_APT; j++) {
          Appointment appointment = schedule[i][j].getAppointment();
          if (appointment != null && appointment.getPatient().equals(patient)) {
              found = true;
              System.out.printf("ID: %s | Date: %s | Time: %s\n",
                      appointment.getID(),
                      schedule[i][j].getDate(),
                      schedule[i][j].getTime());
          }
      }
  }

  if (!found) {
      System.out.println("No upcoming appointments found for this patient.");
  }
}
public void promptAndRescheduleForPatient(Patient patient) {
  Scanner scanner = new Scanner(System.in);

  // Step 1: List all appointments for the patient
  listAppointmentsForPatient(patient);

  // Step 2: Ask for Appointment ID to reschedule
  System.out.print("Enter the Appointment ID to reschedule: ");
  String appointmentID = scanner.nextLine();

  // Step 3: Ask for new date and time
  System.out.print("Enter the new date and time for the appointment (yyyy-MM-dd HH:mm): ");
  String newDateTimeString = scanner.nextLine();
  Date newDateTime;

  // Step 4: Parse the new date and time
  try {
      newDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(newDateTimeString);
  } catch (ParseException e) {
      System.out.println("Invalid date/time format. Please enter in 'yyyy-MM-dd HH:mm' format.");
      return;
  }

  // Step 5: Attempt to reschedule the appointment
  boolean success = rescheduleAppointmentByID(patient, appointmentID, newDateTime);

  // Step 6: Output result
  if (success) {
      System.out.println("Rescheduling successful.");
  } else {
      System.out.println("Rescheduling failed. Please check the appointment ID or time availability.");
  }
}

//cancel appointments
public void promptAndCancelAppointmentForPatient(Patient patient) {
  Scanner scanner = new Scanner(System.in);

  // Step 1: List all appointments for the patient
  listAppointmentsForPatient(patient);

  // Step 2: Ask for Appointment ID to cancel
  System.out.print("Enter the Appointment ID to cancel: ");
  String appointmentID = scanner.nextLine();

  // Step 3: Attempt to cancel the appointment
  boolean success = cancelAppointmentByID(patient, appointmentID);

  // Step 4: Output result
  if (success) {
      System.out.println("Appointment canceled successfully.");
  } else {
      System.out.println("Cancellation failed. Please check the appointment ID.");
  }
}
public boolean cancelAppointmentByID(Patient patient, String appointmentID) {
  for (int i = 0; i < 7; i++) {
      for (int j = 0; j < MAX_APT; j++) {
          Appointment appointment = schedule[i][j].getAppointment();
          if (appointment != null && appointment.getID().equals(appointmentID) && appointment.getPatient().equals(patient)) {
              // Step 1: Remove the appointment and make the slot available
              schedule[i][j].changeAvail(true); // Mark slot as available
              schedule[i][j].addAppointment(null); // Remove the appointment
              return true; // Successful cancellation
          }
      }
  }
  return false; // Appointment not found or mismatch
}




				
	public boolean getAvailability(Date time){
	    for (int i=0; i<7; i++){
		    for (int j=0; j<MAX_APT; j++){
			    if (time == schedule[i][j].getDateTime()) {
			    	return schedule[i][j].getAvail();
			    }
		    }
	    }
	    return false;
}
	
	public boolean addAppointment(Appointment a) {
		
		for (int i = 0; i < 7; i++) {
		    for (int j = 0; j < MAX_APT; j++) {
//		        System.out.println("Checking against slot: " + schedule[i][j].getDateTime());
		        if (a.getDateTime().equals(schedule[i][j].getDateTime())) {
		        	if (schedule[i][j].getAppointment()!=null) {
		        		System.out.println("Slot already taken, appointment cannot be added!");
		        		return false;
		        	}
		            schedule[i][j].addAppointment(a);
		            return true;
		        }
		    }
		}
		System.out.println("Could not find appointment slot.");
		return false;

	}
	
	public void removeAppointment(Appointment a) {
		for (int i=0; i<7; i++){
		  for (int j=0; j<MAX_APT; j++){
//			  System.out.println("Checking against slot: " + schedule[i][j].getDateTime());
			  if (a.getDateTime() == schedule[i][j].getDateTime()) {
			    schedule[i][j].removeAppointment(a);
			    System.out.println("Appointment removed in doctor.");
			    System.out.print("Availability of slot: ");
			    if (schedule[i][j].getAvail()) System.out.println("Available");
			    else System.out.println("Unavailable");
			    return;
			  }
			    
		  }
		}
		System.out.println("Could not find appointment slot.");
	}
	
	//attribute changers
	  public void changeName(String newname) {
	    	super.changeName(newname);
	    	System.out.println("Doctor name changed successfully!");
	    	return;
	    }
	    public void changeAge(int newage) {
	    	super.changeAge(newage);
	    	System.out.println("Doctor age changed successfully!");
	    	return;
	    }
	    public void changeGender(String newgender) {
	    	super.changeGender(newgender);
	    	System.out.println("Doctor gender changed successfully!");
	    	return;
	    }
	    public void changeRole(String newrole) {
	    	super.changeRole(newrole);
	    	System.out.println("Doctor role changed successfully!");
	    	return;
	    }

}
	
