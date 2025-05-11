package com.example.hospitalmanagmentsystem.dao;

    import com.example.hospitalmanagmentsystem.model.Patient;
    import org.junit.jupiter.api.*;
    import static org.junit.jupiter.api.Assertions.*;
    import java.util.List;

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class PatientDAOTest {

        private PatientDAO patientDAO;

        @BeforeAll
        void setup() {
            patientDAO = new PatientDAO();
        }

        @Test
        void createPatient() {
            Patient patient = new Patient("12345", "Doe", "John", "123 Main St", "1234567890");
            boolean result = patientDAO.createPatient(patient);
            assertTrue(result, "Patient should be created successfully");
        }

        @Test
        void getPatientById() {
            String testId = "12345";
            Patient patient = patientDAO.getPatientById(testId);
            assertNotNull(patient, "Patient should be found");
            assertEquals(testId, patient.getId(), "Patient ID should match");
        }

        @Test
        void getPatientByNumber() {
            String testNumber = "12345";
            Patient patient = patientDAO.getPatientByNumber(testNumber);
            assertNotNull(patient, "Patient should be found");
            assertEquals(testNumber, patient.getPatientNumber(), "Patient number should match");
        }

        @Test
        void getPaginatedPatients() {
            List<Patient> patients = patientDAO.getPaginatedPatients(1, 10);
            assertNotNull(patients, "Patients list should not be null");
            assertTrue(patients.size() <= 10, "Patients list size should not exceed page size");
        }

        @Test
        void updatePatient() {
            Patient patient = new Patient("12345", "Smith", "Jane", "456 Elm St", "0987654321");
            boolean result = patientDAO.updatePatient(patient);
            assertTrue(result, "Patient should be updated successfully");
        }

        @Test
        void deletePatient() {
            String testId = "12345";
            boolean result = patientDAO.deletePatient(testId);
            assertTrue(result, "Patient should be deleted successfully");
        }
    }