package Model;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String eventId;
    private String name;
    private String date;
    private String location;
    private List<Session> sessions;

    public Event(String eventId, String name, String date, String location) {
        this.eventId = eventId;
        this.name = name;
        this.date = date;
        this.location = location;
        this.sessions = new ArrayList<>();
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public List<Session> getSessions() {
        return this.sessions;
    }

    public String getEventId() { return this.eventId; }
    public String getName() { return this.name; }
    public String getDate() { return this.date; }
    public String getLocation() { return this.location; }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String toString() {
        return "EventId: " +this.eventId + " EventName: " + this.name +" Date: " + date + " Location: " + location;
    }

}
