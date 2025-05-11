module com.example.hospitalmanagmentsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.postgresql.jdbc;

    // 👇 Open to FXML loader
    opens com.example.hospitalmanagmentsystem to javafx.fxml;

    // 👇 FIX: Open your model package to javafx.base (so TableView works)
    opens com.example.hospitalmanagmentsystem.model to javafx.base;

    exports com.example.hospitalmanagmentsystem;
    exports com.example.hospitalmanagmentsystem.controller;
    opens com.example.hospitalmanagmentsystem.controller to javafx.fxml;
    exports com.example.hospitalmanagmentsystem.consoleDemo;
    opens com.example.hospitalmanagmentsystem.consoleDemo to javafx.fxml;
}
