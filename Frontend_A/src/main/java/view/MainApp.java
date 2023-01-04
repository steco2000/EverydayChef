package view;

import graphic_control.UserLoginGraphicController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.ConfirmBox;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage(){ return primaryStage; };
    private void setPrimaryStage(Stage stage){ primaryStage = stage; }

    @Override
    public void start(Stage stage) throws IOException {
        setPrimaryStage(stage);
        FXMLLoader loginLoader = new FXMLLoader(MainApp.class.getResource("UserLoginView.fxml"));
        loginLoader.setController(new UserLoginGraphicController());
        Scene loginScene = new Scene(loginLoader.load(),1315,810);
        stage.setTitle("EverydayChef");
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
                e.consume();
                boolean answer = ConfirmBox.display("Warning", "Any unsaved changes may be lost, are you sure to proceed?");
                if(answer) stage.close();
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}