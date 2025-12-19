import Manager.*;
import Model.*;
import java.util.List;
import java.util.Scanner;

public  class Main {
    private static EventManager manager = new EventManager();
    private static Scanner scanner = new Scanner(System.in);
    private static Person currentUser = null;

    public static void main(String[] args) {
        FileManager.loadAllData(manager);
        System.out.println("=== EVENT MANAGEMENT SYSTEM ===");
        while (true) {
            System.out.print("\nLogin with User ID (or type 'EXIT'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("EXIT")) {
                FileManager.saveAllData(manager);
                System.out.println("Goodbye!");
                break;
            }


            currentUser = manager.FindPerson(input);

            if (currentUser == null) {
                System.out.println("User ID not found");
            } else {
                System.out.println("Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");


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
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Date: ");
                    String date = scanner.nextLine();
                    System.out.print("Enter Location: ");
                    String loc = scanner.nextLine();
                    String event = manager.validateAndAddEvent(eId, name, date, loc);
                    if (event != null) {
                        System.out.println(event);
                    } else {
                        System.out.println("Event Created.");
                    }
                    break;

                case "2":
                    System.out.print("Enter Target Event ID: ");
                    String targetEid = scanner.nextLine();
                    System.out.print("Enter New Session ID: ");
                    String sId = scanner.nextLine();
                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Speaker ID: ");
                    String spId = scanner.nextLine();
                    System.out.print("Enter Hall Name : ");
                    String hall = scanner.nextLine();
                    System.out.print("Enter Time : ");
                    String time = scanner.nextLine();
                    int cap;
                    while (true) {
                        System.out.print("Enter Capacity: ");
                        String capInput = scanner.nextLine();

                        try {
                            cap = Integer.parseInt(capInput);
                            break;
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid number. Please enter a valid integer.");
                        }
                    }
                    String sessionError = manager.validateAndAddSession(targetEid, sId, title, spId, time, cap, hall);
                    if (sessionError != null) {
                        System.out.println(sessionError);
                    } else {
                        System.out.println("Session added successfully.");
                    }
                    break;
                case "3":
                    printAllEvents();
                    break;
                case "4":
                    System.out.println("Enter Attendee ID: ");
                    String AId=scanner.nextLine();
                    System.out.print("Enter Attendee Name: ");
                    String AName=scanner.nextLine();
                    System.out.print("Enter Attendee Email: ");
                    String AEmail=scanner.nextLine();
                    String attendee = manager.validateAndAddPerson(new Attendee(AId, AName, AEmail));
                    if (attendee!= null) {
                        System.out.println(attendee);
                    } else {
                        System.out.println("Attendee added.");
                    }
                    break;
                case "5":
                    System.out.println("Enter Speaker ID: ");
                    String SId=scanner.nextLine();
                    System.out.print("Enter Speaker Name: ");
                    String SName=scanner.nextLine();
                    System.out.print("Enter Speaker Email: ");
                    String SEmail=scanner.nextLine();
                    System.out.print("Enter Speaker Filed: ");
                    String filed=scanner.nextLine();
                    String speaker = manager.validateAndAddPerson(new Speaker(SId, SName, SEmail, filed));
                    if (speaker != null) {
                        System.out.println(speaker);
                    } else {
                        System.out.println("Speaker added.");
                    }
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






