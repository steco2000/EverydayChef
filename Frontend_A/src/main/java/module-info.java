module Frontend_A {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires Backend;
    requires Utilities;

    opens View to javafx.fxml;
    exports View;
    opens graphic_control to javafx.fxml;
    exports graphic_control;
}