package Model;

public class Session {
    private String sessionId;
    private String title;
    private String speakerId;
    private String timeSlot;
    private int capacity;
    private String Hall;
    private String sessionDate;

    public Session(String sessionId, String title, String speakerId, String timeSlot, int capacity, String Hall, String sessionDate) {
        this.sessionId = sessionId;
        this.title = title;
        this.speakerId = speakerId;
        this.timeSlot = timeSlot;
        this.capacity = capacity;
        this.Hall = Hall;
        this.sessionDate = sessionDate;
    }
    public String getHall() {
        return this.Hall;
    }
    public String getSessionId() { return this.sessionId; }
    public String getTitle() { return this.title; }
    public String getSpeakerId() { return this.speakerId; }
    public String getTimeSlot() { return this.timeSlot; }
    public int getCapacity() { return this.capacity; }
    public String getSessionDate() { return this.sessionDate; }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSpeakerId(String speakerId) {
        this.speakerId = speakerId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public void setHall(String Hall) {
        this.Hall = Hall;
    }
    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }
    public String toString()
    {   return "Id: "+ this.sessionId+" Title: "+this.title+" SpeakerId: "+this.speakerId+" Time: "+this.timeSlot+" capacity: "+this.capacity+" Hall: "+this.Hall;}
}
