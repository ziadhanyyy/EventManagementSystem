package Manager;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private List<Event> events;
    private List<Person> people;
    private List<Registration> registrations;
    private List <Session> sessions;

    public EventManager() {
        this.events = new ArrayList<>();
        this.people = new ArrayList<>();
        this.registrations = new ArrayList<>();
        this.sessions = new ArrayList<>();
    }
    public List<Event> getEvents() { return events; }
    public List<Person> getPeople() { return people; }
    public List<Registration> getRegistrations() { return registrations; }
    public List<Session> getSessions() { return this.sessions; }
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
    //adding data
    public void addPerson(Person p) {
        people.add(p);
    }

    public void addEvent(Event e) {
        events.add(e);
    }

    // Find the event by ID, then add the session to it
    public boolean addSessionToEvent(String eventId, Session session) {
        Event e = findEvent(eventId);
        if (e != null) {
            e.addSession(session);
            return true;
        }
        return false;
    }
    
    // Validation Methods
    
    /**
     * Validates and adds an event. Returns null if successful, error message otherwise.
     */
    public String validateAndAddEvent(String eventId, String name, String date, String location) {
        // Check if event ID already exists
        if (findEvent(eventId) != null) {
            return "Event already exists";
        }
        
        // Check if location is already taken on this date
        for (Event e : events) {
            if (e.getDate().equalsIgnoreCase(date) && e.getLocation().equalsIgnoreCase(location)) {
                return "this location are taken in this day";
            }
        }
        
        // All validations passed, add the event
        addEvent(new Event(eventId, name, date, location));
        return null; // Success
    }
    
    /**
     * Validates and adds a session to an event. Returns null if successful, error message otherwise.
     */
    public String validateAndAddSession(String eventId, String sessionId, String title, 
                                       String speakerId, String timeSlot, int capacity, String hall) {
        // Check if event exists
        Event event = findEvent(eventId);
        if (event == null) {
            return "Event not found.";
        }
        
        // Check if session ID already exists
        if (findSession(sessionId) != null) {
            return "Session already exists.";
        }
        
        // Check if speaker exists and is a speaker
        Person speaker = FindPerson(speakerId);
        if (speaker == null || !speaker.getRole().equalsIgnoreCase("Speaker")) {
            return "Speaker ID not found.";
        }
        
        // Check if hall is already taken at this time for this event
        List<Session> eventSessions = getSessionsOfEvent(eventId);
        for (Session s : eventSessions) {
            if (s.getHall().equalsIgnoreCase(hall) && s.getTimeSlot().equalsIgnoreCase(timeSlot)) {
                return "Hall is taken in this time";
            }
        }
        
        // Get the event date for the session
        String sessionDate = event.getDate();
        
        // Check for speaker conflicts across ALL events
        String speakerConflict = checkSpeakerConflict(speakerId, sessionDate, timeSlot);
        if (speakerConflict != null) {
            return speakerConflict;
        }
        
        // All validations passed, create and add the session
        Session session = new Session(sessionId, title, speakerId, timeSlot, capacity, hall, sessionDate);
        addSessionToEvent(eventId, session);
        return null; // Success
    }
    
    /**
     * Checks if a speaker already has a session at the given date and time.
     * Returns error message if conflict exists, null otherwise.
     */
    private String checkSpeakerConflict(String speakerId, String date, String timeSlot) {
        for (Event event : events) {
            for (Session session : event.getSessions()) {
                if (session.getSpeakerId().equalsIgnoreCase(speakerId) &&
                    session.getSessionDate().equalsIgnoreCase(date) &&
                    session.getTimeSlot().equalsIgnoreCase(timeSlot)) {
                    return "Speaker is not available - already assigned to another session at this time";
                }
            }
        }
        return null;
    }
    
    /**
     * Validates and adds a person. Returns null if successful, error message otherwise.
     */
    public String validateAndAddPerson(Person person) {
        // Check if person ID already exists
        for (Person p : people) {
            if (p.getId().equalsIgnoreCase(person.getId())) {
                return "this person already exists.";
            }
        }
        
        // All validations passed, add the person
        addPerson(person);
        return null; // Success
    }
    public String registerAttendee(String attendeeId, String sessionId) {
        // 1. Validate Attendee
        Person p = FindPerson(attendeeId);
        if (p == null) {
            return "Error: Attendee not found or invalid ID.";
        }

        // 2. Validate Session
        Session s = findSession(sessionId);
        if (s == null) {
            return "Error: Session not found.";
        }

        // 3. Check for Duplicates & Calculate Current Count
        int currentCount = 0;
        for (Registration r : registrations) {
            // Count how many people are in this session
            if (r.getSessionId().equalsIgnoreCase(sessionId)) {
                currentCount++;
            }
            // Check if THIS attendee is already in THIS session
            if (r.getSessionId().equalsIgnoreCase(sessionId) && r.getAttendeeId().equalsIgnoreCase(attendeeId)) {
                return "Error: You are already registered for this session.";
            }
        }

        // 4. Check Capacity
        if (currentCount >= s.getCapacity()) {
            return "Error: Session is full.";
        }
        
        // 5. Check for date and time conflicts
        String newDate = s.getSessionDate();
        String newTime = s.getTimeSlot();
        
        for (Registration r : registrations) {
            if (r.getAttendeeId().equalsIgnoreCase(attendeeId)) {
                Session existingSession = findSession(r.getSessionId());
                if (existingSession != null) {
                    // Check if same date and same time
                    if (existingSession.getSessionDate().equalsIgnoreCase(newDate) &&
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

        // 1. Find all registrations for this person
        for (Registration r : registrations) {
            if (r.getAttendeeId().equalsIgnoreCase(personId)) {

                // 2. Find the session details
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

        // 1. Iterate through all events
        for (Event event : events) {
            // 2. Iterate through all sessions in that event
            for (Session session : event.getSessions()) {

                // 3. Check if this session belongs to the speaker
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


}




