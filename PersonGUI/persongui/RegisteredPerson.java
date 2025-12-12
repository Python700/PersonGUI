package persongui;

import java.io.Serializable;


public class RegisteredPerson extends Person implements Serializable
{
    private String govID;
    
    
    /* Constructors */
    // Creates a new RegisteredPerson
    public RegisteredPerson(String firstName, String lastName, OCCCDate dob, String govID)
    {
        super(firstName, lastName, dob);
        this.govID = govID;
    }
    
    // Create RegisteredPerson from an existing person
    public RegisteredPerson(Person p, String govID)
    {
        super(p);
        this.govID = govID;
    }
    
    // Copy constructor
    public RegisteredPerson(RegisteredPerson p)
    {
        super(p);
        this.govID = p.govID;
    }
    
    /* Methods */
    // Get the person's government id
    public String getGovernmentID()
    {
        return govID;
    }
    
    // Compares two RegisteredPerson objects
    public boolean equals(RegisteredPerson p)
    {
        return super.equals(p) && this.govID.equals(p.govID);
    }
    
    // Compares only the Person fields
    @Override
    public boolean equals(Person p)
    {
        return super.equals(p);
    }
    
    @Override
    public String toString()
    {
        return super.toString() + " [" + govID + "]";
    }
}