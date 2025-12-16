package Manager;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private List<Event> events;
    private List<Person> people;
    private List<Registration> registrations;

    public EventManager() {
        this.events = new ArrayList<>();
        this.people = new ArrayList<>();
        this.registrations = new ArrayList<>();

    }
    public List<Event> getEvents() { return events; }
    public List<Person> getPeople() { return people; }
    public List<Registration> getRegistrations() { return registrations; }
    public int getAttendeeSize(){
        int result=0;
        for(Person person : people){
            if(person.getRole().equalsIgnoreCase("Attendee")){
                result+=1;
            }
        }
        return result;

    }
    public int getSpeakerSize(){
        int result=0;
        for(Person person : people){
            if(person.getRole().equalsIgnoreCase("Speaker")){
                result+=1;
            }
        }
        return result;

    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }
    public Person FindPerson(String id) {
        for (Person person : people) {
            if (person.getId().equalsIgnoreCase(id)) {
                return person;
            }
        }
        return null;
    }
    public Event findEvent(String id) {
        for (Event e : events) {
            if (e.getEventId().equalsIgnoreCase(id)) {
                return e;
            }
        }
        return null;
    }
    public Session findSession(String sessionId) {
        for (Event e : events) {
            for (Session s : e.getSessions()) {
                if (s.getSessionId().equalsIgnoreCase(sessionId)) {
                    return s;
                }
            }
        }
        return null;
    }
    public Registration findRegistration(String id) {
        for (Registration r : registrations) {
            if (r.getRegistrationId().equalsIgnoreCase(id)) {
                return r;
            }
        }
        return null;
    }

    public void addPerson(Person p) {
        people.add(p);
    }

    public void addEvent(Event e) {
        events.add(e);
    }


    public boolean addSessionToEvent(String eventId, Session session) {
        Event e = findEvent(eventId);
        if (e != null) {
            e.addSession(session);
            return true;
        }
        return false;
    }
    public String registerAttendee(String attendeeId, String sessionId) {

        Person p = FindPerson(attendeeId);
        if (p == null) {
            return "Error: Attendee not found or invalid ID.";
        }


        Session s = findSession(sessionId);
        if (s == null) {
            return "Error: Session not found.";
        }


        int currentCount = 0;
        for (Registration r : registrations) {

            if (r.getSessionId().equalsIgnoreCase(sessionId)) {
                currentCount++;
            }

            if (r.getSessionId().equalsIgnoreCase(sessionId) && r.getAttendeeId().equalsIgnoreCase(attendeeId)) {
                return "Error: You are already registered for this session.";
            }
        }


        if (currentCount >= s.getCapacity()) {
            return "Error: Session is full.";
        }
        String newDate = s.getDate();
        String newTime = s.getTimeSlot();

        for (Registration r : registrations) {
            if (r.getAttendeeId().equalsIgnoreCase(attendeeId)) {
                Session existingSession = findSession(r.getSessionId());
                if (existingSession != null) {

                    if (existingSession.getDate().equalsIgnoreCase(newDate) &&
                            existingSession.getTimeSlot().equalsIgnoreCase(newTime)) {
                        return "Attendee already registered for another session at this time";
                    }
                }
            }
        }
            // 6. Success - Create Registration
        String regId = "R" + (registrations.size() + 1); // Simple ID generation
        Registration newReg = new Registration(regId, attendeeId, sessionId);
        registrations.add(newReg);

        return "Success: Registered for " + s.getTitle();
    }


    public List<String> getMemberSchedule(String personId) {
        List<String> schedule = new ArrayList<>();

        for (Registration r : registrations) {
            if (r.getAttendeeId().equalsIgnoreCase(personId)) {

                Session s = findSession(r.getSessionId());
                if (s != null) {
                    schedule.add("Session: " + s.getTitle() + " | Time: " + s.getTimeSlot());
                }
            }
        }
        return schedule;
    }

    public List<String> getSpeakerSchedule(String speakerId) {
        List<String> speakerSchedule = new ArrayList<>();


        for (Event event : events) {
            for (Session session : event.getSessions()) {
                if (session.getSpeakerId().equalsIgnoreCase(speakerId)) {
                    speakerSchedule.add("Event: " + event.getName() +
                            " | Session: " + session.getTitle() +
                            " | Time: " + session.getTimeSlot());
                }
            }
        }
        return speakerSchedule;
    }
    public List<Session>getSessionsOfEvent(String eventId) {
        List<Session> sessions = new ArrayList<>();
        for (Event e : events) {
            if (e.getEventId().equalsIgnoreCase(eventId)) {
                for (Session s : e.getSessions()) {
                    sessions.add(s);
                }
            }
        }
        return sessions;
    }
    public String validateAndAddEvent(String eventId, String name, String date, String location) {
    if (findEvent(eventId) != null) {
        return "Event already exists";
    }
    for (Event e : events) {
        if (e.getDate().equalsIgnoreCase(date) && e.getLocation().equalsIgnoreCase(location)) {
            return "This location is taken on this date";
        }
    }


    addEvent(new Event(eventId, name, date, location));
    return null;
}

    public String validateAndAddSession(String eventId, String sessionId, String title,
                                        String speakerId, String timeSlot, int capacity, String hall) {
        Event event = findEvent(eventId);
        if (event == null) {
            return "Event not found.";
        }
        if (findSession(sessionId) != null) {
            return "Session already exists.";
        }
        Person speaker = FindPerson(speakerId);
        if (speaker == null || !speaker.getRole().equalsIgnoreCase("Speaker")) {
            return "Speaker ID not found.";
        }

        List<Session> eventSessions = getSessionsOfEvent(eventId);
        for (Session s : eventSessions) {
            if (s.getHall().equalsIgnoreCase(hall) && s.getTimeSlot().equalsIgnoreCase(timeSlot)) {
                return "Hall is taken at this time";
            }
        }

        String sessionDate = event.getDate();
        String speakerConflict = checkSpeakerConflict(speakerId, sessionDate, timeSlot);
        if (speakerConflict != null) {
            return speakerConflict;
        }

        Session session = new Session(sessionId, title, speakerId, timeSlot, capacity, hall, sessionDate);
        addSessionToEvent(eventId, session);
        return null;
    }
    private String checkSpeakerConflict(String speakerId, String date, String timeSlot) {
        for (Event event : events) {
            for (Session session : event.getSessions()) {
                if (session.getSpeakerId().equalsIgnoreCase(speakerId) &&
                        session.getDate().equalsIgnoreCase(date) &&
                        session.getTimeSlot().equalsIgnoreCase(timeSlot)) {
                    return "Speaker is not available - already assigned to another session at this time";
                }
            }
        }
        return null;
    }
    public String validateAndAddPerson(Person person) {
        for (Person p : people) {
            if (p.getId().equalsIgnoreCase(person.getId())) {
                return "This person already exists.";
            }
        }
        addPerson(person);
        return null; }


}




