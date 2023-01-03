package View;

import graphic_control.UserLoginGraphicController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.ConfirmBox;

import java.io.IOException;

public class MainApp extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader loginLoader = new FXMLLoader(MainApp.class.getResource("UserLoginView.fxml"));
        loginLoader.setController(new UserLoginGraphicController());
        Scene loginScene = new Scene(loginLoader.load(),1315,810);
        primaryStage.setTitle("EverydayChef");
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> {
                e.consume();
                boolean answer = ConfirmBox.display("Warning", "Any unsaved changes may be lost, are you sure to proceed?");
                if(answer) primaryStage.close();
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}