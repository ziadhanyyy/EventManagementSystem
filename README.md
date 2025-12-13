# Event Management System

A Java-based Event Management System with both Console and JavaFX GUI interfaces.

## Features

The system supports three user roles:
- **Organizer**: Create events, add sessions, register attendees, create users, view reports
- **Attendee**: View events, register for sessions, view personal schedule
- **Speaker**: View speaking schedule

## Project Structure

```
EventManagementSystem/
├── src/
│   ├── Main.java              # Console application entry point
│   ├── UI/
│   │   ├── MainApp.java       # JavaFX GUI application
│   │   └── Menu.java          # (Empty placeholder)
│   ├── Manager/
│   │   ├── EventManager.java  # Core business logic
│   │   └── FileManager.java   # Data persistence
│   └── Model/
│       ├── Person.java        # Abstract base class
│       ├── Organizer.java
│       ├── Attendee.java
│       ├── Speaker.java
│       ├── Event.java
│       ├── Session.java
│       └── Registration.java
├── Data/                       # Data files (people, events, sessions, registrations)
├── pom.xml                     # Maven configuration
└── EventManagementSystem.iml   # IntelliJ IDEA module file
```

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6 or higher (for building and running the JavaFX GUI)

## Running the Application

### Option 1: JavaFX GUI (Recommended)

1. **Build the project:**
   ```bash
   mvn clean compile
   ```

2. **Run the JavaFX GUI:**
   ```bash
   mvn javafx:run
   ```

3. **Login:** Use one of the existing user IDs from `Data/people.txt`:
   - Organizer: `O1`, `O2`
   - Attendee: `A1`, `A2`, `A3`
   - Speaker: `S1`, `S2`, `S3`

### Option 2: Console Application

1. **Compile the console application:**
   ```bash
   javac -d out src/**/*.java src/*.java
   ```

2. **Run the console application:**
   ```bash
   java -cp out Main
   ```

3. **Login:** Enter a user ID when prompted (e.g., `O1`, `A1`, `S1`)

## GUI Features

### Organizer Menu
- **Create Event**: Add new events with ID, name, date, and location
- **Add Session to Event**: Create sessions for events with speaker assignment
- **List All Events and Sessions**: View complete event catalog
- **Register a Person in Event**: Register attendees to sessions
- **Create Attendee**: Add new attendee users
- **Create Speaker**: Add new speaker users
- **View Report**: *(Feature Coming Soon - Placeholder)*
- **Search Event**: Find event by ID
- **Search Session**: Find session by ID
- **Logout**: Return to login screen

### Attendee Menu
- **View Available Events**: Browse all events and sessions
- **Register for a Session**: Sign up for sessions
- **View My Schedule**: See registered sessions
- **Logout**: Return to login screen

### Speaker Menu
- **View My Speaking Schedule**: See assigned speaking sessions
- **Logout**: Return to login screen

## Data Persistence

The system automatically saves and loads data from the `Data/` directory:
- `people.txt`: User accounts (Organizers, Attendees, Speakers)
- `events.txt`: Event information
- `sessions.txt`: Session details
- `registrations.txt`: Attendee-session registrations

Data is automatically:
- Loaded when the application starts
- Saved when the application exits or user logs out

## Building JAR Package

To create an executable JAR file:

```bash
mvn clean package
```

The JAR will be created in `target/EventManagementSystem-1.0-SNAPSHOT.jar`

## Development

### Technology Stack
- **Language**: Java 17
- **GUI Framework**: JavaFX 17.0.2
- **Build Tool**: Maven
- **IDE**: IntelliJ IDEA (project includes .iml file)

### Dependencies
All dependencies are managed through Maven (see `pom.xml`):
- `javafx-controls`: JavaFX UI controls
- `javafx-fxml`: JavaFX FXML support

## Notes

- The "View Report" feature in the Organizer menu is a placeholder for future implementation
- All user interactions in the GUI use dialogs and forms for data entry
- The console application remains fully functional alongside the GUI
- File paths have been updated to use relative paths for cross-platform compatibility

## Troubleshooting

**JavaFX not launching:**
- Ensure JDK 17+ is installed
- Run `mvn javafx:run` instead of `java -jar`
- Verify JAVA_HOME points to JDK 17+

**Data not saving:**
- Ensure the `Data/` directory exists
- Check file permissions for the Data directory

**Maven errors:**
- Run `mvn clean` to clear cached build artifacts
- Delete the `target/` directory and rebuild

## License

This is an educational project for event management system demonstration.
