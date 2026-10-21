module org.example.klient {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.logging;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.google.gson;
    requires java.desktop;
    requires java.management;

    opens com.dto to javafx.base;
    opens com.controller to javafx.fxml;
    exports com;
}
