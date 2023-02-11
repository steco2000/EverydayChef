package graphic_control;

import beans.UserCredBean;
import control.UserLoginController;
import exceptions.PersistentDataAccessException;
import factories.UserLoginControllerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.view.MainApp;
import utilities.AlertBox;
import java.io.IOException;
import java.lang.reflect.MalformedParametersException;

//controller grafico della schermata di registrazione per l'utente

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

    //alla pressione del tasto di registrazione si raccolgono i dati nei campi all'interno del bean, che viene poi passato al controller applicativo per la procedura di registrazione
    @FXML
    private void onRegistrationButtonPression() throws IOException {
        UserCredBean credentials = new UserCredBean();
        UserLoginControllerFactory factory = new UserLoginControllerFactory();

        //nel caso in cui i dati inseriti siano considerati illegali dal sistema il bean lancerà delle eccezioni
        try {
            credentials.setEmail(emailField.getText());
            credentials.setUsername(usernameField.getText());

            String passw = passwordField.getText();
            String passwConf = passwordConfirmationField.getText();

            if (passw.equals(passwConf)) {
                credentials.setPassword(passw);
                UserLoginController controller = factory.createUserLoginController();

                //la registrazione può ovviamente fallire nel caso in cui email o username siano già utilizzati
                if (controller.registerUser(credentials)) {
                    UserLoginGraphicController loginGraphicController = new UserLoginGraphicController();
                    loginGraphicController.loadUI();
                } else {
                    AlertBox.display(ERROR_BOX_TITLE, "Unable to register, username or email already used.");
                }

            } else {
                AlertBox.display(ERROR_BOX_TITLE, "Password doesn't match!");
            }
        }catch(IllegalArgumentException e){
            AlertBox.display(ERROR_BOX_TITLE,"Some required fields are missing.");
        }catch(MalformedParametersException e){
            AlertBox.display(ERROR_BOX_TITLE,"Email is not valid.");
        }catch(PersistentDataAccessException e){
            AlertBox.display(ERROR_BOX_TITLE, e.getMessage());
        }
    }

    //premendo il tasto back si ricarica la schermata di login
    @FXML
    private void onBackButtonPression() throws IOException {
        UserLoginGraphicController logController = new UserLoginGraphicController();
        logController.loadUI();
    }

    //caricamento e visualizzazione dell'interfaccia
    public void loadUI() throws IOException {
        FXMLLoader regViewLoader = new FXMLLoader(MainApp.class.getResource("UserRegView.fxml"));
        regViewLoader.setController(this);
        Scene regView = new Scene(regViewLoader.load(),1315,810);
        MainApp.getPrimaryStage().setScene(regView);
    }
}
