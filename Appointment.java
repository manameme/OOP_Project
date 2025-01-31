package src;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The {@code Appointment} class represents an appointment between a patient and a doctor.
 * <p>
 * It contains details about the appointment such as the patient, doctor, date, time,
 * status, outcome, and prescribed medications.
 * </p>
 */

public class Appointment {
  private String appointmentID;
  private String patientID;
  private String patientName;
  private String doctorName;
  private String doctorID;
  private Date date;
  private String status;
  private String outcome; 
  private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); //display time from Date data in the form 12:00
  private SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM YYYY"); // display date from Date data in the form "Friday, 01 Nov 2024"
  private ArrayList<PrescriptedMed> pMedList = new ArrayList<>();
  private boolean hasMedication = false;


    /**
     * Constructs an {@code Appointment} object with the given patient, doctor, date, and time.
     *
     * @param pat  The patient involved in the appointment.
     * @param doc  The doctor involved in the appointment.
     * @param day  The date of the appointment.
     * @param time The time of the appointment as a string.
     */

    // Constructor
  public Appointment(Patient pat, Doctor doc, Date day, String time) {
    this.appointmentID = generateID(); //  ID logic
    this.patientName = pat.getName();
    this.patientID = pat.getID();
    this.doctorName = doc.getName(); // Assuming Doctor has a getName() method
    this.doctorID = doc.getID();
    this.date = day;
    this.status = "pending";
    pMedList.clear();
  }
  
 

    // Generate a random appointment ID (can be customized)
  private String generateID() {
    return "Appt" + System.currentTimeMillis(); // Simple unique ID
  }
    
    // Getters
  public String getID() { return appointmentID; }
  public String getPatient() { return patientName; }
  public String getPatientID() { return patientID; }
  public String getDoctor() { return doctorName; }
  public String getDoctorID() { return doctorID; }
  public String getDate() { return dateFormat.format(date.getTime());	}
	public String getTime() { return timeFormat.format(date.getTime());	}
	public Date getDateTime() {	return date; }
  public String getStatus() { return status; }
  public String getOutcome() { return outcome; }
  public boolean isHasMedication(){ return hasMedication;}

  // Update Methods
	public void updatePatientID(String newPatientID) {
	    patientID = newPatientID;
	}
	
	public void updatePatientName(String newPatientName) {
	    patientName = newPatientName;
	}
	
	public void updateDoctorID(String newDoctorID) {
	    doctorID = newDoctorID;
	}
	
	public void updateDoctorName(String newDoctorName) {
	    doctorName = newDoctorName;
	}
	
	public void updateDate(String newPatientID) {
	    status = newPatientID;
	}
	
	
	
  public void updateStatus(String newstatus) {
    status = newstatus;
  }
    
  public void updateOutcome(String notes) {
    outcome = notes;
  }
  
 
  
  public void TrueHasMedication(){ hasMedication = true;};

    //Prescription Methods

  public void addPrescript(String name, int amt){
    String med = name.substring(0, 1).toUpperCase() + name.substring(1); //Sets to Prescript
    PrescriptedMed medicine = new PrescriptedMed(med,amt);
    pMedList.add(medicine);
  }

  public boolean removePrescript(String med) {
    int index = findPrescriptIndex(med);

    if (index != -1) {
      pMedList.remove(index);
      System.out.println("Prescription removed successfully.");
      return true;
    } else {
      System.out.println("Prescription " + med + " not found.");
      return false;
    }
  }


  public void fulfillPrescription(String medName) {
    for (PrescriptedMed medicine : pMedList) {
      if (medicine.getMedName().equalsIgnoreCase(medName)) {
        // Mark as fulfilled
        medicine.setMedIsFilled(true);
        System.out.println("Prescription for " + medName + " has been fulfilled.");
        return; // Exit once the prescription is found and fulfilled
      }
    }
    System.out.println("Medication " + medName + " not found in the list.");
  }

  public int getPrescribedAmount(String medName) {
    for (PrescriptedMed medicine : pMedList) {
      if (medicine.getMedName().equalsIgnoreCase(medName)) {
        return medicine.getMedAmount(); // Return the amount if found
      }
    }
    return -1; // Return -1 if the medication is not found
  }

  private int findPrescriptIndex(String med){
    int index=0;
    for (PrescriptedMed medicine : pMedList)
    {
      if(medicine.getMedName().equals(med))
      {
        return index;
      }
      index++;
    }
    return -1;
  }

  public void printPrescription(){

    for(PrescriptedMed n : pMedList) {
      String status;
      if (n.isMedIsFilled()){
        status = "FULFILLED";
      }
        else status = "NOT FULFILLED";

      System.out.printf("%-20s %-10s %-10s \n",n.getMedName(),n.getMedAmount(),status);
      System.out.println("--------------------------------------------------");
    }

  }


  public void printPrescriptionFormatting(){
	  	int MedCount = pMedList.size();
	  	int counter = 0;
	    for(PrescriptedMed n : pMedList) {
	    	counter++;
	      String status;
	      if (n.isMedIsFilled()){
	        status = "FULFILLED";
	      }
	        else status = "NOT FULFILLED";
	      if (counter == 1) {
	    	  System.out.printf("%-20s %-14s %-6s %-6s \n",this.appointmentID,n.getMedName(),n.getMedAmount(),status); 
	      }
	      else {
	    	  System.out.printf("%-20s %-14s %-6s %-6s \n"," ",n.getMedName(),n.getMedAmount(),status); 
	      }
	      
	      if (counter==MedCount) {
	      System.out.println("--------------------------------------------------------");
	      }
	    }

	  }

      public boolean isThisPrescribedMedication(String medName){
          for (PrescriptedMed medicine : pMedList) {
              if (medicine.getMedName().equals(medName)) {
                  return true; // The medication is found in the prescription list
              }
          }
          return false; // The medication is not found
      }

    public boolean isPrescriptionFulfilled(String medName) {
        for (PrescriptedMed medicine : pMedList) {
            if (medicine.getMedName().equalsIgnoreCase(medName)) {
                return medicine.isMedIsFilled(); // Return fulfillment status
            }
        }
        return false; // Return false if the medication is not found
    }
}
