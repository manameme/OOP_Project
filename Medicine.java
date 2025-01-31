package src;

/**
 * The {@code Medicine} class represents a medicine with attributes for its name, quantity,
 * and a low-stock alert threshold. It includes methods for managing stock and updating
 * attributes.
 */


class Medicine {
    private String name;
    private int quantity;
    private int lowStockAlert;

    /**
     * Constructs a {@code Medicine} object with the specified name, quantity, and low-stock alert threshold.
     *
     * @param name   The name of the medicine.
     * @param quantity The current stock quantity of the medicine.
     * @param alert The low-stock alert threshold.
     */

    // Constructor
    public Medicine(String name, int quantity, int alert) {
        this.name = name;
        this.quantity = quantity;
        this.lowStockAlert = alert;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getLowStockAlertAmt() {
        return lowStockAlert;
    }

    public void changeName(String newName) {
    	this.name = newName;
    	System.out.println("Name changed successfully!");
    	return;
    }
    public void changeQuantity(int newquantity) {
    	this.quantity = newquantity;
    	System.out.println("Quantity changed successfully!");
    	return;
    }
    public void changeLowStockAlert(int newlowstockalert) {
    	this.lowStockAlert = newlowstockalert;
    	System.out.println("Low Stock Alert threshold changed successfully!");
    	return;
    }


    @Override
    public String toString() {
        return "Medicine{name='" + name + "', quantity= " + quantity + " , stock alert: " + lowStockAlert + "}";
    }



    public void plusStock(int amount) {
        quantity += amount;
    }

    public void minusStock(int amount) {
        quantity -= amount;
    }


}
