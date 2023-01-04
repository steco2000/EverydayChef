package graphic_control;

import control.UserCredBean;
import control.UserLoginController;
import control.UserLoginControllerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.MainApp;
import utilities.AlertBox;

import java.io.IOException;

//TODO: eccezioni

public class UserRegistrationGraphicController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordConfirmationField;

    private static final String ERROR_BOX_TITLE = "Error";

    @FXML
    private void onRegistrationButtonPression() throws IOException {
        UserCredBean credentials = new UserCredBean();
        UserLoginControllerFactory factory = new UserLoginControllerFactory();

        if(credentials.setEmail(emailField.getText())){
            credentials.setUsername(usernameField.getText());

            String passw = passwordField.getText();
            String passwConf = passwordConfirmationField.getText();

            if(passw.equals(passwConf)){
                credentials.setPassword(passw);
                UserLoginController controller = factory.createUserLoginController();
                if(controller.registerUser(credentials)){
                    UserLoginGraphicController loginGraphicController = new UserLoginGraphicController();
                    loginGraphicController.loadUI();
                }else{
                    AlertBox.display(ERROR_BOX_TITLE, "Unable to register, username or email already used.");
                }

            }else{
                AlertBox.display(ERROR_BOX_TITLE, "Password doesn't match!");
            }

        }else{
            AlertBox.display(ERROR_BOX_TITLE, "Incorrect email!");
        }
    }

    @FXML
    private void onBackButtonPression() throws IOException {
        UserLoginGraphicController logController = new UserLoginGraphicController();
        logController.loadUI();
    }

    public void loadUI() throws IOException {
        FXMLLoader regViewLoader = new FXMLLoader(MainApp.class.getResource("UserRegView.fxml"));
        regViewLoader.setController(this);
        Scene regView = new Scene(regViewLoader.load(),1315,810);
        MainApp.getPrimaryStage().setScene(regView);
    }
}
