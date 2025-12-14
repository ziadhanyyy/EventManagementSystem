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
        //5 conflict check
        List<Session> existingSessions = getAttendeeSessions(attendeeId);
        String newTime = s.getTimeSlot();

        for (Session existingS : existingSessions) {
            // We use simple string comparison for timeSlot, as per project rules
            if (existingS.getTimeSlot().equals(newTime)) {
                return "Error: Scheduling Conflict! You are already registered for '" +
                        existingS.getTitle() + "' at " + newTime + ".";
            }}


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

    public List<Session> getAttendeeSessions(String attendeeId) {
        for (Registration r : registrations) {
            if (r.getAttendeeId().equalsIgnoreCase(attendeeId)) {
                Session s = findSession(r.getSessionId());
                if (s != null) {
                    sessions.add(s);
                }
            }
        }
        return sessions;
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

    /**
     * Validates if a hall is available at the given time for a specific event
     * @param eventId The event ID to check
     * @param hall The hall name
     * @param time The time slot
     * @return true if hall is available, false if already taken
     */
    public boolean isHallAvailable(String eventId, String hall, String time) {
        List<Session> sessions = getSessionsOfEvent(eventId);
        for (Session s : sessions) {
            if (s.getHall().equalsIgnoreCase(hall) && s.getTimeSlot().equalsIgnoreCase(time)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates if a speaker is available for a new session within the same event
     * @param eventId The event ID where the session is being added
     * @param speakerId The speaker ID to check
     * @param time The time slot for the new session
     * @return null if available, error message if conflict exists
     */
    public String validateSpeakerConflictInEvent(String eventId, String speakerId, String time) {
        List<Session> sessions = getSessionsOfEvent(eventId);
        for (Session s : sessions) {
            if (s.getSpeakerId().equalsIgnoreCase(speakerId) && s.getTimeSlot().equalsIgnoreCase(time)) {
                return "Error: Speaker is already scheduled for another session in this event at " + time;
            }
        }
        return null;
    }

    /**
     * Validates if a speaker is available across all events on the same date
     * @param eventId The event ID where the session is being added
     * @param speakerId The speaker ID to check
     * @param time The time slot for the new session
     * @return null if available, error message if conflict exists
     */
    public String validateSpeakerConflictAcrossEvents(String eventId, String speakerId, String time) {
        Event targetEvent = findEvent(eventId);
        if (targetEvent == null) {
            return null;
        }
        
        String eventDate = targetEvent.getDate();
        
        // Check all events on the same date
        for (Event e : events) {
            // Skip the target event (already checked in validateSpeakerConflictInEvent)
            if (e.getEventId().equalsIgnoreCase(eventId)) {
                continue;
            }
            
            // Only check events on the same date
            if (e.getDate().equalsIgnoreCase(eventDate)) {
                for (Session s : e.getSessions()) {
                    if (s.getSpeakerId().equalsIgnoreCase(speakerId) && s.getTimeSlot().equalsIgnoreCase(time)) {
                        return "Error: Speaker is busy at this time in another event (" + e.getName() + ")";
                    }
                }
            }
        }
        return null;
    }


}




