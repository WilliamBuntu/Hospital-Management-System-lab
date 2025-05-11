package com.example.hospitalmanagmentsystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Application class for Hospital Information System
 * JavaFX application entry point
 */
public class HospitalApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/hospitalmanagmentsystem/patient.fxml"));

        // Set the scene
        Scene scene = new Scene(root, 800, 600);

        // Add the CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("/com/example/hospitalmanagmentsystem/application.css").toExternalForm());

        // Configure the stage
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * Main method
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}