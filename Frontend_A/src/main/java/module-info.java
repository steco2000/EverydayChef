module Frontend_A {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires Backend;
    requires Utilities;

    opens view to javafx.fxml;
    exports view;
    opens graphic_control to javafx.fxml;
    exports graphic_control;
}