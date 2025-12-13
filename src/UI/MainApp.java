package UI;

import Manager.*;
import Model.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static EventManager manager = new EventManager();
    private Person currentUser = null;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Load data
        FileManager.loadAllData(manager);
        
        primaryStage.setTitle("Event Management System");
        showLoginScreen();
        primaryStage.show();
    }

    private void showLoginScreen() {
        VBox loginBox = new VBox(15);
        loginBox.setPadding(new Insets(20));
        loginBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("EVENT MANAGEMENT SYSTEM");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label instructionLabel = new Label("Login with User ID:");
        TextField userIdField = new TextField();
        userIdField.setPromptText("Enter User ID");
        userIdField.setMaxWidth(300);

        Button loginButton = new Button("Login");
        Button exitButton = new Button("Exit");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, exitButton);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        loginBox.getChildren().addAll(titleLabel, instructionLabel, userIdField, buttonBox, messageLabel);

        loginButton.setOnAction(e -> {
            String userId = userIdField.getText().trim();
            if (userId.isEmpty()) {
                messageLabel.setText("Please enter a User ID");
                return;
            }

            currentUser = manager.FindPerson(userId);
            if (currentUser == null) {
                messageLabel.setText("User ID not found");
            } else {
                showRoleBasedMenu();
            }
        });

        exitButton.setOnAction(e -> {
            FileManager.saveAllData(manager);
            primaryStage.close();
        });

        Scene scene = new Scene(loginBox, 600, 400);
        primaryStage.setScene(scene);
    }

    private void showRoleBasedMenu() {
        String role = currentUser.getRole();
        
        if (role.equals("Organizer")) {
            showOrganizerMenu();
        } else if (role.equals("Attendee")) {
            showAttendeeMenu();
        } else if (role.equals("Speaker")) {
            showSpeakerMenu();
        }
    }

    private void showOrganizerMenu() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        Label welcomeLabel = new Label("Welcome, " + currentUser.getName() + " (Organizer)");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.getChildren().add(welcomeLabel);
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> showLoginScreen());
        topBox.getChildren().add(logoutButton);
        
        root.setTop(topBox);

        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(20));
        menuBox.setAlignment(Pos.TOP_LEFT);

        Button createEventBtn = new Button("Create Event");
        createEventBtn.setMaxWidth(250);
        createEventBtn.setOnAction(e -> createEventDialog());

        Button addSessionBtn = new Button("Add Session to Event");
        addSessionBtn.setMaxWidth(250);
        addSessionBtn.setOnAction(e -> addSessionDialog());

        Button listEventsBtn = new Button("List All Events and Sessions");
        listEventsBtn.setMaxWidth(250);
        listEventsBtn.setOnAction(e -> listAllEventsDialog());

        Button registerPersonBtn = new Button("Register a Person in Event");
        registerPersonBtn.setMaxWidth(250);
        registerPersonBtn.setOnAction(e -> registerPersonDialog());

        Button createAttendeeBtn = new Button("Create Attendee");
        createAttendeeBtn.setMaxWidth(250);
        createAttendeeBtn.setOnAction(e -> createAttendeeDialog());

        Button createSpeakerBtn = new Button("Create Speaker");
        createSpeakerBtn.setMaxWidth(250);
        createSpeakerBtn.setOnAction(e -> createSpeakerDialog());

        Button viewReportBtn = new Button("View Report");
        viewReportBtn.setMaxWidth(250);
        viewReportBtn.setOnAction(e -> viewReportDialog());

        Button searchEventBtn = new Button("Search Event");
        searchEventBtn.setMaxWidth(250);
        searchEventBtn.setOnAction(e -> searchEventDialog());

        Button searchSessionBtn = new Button("Search Session");
        searchSessionBtn.setMaxWidth(250);
        searchSessionBtn.setOnAction(e -> searchSessionDialog());

        menuBox.getChildren().addAll(
            createEventBtn, addSessionBtn, listEventsBtn, registerPersonBtn,
            createAttendeeBtn, createSpeakerBtn, viewReportBtn, searchEventBtn, searchSessionBtn
        );

        root.setCenter(menuBox);

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setScene(scene);
    }

    private void showAttendeeMenu() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        Label welcomeLabel = new Label("Welcome, " + currentUser.getName() + " (Attendee)");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.getChildren().add(welcomeLabel);
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> showLoginScreen());
        topBox.getChildren().add(logoutButton);
        
        root.setTop(topBox);

        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(20));
        menuBox.setAlignment(Pos.TOP_LEFT);

        Button viewEventsBtn = new Button("View Available Events");
        viewEventsBtn.setMaxWidth(250);
        viewEventsBtn.setOnAction(e -> listAllEventsDialog());

        Button registerSessionBtn = new Button("Register for a Session");
        registerSessionBtn.setMaxWidth(250);
        registerSessionBtn.setOnAction(e -> attendeeRegisterSessionDialog());

        Button viewScheduleBtn = new Button("View My Schedule");
        viewScheduleBtn.setMaxWidth(250);
        viewScheduleBtn.setOnAction(e -> viewMyScheduleDialog());

        menuBox.getChildren().addAll(viewEventsBtn, registerSessionBtn, viewScheduleBtn);

        root.setCenter(menuBox);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
    }

    private void showSpeakerMenu() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        Label welcomeLabel = new Label("Welcome, " + currentUser.getName() + " (Speaker)");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.getChildren().add(welcomeLabel);
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> showLoginScreen());
        topBox.getChildren().add(logoutButton);
        
        root.setTop(topBox);

        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(20));
        menuBox.setAlignment(Pos.TOP_LEFT);

        Button viewScheduleBtn = new Button("View My Speaking Schedule");
        viewScheduleBtn.setMaxWidth(250);
        viewScheduleBtn.setOnAction(e -> viewSpeakerScheduleDialog());

        menuBox.getChildren().add(viewScheduleBtn);

        root.setCenter(menuBox);

        Scene scene = new Scene(root, 700, 400);
        primaryStage.setScene(scene);
    }

    // Organizer dialogs
    private void createEventDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Create Event");
        dialog.setHeaderText("Enter Event Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField eventIdField = new TextField();
        TextField nameField = new TextField();
        TextField dateField = new TextField();
        TextField locationField = new TextField();

        grid.add(new Label("Event ID:"), 0, 0);
        grid.add(eventIdField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(dateField, 1, 2);
        grid.add(new Label("Location:"), 0, 3);
        grid.add(locationField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String eId = eventIdField.getText().trim();
                String name = nameField.getText().trim();
                String date = dateField.getText().trim();
                String loc = locationField.getText().trim();

                if (!eId.isEmpty() && !name.isEmpty() && !date.isEmpty() && !loc.isEmpty()) {
                    manager.addEvent(new Event(eId, name, date, loc));
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Event Created.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void addSessionDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add Session to Event");
        dialog.setHeaderText("Enter Session Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField eventIdField = new TextField();
        TextField sessionIdField = new TextField();
        TextField titleField = new TextField();
        TextField speakerIdField = new TextField();
        TextField timeField = new TextField();
        TextField capacityField = new TextField();

        grid.add(new Label("Target Event ID:"), 0, 0);
        grid.add(eventIdField, 1, 0);
        grid.add(new Label("New Session ID:"), 0, 1);
        grid.add(sessionIdField, 1, 1);
        grid.add(new Label("Title:"), 0, 2);
        grid.add(titleField, 1, 2);
        grid.add(new Label("Speaker ID:"), 0, 3);
        grid.add(speakerIdField, 1, 3);
        grid.add(new Label("Time:"), 0, 4);
        grid.add(timeField, 1, 4);
        grid.add(new Label("Capacity:"), 0, 5);
        grid.add(capacityField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String targetEid = eventIdField.getText().trim();
                    String sId = sessionIdField.getText().trim();
                    String title = titleField.getText().trim();
                    String spId = speakerIdField.getText().trim();
                    String time = timeField.getText().trim();
                    int cap = Integer.parseInt(capacityField.getText().trim());

                    Person speaker = manager.FindPerson(spId);
                    if (speaker == null || !speaker.getRole().equalsIgnoreCase("Speaker")) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Speaker ID not found.");
                        return null;
                    }

                    Session session = new Session(sId, title, spId, time, cap);
                    boolean added = manager.addSessionToEvent(targetEid, session);

                    if (added) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Session added successfully.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Event not found.");
                    }
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid number for capacity.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void listAllEventsDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("All Events and Sessions");
        dialog.setHeaderText("Event Listings");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(500, 400);

        StringBuilder content = new StringBuilder();
        if (manager.getEvents().isEmpty()) {
            content.append("No events found.");
        } else {
            for (Event e : manager.getEvents()) {
                content.append("\n[EVENT] ").append(e.toString()).append("\n");
                for (Session s : e.getSessions()) {
                    content.append("   -> ").append(s.toString()).append("\n");
                }
            }
        }
        textArea.setText(content.toString());

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void registerPersonDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Register a Person in Event");
        dialog.setHeaderText("Enter Registration Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField attendeeIdField = new TextField();
        TextField sessionIdField = new TextField();

        grid.add(new Label("Attendee ID:"), 0, 0);
        grid.add(attendeeIdField, 1, 0);
        grid.add(new Label("Session ID:"), 0, 1);
        grid.add(sessionIdField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String aId = attendeeIdField.getText().trim();
                String sessId = sessionIdField.getText().trim();

                boolean validAttendee = manager.FindPerson(aId) != null && 
                                       manager.FindPerson(aId).getRole().equals("Attendee");
                boolean validSession = manager.findSession(sessId) != null;

                if (validAttendee && validSession) {
                    String result = manager.registerAttendee(aId, sessId);
                    showAlert(Alert.AlertType.INFORMATION, "Registration Result", result);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Attendee or Session ID not found");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void createAttendeeDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Create Attendee");
        dialog.setHeaderText("Enter Attendee Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField emailField = new TextField();

        grid.add(new Label("Attendee ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Attendee Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Attendee Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String aId = idField.getText().trim();
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();

                if (!aId.isEmpty() && !name.isEmpty() && !email.isEmpty()) {
                    manager.addPerson(new Attendee(aId, name, email));
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Attendee added.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void createSpeakerDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Create Speaker");
        dialog.setHeaderText("Enter Speaker Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField fieldField = new TextField();

        grid.add(new Label("Speaker ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Speaker Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Speaker Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Speaker Field:"), 0, 3);
        grid.add(fieldField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String sId = idField.getText().trim();
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String field = fieldField.getText().trim();

                if (!sId.isEmpty() && !name.isEmpty() && !email.isEmpty() && !field.isEmpty()) {
                    manager.addPerson(new Speaker(sId, name, email, field));
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Speaker added.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void viewReportDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("View Report");
        alert.setHeaderText("View Report Feature");
        alert.setContentText("Feature Coming Soon!");
        alert.showAndWait();
    }

    private void searchEventDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Search Event");
        dialog.setHeaderText("Enter Event ID");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField eventIdField = new TextField();
        grid.add(new Label("Event ID:"), 0, 0);
        grid.add(eventIdField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String eid = eventIdField.getText().trim();
                Event event = manager.findEvent(eid);
                
                if (event != null) {
                    showAlert(Alert.AlertType.INFORMATION, "Event Found", event.toString());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Event not found.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void searchSessionDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Search Session");
        dialog.setHeaderText("Enter Session ID");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField sessionIdField = new TextField();
        grid.add(new Label("Session ID:"), 0, 0);
        grid.add(sessionIdField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String sid = sessionIdField.getText().trim();
                Session session = manager.findSession(sid);
                
                if (session != null) {
                    showAlert(Alert.AlertType.INFORMATION, "Session Found", session.toString());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Session not found.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    // Attendee dialogs
    private void attendeeRegisterSessionDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Register for Session");
        dialog.setHeaderText("Enter Session ID");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField sessionIdField = new TextField();
        grid.add(new Label("Session ID:"), 0, 0);
        grid.add(sessionIdField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String sId = sessionIdField.getText().trim();
                
                if (manager.findSession(sId) == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Session not found.");
                    return null;
                }

                String result = manager.registerAttendee(currentUser.getId(), sId);
                showAlert(Alert.AlertType.INFORMATION, "Registration Result", result);
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void viewMyScheduleDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("My Schedule");
        dialog.setHeaderText("My Registered Sessions");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(400, 300);

        StringBuilder content = new StringBuilder();
        var schedule = manager.getMemberSchedule(currentUser.getId());
        
        if (schedule.isEmpty()) {
            content.append("No registrations found.");
        } else {
            for (String item : schedule) {
                content.append(item).append("\n");
            }
        }
        textArea.setText(content.toString());

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    // Speaker dialogs
    private void viewSpeakerScheduleDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("My Speaking Schedule");
        dialog.setHeaderText("My Assigned Sessions");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(500, 300);

        StringBuilder content = new StringBuilder();
        var schedule = manager.getSpeakerSchedule(currentUser.getId());
        
        if (schedule.isEmpty()) {
            content.append("You are not currently assigned to any sessions.");
        } else {
            for (String entry : schedule) {
                content.append(entry).append("\n");
            }
        }
        textArea.setText(content.toString());

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        // Save data when application closes
        FileManager.saveAllData(manager);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
