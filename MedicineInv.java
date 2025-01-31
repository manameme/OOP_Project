package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The {@code MedicineInv} class manages the inventory of medicines, providing
 * methods for adding, removing, updating stock levels, and checking low-stock warnings.
 * It uses the Singleton pattern to ensure a single instance of the inventory is used across
 * the application.
 */


public class MedicineInv {

    // Private constructor to prevent multiple instances
    private MedicineInv() {
        // Initialize the inventory
        this.medicineList = importCSV_M();
        System.out.printf(" M loaded ");
    }

    // Static instance of the class
    private static MedicineInv instance = null;


    // Medicine inventory list
    private ArrayList<Medicine> medicineList;


    //Singleton pattern to ensure only 1 instance of data exist within entire application
    public static MedicineInv getInstance() {
        if (instance == null) {
            instance = new MedicineInv();
        }
        return instance;
    }
    public ArrayList<Medicine> copyMedicineList() {return medicineList;}

    public ArrayList<Medicine> importCSV_M() {
        String filePath = "resources/Medicine_List.csv";

        ArrayList<Medicine> medicines = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(filePath));

            // Skip the header row
            if (sc.hasNextLine()) sc.nextLine();

            // Reading each line of the CSV
            while (sc.hasNextLine()) {
                String line = sc.nextLine();     // Read each line
                String[] values = line.split(","); // Split by comma

                //CSV structure is name,quantity,lowStockAlert
                String name = values[0].trim();
                int quantity = Integer.parseInt(values[1].trim());
                int lowStockAlert = Integer.parseInt(values[2].trim());

                // Create a new Medicine object and add it to the list
                Medicine medicine = new Medicine(name,quantity,lowStockAlert);
                medicines.add(medicine);
            }

            sc.close(); // Close scanner

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing quantity as integer.");
            e.printStackTrace();
        }

        return medicines;
    }


    public int findMedicineIndex(String name){
        int index=0;
        for (Medicine medicine : medicineList)
        {
            if(medicine.getName().equals(name))
            	{
            	return index;
            	}
            index++;
        }
        return -1;
    }

    public void printList() {
        System.out.println("==================================================");
        System.out.printf("%-20s %-10s %-10s \n","Medication","Amount","Stock lvl");
        System.out.println("==================================================");
        String health = new String();
        for (Medicine medicine : medicineList) {
            if(medicine.getQuantity()<=medicine.getLowStockAlertAmt()) health = "LOW";
            else health = "OK";

            System.out.printf("%-20s %-10s %-10s \n",medicine.getName(),Integer.toString(medicine.getQuantity()), health);
            System.out.println("--------------------------------------------------");
        }
        System.out.println();
    }


    public void printLowStock() {

        for (Medicine medicine : medicineList) {
            stockWarning(medicine.getName());
        }

    }

    public int getQuanity(String name) {
        return medicineList.get(findMedicineIndex(name)).getQuantity();
    }

    public int getLowStockAlertAmount(String name) {
        return medicineList.get(findMedicineIndex(name)).getLowStockAlertAmt();
    }

    public void plusStock(String name, int amount){
        System.out.println(amount + " has been added to " + name);
         medicineList.get(findMedicineIndex(name)).plusStock(amount);
    }
    public void minusStock(String name, int amount) {
        if (this.getQuanity(name) == 0) {
            System.out.println(name + " HAS BEEN DEPLETED");
        } else if ((this.getQuanity(name) - amount) < 0) {
            System.out.println("Not enough stock of " + name + " Please choose a lower amount ");
            ;
        } else {
            System.out.println(amount + " has been removed from " + name);
            medicineList.get(findMedicineIndex(name)).minusStock(amount);
            this.stockWarning(name);
            System.out.println("Prescription fulfilled!");
        }
    }

    public void autoRestock(){
        boolean trig = false;
        for (Medicine medicine : medicineList) {
            boolean restock ;
            if(medicine.getQuantity()<=medicine.getLowStockAlertAmt()) restock = true;
            else restock = false;

            if(restock) {
                medicine.plusStock(100);
                trig = true;
            }

        }
        if (trig) System.out.println("----------- All low stocks replenished ! ---------");
        else System.out.println("----------- !!! All stocks healthy !!! -----------");
    }

    public void stockWarning(String name) {
        if (this.getQuanity(name) == 0) {
            System.out.println(name + " HAS BEEN DEPLETED");
            return;
        }
        if (this.getQuanity(name) <= this.getLowStockAlertAmount(name)) {
            System.out.println("ALERT: " + name + " is in short supply");
        }
    }


    public void printLowStockMedicine(){
        boolean trig = false;
        System.out.println("==================================================");
        System.out.printf("%-20s %-10s %-10s \n","Medication","Amount","Low Stock Amt");
        System.out.println("==================================================");
        for (Medicine medicine : medicineList) {
            if(medicine.getQuantity() <= medicine.getLowStockAlertAmt()){
              trig = true;
                System.out.printf("%-20s %-10d %-10d \n", medicine.getName(),medicine.getQuantity(),medicine.getLowStockAlertAmt());

            }
        }
        if(!trig){System.out.println("---------- All stocks are healthy ! :) -----------");};
    }

    //Medicine methods
    public void addMedicine(Medicine medicine) {
        if (medicine != null) {
        	medicineList.add(medicine);
            System.out.println("Medicine added successfully.");
        } else {
            System.out.println("Invalid medicine.");
        }
    }


    public boolean removeMedicine(String medName) {
        int index = findMedicineIndex(medName);

        if (index != -1) {
            medicineList.remove(index);
            System.out.println("Medicine removed successfully.");
            return true;
        } else {
            System.out.println("Medicine " + medName + " not found.");
            return false;
        }
    }

    public boolean medicineExists(String name) {
        for (Medicine medicine : medicineList) {
            if (medicine.getName().equalsIgnoreCase(name)) {
                return true; // Medicine exists in the list
            }
        }
        return false; // Medicine not found in the list
    }
}
