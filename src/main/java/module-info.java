module com.example.hospitalmanagmentsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.hospitalmanagmentsystem to javafx.fxml;
    exports com.example.hospitalmanagmentsystem;
}