package com.example.hospitalmanagmentsystem.consoleDemo;
import com.example.hospitalmanagmentsystem.util.DatabaseConnection;
import com.example.hospitalmanagmentsystem.model.Patient;
import com.example.hospitalmanagmentsystem.dao.PatientDAO;


import java.util.List;
import java.util.Scanner;

/**
 * The Main application class for Hospital Management System
 * Demonstrates CRUD operations on Patient entity
 */
public class HospitalManagementAppDemo {
    private static final PatientDAO patientDAO = new PatientDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Hospital Management System");
        
        // Test database connection
        if (DatabaseConnection.testConnection()) {
            System.out.println("Database connection is working properly.");
        } else {
            System.err.println("Failed to connect to database. Please check connection settings.");
            return;
        }
        
        boolean exit = false;
        while (!exit) {
            displayMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    addNewPatient();
                    break;
                case 2:
                    viewAllPatients();
                    break;
                case 3:
                    viewPatientById();
                    break;
                case 4:
                    updatePatientInfo();
                    break;
                case 5:
                    deletePatient();
                    break;
                case 0:
                    exit = true;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n==== Patient Management ====");
        System.out.println("1. Add New Patient");
        System.out.println("2. View All Patients");
        System.out.println("3. View Patient by ID");
        System.out.println("4. Update Patient Information");
        System.out.println("5. Delete Patient");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void addNewPatient() {
        System.out.println("\n==== Add New Patient ====");
        
        System.out.print("Enter patient number: ");
        String patientNumber = scanner.nextLine();
        
        System.out.print("Enter surname: ");
        String surname = scanner.nextLine();
        
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        
        System.out.print("Enter telephone: ");
        String telephone = scanner.nextLine();
        
        Patient newPatient = new Patient(patientNumber, surname, firstName, address, telephone);
        
        if (patientDAO.createPatient(newPatient)) {
            System.out.println("Patient added successfully!");
        } else {
            System.out.println("Failed to add patient.");
        }
    }
    
    private static void viewAllPatients() {
        System.out.println("\n==== All Patients ====");
        
        List<Patient> patients = patientDAO.getAllPatients();
        
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            System.out.printf("%-10s %-15s %-15s %-15s %-30s %-15s%n", 
                    "ID", "Patient #", "Surname", "First Name", "Address", "Telephone");
            System.out.println("-".repeat(100));
            
            for (Patient patient : patients) {
                System.out.printf("%-10s %-15s %-15s %-15s %-30s %-15s%n",
                        patient.getId(),
                        patient.getPatientNumber(),
                        patient.getSurname(),
                        patient.getFirstName(),
                        patient.getAddress(),
                        patient.getTelephone());
            }
        }
    }
    
    private static void viewPatientById() {
        System.out.println("\n==== View Patient by ID ====");
        
        System.out.print("Enter patient ID: ");
        String id = scanner.nextLine();
        
        Patient patient = patientDAO.getPatientById(id);
        
        if (patient != null) {
            System.out.println("\nPatient Details:");
            System.out.println("ID: " + patient.getId());
            System.out.println("Patient Number: " + patient.getPatientNumber());
            System.out.println("Name: " + patient.getFirstName() + " " + patient.getSurname());
            System.out.println("Address: " + patient.getAddress());
            System.out.println("Telephone: " + patient.getTelephone());
        } else {
            System.out.println("Patient not found with ID: " + id);
        }
    }
    
    private static void updatePatientInfo() {
        System.out.println("\n==== Update Patient Information ====");
        
        System.out.print("Enter patient ID to update: ");
        String id = scanner.nextLine();
        
        Patient patient = patientDAO.getPatientById(id);
        
        if (patient != null) {
            System.out.println("\nCurrent Patient Details:");
            System.out.println("ID: " + patient.getId());
            System.out.println("Patient Number: " + patient.getPatientNumber());
            System.out.println("Name: " + patient.getFirstName() + " " + patient.getSurname());
            System.out.println("Address: " + patient.getAddress());
            System.out.println("Telephone: " + patient.getTelephone());
            
            System.out.println("\nEnter new details (leave blank to keep current value):");
            
            System.out.print("Patient Number [" + patient.getPatientNumber() + "]: ");
            String patientNumber = scanner.nextLine();
            if (!patientNumber.isEmpty()) {
                patient.setPatientNumber(patientNumber);
            }
            
            System.out.print("Surname [" + patient.getSurname() + "]: ");
            String surname = scanner.nextLine();
            if (!surname.isEmpty()) {
                patient.setSurname(surname);
            }
            
            System.out.print("First Name [" + patient.getFirstName() + "]: ");
            String firstName = scanner.nextLine();
            if (!firstName.isEmpty()) {
                patient.setFirstName(firstName);
            }
            
            System.out.print("Address [" + patient.getAddress() + "]: ");
            String address = scanner.nextLine();
            if (!address.isEmpty()) {
                patient.setAddress(address);
            }
            
            System.out.print("Telephone [" + patient.getTelephone() + "]: ");
            String telephone = scanner.nextLine();
            if (!telephone.isEmpty()) {
                patient.setTelephone(telephone);
            }
            
            if (patientDAO.updatePatient(patient)) {
                System.out.println("Patient information updated successfully!");
            } else {
                System.out.println("Failed to update patient information.");
            }
        } else {
            System.out.println("Patient not found with ID: " + id);
        }
    }
    
    private static void deletePatient() {
        System.out.println("\n==== Delete Patient ====");
        
        System.out.print("Enter patient ID to delete: ");
        String id = scanner.nextLine();
        
        Patient patient = patientDAO.getPatientById(id);
        
        if (patient != null) {
            System.out.println("\nPatient Details:");
            System.out.println("ID: " + patient.getId());
            System.out.println("Patient Number: " + patient.getPatientNumber());
            System.out.println("Name: " + patient.getFirstName() + " " + patient.getSurname());
            
            System.out.print("\nAre you sure you want to delete this patient? (y/n): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("y")) {
                if (patientDAO.deletePatient(id)) {
                    System.out.println("Patient deleted successfully!");
                } else {
                    System.out.println("Failed to delete patient.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("Patient not found with ID: " + id);
        }
    }
}