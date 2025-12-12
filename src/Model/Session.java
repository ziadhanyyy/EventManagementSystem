package Model;

public class Session {
    private String sessionId;
    private String title;
    private String speakerId;
    private String timeSlot;
    private int capacity;

    public Session(String sessionId, String title, String speakerId, String timeSlot, int capacity) {
        this.sessionId = sessionId;
        this.title = title;
        this.speakerId = speakerId;
        this.timeSlot = timeSlot;
        this.capacity = capacity;
    }
    public String getSessionId() { return this.sessionId; }
    public String getTitle() { return this.title; }
    public String getSpeakerId() { return this.speakerId; }
    public String getTimeSlot() { return this.timeSlot; }
    public int getCapacity() { return this.capacity; }

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
    public String toString()
    {   return  this.sessionId+" "+this.title+" "+this.speakerId+" "+this.timeSlot+" "+this.capacity;}
}
