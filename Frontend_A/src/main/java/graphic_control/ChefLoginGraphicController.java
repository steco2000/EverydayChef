package graphic_control;

import beans.ChefBean;
import control.ChefLoginController;
import factories.ChefLoginControllerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utilities.AlertBox;
import view.MainApp;

import java.io.IOException;

public class ChefLoginGraphicController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passField;

    @FXML
    private CheckBox rememberMeCheckbox;

    private static final String ERROR_BOX_TITLE = "Login Failed";

    @FXML
    private void onRegButtonPression() throws IOException {
        ChefRegistrationGraphicController regController = new ChefRegistrationGraphicController();
        regController.loadUI();
    }

    @FXML
    private void onUserLoginButtonPression() throws IOException {
        UserLoginGraphicController userLoginController = new UserLoginGraphicController();
        userLoginController.loadUI();
    }

    @FXML
    private void onLoginButtonPression() throws IOException {
        ChefLoginControllerFactory factory = new ChefLoginControllerFactory();
        ChefLoginController controller = factory.createChefLoginController();

        ChefBean chefCredentials = new ChefBean();

        try {
            chefCredentials.setUsername(usernameField.getText());
            chefCredentials.setPassword(passField.getText());
        }catch(IllegalArgumentException e){
            AlertBox.display(ERROR_BOX_TITLE,"Incorrect credentials.");
            return;
        }

        if(controller.attemptChefLogin(chefCredentials)){
            ChefHomeGraphicController homeController = new ChefHomeGraphicController();
            homeController.loadHomeUI();
        }else{
            AlertBox.display(ERROR_BOX_TITLE,"Incorrect credentials");
        }

    }

    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("ChefLoginView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        MainApp.getPrimaryStage().setScene(scene);
    }

}
