package src;

/**
 * The {@code PrescriptedMed} class represents a prescription for a specific medication,
 * including the medication's name, the prescribed amount, and whether the prescription
 * has been fulfilled.
 */

public class PrescriptedMed {

    private String medication;
    private int medicationAmount;
    private boolean medIsFilled;  //True = filled False = not filled

    public PrescriptedMed(String med, int medAmt){
        this.medication = med;
        this.medicationAmount = medAmt;
        this.medIsFilled = false;
    }

    public String getMedName() {
        return medication;
    }
    public int getMedAmount() {
        return medicationAmount;
    }
    public boolean isMedIsFilled() {
        return medIsFilled;
    }

    // Setter for medIsFilled
    public void setMedIsFilled(boolean medIsFilled) {
        this.medIsFilled = medIsFilled;
    }

    // Method to print all the information
    public void printInfo() {
        System.out.printf("Medication: %-20s | Amount: %-10d | Filled: %-10s%n",
                medication,
                medicationAmount,
                medIsFilled ? "Yes" : "No");
    }
}
