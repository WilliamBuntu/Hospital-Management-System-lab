package com.example.hospitalmanagmentsystem.controller;

import com.example.hospitalmanagmentsystem.dao.PatientDAO;
import com.example.hospitalmanagmentsystem.model.Patient;

import com.example.hospitalmanagmentsystem.util.CustomLogger;
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
import java.util.logging.Logger;

/**
 * Controller class for Patient Management screen
 */
public class PatientManagementController implements Initializable {

    private static final Logger logger = CustomLogger.createLogger(PatientManagementController.class.getName());
    public Button btnGetPatientByNumber;
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
    @FXML private Button btnNextPage;
    @FXML private Button btnPreviousPage;

    // Status label
    @FXML private Label lblStatus;
    // Add fields for pagination
    private int pageNumber = 1;
    private final int pageSize = 10; // Number of patients per page


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
        loadPatients(pageNumber); // Load the first page with 10 patients

        // Add selection listener to table
        patientTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPatientDetails(newValue));

        // Initialize buttons state
        updateButtonStates(false);
    }
     // Handler for the "Next Page" button
    @FXML
    private void handleNextPage() {
        pageNumber++;
        loadPatients(pageNumber);
    }
    // Handler for "Previous Page" button
    @FXML
    private void handlePreviousPage() {
        if (pageNumber > 1) {
            pageNumber--;
            loadPatients(pageNumber);
        }
    }

    /**
     * Loads patients from a database and populates the TableView
     */
    private void loadPatients(int pageNumber) {
        try {
            List<Patient> patients = patientDAO.getPaginatedPatients(pageNumber, 10);
            patientList = FXCollections.observableArrayList(patients);
            patientTableView.setItems(patientList);
            logger.info("Loaded " + patients.size() + " patients for page " + pageNumber);
            showStatus("Loaded " + patients.size() + " patients for page " + pageNumber, false);
        } catch (Exception e) {
            logger.severe("Error loading patients: " + e.getMessage());
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

        // Generate temporary ID (will be replaced by a database)
        txtId.setText(UUID.randomUUID().toString().substring(0, 10));

        // Focus on the patient number field
        txtPatientNumber.requestFocus();
    }

    // get patient by number

  @FXML
  private void handleGetPatientByNumber() {
      String patientNumber = txtPatientNumber.getText().trim();

      if (patientNumber.isEmpty()) {
            logger.warning("Patient number is required");
          showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a patient number", null);
          return;
      }

      try {
          // Attempt to retrieve the patient
          Patient patient = patientDAO.getPatientByNumber(patientNumber);

          if (patient != null) {
              // Display patient details
              showPatientDetails(patient);
              logger.info("Loaded patient " + patient.getFirstName() + " " + patient.getSurname());
              showStatus("Loaded patient " + patient.getFirstName() + " " + patient.getSurname(), false);

          } else {
              logger.warning("No patient found with number: " + patientNumber);
              showAlert(Alert.AlertType.WARNING, "Not Found", "No patient found with number: " + patientNumber, null);
          }
      } catch (NumberFormatException e) {
          logger.severe("Invalid patient number format: " + e.getMessage());
          showAlert(Alert.AlertType.ERROR, "Input Error", "Patient number must be numeric", e.getMessage());
      } catch (Exception e) {
          logger.severe("Unexpected error: " + e.getMessage());
          logger.severe("Stack trace: ");
          for (StackTraceElement element : e.getStackTrace()) {
              logger.severe(element.toString());
          }
          showAlert(Alert.AlertType.ERROR, "Unexpected Error", "An unexpected error occurred", e.getMessage());
      }
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
                // Save to a database
                boolean success = patientDAO.createPatient(patient);

                if (success) {
                    // Refresh table
                    loadPatients(pageNumber);

                    // Select the new patient
                    selectPatientById(patient.getId());
                    logger.info("Saved patient " + patient.getFirstName() + " " + patient.getSurname());
                    showStatus("Patient saved successfully", false);
                } else {
                    logger.warning("Failed to save patient " + patient.getFirstName() + " " + patient.getSurname());
                    showStatus("Failed to save patient", true);
                }
            } catch (Exception e) {
                logger.severe("Error saving patient: " + e.getMessage());
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
            // Get a selected patient
            Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();

            if (selectedPatient == null) {
                logger.warning("No patient selected for update");
                showStatus("No patient selected for update", true);
                return;
            }

            // Create an updated patient from form data
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
                    loadPatients( pageNumber);

                    // Select the updated patient
                    selectPatientById(updatedPatient.getId());
                    logger.info("Updated patient " + updatedPatient.getFirstName() + " " + updatedPatient.getSurname());
                    showStatus("Patient updated successfully", false);
                } else {
                    logger.warning("Failed to update patient " + updatedPatient.getFirstName() + " " + updatedPatient.getSurname());
                    showStatus("Failed to update patient", true);
                }
            } catch (Exception e) {
                logger.severe("Error updating patient: " + e.getMessage());
                showAlert(Alert.AlertType.ERROR, "Update Error", "Error updating patient", e.getMessage());
            }
        }
    }

    /**
     * Handler for Delete button
     */
    @FXML
    private void handleDeleteButton() {
        // Get a selected patient
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            logger.warning("No patient selected for deletion");
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
                    loadPatients( pageNumber);

                    // Clear form
                    clearForm();
                    logger.info("Deleted patient " + selectedPatient.getFirstName() + " " + selectedPatient.getSurname());
                    showStatus("Patient deleted successfully", false);
                } else {
                    logger.warning("Failed to delete patient " + selectedPatient.getFirstName() + " " + selectedPatient.getSurname());
                    showStatus("Failed to delete patient", true);
                }
            } catch (Exception e) {
                logger.severe("Error deleting patient: " + e.getMessage());
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
            logger.warning("Patient number is required");
            errorMessage.append("Patient number is required\n");
        }

        // check if patientNumber is a number
        if (!txtPatientNumber.getText().matches("\\d+")) {
            logger.warning("Patient number must be a number");
            errorMessage.append("Patient number must be a number\n");
        }

        if (txtSurname.getText().trim().isEmpty()) {
            logger.warning("Surname is required");
            errorMessage.append("Surname is required\n");
        }

        if (txtFirstName.getText().trim().isEmpty()) {
            logger.warning("First name is required");
            errorMessage.append("First name is required\n");
        }

        if (txtAddress.getText().trim().isEmpty()) {
            logger.warning("Address is required");
            errorMessage.append("Address is required\n");
        }

        if (txtTelephone.getText().trim().isEmpty()) {
            logger.warning("Telephone is required");
            errorMessage.append("Telephone is required\n");
        }

        if (!errorMessage.isEmpty()) {
            logger.warning("Validation Error: " + errorMessage.toString());
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