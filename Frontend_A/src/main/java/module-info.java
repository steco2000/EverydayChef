module Frontend_A {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires Backend;
    requires Utilities;

    opens main to javafx.fxml;
    exports main;
    opens graphic_control to javafx.fxml;
    exports graphic_control;
}