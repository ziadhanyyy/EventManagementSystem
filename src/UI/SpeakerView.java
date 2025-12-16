package UI;

import Manager.EventManager;
import Model.Person;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SpeakerView {
    private Stage primaryStage;
    private EventManager manager;
    private Person currentUser;
    private Scene scene;
    private LogoutHandler logoutHandler;

    public SpeakerView(Stage primaryStage, EventManager manager, Person currentUser, LogoutHandler logoutHandler) {
        this.primaryStage = primaryStage;
        this.manager = manager;
        this.currentUser = currentUser;
        this.logoutHandler = logoutHandler;
        initializeView();
    }

    private void initializeView() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));


        HBox header = new HBox();
        header.setSpacing(10);

        Label title = new Label("Speaker Dashboard - Welcome, " + currentUser.getName());
        title.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");


        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> logoutHandler.showLogoutScreen());


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, spacer, logoutButton);

        TextArea scheduleDisplay = new TextArea();
        scheduleDisplay.setEditable(false);
        scheduleDisplay.setPromptText("Your assigned speaking schedule will appear here.");

        Button refreshButton = new Button("View My Speaking Schedule");
        refreshButton.setOnAction(e -> {
            scheduleDisplay.setText("--- YOUR SPEAKING SESSIONS ---\n");
            for(String item : manager.getSpeakerSchedule(currentUser.getId())) {
                scheduleDisplay.appendText(item + "\n");
            }
            if (manager.getSpeakerSchedule(currentUser.getId()).isEmpty()) {
                scheduleDisplay.appendText("You are not currently assigned to any sessions.");
            }
        });

        layout.getChildren().addAll(header, refreshButton, scheduleDisplay);
        refreshButton.fire(); // Load schedule on startup
        this.scene = new Scene(layout, 600, 400);
    }

    public Scene getScene() {
        return this.scene;
    }
}