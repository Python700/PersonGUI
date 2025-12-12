package persongui;

import java.io.Serializable;


public class OCCCPerson extends RegisteredPerson implements Serializable
{
    private final String studentID;
    
    /* Constructors */
    
    // Create from RegesteredPerson + studentID
    public OCCCPerson(RegisteredPerson p, String studentID)
    {
        super(p);
        this.studentID = studentID;
    }
    
    // Copy constructor
    public OCCCPerson(OCCCPerson p)
    {
        super(p);
        this.studentID = p.studentID;
    }
    
    /* Methods */

    public String getStudentID() {
        return studentID;
    }
    
    // Compare everything
    public boolean equals(OCCCPerson p)
    {
        if (p == null) return false;
        return super.equals(p) && this.studentID.equals(p.studentID);
    }
    
    // Compare only RegisteredPerson fields
    @Override
    public boolean equals(RegisteredPerson p)
    {
        if (p == null) return false;
        return super.equals(p);
    }
    
    // Compare only Person fields
    @Override
    public boolean equals(Person p)
    {
        if (p == null) return false;
        return super.equals(p);
    }
    
    @Override
    public String toString()
    {
        return super.toString() + " {" + studentID + "}";
    }
}
