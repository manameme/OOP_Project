package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;

/**
 * The {@code PatientInv} class manages the inventory of patients, providing methods for
 * importing patient data from a CSV file, printing the patient list, and finding patients by ID.
 * It implements the Singleton pattern to ensure a single instance of the patient inventory is
 * maintained across the application.
 */


public class PatientInv {

    // Private constructor to prevent multiple instances
    public PatientInv(){
        // Initialize the inventory
        this.patientList = importCSV_P();
        System.out.printf(" P loaded ");
    }

    //Static instance of the class
    private static PatientInv instance = null;

    //Patient List
    private ArrayList<Patient> patientList;

    /**
     * Returns the singleton instance of {@code PatientInv}.
     *
     * @return The singleton instance of the {@code PatientInv} class.
     */
    //Singleton pattern to ensure only 1 instance of data exist within entire app
    public static PatientInv getInstance() {
        if (instance == null) {
            instance = new PatientInv();
        }
        return instance;
    }
    public ArrayList<Patient> copyPatientList() {
        return patientList;
    }

    public ArrayList<Patient> importCSV_P() {
        String filePath = "resources/Patient_List.csv";

        ArrayList<Patient> patients = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(filePath));

            // Skip the header row
            if (sc.hasNextLine()) sc.nextLine();

            // Reading each line of the CSV
            while (sc.hasNextLine()) {
                String line = sc.nextLine();     // Read each line
                String[] values = line.split(","); // Split by comma

                //CSV structure
                String patID = values[0].trim(); //patientID = userID
                String name = values[1].trim();
                String date = values[2].trim();
                String gender = values[3].trim();
                String bloodType = values[4].trim();
                String email = values[5].trim();

                String passWord = "password";
                String role = "Patient";


                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                Date dateOfBirth = null;
                try {
                    dateOfBirth = formatter.parse(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String conNum = "-1";

                // Create a new Patient object and add it to the list
                Patient patient = new Patient(patID, passWord, role, patID, name, dateOfBirth, gender, email, conNum, bloodType);
                patients.add(patient);
            }

            sc.close(); // Close scanner

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing quantity as integer.");
            e.printStackTrace();
        }

        return patients;
    }


    public void printList() {
        for (Patient patient : patientList) {
            System.out.println(patient);
        }
        System.out.println();
    }

    //Find Patient Index within List, with UserID
    public int findPatientIndexID(String userID){
        int index;
        for(index = 0; index <60; index++)
        {
            if(patientList.get(index).getID().equals(userID)) break;
        }
        return (index>50 ? -1:index);
    }
}
