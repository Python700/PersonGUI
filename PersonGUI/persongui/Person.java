package persongui;

import java.io.Serializable;


public class Person implements Serializable{
    private String firstName;
    private String lastName;
    private OCCCDate dob; 
    
    /**
     * This constructor initializes the person's first and last name.
     * @param firstName
     * @param lastName 
     */
    public Person(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    /**
     * This constructor initializes the person's first and last name, and the date of birth.
     * @param firstName
     * @param lastName
     * @param dob 
     */
    public Person(String firstName, String lastName, OCCCDate dob)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }
    
    /**
     * Copy's a constructor.
     * @param p 
     */
    public Person(Person p) {
        this.firstName = p.firstName;
        this.lastName = p.lastName;
        this.dob = p.dob;
    }


    /* Getters and Setters */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public OCCCDate getDob() {
        return dob;
    }

    public void setDob(OCCCDate dob) {
        this.dob = dob;
    }
    
    /* Methods */
    public int getAge() {
        if (dob == null) return 0;
        return dob.getDifferenceInYears(); 
    }

    
    @Override
    public String toString()
    {
        String birth = (dob != null) ? dob.toString() : "N/a";
        return lastName + ", " + firstName + " (" + birth + ")";
    }
    
    // Compare two Person objects.
    public boolean equals(Person other)
    {
        if (other == null) return false;
        return this.firstName.equals(other.firstName) &&
                this.lastName.equals(other.lastName);
    }
    
    // Behavior methods.
    public void eat()
    {
        System.out.println("Person is eating...");
    }
    
    public void sleep()
    {
        System.out.println("Person is sleeping...");
    }
    
    public void play()
    {
        System.out.println("Person is playing...");
    }
    
    public void run()
    {
        System.out.println("Person is running...");
    }
}
