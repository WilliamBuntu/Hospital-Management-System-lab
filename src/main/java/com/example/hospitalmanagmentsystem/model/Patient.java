package com.example.hospitalmanagmentsystem.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class representing a patient in the hospital system
 * Using JavaFX properties for TableView binding
 */
public class Patient {
    private final StringProperty id;
    private final StringProperty patientNumber;
    private final StringProperty surname;
    private final StringProperty firstName;
    private final StringProperty address;
    private final StringProperty telephone;

    // Default constructor
    public Patient() {
        this.id = new SimpleStringProperty("");
        this.patientNumber = new SimpleStringProperty("");
        this.surname = new SimpleStringProperty("");
        this.firstName = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.telephone = new SimpleStringProperty("");
    }

    // Constructor with all fields
    public Patient(String id, String patientNumber, String surname, String firstName, String address, String telephone) {
        this.id = new SimpleStringProperty(id);
        this.patientNumber = new SimpleStringProperty(patientNumber);
        this.surname = new SimpleStringProperty(surname);
        this.firstName = new SimpleStringProperty(firstName);
        this.address = new SimpleStringProperty(address);
        this.telephone = new SimpleStringProperty(telephone);
    }

    // Constructor without ID (for new patients)
    public Patient(String patientNumber, String surname, String firstName, String address, String telephone) {
        this.id = new SimpleStringProperty("");
        this.patientNumber = new SimpleStringProperty(patientNumber);
        this.surname = new SimpleStringProperty(surname);
        this.firstName = new SimpleStringProperty(firstName);
        this.address = new SimpleStringProperty(address);
        this.telephone = new SimpleStringProperty(telephone);
    }

    // Getters and setters for JavaFX properties

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getPatientNumber() {
        return patientNumber.get();
    }

    public StringProperty patientNumberProperty() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber.set(patientNumber);
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getTelephone() {
        return telephone.get();
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + getId() + '\'' +
                ", patientNumber='" + getPatientNumber() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", telephone='" + getTelephone() + '\'' +
                '}';
    }
}