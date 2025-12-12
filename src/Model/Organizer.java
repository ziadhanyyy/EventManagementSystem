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
        return  this.getRole()+" "+this.id+" "+this.name+" "+this.email ;
    }
}
