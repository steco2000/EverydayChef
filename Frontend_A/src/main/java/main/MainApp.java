package main;

import graphic_control.UserLoginGraphicController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}