package src;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * The {@code HospitalApp} class serves as the main entry point for a hospital management system.
 * <p>
 * This application handles user authentication and provides different menus for various roles
 * (e.g., Doctor, Pharmacist, Administrator, Patient) based on the user's role. Users can perform
 * actions such as viewing medical records, scheduling appointments, managing staff, and handling
 * inventory.
 * </p>
 */

public class HospitalApp {

	/**
	 * The main method is the entry point of the application.
	 * <p>
	 * It initializes instances of patient, staff, and medicine inventories and handles user login.
	 * Based on the user's role, different menu options are provided to manage hospital-related
	 * operations such as appointments, inventory, and staff.
	 * </p>
	 *
	 * @param args command-line arguments (not used)
	 */

	public static void main(String[] args) {

        PatientInv pList = PatientInv.getInstance();
		StaffInv sList = StaffInv.getInstance();
		MedicineInv mList = MedicineInv.getInstance();
		
		ArrayList<Staff> staffList = sList.copyStaffList(); //Use members in staff list instance from StaffInv
		ArrayList<Patient> patientList = pList.copyPatientList();
		ArrayList<Medicine> medicineList = mList.copyMedicineList();
		ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
		ArrayList<RestockForm> restockList = new ArrayList<>();

		
		Scanner sc = new Scanner(System.in);
		String username, password;
		User user = null;
		boolean loggedIn = false;
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY");
		int patSelect, aptSelect, medSelect, docSelect, patAmount, aptAmount, medAmount, docAmount;
		Patient pat;
		Doctor doc;
		Appointment apt;
		Medicine med;
		int choice;
		
		do {
			do {
				logInScreen();
				System.out.println("Enter Username: ");
				username = sc.nextLine().toUpperCase();
				System.out.println("Enter Password: ");
				password = sc.nextLine();
				
				for (Staff s : staffList) {
					loggedIn = s.login(username, password);
					if (loggedIn) {
						user = s;
						break;
					}
				}
				
				if (!loggedIn) {
				 	for (Patient p : patientList) {
						loggedIn = p.login(username, password);
						if (loggedIn) {
							user = p;
							break;
						}
				 	}
				}   
					
				if (!loggedIn) {
					System.out.println("Username or password incorrect! Please try again.");
				}
			} while (!loggedIn);
			
			System.out.println("Welcome!");

			switch (user.getRole()) {
			case "Doctor":
				Doctor doctor = (Doctor) user;
				
				do {
					System.out.println("==================================================");
					System.out.println("Hello " + doctor.getName() + ", welcome to the doctor menu");
					System.out.println("1. View Patient Medical Records");
					System.out.println("2. Update Patient Medical Records (Outcome)");
					System.out.println("3. View Personal Schedule");
					System.out.println("4. Set Availability for Appointments");
					System.out.println("5. Accept or Decline Appointment Requests");
					System.out.println("6. View Upcoming Appointments");
					System.out.println("7. Record Appointment Outcome");
					System.out.println("8. Change Password");
					System.out.println("9. Logout");
					System.out.println("==================================================");

					while (true) {
						System.out.print("Enter your choice: ");
						if (sc.hasNextInt()) { // Check if input is an integer
							choice = sc.nextInt();
							break; // Exit the validation loop
						} else {
							System.out.println("Invalid input! Please enter a number.");
							sc.next(); // Clear the invalid input
						}
					}
					
					switch(choice) {
					case 1: // view patient medical record
						patAmount = 0;
						for (Patient p : patientList) {
							patAmount++;
							System.out.println((patAmount) + ". " + p.getID() + " " + p.getName());
						} // print patient list

						do {
							System.out.println("Select patient:");

							// Validate input for integer
							while (!sc.hasNextInt()) {
								System.out.println("Invalid input! Please enter a valid integer.");
								sc.next(); // Consume the invalid input
							}

							patSelect = sc.nextInt();

							// Check if the input is within the valid range
							if (patSelect <= 0 || patSelect > patAmount) {
								System.out.println("Invalid patient! Please enter a number between 1 and " + patAmount + ".");
							}

						} while (patSelect <= 0 || patSelect > patAmount); // Continue until a valid patient is selected
						
						pat = patientList.get(patSelect-1);
						System.out.println("Name		: " + pat.getName());
						System.out.println("DOB		: " + dateFormat.format(pat.getDOB()));
						System.out.println("Email		: " + pat.getEmail());
						System.out.println("Contact no.	: " + pat.getContactNum());
						System.out.println("Blood type	: " + pat.getBloodType());
						System.out.println("Previous appointment outcomes:");
						boolean trig = false;
						for (Appointment a : appointmentList) {
							if (pat.getName().equals(a.getPatient()) && (a.getStatus().equals("Confirmed") || a.getStatus().equals("Completed"))) {
								System.out.println(a.getDate() + ": " + a.getOutcome()); trig = true;
							}
						}
						if(!trig) { System.out.println("~ No appointment outcomes found!"); }
						System.out.println();
						break;
						
					case 2: // update patient record
						String IDChoice, update;
						patAmount = 0;
						aptAmount = 0;
						apt = null;
						for (Patient p : patientList) {
							patAmount++;
							System.out.println((patAmount) + ". " + p.getID() + " " + p.getName());
						} // print patient list

						do {
							System.out.println("Select patient:");
							while (!sc.hasNextInt()) {
								System.out.println("Invalid input! Please enter a valid integer.");
								sc.next(); // Consume the invalid input
							}
							patSelect = sc.nextInt();
							// Check if the input is within the valid range
							if (patSelect <= 0 || patSelect > patAmount) {
								System.out.println("Invalid patient! Please enter a number between 1 and " + patAmount + ".");
							}
						} while (patSelect <= 0 || patSelect > patAmount); // Continue until a valid patient is selected
						
						pat = patientList.get(patSelect-1);
						boolean ptrig = false;
						System.out.println("Past records: ");
						for (Appointment a : appointmentList) {
							if (a.getPatient().equals(pat.getName()) && (a.getStatus().equals("Confirmed") || a.getStatus().equals("Completed"))) {
								System.out.println( (a.getID()) + ". " + a.getDate() + ": " + a.getOutcome());
								ptrig = true;
							}
						}
						sc.nextLine();
						if(ptrig){
							do {
								System.out.println("Select appointment ID of record to update: ");
								IDChoice = sc.nextLine();

								for (Appointment a : appointmentList) {
									if (IDChoice.equals(a.getID()) && pat.getName().equals(a.getPatient())) {
										apt = a;
									}
								}
							} while (apt == null);

							System.out.println("Enter updated record: ");
							update = sc.nextLine();
							apt.updateOutcome(update);
							System.out.println("Record updated!");
						}
						else{
							System.out.println("No appointment outcomes found!");
						}
						System.out.println();
						break;
						
					case 3: // personal schedule
						doctor.getAllAvailability(true);
						System.out.println();
						break;
					
					case 4: // set availability
						doctor.setAvailability();
						System.out.println();
						break;
						
					case 5: // accept or reject appointments
						aptAmount = 0;
						String aptIDSelect = null;
						for (Appointment a : appointmentList) {
							if(a.getDoctor().equals(doctor.getName()) && a.getStatus().equals("Pending")) { // list all appointments under dr's name
								aptAmount++;
								System.out.println(aptAmount + ". " + a.getID() + ": " + "(" + a.getPatient() + ") " + a.getDate() + " " + a.getTime());
							}
						} 
						
						if (aptAmount != 0) {
							
							boolean exitAcceptApt = false;
							apt = null;
							
							do {					
								System.out.println("Enter full appointment ID to accept (Enter 'Back' to cancel): ");
								aptAmount = 0;
								aptIDSelect = null;
								sc.nextLine(); //clear scanner cos its acting up bruh
								
								aptIDSelect = sc.nextLine();
								if (aptIDSelect.equals("Back")) {
									exitAcceptApt = true;
									break;
								}
								for (Appointment a : appointmentList) {
									if (a.getID().equals(aptIDSelect)) {
										apt = a;
										aptAmount++;
									}
								}
								
								if (aptAmount == 0) System.out.println("Invalid appointment! Please enter again.");
							} while (aptAmount == 0);
							
							if (exitAcceptApt) break;
							int acc;
							
							System.out.println("1. Accept appointment");
							System.out.println("2. Reject appointment");
							do {
								System.out.println("Enter selection: ");
								while (!sc.hasNextInt()) {
									System.out.println("Invalid input! Please enter a valid integer.");
									sc.next();
								}
								acc = sc.nextInt();
								if (acc > 2 || acc <= 0) {
									System.out.println("Invalid selection! Please enter 1 or 2.");
								}
							} while (acc > 2 || acc <= 0);
							
							switch (acc) {
							case 1:
								apt.updateStatus("Confirmed");
								//doctor.addAppointment(apt);
								System.out.println("Appointment accepted.");
								break;
							case 2:
								apt.updateStatus("Cancelled");
								doctor.removeAppointment(apt);
								System.out.println("Appointment rejected.");
								break;
							}
						}
						
						else System.out.println("No appointment found!");
						System.out.println();
						break;
						
					case 6: // view upcoming appointments
						System.out.println("Upcoming appointments: ");
						
						aptAmount = 0;
						
						for (Appointment a : appointmentList) {
							if (a.getDoctor().equals(doctor.getName()) && a.getStatus().equals("Confirmed")) { // list appointments that are in the future and under dr's name
								System.out.println();
								System.out.println("Appointment ID	: " + a.getID());
								System.out.println("Date			: " + a.getDate());
								System.out.println("Time 			: " + a.getTime());
								System.out.println("Patient			: " + a.getPatient());
								aptAmount++;
							}
						}
						
						if (aptAmount == 0) System.out.println("No appointment found!");
						System.out.println();
						break;
					
					case 7: // appointment outcome
						int medYN, medAptAmt;
						int medAmountCounter = 0;
						aptAmount = 0;
						aptIDSelect = null;
						for (Appointment a : appointmentList) {
							if(a.getDoctor().equals(doctor.getName()) && a.getStatus().equals("Confirmed")) { // list all appointments under dr's name
								aptAmount++;
								System.out.println(aptAmount + ". " + a.getID() + ": " + "(" + a.getPatient() + ") " + a.getDate() + " " + a.getTime());
							}
						} 
						
						if (aptAmount != 0) {
							
							
							apt = null;
							sc.nextLine();
							do {					
								System.out.println("Enter full appointment ID to update outcome: ");
								aptAmount = 0;
								
								aptIDSelect = sc.nextLine();
								for (Appointment a : appointmentList) {
									if (a.getID().equals(aptIDSelect)) {
										apt = a;
										aptAmount++;
									}
								}
								
								if (aptAmount == 0) System.out.println("Invalid appointment! Please enter again.");
							} while (aptAmount == 0);
							
							apt.updateStatus("Completed");
							System.out.println("Enter appointment outcome: ");
							apt.updateOutcome(sc.nextLine());
							System.out.println("Any medications prescribed?");
							System.out.println("1. Yes");
							System.out.println("2. No");
							
							do {
								System.out.println("Enter choice: ");
								medYN = sc.nextInt();
								
								if (medYN>2 || medYN<1) System.out.println("Invalid choice! Please enter again.");
							} while (medYN>2 || medYN<1);
							
							switch (medYN) {
							case 1:
								apt.TrueHasMedication();
								
								System.out.println("Medicine list: ");
								for (Medicine m : medicineList) {
									medAmountCounter++;
									System.out.println(medAmountCounter + ". " + m.getName());
								}
								System.out.println((medAmountCounter + 1) + ". (Exit Menu)");

								do {
									do {
										System.out.println("Enter medicine index or Exit: ");
										while (!sc.hasNextInt()) {
											System.out.println("Invalid input! Please enter a valid integer.");
											sc.next(); // Consume invalid input
										}
										medSelect = sc.nextInt();
										if (medSelect < 1 || medSelect > medAmountCounter + 1) {
											System.out.println("Invalid medicine! Please enter a number between 1 and " + (medAmountCounter + 1) + ".");
										}
									} while (medSelect < 1 || medSelect > medAmountCounter + 1);
									// Process selected medicine
									if (medSelect != medAmountCounter + 1) { // If not the exit option
										med = medicineList.get(medSelect - 1);
										System.out.println("Enter medication amount: ");
										// Validate input for medication amount
										while (!sc.hasNextInt()) {
											System.out.println("Invalid input! Please enter a valid integer.");
											sc.next(); // Consume invalid input
										}
										medAptAmt = sc.nextInt();
										System.out.println();
										apt.addPrescript(med.getName(), medAptAmt); // Add prescription
									}
								} while (medSelect != medAmountCounter + 1); // Loop until user selects the exit option

								break;
								
							case 2:
								break;
							}
							
							System.out.println("Appointment outcomes for " + apt.getID() +  " | (" + apt.getPatient() + ") "+ apt.getDate() + " "+ apt.getTime() + " successfully recorded.");
						}
						
						
						
						else System.out.println("No appointment found!");
						System.out.println();
						break;
						
					case 8: // change password
						String oldPa, newPa;
						sc.nextLine();
						System.out.println("Please enter your current password: ");
						oldPa = sc.nextLine();
						System.out.println("Please enter your new password: ");
						newPa = sc.nextLine();

						if(!doctor.changePassword(oldPa,newPa))
						{System.out.println("Password is incorrect");}
						break;
					case 9: // logout
						System.out.println("Logging out...");
						sc.nextLine();
						loggedIn = false;
						System.out.println();
						break;
						
					default:
						System.out.println("Invalid choice! Please enter again.");
						break;
					}
				} while (choice != 9);
				
				
				break;
				
			case "Pharmacist":
				Pharmacist pharma = (Pharmacist) user;

				Scanner scPharma = new Scanner(System.in); //Used new scanner to prevent Username from being entered on exit.

				int choiceP = 0;

				do {
					System.out.println();
					System.out.println("===============================================");
					System.out.println("Hello " + pharma.getName() + ", welcome to the Pharmacy menu");
					System.out.println("1. All medication orders"); //TODO
					System.out.println("2. Display stock"); //TODO
					System.out.println("3. Request restock menu");
					System.out.println("4. Request log"); //TODO
					System.out.println("5. Change Password");
					System.out.println("6. Log off");
					System.out.println("===============================================");



					do {
						System.out.println("Enter selection: ");
						while (!scPharma.hasNextInt()) {
							System.out.println("Invalid input! Please enter a valid integer.");
							scPharma.next(); // Consume the invalid input
						}
						choiceP = scPharma.nextInt();
						if (choiceP > 6 || choiceP < 1) {
							System.out.println("Invalid choice! Please enter a number between 1 and 6.");
						}

					} while (choiceP > 6 || choiceP < 1); // Ensure input is within the valid range


					switch (choiceP) {
						case 1:
							//1. Fulfill medication orders
							//Print all appointments with prescriptions
							Appointment aptToFulfil = null;
							String aptID = null;
							for (Appointment a : appointmentList) {
								if(a.isHasMedication())
								{
									aptID = a.getID();
									System.out.println("==================== Prescription ======================");
									System.out.printf("%-20s %-14s %-6s %-6s \n","Appointment ID","Medication","Amount","Status");
									System.out.println("========================================================");

									
									a.printPrescriptionFormatting();
								}
							}
							//Choose appointment with the index shown
							if(aptID != null)
							{
								scPharma.nextLine();
								System.out.println("Enter Appointment ID to fulfil medicine: ");
								while (true) {
									aptID = scPharma.nextLine(); //Reject non-int inputs
									for (Appointment a : appointmentList) {
										if (a.getID().equals(aptID) && a.isHasMedication()) {
											aptToFulfil = a;
										}
									}
									if (aptToFulfil != null) {
										
										break; // Exit loop if an integer was successfully read
									} else {
										System.out.println("Invalid input. Please enter again.");
										scPharma.next(); // Consume the invalid input to avoid infinite loop
									}
								}
								
								System.out.println("--------------------------------------------------");
								aptToFulfil.printPrescription();

								System.out.println("Enter prescription you would like to fulfill (Case Sensitive)");
								//Choose which medication to fulfill, update "medIsFilled" per PrescriptedMed and update medicineList

								String prescription = scPharma.nextLine().trim();

								if (aptToFulfil.isThisPrescribedMedication(prescription)) {
									if (aptToFulfil.isPrescriptionFulfilled(prescription)) { // Check if already fulfilled
										System.out.println("The prescription for " + prescription + " is already fulfilled.");
									} else {
										if (mList.getQuanity(prescription) >= aptToFulfil.getPrescribedAmount(prescription)) {
											// Deduct the stock
											mList.minusStock(prescription, aptToFulfil.getPrescribedAmount(prescription));
											// Mark the prescription as fulfilled
											aptToFulfil.fulfillPrescription(prescription);
										} else {
											System.out.println("There is not enough stock, please refill.");
										}
									}
								} else {
									// Invalid prescription input
									System.out.println("The entered medication is not in the prescribed list. Please try again.");
								}
							}else System.out.println("No appointment found!");


							break;
						case 2:
							//2. Display stock
							mList.printList();
							break;
						case 3:
							//3. Menu
							int m;
							do {
								mList.printList();

								System.out.println("1. Request restock - Specific medication");
								System.out.println("2. Request restock - All of low");
								System.out.println("3. Cancel restock request");
								System.out.println("4. Back");
								do {
									System.out.println("Enter selection: ");

									// Check if the input is an integer
									while (!scPharma.hasNextInt()) {
										System.out.println("Invalid input! Please enter an integer.");
										scPharma.next(); // Consume the invalid input to avoid an infinite loop
									}

									m = scPharma.nextInt();

									if (m > 4 || m < 1) {
										System.out.println("Invalid choice! Please enter again.");
									}
								} while (m > 4 || m < 1);

								scPharma.nextLine();

								switch (m) {
									case 1:
										// 1. Request restock - Specific medication
										mList.printList();
										String restockMed = null;
										int restockAmt = 0;

										boolean validMedicine = false;

										// Loop until a valid medication is entered
										do {
											System.out.println("Which medication would you like to restock? (Case Sensitive)");

											restockMed = scPharma.nextLine();

											if (mList.findMedicineIndex(restockMed) != -1) {
												validMedicine = true; // Medication is found in the list
											} else {
												System.out.println("Invalid medication name! Please enter a valid medication from the list.");
											}
										} while (!validMedicine);

										System.out.println("How much would you like to restock?");

										// Input validation to ensure restockAmt is a valid integer
										while (true) {
											while (!scPharma.hasNextInt()) {
												System.out.println("Invalid input! Please enter an integer for the restock amount.");
												scPharma.next(); // Consume the invalid input
											}
											restockAmt = scPharma.nextInt();
											if (restockAmt >= 0) {
												break; // Exit loop if the input is valid
											} else {
												System.out.println("Invalid amount! Please enter a non-negative number.");
											}
										}

										// Create RestockForm and add to restockList
										RestockForm rForm = new RestockForm(restockMed, restockAmt);
										restockList.add(rForm);

										System.out.println("Restock request has been made successfully.");
										break;
									case 2:
										//2. Request restock - All low
										mList.printLowStockMedicine();
										boolean  lsTrig = false;

										// Create restock requests for all low stock medications
										for (Medicine medicine : medicineList) {
											if (medicine.getQuantity() <= medicine.getLowStockAlertAmt()) {
												int restockAmount = medicine.getLowStockAlertAmt() - medicine.getQuantity() + 10; //Restock to have 10 above the low stock amount
												RestockForm rLowForm = new RestockForm(medicine.getName(), restockAmount);
												restockList.add(rLowForm);
												System.out.println("Restock request created for " + medicine.getName() + ": " + restockAmount + " units.");
												lsTrig = true;
											}
										}

										if (lsTrig) {System.out.println("Restock requests for all low stock medications have been made successfully.");}

										break;
									case 3:
										// 3. Cancel restock request
										// Lists all requests not yet fulfilled
										boolean triggg = false;
										ArrayList<RestockForm> unfulfilledRequests = new ArrayList<>();
										System.out.println("===== Unfulfilled requests =====");
										System.out.printf("%-10s %-10s %-20s %-10s %-20s\n", "Index", "restockID", "Medicine", "Amount", "Status");

										int index = 0;
										for (RestockForm rF : restockList) {
											if (!rF.isFulfilled()) {
												System.out.printf("%-10d", index + 1); // Print the index for selection
												rF.printFormDetails();
												unfulfilledRequests.add(rF);
												triggg = true;
												index++;
											}
										}

										if (!triggg) {
											System.out.println("==========  There are no past requests  ==========");
											break;
										}

										// Let the user choose an unfulfilled restock request to cancel
										int cancelIndex = -1;
										do {
											System.out.println("Enter the index of the restock request you want to cancel (or enter 0 to go back): ");

											// Validate that the input is an integer
											while (!scPharma.hasNextInt()) {
												System.out.println("Invalid input! Please enter a valid integer.");
												scPharma.next(); // Consume the invalid input
											}
											cancelIndex = scPharma.nextInt();

											if (cancelIndex == 0) {
												// User chooses to go back
												break;
											} else if (cancelIndex > 0 && cancelIndex <= unfulfilledRequests.size()) {
												// Remove the selected request
												RestockForm toRemove = unfulfilledRequests.get(cancelIndex - 1);
												restockList.remove(toRemove);
												System.out.println("Restock request for " + toRemove.getMedicationName() + " has been successfully cancelled.");
												break;
											} else {
												System.out.println("Invalid index! Please enter a valid index.");
											}
										} while (true);

										break;
									case 4:
										//4. Back
										break;
								}
							} while (m != 4);
							break;
						case 4:
							//4. Request log
							boolean trigg = false;
							System.out.printf("%-10s %-20s %-10s %-20s\n","restockID","Medicine","Amount","Status");
							for (RestockForm rF : restockList) {
								rF.printFormDetails();
								trigg = true;
							}
							if (!trigg) {System.out.println("==========  There are no past requests  ==========");}

							break;
						case 5:
							//5. Change Password
							scPharma.nextLine();
							String oldP, newP;
							System.out.println("Please enter your current password: ");
							oldP = scPharma.nextLine();
							System.out.println("Please enter your new password: ");
							newP = scPharma.nextLine();

							if(!pharma.changePassword(oldP,newP))
							{System.out.println("Password is incorrect");}
							break;
						case 6:
							//6. Log off
							loggedIn = false;
							break;
					}

				} while (choiceP != 6);



				break;
				
			case "Administrator":
				
				Administrator admin = (Administrator) user;
				int admchoice, staffSelect=0, staffAmount=0;
				
				//enter your code here
				do {
					System.out.println("===============================================");
					System.out.println("Hello " + admin.getName() + ", welcome to the Administrator menu");
					System.out.println("1. View and Manage Hospital Staff");
					System.out.println("2. Manage Appointments");
					System.out.println("3. View and Manage Medication Inventory");
					System.out.println("4. Approve Replenishment Requests");
					System.out.println("5. Change Password");
					System.out.println("6. Logout");
					System.out.println("===============================================");
					
					while (!sc.hasNextInt()) {
						System.out.println("Invalid input! Please enter a valid integer.");
						sc.next(); // Consume the invalid input
					}
					
					admchoice = sc.nextInt();
					sc.nextLine();
					switch(admchoice) {
		        case 1:
		            int choice2;
		            do {
		            System.out.println("---- View and Manage Hospital Staff ----");
		            System.out.println("1. View Hospital Staff");
		            System.out.println("2. Manage Hospital Staff");
		            System.out.println("3. Back");
		            
		            while (!sc.hasNextInt()) {
						System.out.println("Invalid input! Please enter a valid integer.");
						sc.next(); // Consume the invalid input
					}
		            
		            choice2 = sc.nextInt();
		            sc.nextLine();
		            switch(choice2){
		                case 1:
		                	staffAmount=0;
		                	int filterc = 0;
		                    System.out.println("Viewing hospital staff...");
		                   
		                    do {
		                    System.out.println("1. Filter by Staff Role");
		                    System.out.println("2. Filter by Staff Gender");
		                    System.out.println("3. Filter by Staff Age");
		                    System.out.println("4. View all Staff");
		                    System.out.println("5. Back");
		                    
		                    while (!sc.hasNextInt()) {
								System.out.println("Invalid input! Please enter a valid integer.");
								sc.next(); // Consume the invalid input
							}
		                    
		                    filterc = sc.nextInt();
		                    sc.nextLine();
		                    if (filterc>5 || filterc<1) System.out.println("Invalid choice! Please choose again.");
		                    
		                    
		                    else if (filterc == 4) {
		                    	System.out.println("Staff list: ");
		                    	sList.printList();
		                    	break;
		                    }
		                    
		                    else {
		                    	 // Prompt the user to choose a filter criterion

		                        // Filter based on the chosen criterion
		                        ArrayList<Staff> filteredStaff = new ArrayList<>();
		                        do {
		                            switch (filterc) {
		                                case 1:
		                                    System.out.print("Enter the role to filter by (e.g., Doctor, Nurse): ");
		                                    String role = sc.nextLine();
		                                    for (Staff staff : staffList) {
		                                        if (staff.getRole().equalsIgnoreCase(role)) {
		                                            filteredStaff.add(staff);
		                                        }
		                                    }
		                                    break;
		                                case 2:
		                                    System.out.print("Enter the gender to filter by (e.g., Male, Female): ");
		                                    String gender = sc.nextLine();
		                                    for (Staff staff : staffList) {
		                                        if (staff.getGender().equalsIgnoreCase(gender)) {
		                                            filteredStaff.add(staff);
		                                        }
		                                    }
		                                    break;
		                                case 3:
		                                    System.out.print("Enter the minimum age to filter by: ");
		                                    int minAge = sc.nextInt();
		                                    System.out.print("Enter the maximum age to filter by: ");
		                                    int maxAge = sc.nextInt();
		                                    for (Staff staff : staffList) {
		                                        if (staff.getAge() >= minAge && staff.getAge() <= maxAge) {
		                                            filteredStaff.add(staff);
		                                        }
		                                    }
		                                    break;
		                                default:
		                                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
		                                    filterc = 4;
		                            }
		                        } while (filterc == 4);

		                        if (filteredStaff.isEmpty()) {
		                            System.out.println("No staff members found matching the criteria.");
		                        } else {
		                            System.out.println("Filtered Staff List:");
		                            for (Staff staff : filteredStaff) {
		                                System.out.println(staff);
		                            }
		                        } 
		                    	break;
		                    }
		                    
		                    } while (filterc!=5);
		                    // view view view
		                    break;										
		
		                case 2:
		                    int choice3;
		                    System.out.println("-----------Manage hospital staff------------");
		                    //show hospital staff
		                    do {
		                        System.out.println("Select action: ");
		                        System.out.println("1. Add Staff");
		                        System.out.println("2. Update Staff Details");
		                        System.out.println("3. Remove Staff");
		                        System.out.println("4. Back");
		                        
		                        while (!sc.hasNextInt()) {
									System.out.println("Invalid input! Please enter a valid integer.");
									sc.next(); // Consume the invalid input
								}
		                        
		                        choice3 = sc.nextInt();
		                        sc.nextLine();
		                        switch(choice3){
		                            case 1:
		                                System.out.println("---- Add hospital staff ----");
		                                boolean duplicateindicator = false;
		                                String userID = "null";
		                                do {
		                                    duplicateindicator = false;
		                                    System.out.print("Enter User ID: ");
		                                    userID = sc.nextLine();
		                                    for (Staff staff : staffList) {
		                                        if (staff.getID().equalsIgnoreCase(userID)) {
		                                            System.out.println("User ID taken! Please select another.");
		                                            duplicateindicator = true;
		                                            break;
		                                        }
		                                    }
		                                } while (duplicateindicator);


		                                System.out.print("Enter Name: ");
		                                String name = sc.nextLine();

		                                System.out.print("Enter Role: ");
		                                String role = sc.nextLine();

		                                System.out.print("Enter Gender: ");
		                                String gender = sc.nextLine();

		                                System.out.print("Enter Age: ");
		                                int age = sc.nextInt();

		                                // Clear the scanner buffer
		                                sc.nextLine();

		                                Staff newStaff = null;
		                                //ADD SWITCH STATEMENT TO CREATE diff staff objects

		                                switch(role) {
		                                    case "Doctor":
		                                        newStaff = new Doctor(userID, name, role, gender, age, "password");
		                                        break;
		                                    case "Pharmacist":
		                                        newStaff = new Pharmacist(userID, name, role, gender, age, "password");
		                                        break;

		                                }



		                                // Add the new staff member to the staffInventory
		                                sList.addStaff(newStaff);

		                                System.out.println("[" + userID + ", " + name + ", " + role + ", " + gender + ", " + age + " years old]" + " added successfully.");


		                                break;
		                            case 2:
		                                int choice4, staffCount=0;
		                                boolean found = false;
		                                String desiredID;
		                                System.out.println("---- Update hospital staff ----");
		                                
		                                do{
		                                System.out.println("Enter User ID of staff to update:");
		                                desiredID = sc.nextLine();
		                                staffAmount = 0;
		                                for (Staff staff : staffList) {
		            	                    if (staff.getID().equalsIgnoreCase(desiredID)) {
		            	                        staffSelect = staffAmount;
		            	                        found=true;
		            	                        break;
		            	                    }
		            	                    staffAmount++;
		            	                }
		                                if (!found) System.out.println("User ID not found! Please try again.");
		                                } while (!found);
		                                
		                                Staff staffy = staffList.get(staffSelect);
		                                System.out.println("Staff ID	: " + staffy.getID());
										System.out.println("Name		: " + staffy.getName());
										System.out.println("Gender		: " + staffy.getGender());
										System.out.println("Age		: " + staffy.getAge());
										System.out.println("Role		: " + staffy.getRole());
								
		                                
		                                do {
		                                System.out.println("Select item to edit: ");
		    							System.out.println("1. Name");
		    							System.out.println("2. Gender");
		    							System.out.println("3. Age");
		    							System.out.println("4. Role");
		    							System.out.println("5. Back");
		    							
		    							while (!sc.hasNextInt()) {
		    								System.out.println("Invalid input! Please enter a valid integer.");
		    								sc.next(); // Consume the invalid input
		    							}
		    							
		    							choice4 = sc.nextInt();
		    							sc.nextLine();
		    							switch(choice4){
		    							    case 1:
		    							        System.out.println("Enter new staff name:");
		    							        String newfield = sc.nextLine();
		    							        staffy.changeName(newfield);
		    							        break;
		    							    case 2:
		    							        System.out.println("Enter new staff gender:");
		    							        newfield = sc.nextLine();
		    							        staffy.changeGender(newfield);
		    							        break;
		    							    case 3:
		    							        System.out.println("Enter new staff age:");
		    							        int newage = sc.nextInt();
		    							        staffy.changeAge(newage);
		    							        sc.nextLine();
		    							        break;
		    							    case 4:
		    							        System.out.println("Enter new staff role:");
		    							        newfield = sc.nextLine();
		    							        staffy.changeRole(newfield);
		    							        break;
		    							    case 5: 
		    							        System.out.println("Going back...");
		    							        break;
		    							    default:
		    									System.out.println("Invalid option entered. Please try again. ");
		    									break;
		    							}
		                                } while (choice4!=5);
		                                break;
		                                
		    							
		        
		                            case 3:
		                                System.out.println("---- Remove hospital staff ----");
		                                // Prompt the user to enter the User ID of the staff member to remove
		                                System.out.print("Enter the User ID of the staff member to remove: ");
		                                String userID2 = sc.nextLine();

		                                // Attempt to remove the staff member from the staffInventory
		                                boolean success = sList.removeStaff(userID2);

		                                // Optionally, update the CSV file if the staff member was removed
		                                if (success) {
		                                    System.out.println("User ID " + userID2 + " removed.");
		                                }

		                                else {
		                                    System.out.println("User ID " + userID2 + " does not exist.");
		                                    // Close the scanner
		                                }
		                                break;
		                            case 4:
		                                System.out.println("Going back..."); 
		                                break;
		                            default:
										System.out.println("Invalid option entered. Please try again. ");
										break;
		                        }
		                        //((Administrator) user).updateStaffInv();
		                        
		                    } while (choice3!=4);
		                    break;
		                case 3:
		                    System.out.println("Going back..."); 
		                    break;
		                default:
							System.out.println("Invalid option entered. Please try again. ");
							break;
		            }
		            }  while (choice2!=3);
		            break;
		        case 2:
		        	int choicey, choicez, apptamt, choicef = 0;
		        	Staff tttStaff = null;
		        	Doctor tttDoc = null;
		        	Patient tttPat = null;
		        	boolean IDexists = false;
		        	String docID, patID = null;
		        	 do  {
		        	       System.out.println("1. View Appointment Details");
		        	       System.out.println("2. Manage Appointment Details");
		        	       System.out.println("3. Back");
		        	       System.out.println("Select option: ");
		        	       
		        	       while (!sc.hasNextInt()) {
								System.out.println("Invalid input! Please enter a valid integer.");
								sc.next(); // Consume the invalid input
							}
		        	       
		        	       choicey = sc.nextInt();
		        	       switch (choicey) {
		        	           case 1:
		        	                System.out.println("---------View Appointment Details----------");
		        	                do {
		        	                    System.out.println("1. Filter by doctor");
		        	                    System.out.println("2. Filter by patient");
		        	                    System.out.println("3. Filter by Appointment ID");
		        	                    System.out.println("4. Back");
		        	                    System.out.println("Select option: ");
		        	                    
		        	                    while (!sc.hasNextInt()) {
		    								System.out.println("Invalid input! Please enter a valid integer.");
		    								sc.next(); // Consume the invalid input
		    							}
		        	                    
		        	                    choicez = sc.nextInt();
		        	                    switch (choicez) {
		        	                        case 1:
		        	                        	IDexists = false;
		        	                            System.out.println("---------Filter by doctor----------");
		        	                            sc.nextLine();
		        	                            do {
			        	                            System.out.println("Enter doctor ID: ");
			        	                            docID = sc.nextLine();
			        	                            for (Staff s:staffList) {
			        	                            	if (s.getID().equals(docID)) {
			        	                            		tttStaff = s;
			        	                            		tttDoc = (Doctor) tttStaff;
			        	                            		IDexists = true;
			        	                            	}
			        	                            }
			        	                            if(!IDexists) {
			        	                            	System.out.println("ID does not exist!");
			        	                            }
		        	                            } while(!IDexists);
		        	                           
    	                                        System.out.println("Viewing appointments for " + docID + " " + tttDoc.getName());
    	                                        apptamt = 0;
    	                						
    	                						for (Appointment a : appointmentList) {
    	                							if (a.getDoctorID().equals(docID)) {
    	                								System.out.println();
    	                								System.out.println("Appointment ID	: " + a.getID());
    	                								System.out.println("Date			: " + a.getDate());
    	                								System.out.println("Time 			: " + a.getTime());
    	                								System.out.println("Patient			: " + a.getPatient());
    	                								System.out.println("Status			: " + a.getStatus());
    	                								if(a.getOutcome()==null) {
    	                									System.out.println("Outcome			: N/A, Appointment not completed");
    	                								}
    	                								else {
    	                								System.out.println("Outcome			: " + a.getOutcome());
    	                								}
    	                								apptamt++;
    	                							}
    	                						}
    	                						
    	                						if (apptamt == 0) System.out.println("No appointment found!");
    	                						System.out.println();
    	                                        break;
		        	                            
		        	                        case 2:
		        	                        	IDexists = false;
		        	                        	System.out.println("---------Filter by patient----------");
		        	                            sc.nextLine();
		        	                            do {
			        	                            System.out.println("Enter patient ID: ");
			        	                            patID = sc.nextLine();
			        	                            for (Patient p:patientList) {
			        	                            	if (p.getID().equals(patID)) {
			        	                            		tttPat = p;
			        	                            		IDexists = true;
			        	                            	}
			        	                            }
			        	                            if(!IDexists) {
			        	                            	System.out.println("ID does not exist!");
			        	                            }
		        	                            } while(!IDexists);
		        	                           
    	                                        System.out.println("Viewing appointments for " + patID + " " + tttPat.getName());
    	                                        apptamt=0;
    	                						for (Appointment a : appointmentList) {
    	                							if (a.getPatientID().equals(patID)) { // list appointments that are in the future and under dr's name
    	                								System.out.println();
    	                								System.out.println("Appointment ID	: " + a.getID());
    	                								System.out.println("Date			: " + a.getDate());
    	                								System.out.println("Time 			: " + a.getTime());
    	                								System.out.println("Patient			: " + a.getPatient());
    	                								System.out.println("Status			: " + a.getStatus());
    	                								if(a.getOutcome()==null) {
    	                									System.out.println("Outcome			: N/A, Appointment not completed");
    	                								}
    	                								else {
    	                								System.out.println("Outcome			: " + a.getOutcome());
    	                								}
    	                								apptamt++;
    	                							}
    	                						}
    	                						
    	                						if (apptamt == 0) System.out.println("No appointment found!");
    	                						System.out.println();
    	                                        break;
		        	                                    
		        	                            
		        	                            
		        	                        case 3:
		        	                            System.out.println("---------Filter by Appointment ID----------");
		        	                            sc.nextLine();
		        	                            System.out.println("Enter Appointment ID: ");
		        	                            String apptID = sc.nextLine();
		        	                            
    	                                        apptamt=0;
    	                						for (Appointment a : appointmentList) {
    	                							if (a.getID().equals(apptID)) { // list appointments that are in the future and under dr's name
    	                								System.out.println();
    	                								System.out.println("Appointment ID	: " + a.getID());
    	                								System.out.println("Date			: " + a.getDate());
    	                								System.out.println("Time 			: " + a.getTime());
    	                								System.out.println("Patient			: " + a.getPatient());
    	                								System.out.println("Status			: " + a.getStatus());
    	                								if(a.getOutcome()==null) {
    	                									System.out.println("Outcome			: N/A, Appointment not completed");
    	                								}
    	                								else {
    	                								System.out.println("Outcome			: " + a.getOutcome());
    	                								}
    	                								apptamt++;
    	                							}
    	                						}
    	                						
    	                						if (apptamt == 0) System.out.println("No appointment found!");
    	                						System.out.println();
    	                                        
		        	                          
		        	                            break;
		        	                        case 4:
		        	                            System.out.println("Going back...");
		        	                            break;
		        	                        default:
		        								System.out.println("Invalid option entered. Please try again. ");
		        								break;
		        	                    }
		        	                } while (choicez!=4);
		        	                
		        	                break;
		        	           case 2:
		        	                System.out.println("---------Manage Appointment Details----------");
	///////////////////////////////////EDIT HERE!!!!!////
		        	                
	
   	                            	System.out.println("Enter Appointment ID: ");
   	                            	sc.nextLine();
   	                            	String apptID = sc.nextLine();
   	                            	apptamt = 0;
	           						for (Appointment a : appointmentList) {
	           							if (a.getID().equals(apptID)) { // print current details of appointment to be edited
	           								
	           								String tempPatientID = null;
	           								String tempDoctorID = null;
	           								
	           								for (Patient tempPat : patientList) {
	           									if (tempPat.getName().equals(a.getPatient())){
	           										tempPatientID = tempPat.getID();
	           									}
	           								}
	           								
	           								for (Staff tempDoc : staffList) {
	           									if (tempDoc.getName().equals(a.getDoctor())){
	           										tempDoctorID = tempDoc.getID();
	           									}
	           								}
	           								
	           								System.out.println();
	           								System.out.println("Current Appointment Details:");
	           								System.out.println("==================================");
	           								System.out.println("(1) Patient ID			: " + tempPatientID);
	           								System.out.println("(2) Doctor ID			: " + tempDoctorID);
	           								System.out.println("(3) Appointment Status	 	: " + a.getStatus());
	           								System.out.println("(4) Date			: " + a.getDate());
	           								System.out.println("(5) Time			: " + a.getTime());
	           								if(a.getStatus().equals("Confirmed") || a.getStatus().equals("Pending") || a.getStatus().equals("Cancelled")) {
	           									System.out.println("(6) Outcome Record		: N/A");
	           								}
	           								else {
	           									System.out.println("(6) Outcome Record		: " + a.getOutcome());
	           								}
	           								System.out.println("(7) Back");
	           								System.out.println("==================================");
	           								apptamt++;
	           								
	           								break;
	           							}
	           						}
	           							
	           						if (apptamt == 0) {
	           							System.out.println("No appointment found!");
	           							System.out.println();
	           						}
	           						
	           						else {
	           							for (Appointment a : appointmentList) {
		           							if (a.getID().equals(apptID)) {
			           							

			           							int manageAptChoice = 0;
			           							Patient tempPat = null;
			           							Staff tempStaff = null;
			           							boolean patExists = false;
			           							boolean staffExists = false;
			           							do {
			           								System.out.println("Choose option from (1)-(7) to edit: ");
			           								while (!sc.hasNextInt()) {
			           									System.out.println("Invalid input! Please enter a valid integer.");
			           									sc.next(); // Consume the invalid input
			           								}
				           							manageAptChoice = sc.nextInt();
			           								switch(manageAptChoice) {
			           								case 1:
			           									System.out.println("Current Patient ID: " + a.getPatientID() + ", Patient Name: " + a.getPatient());
			           									System.out.println("Enter new Patient ID: ");
			           									sc.nextLine();
			           									String newPatID = sc.nextLine();
			           									
			           									
			           									for(Patient p : patientList) {
			           										if (p.getID().equals(newPatID)){
			           											tempPat = p;
			           											patExists = true;
			           											break;
			           										}
			           									}
			           									if (patExists) {
			           										a.updatePatientID(newPatID);
			           										a.updatePatientName(tempPat.getName());
			           										
			           										System.out.println("Patient ID updated to " + newPatID + ", Patient Name automatically updated to " + a.getPatient());
			           									}
			           									break;
			           								case 2:
			           									Doctor newdoctor = null;
			           									Doctor olddoctor = null;
			           									System.out.println("Current Doctor ID: " + a.getDoctorID() + ", Doctor Name: " + a.getDoctor());
			           									System.out.println("Enter new Doctor ID: ");
			           									sc.nextLine();
			           									String newDocID = sc.nextLine();
			           									for(Staff s : staffList) {
			           										if (s.getID().equals(newDocID)){
			           											tempStaff = s;
			           											newdoctor = (Doctor)s;
			           											staffExists = true;
			           											break;
			           										}
			           									}
			           									newdoctor = (Doctor) staffList.get(sList.findStaffIndexID(newDocID));
			           									olddoctor = (Doctor) staffList.get(sList.findStaffIndexID(a.getDoctorID()));
			           									
			           									if (staffExists) {
			           										a.updateDoctorID(newDocID);
			           										a.updateDoctorName(tempStaff.getName());
			           										olddoctor.removeAppointment(a);
			           										newdoctor.addAppointment(a);
			           										
			           										System.out.println("Doctor ID updated to " + newDocID + ", Doctor Name automatically updated to " + a.getDoctor());
			           									}
			           									break;

			           								case 3:
			           									Doctor tempDoc = null;
			           									Staff tStaff = null;
			           									for(Staff s:staffList) {
			           										if (s.getID().equals(a.getDoctorID())){
			           											tStaff = s;
			           										}
			           									}
			           									tempDoc = (Doctor) staffList.get(sList.findStaffIndexID(a.getDoctorID()));
			           									boolean isDefault = true;
				           									if(a.getStatus().equals("Cancelled")) {
				           										System.out.println("Current status: Cancelled");
				           										System.out.println("Enter new status of Appointment (Pending/Confirmed:) ");
					           									String newStatusChoice = null;
					           									sc.nextLine();		
						           								do{
						           									newStatusChoice = sc.nextLine();
					           										switch(newStatusChoice){
						           									case "Pending":
						           										if (tempDoc.addAppointment(a)) {
						           											a.updateStatus(newStatusChoice);
						           										}
						           										
						           										break;
						           									case "Cancelled":
						           										System.out.println("No change in status.");	
						           										break;
						           									case "Confirmed":
						           										if (tempDoc.addAppointment(a)) {
						           											a.updateStatus(newStatusChoice);
						           										}
						           					           										
						           										
						           										break;
						           									default: System.out.println("Invalid input.");
						           									}
						           								}while(!isDefault);
				           									}
				           									
				           									else if(a.getStatus().equals("Confirmed")) {
				           										System.out.println("Current status: Confirmed");
				           										System.out.println("Enter new status of Appointment (Pending/Cancelled:) ");
					           									String newStatusChoice = null;
					           									sc.nextLine();		
						           								do{
						           									newStatusChoice = sc.nextLine();
					           										switch(newStatusChoice){
						           									case "Pending":

						           									//tempDoc.removeAppointment(a);
						           										a.updateStatus(newStatusChoice);
						           										break;
						           									case "Cancelled":
						           										a.updateStatus(newStatusChoice);
						           										tempDoc.removeAppointment(a);
						           										break;
						           									case "Confirmed":
						           										System.out.println("No change in status.");	
						           										break;
						           									default: System.out.println("Invalid input.");
						           									}
						           								}while(!isDefault);
				           									}
				           									
				           									else if(a.getStatus().equals("Pending")) {
				           										System.out.println("Current status: Pending");
				           										System.out.println("Enter new status of Appointment (Confirmed/Cancelled:) ");
					           									String newStatusChoice = null;
					           									sc.nextLine();		
						           								do{
						           									newStatusChoice = sc.nextLine();
						           									switch(newStatusChoice){
						           									case "Pending":
	
						           										System.out.println("No change in status.");	
						           										break;
						           									case "Cancelled":
						           										tempDoc.removeAppointment(a);
						           										a.updateStatus(newStatusChoice);
						           										break;
						           									case "Confirmed":
						           										a.updateStatus(newStatusChoice);
						           										tempDoc.addAppointment(a);
						           										break;
						           									default: System.out.println("Invalid input.");
						           									}
						           								}while(!isDefault);
				           									}
				           									
				           								
			           									break;
			           								case 4:
			           									int tChoice, dChoice = 0;
			           									
			           									doc = (Doctor) staffList.get(sList.findStaffIndexID(a.getDoctorID()));
			           									pat = (Patient) patientList.get(pList.findPatientIndexID(a.getPatientID()));
			           									
			           									do {
			           										for (int i=0; i<7; i++) {
			           											System.out.format("%d. %s\n", i+1, doc.getSchedule()[i][1].getDate());
			           										}
			           										System.out.println("8. Back");
			           										System.out.println("Choose new date: ");
			           										dChoice = sc.nextInt();
			           										
			           										if (dChoice > 0 && dChoice < 8) {
			           											do {
			           												System.out.format("%s\n", doc.getSchedule()[dChoice-1][1].getDate());
			           												for(int j=0; j<7; j++) {
			           													String slotAvailability;
			           													if (doc.getSchedule()[dChoice-1][j].getAvail()) {
			           														slotAvailability = "Available";
			           													}
			           													else {
			           														slotAvailability = "Unavailable";
			           													}
			           													System.out.format("%d. %s\n", j+1, doc.getSchedule()[dChoice-1][j].getTime() + " : " + slotAvailability);
			           													}
			           												System.out.println((7+1) + ". Back");
			           												System.out.println("Choose time: ");
			           												tChoice = sc.nextInt();
			           												
			           												if (tChoice > 0 && tChoice < 7+1) {
			           													if (doc.getSchedule()[dChoice-1][tChoice-1].getAvail()) {
			           														Appointment appoint = new Appointment(pat, doc, doc.getSchedule()[dChoice-1][tChoice-1].getDateTime(), doc.getSchedule()[dChoice-1][tChoice-1].getTime());
			           														System.out.println("Appointment successfully booked with " + doc.getName() + " at " + doc.getSchedule()[dChoice-1][tChoice-1].getDate() + ", " + doc.getSchedule()[dChoice-1][tChoice-1].getTime());
			           														appoint.updateStatus("Pending");
			           														appointmentList.add(appoint);
			           														doc.addAppointment(appoint);
			           														doc.removeAppointment(a);
			           														a.updateStatus("Cancelled");
			           														
			           													}
			           													else {
			           														System.out.println("Slot is unavailable!");
			           													}
			           												}
			           												else if(tChoice > 7+1 || tChoice < 1){
			           													System.out.println("Invalid time! Please enter again.");
			           												}
			           												
			           											} while (tChoice > 7+1 || tChoice < 1);
			           										}
			           											
			           										else if (dChoice > 8 || dChoice < 1){
			           												System.out.println("Date invalid! Please enter again.");
			           										}
			           									} while (dChoice > 8 || dChoice < 1); 
			           									
			           									
			           									System.out.println();
			           									break;
			           								case 5:
			           									int timeChoice, dateChoice = 0;
			           									
			           									doc = (Doctor) staffList.get(sList.findStaffIndexID(a.getDoctorID()));
			           									pat = (Patient) patientList.get(pList.findPatientIndexID(a.getPatientID()));
			           									
			           									do {
			           										for (int i=0; i<7; i++) {
			           											System.out.format("%d. %s\n", i+1, doc.getSchedule()[i][1].getDate());
			           										}
			           										System.out.println("8. Back");
			           										System.out.println("Choose new date: ");
			           										dateChoice = sc.nextInt();
			           										
			           										if (dateChoice > 0 && dateChoice < 8) {
			           											do {
			           												System.out.format("%s\n", doc.getSchedule()[dateChoice-1][1].getDate());
			           												for(int j=0; j<7; j++) {
			           													String slotAvailability;
			           													if (doc.getSchedule()[dateChoice-1][j].getAvail()) {
			           														slotAvailability = "Available";
			           													}
			           													else {
			           														slotAvailability = "Unavailable";
			           													}
			           													System.out.format("%d. %s\n", j+1, doc.getSchedule()[dateChoice-1][j].getTime() + " : " + slotAvailability);
			           													}
			           												System.out.println((7+1) + ". Back");
			           												System.out.println("Choose time: ");
			           												timeChoice = sc.nextInt();
			           												
			           												if (timeChoice > 0 && timeChoice < 7+1) {
			           													if (doc.getSchedule()[dateChoice-1][timeChoice-1].getAvail()) {
			           														Appointment appoint = new Appointment(pat, doc, doc.getSchedule()[dateChoice-1][timeChoice-1].getDateTime(), doc.getSchedule()[dateChoice-1][timeChoice-1].getTime());
			           														System.out.println("Appointment successfully booked with " + doc.getName() + " at " + doc.getSchedule()[dateChoice-1][timeChoice-1].getDate() + ", " + doc.getSchedule()[dateChoice-1][timeChoice-1].getTime());
			           														appoint.updateStatus("Pending");
			           														appointmentList.add(appoint);
			           														doc.addAppointment(appoint);
			           														doc.removeAppointment(a);
			           														a.updateStatus("Cancelled");
			           														
			           													}
			           													else {
			           														System.out.println("Slot is unavailable!");
			           													}
			           												}
			           												else if(timeChoice > 7+1 || timeChoice < 1){
			           													System.out.println("Invalid time! Please enter again.");
			           												}
			           												
			           											} while (timeChoice > 7+1 || timeChoice < 1);
			           										}
			           											
			           										else if (dateChoice > 8 || dateChoice < 1){
			           												System.out.println("Date invalid! Please enter again.");
			           										}
			           									} while (dateChoice > 8 || dateChoice < 1); 
			           									
			           									
			           									System.out.println();
			           									break;
			           								case 6:
			           									           									
			           									if(a.getStatus().equals("Confirmed") || a.getStatus().equals("Pending") || a.getStatus().equals("Cancelled")) {
				           									System.out.println("Appointment not completed, no outcome to update.");
				           								}
			           									else {
			           										System.out.println("Current outcome: " + a.getOutcome());
			           										System.out.println("Enter new outcome: ");
			           										String newOutcome = null;
			           										newOutcome = sc.nextLine();
			           										a.updateOutcome(newOutcome);
			           									}
			           									break;
			           								case 7:
			           									System.out.println("Going back...");
			           									break;
			           								default:
			           									System.out.println("Invalid option entered. Please try again. ");
			           									break;
			           								}
			           							} while (manageAptChoice < 1 || manageAptChoice > 7);
			           							break;
		           							}
	           							}
	           						}
	           						
		        	                break;
		        	           case 3:
		        	               System.out.println("Going back...");
		        	                break;
		        	           default:
									System.out.println("Invalid option entered. Please try again. ");
									break;
		        	          
		        	            
		        	       }
		        	       
		        	   } while (choicey != 3);
		        	   
		            break;
		        case 3:
		        	System.out.println("-------View and Manage Medication Inventory-------");
		            int choicew, choicev;
		            do {
		            System.out.println("Select action: ");
		            System.out.println("1. View Medicine Inventory");
		            System.out.println("2. Manage Medicine Inventory");
		            System.out.println("3. Back");
		            while (!sc.hasNextInt()) {
						System.out.println("Invalid input! Please enter a valid integer.");
						sc.next(); // Consume the invalid input
					}
		            choicew = sc.nextInt();
		            switch(choicew) {
		                case 1:
		                    System.out.println("Viewing medication...");
		                    ((Administrator) admin).getMedicineInventory().printList();
		                    break;
		                case 2:
		                     do {
		                        System.out.println("Select action: ");
		                        System.out.println("1. Add Medication");
		                        System.out.println("2. Update Medication Fields");
		                        System.out.println("3. Back");
		                        while (!sc.hasNextInt()) {
									System.out.println("Invalid input! Please enter a valid integer.");
									sc.next(); // Consume the invalid input
								}
		                        choicev = sc.nextInt();
		                        switch(choicev) {
		                            case 1:
		                                System.out.println("Adding medication...");
		                                ((Administrator) admin).addMedicine();
		                                break;
		                            case 2:
		                            	sc.nextLine();
		                                System.out.println("Updating medication...");
		                                int choice4, medCount=0; 
													medSelect=0;
													medAmount=0;
		                                boolean foundm = false;
		                                String desiredMedName;
		                                System.out.println("---- Update Medication ----");
		                                
		                                do{
		                                medAmount = 0;
		                                System.out.println("Enter name of medication to update:");
		                                desiredMedName = sc.nextLine();
		                                for (Medicine medicine : medicineList) {
		            	                    if (medicine.getName().equalsIgnoreCase(desiredMedName)) {
		            	                        medSelect = medAmount;
		            	                        foundm=true;
		            	                        break;
		            	                    }
		            	                    medAmount++;
		            	                }
		                                if (!foundm) System.out.println("Medicine not found! Please try again.");
		                                } while (!foundm);
		                                
		                                Medicine meddo = medicineList.get(medSelect);
		                                
										System.out.println("Name		: " + meddo.getName());
										System.out.println("Quantity		: " + meddo.getQuantity());
										System.out.println("Low Stock Alert		: " + meddo.getLowStockAlertAmt());
	
								
		                                
		                                do {
		                                System.out.println("Select item to edit: ");
		    							System.out.println("1. Name");
		    							System.out.println("2. Quantity");
		    							System.out.println("3. Low Stock Alert");
		    							
		    							System.out.println("4. Back");
		    							while (!sc.hasNextInt()) {
		    								System.out.println("Invalid input! Please enter a valid integer.");
		    								sc.next(); // Consume the invalid input
		    							}
		    							choice4 = sc.nextInt();
		    							sc.nextLine();
		    							switch(choice4){
		    							    case 1:
		    							        System.out.println("Enter new medicine name:");
		    							        String newfield = sc.nextLine();
		    							        meddo.changeName(newfield);
		    							        
		    							        break;
		    							    case 2:
		    							        System.out.println("Enter new medicine quantity:");
		    							        int newAmount = sc.nextInt();
		    							        meddo.changeQuantity(newAmount);
		    							        sc.nextLine();
		    							        break;
		    							    case 3:
		    							        System.out.println("Enter new low stock alert threshold:");
		    							        newAmount = sc.nextInt();
		    							        meddo.changeLowStockAlert(newAmount);
		    							        sc.nextLine();
		    							        break;
		    							    
		    							    case 4: 
		    							        System.out.println("Going back...");
		    							        break;
		    							        
		    								default:
		    									System.out.println("Invalid option entered. Please try again. ");
		    									break;
		    							}
		                                } while (choice4!=4);
		                                break;
		                               
		                            case 3:
		                                System.out.println("Going back...");
		                                break;
		                                
									default:
										System.out.println("Invalid option entered. Please try again. ");
										break;
		                        }
		                
		            } while (choicev!=3);
		            
		                    break;
		                case 3:
		                    System.out.println("Going back...");
		                    break;
		                    
						default:
							System.out.println("Invalid option entered. Please try again. ");
							break;
		                    
		            }

		            }while (choicew!=3);
		            break;
		        case 4: 
		        	int choicex;
		        	do {
		        		
			        	System.out.println("--------- Approve replenishment requests ---------");
			        	System.out.println("1. View approved replenishment requests");
			        	System.out.println("2. View pending replenishment requests");
			        	System.out.println("3. Approve pending replenishment requests");
			        	System.out.println("4. Back");
			        	System.out.println("Enter selection:");
			        	while (!sc.hasNextInt()) {
							System.out.println("Invalid input! Please enter a valid integer.");
							sc.next(); // Consume the invalid input
						}
			        	choicex = sc.nextInt();
		        		switch(choicex) {
		        		case 1:
		        			boolean anyrequests = false;
		        			System.out.println("Approved replenishment requests: ");
		        			System.out.printf("%-10s %-20s %-10s %-20s\n","restockID","Medicine","Amount","Status");
							for (RestockForm rF : restockList) {
								if (rF.isFulfilled() == true) {
								rF.printFormDetails();
								anyrequests = true;
								}
							}
							if (anyrequests == false) {
								System.out.println("There are no approved replenishment requests!");
							}
		        			break;
		        		case 2:
		        			System.out.println("Pending replenishment requests: ");
		        			boolean anyrequest = false;
		        
		        			System.out.printf("%-10s %-20s %-10s %-20s\n","restockID","Medicine","Amount","Status");
							for (RestockForm rF : restockList) {
								if (rF.isFulfilled() == false) {
								rF.printFormDetails();
								anyrequest = true;
								}
							}
							if (anyrequest == false) {
								System.out.println("There are no pending replenishment requests!");
							}
		        			break;
		        		case 3:
		        			System.out.println("Pending replenishment requests: ");
		        	
		        			boolean anyreques = false, anyrestockID = false;
		        			int approveID = -1;
		        			
		        			
		        			System.out.printf("%-10s %-20s %-10s %-20s\n","restockID","Medicine","Amount","Status");
							for (RestockForm rF : restockList) {
								if (rF.isFulfilled() == false) {
								rF.printFormDetails();
								anyreques = true;
								}
							}
							if (anyreques == false) {
								System.out.println("There are no pending replenishment requests!");
								break;
							}
							
							do {
		        			System.out.println("Enter Restock ID of request to approve: ");
		        			approveID = sc.nextInt();
		        			anyrestockID = false;
		        			for (RestockForm rF : restockList) {
								if (rF.getRestockID() == approveID && rF.getFulfilled() == false) {
				
									((Administrator) admin).plusStock(rF.getMedicationName(), rF.getRestockAmount());
									rF.setFulfilled(true);
									mList.printList();
									anyrestockID = true;
									break;
				
								}
								
							}
		        			
		        			if (anyrestockID == false) {
		        				System.out.println("This Restock ID does not exist!");
		        			}
							} while (anyrestockID == false);
		        			
		        			break;
		        		case 4:
		        			System.out.println("Going back...");
		        			break;
						default:
							System.out.println("Invalid option entered. Please try again. ");
							break;
		        		}
		        	} while (choicex!=4);

		            break;
						case 5:
							String oldPas, newPas;
							sc.nextLine();
							System.out.println("Please enter your current password: ");
							oldPas = sc.nextLine();
							System.out.println("Please enter your new password: ");
							newPas = sc.nextLine();

							if(!user.changePassword(oldPas,newPas))
							{System.out.println("Password is incorrect");}
							break;
							case 6:
								System.out.println("Logging out...");
								loggedIn = false;
								break;
							default:
								System.out.println("Invalid option entered. Please try again. ");
								break;
						}
						} while (admchoice!=6);

							break;
				
			case "Patient":
				Patient patient = (Patient) user;
				int counting = 0;
				int patientChoice = 0;
				String docID = "";
				do {
					System.out.println();
					System.out.println("===============================================");
					System.out.println("Hello " + patient.getName() + ", welcome to the patient menu");
					System.out.println("1. View Medical Records");
					System.out.println("2. Update Personal Information");
					System.out.println("3. View Available Appointment Slots");
					System.out.println("4. Schedule Appointment");
					System.out.println("5. Reschedule Appointment");
					System.out.println("6. Cancel Appointment");
					System.out.println("7. View Scheduled Appointments");
					System.out.println("8. View Past Appointment Records");
					System.out.println("9. Change Password");
					System.out.println("10. Logout");
					System.out.println("===============================================");

					while (true) {
						System.out.print("Enter your choice: ");
						if (sc.hasNextInt()) { // Check if input is an integer
							patientChoice = sc.nextInt();
							break; // Exit the validation loop
						} else {
							System.out.println("Invalid input! Please enter a number.");
							sc.next(); // Clear the invalid input
						}
					}
					
					switch(patientChoice) {
					case 1: // view med record
						System.out.println("Name		: " + patient.getName());
						System.out.println("DOB		: " + dateFormat.format(patient.getDOB()));
						System.out.println("Email		: " + patient.getEmail());
						System.out.println("Contact no.	: " + patient.getContactNum());
						System.out.println("Blood type	: " + patient.getBloodType());
						System.out.println("Previous appointment outcomes:");
						boolean trig = false;
						for (Appointment a : appointmentList) {
							if (patient.getName().equals(a.getPatient())) {
								System.out.println(a.getDate() + ": " + a.getOutcome()); trig = true;
							}
						}
						if(!trig) { System.out.println("~ No appointment outcomes found!"); }
						System.out.println();
						break;
						
					case 2: // update particular
						patient.updatePart();
						System.out.println();
						break;
						
					case 3: // view avail apt slots
						docSelect = 0;
						docAmount = 0;
						counting = 0;
						System.out.println("Select Doctor:");
						for (Staff s : staffList) {
							if (s.getRole().equals("Doctor")) {
								docAmount++;
								System.out.println(docAmount + ". dr. " + s.getName());
							}	
						}

						do {
							System.out.println("Select doctor: ");

							// Check if the next input is an integer
							if (sc.hasNextInt()) {
								docSelect = sc.nextInt();

								if (docSelect > docAmount) {
									System.out.println("Invalid doctor! Please enter again.");
								}
							} else {
								System.out.println("Invalid input! Please enter a valid integer.");
								sc.next(); // Consume the invalid input to avoid an infinite loop
								docSelect = -1; // Reset docSelect to an invalid value to continue the loop
							}
						} while (docSelect > docAmount || docSelect <= 0); // Ensure docSelect is valid
						
						for (Staff s : staffList) {
							if (s.getRole().equals("Doctor")) {
								counting++;
								if (counting == docSelect) {
									docID = s.getID();
									break;
								}
							}
						}
						
						doc = (Doctor) staffList.get(sList.findStaffIndexID(docID));
						
						doc.getAvailSlots();
						
						System.out.println("==================================================");
						break;
						
					case 4: // make apt
						docSelect = 0;
						docAmount = 0;
						counting = 0;
						int dChoice, tChoice;
						
						for (Staff s : staffList) {
							if (s.getRole().equals("Doctor")) {
								docAmount++;
								System.out.println(docAmount + ". dr. " + s.getName());
							}
						}

						do {
							System.out.println("Select doctor: ");

							if (sc.hasNextInt()) {
								docSelect = sc.nextInt();

								if (docSelect > docAmount || docSelect <= 0) {
									System.out.println("Invalid doctor! Please enter again.");
								}
							} else {
								System.out.println("Invalid input! Please enter an integer.");
								sc.next(); // Consume the invalid input to prevent an infinite loop
								docSelect = -1; // Set to a value that will re-trigger the loop
							}

						} while (docSelect > docAmount || docSelect <= 0);
						
						for (Staff s : staffList) {
							if (s.getRole().equals("Doctor")) {
								counting++;
								if (counting == docSelect) {
									docID = s.getID();
									break;
								}
							}
						}
						
						doc = (Doctor) staffList.get(sList.findStaffIndexID(docID));

						do {
							for (int i = 0; i < 7; i++) {
								System.out.format("%d. %s\n", i + 1, doc.getSchedule()[i][1].getDate());
							}
							System.out.println("8. Back");
							System.out.println("Choose date: ");

							// Input validation for dChoice
							while (!sc.hasNextInt()) {
								System.out.println("Invalid input! Please enter an integer.");
								sc.next(); // Consume the invalid input
							}
							dChoice = sc.nextInt();

							if (dChoice > 0 && dChoice < 8) {
								do {
									System.out.format("%s\n", doc.getSchedule()[dChoice - 1][1].getDate());
									for (int j = 0; j < 7; j++) {
										String slotAvailability;
										if (doc.getSchedule()[dChoice - 1][j].getAvail()) {
											slotAvailability = "Available";
										} else {
											slotAvailability = "Unavailable";
										}
										System.out.format("%d. %s\n", j + 1, doc.getSchedule()[dChoice - 1][j].getTime() + " : " + slotAvailability);
									}
									System.out.println((7 + 1) + ". Back");
									System.out.println("Choose time: ");

									// Input validation for tChoice
									while (!sc.hasNextInt()) {
										System.out.println("Invalid input! Please enter an integer.");
										sc.next(); // Consume the invalid input
									}
									tChoice = sc.nextInt();

									if (tChoice > 0 && tChoice < 7 + 1) {
										if (doc.getSchedule()[dChoice - 1][tChoice - 1].getAvail()) {
											Appointment appoint = new Appointment(patient, doc, doc.getSchedule()[dChoice - 1][tChoice - 1].getDateTime(), doc.getSchedule()[dChoice - 1][tChoice - 1].getTime());
											System.out.println("Appointment successfully booked with dr. " + doc.getName() + " at " + doc.getSchedule()[dChoice - 1][tChoice - 1].getDate() + ", " + doc.getSchedule()[dChoice - 1][tChoice - 1].getTime());
											appoint.updateStatus("Pending");
											appointmentList.add(appoint);
											doc.addAppointment(appoint);
										} else {
											System.out.println("Slot is unavailable!");
										}
									} else if (tChoice > 7 + 1 || tChoice < 1) {
										System.out.println("Invalid time! Please enter again.");
									}

								} while (tChoice > 7 + 1 || tChoice < 1);
							} else if (dChoice > 8 || dChoice < 1) {
								System.out.println("Date invalid! Please enter again.");
							}
						} while (dChoice > 8 || dChoice < 1);



						System.out.println();
						break;
						
					case 5: // reschedule apt
						aptAmount = 0;
						String aptIDSelect = null;
						
						for (Appointment a : appointmentList) {
							if (patient.getName().equals(a.getPatient()) && (a.getStatus().equals("Confirmed") || a.getStatus().equals("Pending"))) { 
								aptAmount++;
								System.out.println(a.getID() + ": (Doctor: dr. " + a.getDoctor() + " )" + a.getDate() +  " " + a.getTime() + "| Status: " + a.getStatus()); 
							}
						}
						if (aptAmount != 0) {
							medAmount = 0;
							medSelect = 0;
							Doctor cancelledDoc = null;
							apt = null;
								
							do {					
								System.out.println("Enter full appointment ID to reschedule: ");
								aptAmount = 0;
								aptIDSelect = sc.nextLine();
								for (Appointment a : appointmentList) {
									if (a.getID().equals(aptIDSelect)) {
										apt = a;
										aptAmount++;
									}
								}
								
								if (aptAmount == 0) System.out.println("Invalid appointment! Please enter again.");
							} while (aptAmount == 0);
								
							
							
							//give option to reschedule to new avail slot
							System.out.println("Rescheduling appointment...... ");
							apt.updateStatus("Cancelled");
							for (Staff s : staffList) {
								if (apt.getDoctor().equals(s.getName())) {
									cancelledDoc = (Doctor) s;
								}
							}
							System.out.println("Appointment with dr. " + apt.getDoctor() + " at " + apt.getDate() + " " + apt.getTime() + " successfully cancelled.");
							System.out.println("==================================================");
							cancelledDoc.removeAppointment(apt);
							
							docSelect = 0;
							docAmount = 0;
							dChoice = 0; 
							tChoice = 0;
							
							for (Staff s : staffList) {
								if (s.getRole().equals("Doctor")) {
									docAmount++;
									System.out.println(docAmount + ". dr. " + s.getName());
								}
								
								
							}
							
							do {
								System.out.println("Select doctor to reschedule to: ");
								docSelect = sc.nextInt();
								
								if (docSelect>docAmount) System.out.println("Invalid doctor! Please enter again.");
							} while (docSelect>docAmount);
							
							for (Staff s : staffList) {
								if (s.getRole().equals("Doctor")) {
									counting++;
									if (counting == docSelect) {
										docID = s.getID();
										break;
									}
								}
							}
							
							doc = (Doctor) staffList.get(sList.findStaffIndexID(docID));
							
							do {
								for (int i=0; i<7; i++) {
									System.out.format("%d. %s\n", i+1, doc.getSchedule()[i][1].getDate());
								}
								System.out.println("8. Back");
								System.out.println("Choose date: ");
								dChoice = sc.nextInt();
								
								if (dChoice > 0 && dChoice < 8) {
									do {
										System.out.format("%s\n", doc.getSchedule()[dChoice-1][1].getDate());
										for(int j=0; j<7; j++) {
											String slotAvailability;
											if (doc.getSchedule()[dChoice-1][j].getAvail()) {
												slotAvailability = "Available";
											}
											else {
												slotAvailability = "Unavailable";
											}
											System.out.format("%d. %s\n", j+1, doc.getSchedule()[dChoice-1][j].getTime() + " : " + slotAvailability);
											}
										System.out.println((7+1) + ". Back");
										System.out.println("Choose time: ");
										tChoice = sc.nextInt();
										
										if (tChoice > 0 && tChoice < 7+1) {
											if (doc.getSchedule()[dChoice-1][tChoice-1].getAvail()) {
												Appointment appoint = new Appointment(patient, doc, doc.getSchedule()[dChoice-1][tChoice-1].getDateTime(), doc.getSchedule()[dChoice-1][tChoice-1].getTime());
												System.out.println("Appointment successfully booked with dr. " + doc.getName() + " at " + doc.getSchedule()[dChoice-1][tChoice-1].getDate() + ", " + doc.getSchedule()[dChoice-1][tChoice-1].getTime());
												appointmentList.add(appoint);
												appoint.updateStatus("Pending");
												doc.addAppointment(appoint);
											}
										}
										else if(tChoice > 7+1 || tChoice < 1){
											System.out.println("Invalid time! Please enter again.");
										}
										
									} while (tChoice > 7+1 || tChoice < 1);
								}
									
								else if (dChoice > 8 || dChoice < 1){
										System.out.println("Date invalid! Please enter again.");
								}
							} while (dChoice > 8 || dChoice < 1); 
							
							
							System.out.println();
							break;
				
						}
						
						else System.out.println("No scheduled appointment.");
						System.out.println();
						break;
						
					case 6: // cancel apt
						aptAmount = 0;
						aptIDSelect = null;
						
						for (Appointment a : appointmentList) {
							if (patient.getName().equals(a.getPatient()) && (a.getStatus().equals("Confirmed") || a.getStatus().equals("Pending"))) { 
								aptAmount++;
								System.out.println(a.getID() + ": (Doctor: dr. " + a.getDoctor() + " )" + a.getDate() +  " " + a.getTime() + "| Status: " + a.getStatus());
							}
						}
						if (aptAmount != 0) {
							medAmount = 0;
							medSelect = 0;
							Doctor cancelledDoc = null;
							apt = null;
							sc.nextLine();
								
							do {					
								System.out.println("Enter full appointment ID to cancel: ");
								aptAmount = 0;
								aptIDSelect = sc.nextLine();
								for (Appointment a : appointmentList) {
									if (a.getID().equals(aptIDSelect)) {
										apt = a;
										aptAmount++;
									}
								}
								
								if (aptAmount == 0) System.out.println("Invalid appointment! Please enter again.");
							} while (aptAmount == 0);
								
							
							
							//give option to reschedule to new avail slot
							System.out.println("Cancelling appointment...... ");
							apt.updateStatus("Cancelled");
							for (Staff s : staffList) {
								if (apt.getDoctor().equals(s.getName())) {
									cancelledDoc = (Doctor) s;
								}
							}
							System.out.println("Appointment with dr. " + apt.getDoctor() + " at " + apt.getDate() + " " + apt.getTime() + " successfully cancelled.");
							System.out.println("==================================================");
							cancelledDoc.removeAppointment(apt);
						}
						else {
							System.out.println("No upcoming appointment to cancel.");
						}
						break;
						
					case 7: // view apt
						aptAmount = 0;
						
						System.out.println("Showing upcoming appointment status:");
						System.out.println("==================================================");
						for (Appointment a : appointmentList) {
							if (patient.getName().equals(a.getPatient()) && (a.getStatus().equals("Confirmed") || a.getStatus().equals("Pending"))) { 
								aptAmount++;
								System.out.println(a.getID() + ": (Doctor: dr. " + a.getDoctor() + " )" + a.getDate() +  " " + a.getTime() + "| Status: " + a.getStatus());
							}
						}
						if (aptAmount == 0) {
							System.out.println("No upcoming appointments scheduled.");
							System.out.println();
						}
						break;
						
					case 8: // view apt record
						aptAmount = 0;
						
						System.out.println("Past appointments and statuses:");
						System.out.println("==================================================");
						for (Appointment a : appointmentList) {
							if (patient.getName().equals(a.getPatient()) && a.getStatus().equals("Completed")) { 
								aptAmount++;
								System.out.println(a.getID() + ": (Doctor: dr. " + a.getDoctor() + " )" + a.getDate() +  " " + a.getTime() + "| Status: " + a.getStatus()); 
							}
						}
						if (aptAmount != 0) {
							
							apt = null;
								
							do {					
								System.out.println("Enter full appointment ID to display past outcome record: ");
								aptAmount = 0;
								sc.nextLine();
								aptIDSelect = sc.nextLine();
								for (Appointment a : appointmentList) {
									if (a.getID().equals(aptIDSelect)) {
										apt = a;
										aptAmount++;
									}
								}
								
								if (aptAmount == 0) System.out.println("Invalid appointment! Please enter again.");
							} while (aptAmount == 0);
							
							System.out.println("Appointment records of " + apt.getID());
							System.out.println("==================================================");
							System.out.println("Appointment ID		: " + apt.getID());
							System.out.println("Patient Name		: " + apt.getPatient());
							System.out.println("Doctor Name		: dr. " + apt.getDoctor());
							System.out.println("Date and Time		: " + apt.getDate() + " " + apt.getTime());
							System.out.println("Status			: " + apt.getStatus());
							System.out.println("Outcome			: " + apt.getOutcome());
							System.out.println();
							System.out.println("Prescribed Medicine");
							System.out.println("--------------------------------------------------");
							apt.printPrescription();
							System.out.println();
						}
						
						else {
							System.out.println("No past appointment record!");
							System.out.println();
						}
						break;
						
					case 9: // change password
						String oldPass, newPass;
						sc.nextLine();
						System.out.println("Please enter your current password: ");
						oldPass = sc.nextLine();
						System.out.println("Please enter your new password: ");
						newPass = sc.nextLine();

						if(!patient.changePassword(oldPass,newPass))
						{System.out.println("Password is incorrect");}
						break;
					case 10:
						System.out.println("Logging out...");
						sc.nextLine();
						loggedIn = false;
						System.out.println();
						break;
						
					default:
						System.out.println("Invalid choice! Please enter again.");
						break;
					}
				} while (patientChoice != 10);
				
				break;
			default: 
				loggedIn = false;
				System.out.println("User on Leave or otherwise. Redirecting...");
				break;
			}
		} while (!loggedIn); //go back to login page
	}

	/**
	 * Displays the login screen banner to the user.
	 * <p>
	 * This method prints an ASCII art banner for the hospital management system.
	 */

	private static void logInScreen(){
		System.out.println(
				"         _   _                 _ _        _ \n" +
				"   _    | | | | ___  ___ _ __ (_) |_ __ _| |\n" +
				" _| |_  | |_| |/ _ \\/ __| '_ \\| | __/ _` | |\n" +
				"|_   _| |  _  | (_) \\__ \\ |_) | | || (_| | |\n" +
				" _|_|__ |_| |_|\\___/|___/ .__/|_|\\__\\__,_|_|\n" +
				"|  \\/  | __ _ _ __   __ |_|__ _  ___ _ __   \n" +
				"| |\\/| |/ _` | '_ \\ / _` |/ _` |/ _ \\ '__|  \n" +
				"| |  | | (_| | | | | (_| | (_| |  __/ |     \n" +
				"|_|  |_|\\__,_|_| |_|\\__,_|\\__, |\\___|_|     \n" +
				"                          |___/             ");

	}
}
