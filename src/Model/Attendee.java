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
        return  this.getRole()+" "+this.id+" "+this.name+" "+this.email;
    }
}
