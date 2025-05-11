-- Create database for Hospital Information System
CREATE DATABASE HospitalManagmentSystemDb;
USE HospitalManagmentSystemDb;

-- Create employees table
CREATE TABLE employees (
    id VARCHAR(10) PRIMARY KEY,
    employee_number VARCHAR(20) UNIQUE NOT NULL,
    surname VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    address VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('doctor', 'nurse'))
);

-- Create departments table
CREATE TABLE departments (
    id VARCHAR(10) PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    building VARCHAR(50) NOT NULL,
    director_doctor_id VARCHAR(10) NOT NULL,
    CONSTRAINT fk_department_director FOREIGN KEY (director_doctor_id) REFERENCES employees(id)
);

-- Create doctors table
CREATE TABLE doctors (
    id VARCHAR(10) PRIMARY KEY,
    employee_id VARCHAR(10) NOT NULL,
    speciality VARCHAR(100) NOT NULL,
    CONSTRAINT fk_doctor_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- Create wards table
CREATE TABLE wards (
    id VARCHAR(10) PRIMARY KEY,
    department_id VARCHAR(10) NOT NULL,
    ward_number INT NOT NULL,
    number_of_beds INT NOT NULL CHECK (number_of_beds > 0),
    supervisor_nurse_id VARCHAR(10) NOT NULL,
    CONSTRAINT fk_ward_department FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT fk_ward_supervisor FOREIGN KEY (supervisor_nurse_id) REFERENCES employees(id),
    CONSTRAINT uk_ward_dept_number UNIQUE (department_id, ward_number)
);

-- Create nurses table
CREATE TABLE nurses (
    id VARCHAR(10) PRIMARY KEY,
    employee_id VARCHAR(10) NOT NULL,
    department_id VARCHAR(10) NOT NULL,
    rotation VARCHAR(50) NOT NULL,
    salary DECIMAL(10,2) NOT NULL CHECK (salary > 0),
    CONSTRAINT fk_nurse_employee FOREIGN KEY (employee_id) REFERENCES employees(id),
    CONSTRAINT fk_nurse_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Create patients table
CREATE TABLE patients (
    id VARCHAR(10) PRIMARY KEY,
    patient_number VARCHAR(20) UNIQUE NOT NULL,
    surname VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    address VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) NOT NULL
);

-- Create hospitalizations table
CREATE TABLE hospitalizations (
    id VARCHAR(10) PRIMARY KEY,
    patient_id VARCHAR(10) NOT NULL,
    ward_id VARCHAR(10) NOT NULL,
    bed_number INT NOT NULL,
    treating_doctor_id VARCHAR(10) NOT NULL,
    diagnosis VARCHAR(255) NOT NULL,
    admission_date DATE NOT NULL,
    discharge_date DATE,
    CONSTRAINT fk_hospitalization_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
    CONSTRAINT fk_hospitalization_ward FOREIGN KEY (ward_id) REFERENCES wards(id),
    CONSTRAINT fk_hospitalization_doctor FOREIGN KEY (treating_doctor_id) REFERENCES doctors(id),
    CONSTRAINT chk_discharge_date CHECK (discharge_date IS NULL OR discharge_date >= admission_date)
);

-- Create transfers table
CREATE TABLE transfers (
    id VARCHAR(10) PRIMARY KEY,
    patient_id VARCHAR(10) NOT NULL,
    transfer_date DATE NOT NULL,
    reason VARCHAR(255) NOT NULL,
    from_ward_id VARCHAR(10) NOT NULL,
    to_ward_id VARCHAR(10) NOT NULL,
    CONSTRAINT fk_transfer_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
    CONSTRAINT fk_transfer_from_ward FOREIGN KEY (from_ward_id) REFERENCES wards(id),
    CONSTRAINT fk_transfer_to_ward FOREIGN KEY (to_ward_id) REFERENCES wards(id),
    CONSTRAINT chk_different_wards CHECK (from_ward_id != to_ward_id)
);

-- Create indexes for better performance
CREATE INDEX idx_employee_role ON employees(role);
CREATE INDEX idx_nurse_department ON nurses(department_id);
CREATE INDEX idx_ward_department ON wards(department_id);
CREATE INDEX idx_hospitalization_ward ON hospitalizations(ward_id);
CREATE INDEX idx_hospitalization_patient ON hospitalizations(patient_id);
CREATE INDEX idx_hospitalization_doctor ON hospitalizations(treating_doctor_id);
CREATE INDEX idx_transfer_patient ON transfers(patient_id);
