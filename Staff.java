package src;

/**
 * The {@code Staff} class represents a staff member in the system, inheriting from the {@code User} class.
 * This class stores additional attributes specific to staff, such as name, gender, and age.
 */

public class Staff extends User {

    private String name;
    private String gender;
    private int age;

    /**
     * Constructs a {@code Staff} object with the specified details.
     *
     * @param userID   The unique identifier for the staff member.
     * @param name     The name of the staff member.
     * @param role     The role of the staff member (e.g., "Doctor", "Administrator").
     * @param gender   The gender of the staff member.
     * @param age      The age of the staff member.
     * @param password The password for the staff member's account.
     */

    public Staff(String userID, String name, String role, String gender, int age, String password) {
        super(userID, password, role);
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    //Getters

    public String getName() {
        return name;
    }
    public String getGender() {
        return gender;
    }
    public int getAge() {
        return age;
    }
    public String getID() {
    	return getUserID();
    }
    public String getRole() {
    	return super.getRole();
    }
    
    public void changeName(String newname) {
    	this.name = newname;
    	return;
    }
    public void changeAge(int newage) {
    	this.age = newage;
    	return;
    }
    public void changeGender(String newgender) {
    	this.gender = newgender;
    	return;
    }
    public void changeRole(String newrole) {
    	super.setRole(newrole);
    	return;
    }

    @Override
    public String toString() { //FOR TESTING ONLY
        return "Staff {" +
                "StaffID='" + super.getUserID()+
                "', Password: " + super.getPassword()+ "\n"+
                "       Role: " + super.getRole()+
                " Name: " + this.name+
                " , Gender: " + this.gender+
                " , Age: " + this.age + "} \n";
    }
}
