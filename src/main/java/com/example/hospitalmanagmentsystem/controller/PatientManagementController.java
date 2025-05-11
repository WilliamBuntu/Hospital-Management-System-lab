package com.example.hospitalmanagmentsystem.controller;

import com.example.hospitalmanagmentsystem.dao.PatientDAO;
import com.example.hospitalmanagmentsystem.model.Patient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller class for Patient Management screen
 */
public class PatientManagementController implements Initializable {

    // TableView and columns
    @FXML private TableView<Patient> patientTableView;
    @FXML private TableColumn<Patient, String> colId;
    @FXML private TableColumn<Patient, String> colPatientNumber;
    @FXML private TableColumn<Patient, String> colSurname;
    @FXML private TableColumn<Patient, String> colFirstName;
    @FXML private TableColumn<Patient, String> colAddress;
    @FXML private TableColumn<Patient, String> colTelephone;

    // Form fields
    @FXML private TextField txtId;
    @FXML private TextField txtPatientNumber;
    @FXML private TextField txtSurname;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtTelephone;

    // Buttons
    @FXML private Button btnNew;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    // Status label
    @FXML private Label lblStatus;

    // Data access object
    private final PatientDAO patientDAO = new PatientDAO();

    // Observable list for TableView
    private ObservableList<Patient> patientList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatientNumber.setCellValueFactory(new PropertyValueFactory<>("patientNumber"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        // Load patients
        loadPatients();

        // Add selection listener to table
        patientTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPatientDetails(newValue));

        // Initialize buttons state
        updateButtonStates(false);
    }

    /**
     * Loads patients from database and populates the TableView
     */
    private void loadPatients() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            patientList = FXCollections.observableArrayList(patients);
            patientTableView.setItems(patientList);

            showStatus("Loaded " + patients.size() + " patients", false);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading patients", e.getMessage());
        }
    }

    /**
     * Displays the selected patient's details in the form
     * @param patient the selected patient or null if none selected
     */
    private void showPatientDetails(Patient patient) {
        if (patient != null) {
            // Fill the form
            txtId.setText(patient.getId());
            txtPatientNumber.setText(patient.getPatientNumber());
            txtSurname.setText(patient.getSurname());
            txtFirstName.setText(patient.getFirstName());
            txtAddress.setText(patient.getAddress());
            txtTelephone.setText(patient.getTelephone());

            // Update buttons state
            updateButtonStates(true);
        } else {
            // Clear the form
            clearForm();
        }
    }

    /**
     * Clears the form fields
     */
    private void clearForm() {
        txtId.clear();
        txtPatientNumber.clear();
        txtSurname.clear();
        txtFirstName.clear();
        txtAddress.clear();
        txtTelephone.clear();

        // Update buttons state
        updateButtonStates(false);
    }

    /**
     * Updates the enabled/disabled state of buttons based on selection
     * @param patientSelected whether a patient is selected
     */
    private void updateButtonStates(boolean patientSelected) {
        btnUpdate.setDisable(!patientSelected);
        btnDelete.setDisable(!patientSelected);
    }

    /**
     * Handler for New button
     */
    @FXML
    private void handleNewButton() {
        clearForm();
        updateButtonStates(false);

        // Generate temporary ID (will be replaced by database)
        txtId.setText(UUID.randomUUID().toString().substring(0, 10));

        // Focus on patient number field
        txtPatientNumber.requestFocus();
    }

    /**
     * Handler for Save button
     */
    @FXML
    private void handleSaveButton() {
        if (validateForm()) {
            // Create patient from form data
            Patient patient = new Patient(
                    txtId.getText(),
                    txtPatientNumber.getText(),
                    txtSurname.getText(),
                    txtFirstName.getText(),
                    txtAddress.getText(),
                    txtTelephone.getText()
            );

            try {
                // Save to database
                boolean success = patientDAO.createPatient(patient);

                if (success) {
                    // Refresh table
                    loadPatients();

                    // Select the new patient
                    selectPatientById(patient.getId());

                    showStatus("Patient saved successfully", false);
                } else {
                    showStatus("Failed to save patient", true);
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Save Error", "Error saving patient", e.getMessage());
            }
        }
    }

    /**
     * Handler for Update button
     */
    @FXML
    private void handleUpdateButton() {
        if (validateForm()) {
            // Get selected patient
            Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();

            if (selectedPatient == null) {
                showStatus("No patient selected for update", true);
                return;
            }

            // Create updated patient from form data
            Patient updatedPatient = new Patient(
                    txtId.getText(),
                    txtPatientNumber.getText(),
                    txtSurname.getText(),
                    txtFirstName.getText(),
                    txtAddress.getText(),
                    txtTelephone.getText()
            );

            try {
                // Update in database
                boolean success = patientDAO.updatePatient(updatedPatient);

                if (success) {
                    // Refresh table
                    loadPatients();

                    // Select the updated patient
                    selectPatientById(updatedPatient.getId());

                    showStatus("Patient updated successfully", false);
                } else {
                    showStatus("Failed to update patient", true);
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Update Error", "Error updating patient", e.getMessage());
            }
        }
    }

    /**
     * Handler for Delete button
     */
    @FXML
    private void handleDeleteButton() {
        // Get selected patient
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            showStatus("No patient selected for deletion", true);
            return;
        }

        // Confirm deletion
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Patient");
        confirmDialog.setContentText("Are you sure you want to delete patient " +
                selectedPatient.getFirstName() + " " + selectedPatient.getSurname() + "?");

        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete from database
                boolean success = patientDAO.deletePatient(selectedPatient.getId());

                if (success) {
                    // Refresh table
                    loadPatients();

                    // Clear form
                    clearForm();

                    showStatus("Patient deleted successfully", false);
                } else {
                    showStatus("Failed to delete patient", true);
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Delete Error", "Error deleting patient", e.getMessage());
            }
        }
    }

    /**
     * Handler for Clear Form button
     */
    @FXML
    private void handleClearButton() {
        clearForm();
        patientTableView.getSelectionModel().clearSelection();
    }

    /**
     * Validates the form data
     * @return true if valid, false otherwise
     */
    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();

        if (txtPatientNumber.getText().trim().isEmpty()) {
            errorMessage.append("Patient number is required\n");
        }

        if (txtSurname.getText().trim().isEmpty()) {
            errorMessage.append("Surname is required\n");
        }

        if (txtFirstName.getText().trim().isEmpty()) {
            errorMessage.append("First name is required\n");
        }

        if (txtAddress.getText().trim().isEmpty()) {
            errorMessage.append("Address is required\n");
        }

        if (txtTelephone.getText().trim().isEmpty()) {
            errorMessage.append("Telephone is required\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please correct the following errors:", errorMessage.toString());
            return false;
        }

        return true;
    }

    /**
     * Selects a patient in the table by ID
     * @param id the patient ID
     */
    private void selectPatientById(String id) {
        for (Patient patient : patientList) {
            if (patient.getId().equals(id)) {
                patientTableView.getSelectionModel().select(patient);
                patientTableView.scrollTo(patient);
                break;
            }
        }
    }

    /**
     * Shows a status message
     * @param message the status message
     * @param isError whether it's an error message
     */
    private void showStatus(String message, boolean isError) {
        lblStatus.setText(message);
        lblStatus.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");

        // Clear status after 5 seconds
        new javafx.animation.Timeline(new javafx.animation.KeyFrame(
                Duration.seconds(5),
                event -> lblStatus.setText("")
        )).play();
    }

    /**
     * Shows an alert dialog
     * @param type the alert type
     * @param title the alert title
     * @param header the alert header
     * @param content the alert content
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}