package src;

/**
 * The {@code RestockForm} class represents a restock request for a specific medication.
 * Each restock form includes a unique ID, medication name, restock amount, and
 * fulfillment status.
 */

public class RestockForm {
	private int restockID;
	private static int restockIDCounter=0;
    private String medicationName;
    private int restockAmount = -1;

    private boolean isFulfilled = false; // false = Pending_Admin, true = Fulfilled

    /**
     * Constructs a new {@code RestockForm} with the specified medication name and restock amount.
     * The form is initially marked as pending (not fulfilled).
     *
     * @param medicationName The name of the medication to be restocked.
     * @param restockAmount  The amount of medication to be restocked.
     */

    // Constructor
    public RestockForm(String medicationName, int restockAmount) {
        this.restockID = ++restockIDCounter; // Auto-increment ID
        this.medicationName = medicationName;
        this.restockAmount = restockAmount;
    }

    // Getters
    public int getRestockID() {
        return restockID;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public int getRestockAmount() {
        return restockAmount;
    }

    public boolean isFulfilled() {
        return isFulfilled;
    }

    // Setters
    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public void setRestockAmount(int restockAmount) {
        this.restockAmount = restockAmount;
    }

    public void setFulfilled(boolean fulfilled) {
        this.isFulfilled = fulfilled;
    }
    public boolean getFulfilled() {
    	return this.isFulfilled;
    }

    // Print Method
    public void printFormDetails() {
        System.out.printf("%-10d %-20s %-10d %-20s\n", restockID, medicationName, restockAmount, isFulfilled ? "Fulfilled" : "Pending_Admin");
    }
}
