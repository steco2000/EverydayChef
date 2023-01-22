package graphic_control;

import main.view.MainApp;
import control.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

public class UserHomeGraphicController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void onInventoryButtonPression() throws IOException {
        InventoryGraphicController controller = new InventoryGraphicController();
        controller.loadUI();
    }

    @FXML
    private void onRecipeBrowsingButtonPression() throws IOException {
        BrowseRecipesGraphicController browseController = new BrowseRecipesGraphicController();
        browseController.loadUI();
    }

    @FXML
    private void onLogOutButtonPression() throws IOException {
        UserLoginGraphicController controller = new UserLoginGraphicController();
        controller.loadUI();
    }

    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("UserHomeView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        welcomeLabel.setText(welcomeLabel.getText()+", "+LoginController.getUserLogged().getUsername()+"!");
        MainApp.getPrimaryStage().setScene(scene);
    }

}
