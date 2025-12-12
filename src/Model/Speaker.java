package Model;

public class Speaker extends Person {
    private String  field ;
    public Speaker(String id, String name, String email, String  field) {
        super(id, name, email);
        this.field=field;
    }

    public String getField() {
        return this.field;
    }
    void setField(String field) {
        this.field = field;
    }

    @Override
    public String getRole() {
        return "Speaker";
    }
    @Override
    public String toString() {
        return  this.getRole()+" "+this.id+" "+this.name+" "+this.email +" "+this.field;
    }
}
