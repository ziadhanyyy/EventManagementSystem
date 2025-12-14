import Manager.*;
import Model.*;
import java.util.List;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static EventManager manager = new EventManager();
    private static Scanner scanner = new Scanner(System.in);
    private static Person currentUser = null;

    public static void main(String[] args) {

        // 1. Load some starter data so we can log in
        FileManager.loadAllData(manager);

        System.out.println("=== EVENT MANAGEMENT SYSTEM ===");

        // 2. Main Application Loop
        while (true) {
            System.out.print("\nLogin with User ID (or type 'EXIT'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("EXIT")) {
                FileManager.saveAllData(manager);
                System.out.println("Goodbye!");
                break;
            }

            // 3. Authenticate User
            currentUser = manager.FindPerson(input);

            if (currentUser == null) {
                System.out.println("User ID not found");
            } else {
                System.out.println("Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");

                // 4. Role-Based Menu Dispatch
                String role = currentUser.getRole();

                if (role.equals("Organizer")) {
                    showOrganizerMenu();
                } else if (role.equals("Attendee")) {
                    showAttendeeMenu();
                } else if (role.equals("Speaker")) {
                    showSpeakerMenu();
                }
            }
        }
    }

    // --- ORGANIZER MENU ---
    private static void showOrganizerMenu() {
        while (true) {
            System.out.println("\n--- ORGANIZER MENU ---");
            System.out.println("1. Create Event");
            System.out.println("2. Add Session to Event");
            System.out.println("3. List All Events  and Sessions");
            System.out.println("4. Create Attendee");
            System.out.println("5. Create Speaker");
            System.out.println("6. View Reports");
            System.out.println("7. Search Event ");
            System.out.println("8. Search Session");
            System.out.println("0. Logout");
            System.out.print("Select: ");

            String choice = scanner.nextLine();

            if (choice.equals("0")) break;

            switch (choice) {
                case "1":
                    System.out.print("Enter Event ID: ");
                    String eId = scanner.nextLine();
                    if(manager.findEvent(eId) != null) {
                        System.out.println("Event already exists");
                        break;
                    }
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Date: ");
                    String date = scanner.nextLine();
                    System.out.print("Enter Location: ");
                    String loc = scanner.nextLine();
                    for(Event e : manager.getEvents()) {
                        if(e.getDate().equalsIgnoreCase(date) && e.getLocation().equalsIgnoreCase(loc)) {
                            System.out.println("this location are taken in this day");
                            break;
                        }
                    }

                    manager.addEvent(new Event(eId, name, date, loc));
                    System.out.println("Event Created.");
                    break;

                case "2":
                    System.out.print("Enter Target Event ID: ");
                    String targetEid = scanner.nextLine();
                    if(manager.findEvent(targetEid) == null) {
                        System.out.println("Event not found.");
                        break;
                    }
                    System.out.print("Enter New Session ID: ");
                    String sId = scanner.nextLine();
                    if(manager.findSession(sId) != null) {
                        System.out.println("Session already exists.");
                        break;
                    }
                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Speaker ID: ");
                    String spId = scanner.nextLine();
                    Person speaker = manager.FindPerson(spId);

                    if (speaker == null || !speaker.getRole().equalsIgnoreCase("Speaker")) {
                        System.out.println("Speaker ID not found.");
                        break;
                    }
                    System.out.print("Enter Hall Name : ");
                    String hall = scanner.nextLine();
                    System.out.print("Enter Time : ");
                    String time = scanner.nextLine();
                    
                    // Validate hall availability
                    if (!manager.isHallAvailable(targetEid, hall, time)) {
                        System.out.println("Hall is taken in this time");
                        break;
                    }
                    
                    // Validate speaker conflict within the same event
                    String conflictInEvent = manager.validateSpeakerConflictInEvent(targetEid, spId, time);
                    if (conflictInEvent != null) {
                        System.out.println(conflictInEvent);
                        break;
                    }
                    
                    // Validate speaker conflict across events on the same date
                    String conflictAcrossEvents = manager.validateSpeakerConflictAcrossEvents(targetEid, spId, time);
                    if (conflictAcrossEvents != null) {
                        System.out.println(conflictAcrossEvents);
                        break;
                    }
                    
                    int cap;
                    while (true) {
                        System.out.print("Enter Capacity: ");
                        String capInput = scanner.nextLine();

                        try {
                            cap = Integer.parseInt(capInput);
                            break; // valid number → exit loop
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid number. Please enter a valid integer.");
                        }
                    }

                    // =======================
                    // CREATE SESSION
                    // =======================
                    Session session = new Session(sId, title, spId, time, cap, hall);
                    boolean added = manager.addSessionToEvent(targetEid, session);

                    if (added) {
                        System.out.println("Session added successfully.");
                    } else {
                        System.out.println("Event not found.");
                    }
                    break;
                case "3":
                    printAllEvents();
                    break;
                case "4":
                    System.out.println("Enter Attendee ID: ");
                    String AId=scanner.nextLine();
                    for(Person a: manager.getPeople()){
                        if(AId.equalsIgnoreCase(a.getId())) {
                            System.out.println("this person already exists.");
                        }
                    }
                    System.out.print("Enter Attendee Name: ");
                    String AName=scanner.nextLine();
                    System.out.print("Enter Attendee Email: ");
                    String AEmail=scanner.nextLine();
                    manager.addPerson(new Attendee(AId, AName, AEmail));
                    System.out.println("Attendee added.");
                    break;
                case "5":
                    System.out.println("Enter Speaker ID: ");
                    String SId=scanner.nextLine();
                    for(Person a: manager.getPeople()){
                        if(SId.equalsIgnoreCase(a.getId())) {
                            System.out.println("this person already exists.");
                        }
                    }

                    System.out.print("Enter Speaker Name: ");
                    String SName=scanner.nextLine();
                    System.out.print("Enter Speaker Email: ");
                    String SEmail=scanner.nextLine();
                    System.out.print("Enter Speaker Filed: ");
                    String filed=scanner.nextLine();
                    manager.addPerson(new Speaker(SId, SName, SEmail,filed));
                    System.out.println("Speaker added.");
                    break;
                case"6":
                    System.out.println("We Have "+manager.getEvents().size()+" Events ");
                    System.out.println(" with a "+manager.getSpeakerSize()+" Speakers ");
                    System.out.println("and a "+manager.getAttendeeSize()+" Attendees ");
                    System.out.println(" with a "+manager.getRegistrations().size()+" Registrations ");
                    break;

                case "7":
                    System.out.print("Enter Target Event ID: ");
                    String Eid = scanner.nextLine();
                    if(manager.findEvent(Eid) != null){
                        System.out.println(manager.findEvent(Eid));
                    }
                    else{
                        System.out.println("Event not found.");}
                    break;
                case"8":
                    System.out.print("Enter Session ID: ");
                    String Sid = scanner.nextLine();
                    if(manager.findSession(Sid) != null){
                        System.out.println(manager.findSession(Sid));
                    }
                    else{
                        System.out.println("Session not found.");}
                    break;
                default:
                    System.out.printf("Invalid choice.");
                    break;
            }


        }
    }

    // --- ATTENDEE MENU ---
    private static void showAttendeeMenu() {
        while (true) {
            System.out.println("\n--- ATTENDEE MENU ---");
            System.out.println("1. View Available Events");
            System.out.println("2. Register for a Session");
            System.out.println("3. View My Schedule");
            System.out.println("0. Logout");
            System.out.print("Select: ");

            String choice = scanner.nextLine();

            if (choice.equals("0")) break;

            switch (choice) {
                case "1":
                    printAllEvents();
                    break;
                case "2":
                    System.out.print("Enter Session ID to Register: ");
                    String sId = scanner.nextLine();
                    if(manager.findSession(sId) == null){
                        System.out.println("Session not found.");
                        break;
                    }
                    for(Registration r: manager.getRegistrations()){
                        if(r.getSessionId().equalsIgnoreCase(sId)&& r.getAttendeeId().equalsIgnoreCase(currentUser.getId())){
                            System.out.println("You are already registered for this Session.");
                        }
                    }
                    // Pass the ID of the currently logged-in user
                    String result = manager.registerAttendee(currentUser.getId(), sId);
                    System.out.println(result);
                    break;
                case "3":
                    System.out.println("--- My Schedule ---");
                    List<String> schedule = manager.getMemberSchedule(currentUser.getId());
                    if (schedule.isEmpty()) System.out.println("No registrations found.");
                    for (String item : schedule) System.out.println(item);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // --- SPEAKER MENU ---
    private static void showSpeakerMenu() {
        while (true) {
            System.out.println("\n--- SPEAKER MENU ---");
            System.out.println("1. View My Speaking Schedule");
            System.out.println("0. Logout");
            System.out.print("Select: ");

            String choice = scanner.nextLine();

            if (choice.equals("0")) break;

            switch (choice) {
                case "1":
                    System.out.println("\n--- My Assigned Sessions ---");
                    // Use the ID of the currently logged-in user
                    List<String> schedule = manager.getSpeakerSchedule(currentUser.getId());

                    if (schedule.isEmpty()) {
                        System.out.println("You are not currently assigned to any sessions.");
                    } else {
                        for (String entry : schedule) {
                            System.out.println(entry);
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // --- HELPER: PRINT EVENTS ---
    private static void printAllEvents() {
        if (manager.getEvents().isEmpty()) {
            System.out.println("No events found.");
            return;
        }
        for (Event e : manager.getEvents()) {
            System.out.println("\n[EVENT] " + e.toString());
            for (Session s : e.getSessions()) {
                System.out.println("   -> " + s.toString());
            }
        }
    }

}






