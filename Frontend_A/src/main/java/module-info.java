module Frontend_A {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires Backend;
    requires Utilities;

    opens main.view to javafx.fxml;
    exports main.view;
    opens graphic_control to javafx.fxml;
    exports graphic_control;
}