package Model;

public class Organizer extends Person{
    public Organizer(String id, String name, String email) {
        super(id, name, email);
    }
    @Override
    public String getRole() {
        return "Organizer";
    }
    @Override
    public String toString() {
        return   "Role: " + this.getRole()+" Id: "+this.id+" Name: "+this.name+" email: "+this.email;
    }
}
