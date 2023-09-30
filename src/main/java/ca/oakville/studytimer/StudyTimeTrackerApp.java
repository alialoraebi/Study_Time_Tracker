//Study Timer Tracker App
//Made by Ali Al Aoraebi

package ca.oakville.studytimer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;

public class StudyTimeTrackerApp extends Application {
    private SimpleIntegerProperty seconds = new SimpleIntegerProperty(0);
    private Timeline timeline;
    private Text timerDisplay;
    private Button startButton;
    private String startTime;
    private TableView<TimerRecord> timerHistoryTable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Study Time Tracker");
        timerDisplay = new Text("00:00:00");
        timerDisplay.setFont(Font.font(48));
        timerDisplay.setId("timerDisplay");

        startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        Button resetButton = new Button("Reset");

        startButton.setOnAction(e -> startTimer());
        stopButton.setOnAction(e -> stopTimer());
        resetButton.setOnAction(e -> resetTimer());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER); // Center align buttons
        buttonBox.getChildren().addAll(startButton, stopButton, resetButton);

        // Create columns for the table
        TableColumn<TimerRecord, String> startTimeCol = new TableColumn<>("Start Time");
        startTimeCol.setCellValueFactory(data -> data.getValue().startTimeProperty());

        TableColumn<TimerRecord, String> endTimeCol = new TableColumn<>("End Time");
        endTimeCol.setCellValueFactory(data -> data.getValue().endTimeProperty());

        TableColumn<TimerRecord, String> totalTimeCol = new TableColumn<>("Total Time");
        totalTimeCol.setCellValueFactory(data -> data.getValue().totalTimeProperty());

        // Create the timer history table and add columns
        timerHistoryTable = new TableView<>();
        timerHistoryTable.getColumns().addAll(startTimeCol, endTimeCol, totalTimeCol);

        // Remove the 4th empty column
        timerHistoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create an HBox to hold the buttons horizontally
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);

        // Create a button to refresh the table
        Button refreshButton = new Button("Refresh Table");
        refreshButton.setOnAction(e -> refreshTimerHistoryTable());

        // Create a button to clear the table
        Button clearButton = new Button("Clear Table");
        clearButton.setOnAction(e -> clearTimerHistoryTable());

        // Add the buttons to the button container
        buttonContainer.getChildren().addAll(refreshButton, clearButton);

        // Create a VBox to hold the table and the button container
        VBox tableBox = new VBox(10);
        tableBox.setAlignment(Pos.CENTER);
        tableBox.getChildren().addAll(timerHistoryTable, buttonContainer);

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(timerDisplay, buttonBox, tableBox);
        root.setId("main-container");

        Scene scene = new Scene(root, 500, 300);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private List<TimerRecord> readTimerRecordsFromFile(String filePath) {
        List<TimerRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String startTime = parts[0].substring(parts[0].indexOf(' ') + 1).trim(); // Extract time part
                    String endTime = parts[1].substring(parts[1].indexOf(' ') + 1).trim(); // Extract time part
                    String totalTime = parts[2].trim();
                    records.add(new TimerRecord(startTime, endTime, totalTime));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private void refreshTimerHistoryTable() {
        // Clear existing data in the table
        timerHistoryTable.getItems().clear();

        // Read timer records from the file
        List<TimerRecord> records = readTimerRecordsFromFile("study_sessions.txt");

        // Add the records to the table
        timerHistoryTable.getItems().addAll(records);

        // Refresh the table view
        timerHistoryTable.refresh();
    }

    private void clearTimerHistoryTable() {
        timerHistoryTable.getItems().clear();
    }

    private void setupTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seconds.set(seconds.get() + 1);
                updateTimeDisplay();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    private void updateTimeDisplay() {
        int hours = seconds.get() / 3600;
        int minutes = (seconds.get() % 3600) / 60;
        int secs = seconds.get() % 60;
        String time = String.format("%02d:%02d:%02d", hours, minutes, secs);
        timerDisplay.setText(time);
    }

    private void startTimer() {
        setupTimer();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = dateFormat.format(new Date());
        timeline.play();
        timerDisplay.setFill(Color.WHITE);
        startButton.setDisable(true);

    }

    private void stopTimer() {
        timeline.pause();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = dateFormat.format(new Date());

        // Calculate total time spent in minutes and seconds
        int totalSeconds = seconds.get();
        int minutes = totalSeconds / 60;
        int remainingSeconds = totalSeconds % 60;

        // Format the total time as "mm:ss"
        String totalTime = String.format("%02d:%02d", minutes, remainingSeconds);

        // Create a study session instance
        StudySession session = new StudySession(startTime, endTime, totalTime);

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Store Time");
        alert.setHeaderText("Do you want to store the current time?");
        alert.setContentText("Click OK to store the time or Cancel to discard it.");

        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        alert.showAndWait().ifPresent(result -> {
            if (result == buttonTypeOK) {
                try (FileWriter fileWriter = new FileWriter("study_sessions.txt", true)) {
                    // Write the session data to the file
                    fileWriter.write(session.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle any file-related errors here
                }
            }
        });

        // Update the start time to the current time
        startTime = endTime;
        // Enable the "Start" button after stopping
        startButton.setDisable(false);
    }

    private void resetTimer() {
        timeline.stop();
        seconds.set(0);
        updateTimeDisplay();
        startButton.setDisable(false);
        timerDisplay.setFill(Color.BLACK);

    }
}
