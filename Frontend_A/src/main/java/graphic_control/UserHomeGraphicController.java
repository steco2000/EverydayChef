package graphic_control;

import View.MainApp;
import control.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

public class UserHomeGraphicController {

    @FXML
    private Label welcomeLabel;

    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("UserHomeView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        welcomeLabel.setText(welcomeLabel.getText()+", "+LoginController.userLogged.getUsername()+"!");
        MainApp.primaryStage.setScene(scene);
    }

    //TODO: eccezioni
    @FXML
    public void onInventoryButtonPression() throws IOException {
        InventoryGraphicController controller = new InventoryGraphicController();
        controller.loadUI();
    }

    @FXML
    public void onRecipeBrowsingButtonPression(){
        //TODO: implementa
    }

}
