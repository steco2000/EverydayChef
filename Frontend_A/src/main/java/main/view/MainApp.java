package main.view;

import graphic_control.UserLoginGraphicController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.ConfirmBox;

import java.io.IOException;

//main applicazione JavaFX

public class MainApp extends Application {

    //lo stage deve essere accessibile a tutti i controller grafici
    private static Stage primaryStage;

    public static Stage getPrimaryStage(){ return primaryStage; }
    private static void setPrimaryStage(Stage stage){ primaryStage = stage; }

    @Override
    public void start(Stage stage) throws IOException {
        setPrimaryStage(stage);
        FXMLLoader loginLoader = new FXMLLoader(MainApp.class.getResource("UserLoginView.fxml"));
        loginLoader.setController(new UserLoginGraphicController());
        Scene loginScene = new Scene(loginLoader.load(),1315,810);
        stage.setTitle("EverydayChef");
        stage.setScene(loginScene);

        //per evitare problemi di visualizzazione dell'interfaccia:
        stage.setResizable(false);

        /*
        alla richiesta di chiusura dell'applicazione l'utente viene avvertito che eventuali cambiamenti potrebbero essere persi se non salvati, per via delle varie procedure di
        caching che effettua il sistema (inventario e ricette)
         */
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