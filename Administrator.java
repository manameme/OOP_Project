package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code Administrator} class represents a hospital administrator who manages
 * appointments, medicine inventory, and other administrative tasks.
 * <p>
 * It extends the {@code Staff} class and provides specific functionalities for
 * handling appointments and managing the hospital's medicine inventory.
 * </p>
 */

public class Administrator extends Staff {

    private List<Appointment> appointments;
    private List<MedicineInv> inventory;
    private MedicineInv medicineInventory;

    /**
     * Constructs an {@code Administrator} object with the specified details.
     *
     * @param userID    The unique identifier of the administrator.
     * @param name      The name of the administrator.
     * @param role      The role of the administrator.
     * @param gender    The gender of the administrator.
     * @param age       The age of the administrator.
     * @param password  The password for the administrator's account.
     */

    public Administrator(String userID, String name, String role, String gender, int age, String password) {
        super(userID, name, role, gender, age, password);

        this.appointments = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.medicineInventory = MedicineInv.getInstance();
    }



    public MedicineInv getMedicineInventory() {
        return this.medicineInventory;
    }


    // Appointment Management
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void updateAppointmentStatus(String appointmentID, String newStatus) {
        for (Appointment appointment : appointments) {
            if (appointment.getID().equals(appointmentID)) {
                appointment.updateStatus(newStatus);
                break;
            }
        }
    }

    public void viewAppointmentDetails(String appointmentID) {
        for (Appointment appointment : appointments) {
            if (appointment.getID().equals(appointmentID)) {
                System.out.println("Appointment ID: " + appointment.getID());
                System.out.println("Patient: " + appointment.getPatient());
                System.out.println("Doctor: " + appointment.getDoctor());
                System.out.println("Date: " + appointment.getDate());
                System.out.println("Time Slot: " + appointment.getTime());
                System.out.println("Status: " + appointment.getStatus());
                System.out.println("Outcome: " + appointment.getOutcome());
                break;
            }
        }
    }

    //Medicine Inventory Management
    public void addMedicine() {
        Scanner scanner = new Scanner(System.in);
        boolean duplicateindicator = false;
        String medName = "null";
        do {
            duplicateindicator = false;
            System.out.print("Enter Medicine Name: ");
            medName = scanner.nextLine();
            for (Medicine medicine : medicineInventory.copyMedicineList()) {
                if (medicine.getName().equalsIgnoreCase(medName)) {
                    System.out.println("Medicine exists! Re-enter medicine name.");
                    duplicateindicator = true;
                    break;
                }
            }
        } while (duplicateindicator);


        int quantity = 0;
        int lowstockalertval = 0;

        // Get Quantity with input validation
        while (true) {
            System.out.print("Enter Quantity: ");

            if (scanner.hasNextInt()) {
                quantity = scanner.nextInt();
                if (quantity > 0) { // Validate positive quantity
                    break;
                } else {
                    System.out.println("Quantity must be greater than 0. Please try again.");
                }
            } else {
                System.out.println("Invalid input! Please enter a valid integer.");
                scanner.next(); // Consume the invalid token
            }
        }

        // Get Low Stock Alert Value with input validation
        while (true) {
            System.out.print("Enter Low Stock Alert Value: ");

            if (scanner.hasNextInt()) {
                lowstockalertval = scanner.nextInt();
                if (lowstockalertval > 0) { // Validate positive value
                    break;
                } else {
                    System.out.println("Low Stock Alert Value must be greater than 0. Please try again.");
                }
            } else {
                System.out.println("Invalid input! Please enter a valid integer.");
                scanner.next(); // Consume the invalid token
            }
        }

        // Clear the scanner buffer
        scanner.nextLine();

        // Create a new Medicine object
        Medicine newMedicine = new Medicine(medName, quantity, lowstockalertval);

        // Add the new medicine to the medicineInventory
        medicineInventory.addMedicine(newMedicine);

        System.out.println("[Medicine Name: " + medName + ", Quantity: " + quantity + ", Low Stock Level Alert: " + lowstockalertval + "] added successfully.");



    }

    public void removeMedicine() {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the name of the medicine to remove
        System.out.println("Enter the name of the medicine to remove: ");
        String medName = scanner.nextLine();

        // Attempt to remove the medicine from the medicineInventory
        boolean success = medicineInventory.removeMedicine(medName);


        if (success) {
            System.out.println("Medicine " + medName + " removed.");
        }

        else {
            System.out.println("Medicine " + medName + " does not exist.");
            // Close the scanner

        }


    }
    public void plusStock(String medName, int medAmount) {
        medicineInventory.plusStock(medName, medAmount);

    }
    
	//attribute changers
	  public void changeName(String newname) {
	    	super.changeName(newname);
	    	System.out.println("Administrator name changed successfully!");
	    	return;
	    }
	    public void changeAge(int newage) {
	    	super.changeAge(newage);
	    	System.out.println("Administrator age changed successfully!");
	    	return;
	    }
	    public void changeGender(String newgender) {
	    	super.changeGender(newgender);
	    	System.out.println("Administrator gender changed successfully!");
	    	return;
	    }
	    public void changeRole(String newrole) {
	    	super.changeRole(newrole);
	    	System.out.println("Administrator role changed successfully!");
	    	return;
	    }

}
