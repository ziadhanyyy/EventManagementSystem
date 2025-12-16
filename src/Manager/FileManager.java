package Manager;
import Model.*;
import java.io.*;
import java.util.Scanner;

public class FileManager {
    private static final String PERSONS_FILE = "Data/people.txt";
    private static final String EVENTS_FILE = "Data/events.txt";
    private static final String SESSIONS_FILE = "Data/sessions.txt";
    private static final String REGISTRATIONS_FILE = "Data/registrations.txt";

    // --- SAVE METHOD ---
    public static void saveAllData(EventManager manager) {
        System.out.println("Saving data to files...");

        savePersons(manager);
        saveEvents(manager);
        saveSessions(manager);
        saveRegistrations(manager);

        System.out.println("Data saved successfully.");
    }

    private static void savePersons(EventManager manager) {
        try (PrintWriter writer = new PrintWriter(PERSONS_FILE)) {
            for (Person p : manager.getPeople()) {
                StringBuilder line = new StringBuilder();

                // Common fields for all people
                line.append(p.getRole()).append("|");
                line.append(p.getId()).append("|");
                line.append(p.getName()).append("|");
                line.append(p.getEmail());

                // Role-specific fields
                if (p instanceof Speaker) {
                    Speaker s = (Speaker) p;
                    line.append("|").append(s.getField());

                }
                // Attendee and Organizer don't need extra fields in the file

                writer.println(line.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving persons data: " + e.getMessage());
        }
    }

    private static void saveEvents(EventManager manager) {
        try (PrintWriter writer = new PrintWriter(EVENTS_FILE)) {
            for (Event e : manager.getEvents()) {
                // eventId|name|date|location
                writer.printf("%s|%s|%s|%s\n",
                        e.getEventId(), e.getName(), e.getDate(), e.getLocation());
            }
        } catch (IOException e) {
            System.err.println("Error saving events data: " + e.getMessage());
        }
    }

    private static void saveSessions(EventManager manager) {
        try (PrintWriter writer = new PrintWriter(SESSIONS_FILE)) {
            for (Event e : manager.getEvents()) {
                String eventId = e.getEventId();
                for (Session s : e.getSessions()) {
                    // sessionId|eventId|title|speakerId|timeSlot|capacity|hall|sessionDate
                    writer.printf("%s|%s|%s|%s|%s|%d|%s|%s\n",
                            s.getSessionId(), eventId, s.getTitle(), s.getSpeakerId(),
                            s.getTimeSlot(), s.getCapacity(),s.getHall(), s.getSessionDate());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving sessions data: " + e.getMessage());
        }
    }

    private static void saveRegistrations(EventManager manager) {
        try (PrintWriter writer = new PrintWriter(REGISTRATIONS_FILE)) {
            for (Registration r : manager.getRegistrations()) {
                // registrationId|attendeeId|sessionId
                writer.printf("%s|%s|%s\n",
                        r.getRegistrationId(), r.getAttendeeId(), r.getSessionId());
            }
        } catch (IOException e) {
            System.err.println("Error saving registrations data: " + e.getMessage());
        }
    }
    // --- LOAD METHOD ---
    public static void loadAllData(EventManager manager) {
    System.out.println("Loading data from files...");
    loadPersons(manager);
    loadEvents(manager);
    loadSessions(manager);
    loadRegistrations(manager);
    System.out.println("Loading complete. " + manager.getPeople().size() + " users, " +
            manager.getEvents().size() + " events loaded.");
}

    private static void loadPersons(EventManager manager) {
        try (Scanner fileScanner = new Scanner(new File(PERSONS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length >= 4) {
                    String role = parts[0];
                    String id = parts[1];
                    String name = parts[2];
                    String email = parts[3];

                    switch (role) {
                        case"Attendee":
                            manager.addPerson(new Attendee(id, name, email));
                            break;
                        case "Organizer":
                            manager.addPerson(new Organizer(id, name, email));
                            break;
                        case "Speaker":
                            // Speaker needs one additional fields
                            if (parts.length >= 5) {
                                manager.addPerson(new Speaker(id, name, email, parts[4]));
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Persons file not found, starting fresh.");
        }
    }

    private static void loadEvents(EventManager manager) {
        try (Scanner fileScanner = new Scanner(new File(EVENTS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    // eventId|name|date|location
                    manager.addEvent(new Event(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Events file not found, starting fresh.");
        }
    }

    private static void loadSessions(EventManager manager) {
        try (Scanner fileScanner = new Scanner(new File(SESSIONS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                // Minimum 7 fields required: sessionId|eventId|title|speakerId|timeSlot|capacity|hall
                // 8th field (sessionDate) is optional for backward compatibility
                if (parts.length >= 7) {
                    String eventId = parts[1]; // Get the eventId to link the session
                    String sessionDate = (parts.length >= 8) ? parts[7] : "";
                    Session s = new Session(parts[0], parts[2], parts[3], parts[4], Integer.parseInt(parts[5]), parts[6], sessionDate);
                    manager.addSessionToEvent(eventId, s); // Add session to the correct event
                }
            }
        } catch (IOException e) {
            System.out.println("Sessions file not found, starting fresh.");
        }
    }

    private static void loadRegistrations(EventManager manager) {
        try (Scanner fileScanner = new Scanner(new File(REGISTRATIONS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    // registrationId|attendeeId|sessionId
                    Registration r = new Registration(parts[0], parts[1], parts[2]);
                    manager.getRegistrations().add(r); // Add registration directly to the list
                }
            }
        } catch (IOException e) {
            System.out.println("Registrations file not found, starting fresh.");
        }
    }

}


