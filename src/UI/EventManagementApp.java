package UI;

import Manager.EventManager;
import Model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EventManagementApp extends Application implements LogoutHandler {
    private EventManager manager = new EventManager();
    private Person currentUser = null;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Event Management System");

        tryLoadData();

        showLoginScreen();
    }


    @Override
    public void stop() {
        System.out.println("Saving data and shutting down...");
        try {
            Manager.FileManager.saveAllData(manager);
            System.out.println("Data saved successfully.");
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @Override
    public void showLogoutScreen() {
        showAlert(Alert.AlertType.INFORMATION, "Logged Out", "You have been logged out successfully.");
        currentUser = null;
        showLoginScreen();
    }

    private void tryLoadData() {
        try {
            Manager.FileManager.loadAllData(manager);
            System.out.println("Data loaded from files.");
        } catch (Exception e) {
            System.err.println("File Manager not found or failed to load data. Starting with empty system.");

        }
    }

    private void showLoginScreen() {
        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(30));
        root.getStyleClass().add("login-box");

        Label title = new Label("Event System Login");
        title.getStyleClass().add("login-title");

        TextField userIdField = new TextField();
        userIdField.setPromptText("Enter User ID");

        Button loginButton = new Button("Login");
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(e -> handleLogin(userIdField.getText()));

        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("warning");
        exitButton.setOnAction(e -> {
            primaryStage.close();
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(loginButton, exitButton);

        root.getChildren().addAll(title, userIdField, buttonBox);

        Scene scene = new Scene(root, 350, 250);
        applyStylesheet(scene);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String userId) {
        currentUser = manager.FindPerson(userId.trim());

        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "User ID not found.");
        } else {
            String role = currentUser.getRole();
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + currentUser.getName() + " (" + role + ")");

            if (role.equals("Organizer")) {
                showOrganizerView();
            } else if (role.equals("Attendee")) {
                showAttendeeView();
            } else if (role.equals("Speaker")) {
                showSpeakerView();
            } else {
                showAlert(Alert.AlertType.WARNING, "Role Unknown", "Cannot determine view for this role.");
            }
        }
    }

    public void logout() {
        currentUser = null;
        showAlert(Alert.AlertType.INFORMATION, "Logged Out", "You have been logged out successfully.");
        showLoginScreen();
    }

    private void showOrganizerView() {
        OrganizerView organizerView = new OrganizerView(primaryStage, manager, currentUser, this);
        Scene scene = organizerView.getScene();
        applyStylesheet(scene);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
    }

    private void showAttendeeView() {
        AttendeeView attendeeView = new AttendeeView(primaryStage, manager, currentUser, this);
        Scene scene = attendeeView.getScene();
        applyStylesheet(scene);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
    }

    private void showSpeakerView() {
        SpeakerView speakerView = new SpeakerView(primaryStage, manager, currentUser, this);
        Scene scene = speakerView.getScene();
        applyStylesheet(scene);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
    }


    private void applyStylesheet(Scene scene) {
        try {

            String stylesheet = getClass().getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(stylesheet);
            System.out.println("CSS loaded successfully from classpath");
        } catch (Exception e) {
            try {

                String stylesheet = getClass().getResource("styles.css").toExternalForm();
                scene.getStylesheets().add(stylesheet);
                System.out.println("CSS loaded successfully from file system");
            } catch (Exception ex) {
                System.err.println("Warning: Could not load styles.css - " + ex.getMessage());
                System.err.println("The application will run with default JavaFX styling.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}