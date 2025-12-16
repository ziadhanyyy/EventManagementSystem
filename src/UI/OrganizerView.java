package UI;

import Manager.EventManager;
import Model.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OrganizerView {
    private Stage primaryStage;
    private EventManager manager;
    private Person currentUser;
    private Scene scene;
    private LogoutHandler logoutHandler; // Use interface

    public OrganizerView(Stage primaryStage, EventManager manager, Person currentUser, LogoutHandler logoutHandler) {
        this.primaryStage = primaryStage;
        this.manager = manager;
        this.currentUser = currentUser;
        this.logoutHandler = logoutHandler;
        initializeView();
    }

    private void initializeView() {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));


        HBox header = new HBox();
        header.setSpacing(10);

        Label title = new Label("Organizer Dashboard - Welcome, " + currentUser.getName());
        title.setStyle("-fx-font-size: 20pt; -fx-font-weight: bold;");


        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> logoutHandler.showLogoutScreen());


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, spacer, logoutButton);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createEventTab(),
                createAddSessionTab(),
                createCreatePersonTab(),
                createRegisterPersonTab(),
                createListEventsTab(),
                createReportsTab()
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        mainLayout.getChildren().addAll(header, tabPane);
        this.scene = new Scene(mainLayout, 850, 700);
    }





    private Tab createCreatePersonTab() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        GridPane grid = createInputGrid();


        TextField idField = addGridRow(grid, "ID:", 0);
        TextField nameField = addGridRow(grid, "Name:", 1);
        TextField emailField = addGridRow(grid, "Email:", 2);


        ComboBox<String> roleCombo = new ComboBox<>(FXCollections.observableArrayList("Attendee", "Speaker"));
        roleCombo.setValue("Attendee");
        grid.add(new Label("Role:"), 0, 3);
        grid.add(roleCombo, 1, 3);


        Label fieldLabel = new Label("Field (Speaker Only):");
        TextField fieldField = new TextField();
        fieldField.setPromptText("e.g., IT, Marketing");
        fieldField.setDisable(true);
        grid.add(fieldLabel, 0, 4);
        grid.add(fieldField, 1, 4);


        roleCombo.setOnAction(e -> fieldField.setDisable(!roleCombo.getValue().equals("Speaker")));

        Button createButton = new Button("Create Person");
        grid.add(createButton, 1, 5);

        createButton.setOnAction(e -> {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                String email = emailField.getText();
                String role = roleCombo.getValue();

                if (id.isEmpty() || name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "ID and Name cannot be empty.");
                    return;
                }

                if (manager.FindPerson(id) != null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Person with this ID already exists.");
                    return;
                }

                Person newPerson = null;
                if (role.equals("Attendee")) {
                    newPerson = new Attendee(id, name, email);
                } else if (role.equals("Speaker")) {
                    newPerson = new Speaker(id, name, email, fieldField.getText());
                }

                if (newPerson != null) {
                    manager.addPerson(newPerson);
                    showAlert(Alert.AlertType.INFORMATION, "Success", role + " '" + name + "' created successfully!");
                    idField.clear(); nameField.clear(); emailField.clear(); fieldField.clear();
                }

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not create person: " + ex.getMessage());
            }
        });

        root.getChildren().add(grid);
        return new Tab("Create Person", root);
    }


    private Tab createRegisterPersonTab() {
        GridPane grid = createInputGrid();

        TextField attendeeIdField = addGridRow(grid, "Attendee ID:", 0);
        TextField sessionIdField = addGridRow(grid, "Session ID:", 1);

        Button registerButton = new Button("Force Register Attendee");
        grid.add(registerButton, 1, 2);

        registerButton.setOnAction(e -> {
            String attendeeId = attendeeIdField.getText();
            String sessionId = sessionIdField.getText();

            // 1. Basic Check (mimicking the CLI)
            boolean validAttendee = manager.FindPerson(attendeeId) != null && manager.FindPerson(attendeeId).getRole().equals("Attendee");
            boolean validSession = manager.findSession(sessionId) != null;

            if (!validAttendee || !validSession) {
                showAlert(Alert.AlertType.ERROR, "Error", "Attendee ID or Session ID not found/valid.");
                return;
            }


            String result = manager.registerAttendee(attendeeId, sessionId);

            if (result.startsWith("Error")) {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", result);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Registration Success", result);
                attendeeIdField.clear();
                sessionIdField.clear();
            }
        });

        return new Tab("Register Person", grid);
    }


    private Tab createReportsTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label title = new Label("System Reports");
        title.setStyle("-fx-font-weight: bold;");

        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setPrefHeight(400);

        Button refreshButton = new Button("Generate Report");
        refreshButton.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("--- EVENT MANAGEMENT SYSTEM STATISTICS ---\n");
            sb.append("Total Events: ").append(manager.getEvents().size()).append("\n");
            sb.append("Total Attendees: ").append(manager.getAttendeeSize()).append("\n");
            sb.append("Total Speakers: ").append(manager.getSpeakerSize()).append("\n");
            sb.append("Total Registrations: ").append(manager.getRegistrations().size()).append("\n\n");
            sb.append("--- CURRENT CAPACITY USAGE ---\n");

            for (Event event : manager.getEvents()) {
                for (Session session : event.getSessions()) {
                    int registeredCount = 0;
                    for (Registration reg : manager.getRegistrations()) {
                        if (reg.getSessionId().equals(session.getSessionId())) {
                            registeredCount++;
                        }
                    }
                    sb.append(String.format("Event %s | Session %s: %d/%d capacity\n",
                            event.getEventId(), session.getSessionId(), registeredCount, session.getCapacity()));
                }
            }
            reportArea.setText(sb.toString());
        });

        layout.getChildren().addAll(title, refreshButton, reportArea);
        refreshButton.fire();

        return new Tab("Reports", layout);
    }

    private Tab createEventTab() {
        GridPane grid = createInputGrid();
        TextField eventIdField = addGridRow(grid, "Event ID:", 0);
        TextField nameField = addGridRow(grid, "Name:", 1);
        TextField dateField = addGridRow(grid, "Date (YYYY-MM-DD):", 2);
        TextField locationField = addGridRow(grid, "Location:", 3);

        Button createButton = new Button("Create Event");
        grid.add(createButton, 1, 4);

        createButton.setOnAction(e -> {
            try {
                Event newEvent = new Event(
                        eventIdField.getText(),
                        nameField.getText(),
                        dateField.getText(),
                        locationField.getText()
                );
                manager.addEvent(newEvent);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Event '" + newEvent.getName() + "' created successfully!");
                eventIdField.clear(); nameField.clear(); dateField.clear(); locationField.clear();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not create event. Check inputs.");
            }
        });

        return new Tab("Create Event", grid);
    }

    private Tab createAddSessionTab() {
        GridPane grid = createInputGrid();
        TextField eventIdField = addGridRow(grid, "Target Event ID (e.g., E1):", 0);
        TextField sessionIdField = addGridRow(grid, "Session ID:", 1);
        TextField titleField = addGridRow(grid, "Title:", 2);
        TextField speakerIdField = addGridRow(grid, "Speaker ID (e.g., S1):", 3);
        TextField timeSlotField = addGridRow(grid, "Time Slot (e.g., 10:00-11:00):", 4);
        TextField capacityField = addGridRow(grid, "Capacity:", 5);
        TextField hallField = addGridRow(grid, "Hall Name:", 6); // ADD THIS

        Button addButton = new Button("Add Session");
        grid.add(addButton, 1, 7); // Update row to 7

        addButton.setOnAction(e -> {
            try {
                int capacity = Integer.parseInt(capacityField.getText());

                // Fix: Add the eventIdField.getText() as the last parameter
                Session newSession = new Session(sessionIdField.getText(), titleField.getText(),
                        speakerIdField.getText(), timeSlotField.getText(),
                        capacity, hallField.getText(), manager.findEvent(eventIdField.getText()).getDate());
                boolean success = manager.addSessionToEvent(eventIdField.getText(), newSession);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Session '" + newSession.getTitle() + "' added to event " + eventIdField.getText());
                    sessionIdField.clear(); titleField.clear(); speakerIdField.clear();
                    timeSlotField.clear(); capacityField.clear(); hallField.clear(); // Clear hall too
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Event ID '" + eventIdField.getText() + "' not found.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Capacity must be a valid number.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not add session: " + ex.getMessage());
            }
        });

        return new Tab("Add Session", grid);
    }

    private Tab createListEventsTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextArea searchResultArea = new TextArea();
        searchResultArea.setEditable(false);
        searchResultArea.setPrefHeight(400);


        TextField searchField = new TextField();
        searchField.setPromptText("Search Event ID (e.g., E1) or Session ID (e.g., Sess1) or leave empty for all");

        Button searchButton = new Button("Search / List All");
        searchButton.setOnAction(e -> {
            String query = searchField.getText().trim();
            StringBuilder sb = new StringBuilder();

            if (query.isEmpty()) {
                sb.append("--- LISTING ALL EVENTS AND SESSIONS ---\n");
                if (manager.getEvents().isEmpty()) {
                    sb.append("No events found.");
                } else {
                    for (Event ev : manager.getEvents()) {
                        sb.append("\n[EVENT] ID: ").append(ev.getEventId()).append(", Name: ").append(ev.getName()).append("\n");
                        for (Session s : ev.getSessions()) {
                            sb.append("   -> SESSION: ").append(s.toString()).append("\n");
                        }
                    }
                }
            } else {
                // Search Event by ID (Option 8)
                Event eFound = manager.findEvent(query);
                if (eFound != null) {
                    sb.append("--- FOUND EVENT ---\n");
                    sb.append("[EVENT] ").append(eFound.toString()).append("\n");
                    for (Session s : eFound.getSessions()) {
                        sb.append("   -> SESSION: ").append(s.toString()).append("\n");
                    }
                }


                Session sFound = manager.findSession(query);
                if (sFound != null) {
                    sb.append(eFound != null ? "\n--- FOUND SESSION ---\n" : "--- FOUND SESSION ---\n");
                    sb.append("[SESSION] ").append(sFound.toString()).append("\n");
                }

                if (eFound == null && sFound == null) {
                    sb.append("No Event or Session found for ID: ").append(query);
                }
            }

            searchResultArea.setText(sb.toString());
        });

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setPadding(new Insets(0, 0, 10, 0));

        layout.getChildren().addAll(searchBox, searchResultArea);
        searchButton.fire();

        return new Tab("View/Search Data", layout);
    }

    // --- Utility Methods ---
    private GridPane createInputGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        return grid;
    }

    private TextField addGridRow(GridPane grid, String label, int row) {
        TextField field = new TextField();
        grid.add(new Label(label), 0, row);
        grid.add(field, 1, row);
        return field;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return this.scene;
    }
}