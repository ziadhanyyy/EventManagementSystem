package UI;

import Manager.EventManager;
import Model.Event;
import Model.Person;
import Model.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AttendeeView {
    private Stage primaryStage;
    private EventManager manager;
    private Person currentUser;
    private Scene scene;
    private LogoutHandler logoutHandler;

    public AttendeeView(Stage primaryStage, EventManager manager, Person currentUser, LogoutHandler logoutHandler) {
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

        Label title = new Label("Attendee Dashboard - Welcome, " + currentUser.getName());
        title.setStyle("-fx-font-size: 20pt; -fx-font-weight: bold;");


        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> logoutHandler.showLogoutScreen());


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, spacer, logoutButton);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createViewScheduleTab(),
                createRegisterTab(),
                new Tab("Events", createListAllEventsView())
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        mainLayout.getChildren().addAll(header, tabPane);
        this.scene = new Scene(mainLayout, 750, 550);
    }





    private Tab createViewScheduleTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextArea scheduleDisplay = new TextArea();
        scheduleDisplay.setEditable(false);
        scheduleDisplay.setPromptText("Your registered schedule will appear here.");

        Button refreshButton = new Button("View My Schedule");
        refreshButton.setOnAction(e -> {
            scheduleDisplay.setText("--- YOUR REGISTERED SESSIONS ---\n");
            for(String item : manager.getMemberSchedule(currentUser.getId())) {
                scheduleDisplay.appendText(item + "\n");
            }
            if (manager.getMemberSchedule(currentUser.getId()).isEmpty()) {
                scheduleDisplay.appendText("You are not registered for any sessions yet.");
            }
        });

        layout.getChildren().addAll(refreshButton, scheduleDisplay);
        refreshButton.fire();
        return new Tab("View My Schedule", layout);
    }


    private Tab createRegisterTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label instruction = new Label("Available Sessions:");

        ListView<String> sessionListView = new ListView<>();
        sessionListView.setPrefHeight(300);

        TextField sessionIdField = new TextField();
        sessionIdField.setPromptText("Enter Session ID to register (e.g., Sess1)");

        Button registerButton = new Button("Register for Selected Session");

        registerButton.setOnAction(e -> {
            String sessionId = sessionIdField.getText().trim();
            if (sessionId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing ID", "Please enter a Session ID.");
                return;
            }


            String result = manager.registerAttendee(currentUser.getId(), sessionId);

            if (result.startsWith("Success")) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", result);
                sessionIdField.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", result);
            }
        });

        Button refreshListButton = new Button("Refresh Sessions List");
        refreshListButton.setOnAction(e -> {
            ObservableList<String> sessions = FXCollections.observableArrayList();
            for(Event ev : manager.getEvents()) {
                for (Session s : ev.getSessions()) {
                    sessions.add(String.format("ID: %s | Title: %s | Time: %s | Capacity: %d",
                            s.getSessionId(), s.getTitle(), s.getTimeSlot(), s.getCapacity()));
                }
            }
            sessionListView.setItems(sessions);
            if (sessions.isEmpty()) {
                sessionListView.setItems(FXCollections.observableArrayList("No sessions available to register."));
            }
        });

        layout.getChildren().addAll(instruction, refreshListButton, sessionListView, sessionIdField, registerButton);
        refreshListButton.fire();

        return new Tab("Register", layout);
    }


    private VBox createListAllEventsView() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextArea eventDisplay = new TextArea();
        eventDisplay.setEditable(false);
        eventDisplay.setPrefHeight(400);

        Button refreshButton = new Button("List All Events");
        refreshButton.setOnAction(e -> {
            StringBuilder sb = new StringBuilder("--- ALL AVAILABLE EVENTS AND SESSIONS ---\n");
            if (manager.getEvents().isEmpty()) {
                sb.append("No events found.");
            } else {
                for (Event ev : manager.getEvents()) {
                    sb.append("\n[EVENT] ").append(ev.toString()).append("\n");
                    for (Session s : ev.getSessions()) {
                        sb.append("   -> Session: ").append(s.toString()).append("\n");
                    }
                }
            }
            eventDisplay.setText(sb.toString());
        });

        layout.getChildren().addAll(refreshButton, eventDisplay);
        refreshButton.fire();
        return layout;
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