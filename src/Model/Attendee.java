package Model;

import java.util.ArrayList;

public class Attendee extends Person {
    public Attendee(String id, String name, String email) {
        super(id, name, email);
    }
    @Override
    public String getRole() {
        return "Attendee";
    }

    @Override
    public String toString() {
        return   "Role: " + this.getRole()+" Id: "+this.id+" Name: "+this.name+" email: "+this.email;
    }
}
