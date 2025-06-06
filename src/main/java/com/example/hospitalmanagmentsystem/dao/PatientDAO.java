package com.example.hospitalmanagmentsystem.dao;
import com.example.hospitalmanagmentsystem.model.Patient;
import com.example.hospitalmanagmentsystem.repository.DatabaseConnection;
import com.example.hospitalmanagmentsystem.util.CustomLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Data Access Object for Patient entity
 * Implements CRUD operations for patients
 */
public class PatientDAO {

    private static final Logger logger = CustomLogger.createLogger(PatientDAO.class.getName());

    /**
     * Creates a new patient in the database
     * @param patient Patient object to be created
     * @return true if successful, false otherwise
     */
    public boolean createPatient(Patient patient) {
        String sql = "INSERT INTO patients (id, patient_number, surname, first_name, address, telephone) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // If no ID is provided, generate a random UUID
            if (patient.getId() == null || patient.getId().isEmpty()) {
                patient.setId(UUID.randomUUID().toString().substring(0, 10));
            }
            
            stmt.setString(1, patient.getId());
            stmt.setString(2, patient.getPatientNumber());
            stmt.setString(3, patient.getSurname());
            stmt.setString(4, patient.getFirstName());
            stmt.setString(5, patient.getAddress());
            stmt.setString(6, patient.getTelephone());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            logger.severe("Error creating patient: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a patient by their ID
     * @param id Patient ID
     * @return Patient object or null if not found
     */
    public Patient getPatientById(String id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPatientFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error retrieving patient: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Retrieves a patient by their patient number
     * @param patientNumber Patient number
     * @return Patient object or null if not found
     */
    public Patient getPatientByNumber(String patientNumber) {
        String sql = "SELECT * FROM patients WHERE patient_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, patientNumber);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPatientFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error retrieving patient: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Retrieves all patients from the database
     * @return List of Patient objects
     */
    public List<Patient> getPaginatedPatients(int pageNumber, int pageSize) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY surname, first_name LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int offset = (pageNumber - 1) * pageSize;
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(extractPatientFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            logger.severe("Error retrieving paginated patients: " + e.getMessage());
        }

        return patients;
    }

    /**
     * Updates an existing patient in the database
     * @param patient Patient object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patients SET patient_number = ?, surname = ?, first_name = ?, address = ?, telephone = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, patient.getPatientNumber());
            stmt.setString(2, patient.getSurname());
            stmt.setString(3, patient.getFirstName());
            stmt.setString(4, patient.getAddress());
            stmt.setString(5, patient.getTelephone());
            stmt.setString(6, patient.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            logger.severe("Error updating patient: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a patient from the database
     * @param id Patient ID
     * @return true if successful, false otherwise
     */
    public boolean deletePatient(String id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            logger.severe("Error deleting patient: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to extract a Patient object from a ResultSet
     */
    private Patient extractPatientFromResultSet(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getString("id"));
        patient.setPatientNumber(rs.getString("patient_number"));
        patient.setSurname(rs.getString("surname"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setAddress(rs.getString("address"));
        patient.setTelephone(rs.getString("telephone"));
        return patient;
    }
}