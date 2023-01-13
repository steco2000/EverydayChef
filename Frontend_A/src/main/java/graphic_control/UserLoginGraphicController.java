package graphic_control;

import beans.UserCredBean;
import control.UserLoginController;
import factories.UserLoginControllerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.MainApp;
import utilities.AlertBox;

import java.io.IOException;

//TODO: eccezioni

public class UserLoginGraphicController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passField;

    @FXML
    private CheckBox rememberMeCheckbox;

    private Scene scene;

    @FXML
    private void onLoginButtonPression() throws IOException {
        String username = usernameField.getText();
        String password = passField.getText();
        UserLoginControllerFactory controllerFactory = new UserLoginControllerFactory();
        UserLoginController loginController = controllerFactory.createUserLoginController();

        UserCredBean credBean = new UserCredBean();
        credBean.setUsername(username);
        credBean.setPassword(password);
        credBean.setRememberMe(rememberMeCheckbox.isSelected());

        if(loginController.attemptUserLogin(credBean)){
            UserHomeGraphicController controller = new UserHomeGraphicController();
            controller.loadUI();
        }else{
            AlertBox.display("Login Failed","Login failed: incorrect credentials");
        }
    }

    @FXML
    private void onRegButtonPression() throws IOException {
        UserRegistrationGraphicController controller = new UserRegistrationGraphicController();
        controller.loadUI();
    }


    @FXML
    private void onChefLoginButtonPression() throws IOException {
        ChefLoginGraphicController chefController = new ChefLoginGraphicController();
        chefController.loadUI();
    }

    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("UserLoginView.fxml"));
        uiLoader.setController(this);
        scene = new Scene(uiLoader.load(),1315,810);
        MainApp.getPrimaryStage().setScene(scene);
    }
}