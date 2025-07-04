# Hospital-Management-System-lab
This project implements a Hospital Information System database with Java JDBC integration as part of a database design and implementation assignment.

## Project Structure

- `hospital_db_schema.sql`: SQL script to create the database schema
- `src/main/java/com/example/hospitalmanagmentsystem/db/DatabaseConnection.java:`: Database connection management
- `src/main/java/com/example/hospitalmanagmentsystem/model/Patient.java`: Patient model class
- `src/main/java/com/example/hospitalmanagmentsystem/dao/PatientDAO.java`: Data Access Object for Patient CRUD operations
- `src/main/java/com/example/hospitalmanagmentsystem/HospitalApplication.java`: Main application demonstrating the CRUD operations
- `src/main/resources/patient.fxml`:  FXML file for the patient interface
- `src/main/resources/application.css` : CSS file for styling the JavaFX application

## Database Schema

The database schema is designed based on the provided Entity-Relationship Diagram (ERD) and requirements. It includes the following tables:

1. `employees`: Stores information about hospital employees (doctors and nurses)
2. `departments`: Stores information about hospital departments
3. `doctors`: Stores information specific to doctors
4. `nurses`: Stores information specific to nurses
5. `wards`: Stores information about hospital wards
6. `patients`: Stores information about hospital patients
7. `hospitalizations`: Stores information about patient admissions
8. `transfers`: Stores information about patient transfers between wards

## Getting Started

### Prerequisites

- Java JDK 8 or higher
- MySQL or Postgres database server
- JDBC driver for your database

### Setup Instructions

1. **Set up the database**:
    - Create a MySQL database named `HospitalManagementSystemDb`
    - Run the SQL script `hospital_db_schema.sql` to create the tables

2. **Configure database connection**:
    - Open `DatabaseConnection.java`
    - Update the database URL, username, and password to match your database setup

3. **Compile the Java files**:
   ```
   javac -d out src/main/java/com/example/hospitalmanagmentsystem/db/DatabaseConnection.java src/main/java/com/example/hospitalmanagmentsystem/db//model/Patient.java src/main/java/com/example/hospitalmanagmentsystem/dao/PatientDAO.java src/main/java/com/example/hospitalmanagmentsystem/HospitalManagementApp.java
   ```

4. **Run the application**:
   ```
   java -cp out:mysql-connector-java.jar hospital.HospitalManagementApp
   ```
   (Replace `mysql-connector-java.jar` with the path to your JDBC driver JAR file)

## Features

### CRUD Operations on Patient Entity

The application demonstrates all CRUD operations on the Patient entity:

1. **Create**: Add a new patient to the database
2. **Read**: View all patients or find a patient by ID
3. **Update**: Modify patient information
4. **Delete**: Remove a patient from the database

### Database Normalization

The database schema is normalized to the Third Normal Form (3NF) to eliminate data redundancy:

- 1NF: All tables have a primary key and all attributes are atomic
- 2NF: All non-key attributes are fully functionally dependent on the primary key
- 3NF: No transitive dependencies (non-key attributes don't depend on other non-key attributes)

### Entity Relationships

- Each department has a director who is a doctor
- Each ward belongs to one department and has a supervisor who is a nurse
- A nurse is assigned to only one department
- Doctors aren't assigned to departments but have specialties
- Patients are hospitalized in wards and treated by doctors
- Patient transfers between wards are tracked with reasons

## Project Requirements Fulfilled

1. ✅ Identified and defined entities in the hospital information system
2. ✅ Designed entity-relationship model for the hospital system
3. ✅ Established appropriate relationships and constraints between entities
4. ✅ Normalized the database schema to 3NF
5. ✅ Implemented CRUD operations on the Patient entity


