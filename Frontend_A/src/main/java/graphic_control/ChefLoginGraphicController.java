package graphic_control;

import control.ChefBean;
import control.ChefLoginController;
import control.ChefLoginControllerFactory;
import control.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.MainApp;

import java.io.IOException;

public class ChefLoginGraphicController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passField;

    @FXML
    private CheckBox rememberMeCheckbox;

    private FXMLLoader uiLoader;

    //TODO: gestisci eccezioni
    @FXML
    private void onRegButtonPression() throws IOException {
        ChefRegistrationGraphicController regController = new ChefRegistrationGraphicController();
        regController.loadUI();
    }

    //TODO: eccezioni
    @FXML
    private void onUserLoginButtonPression() throws IOException {
        UserLoginGraphicController userLoginController = new UserLoginGraphicController();
        userLoginController.loadUI();
    }

    @FXML
    private void onLoginButtonPression(){
        //TODO: attempt chef login
        ChefLoginControllerFactory factory = new ChefLoginControllerFactory();
        ChefLoginController controller = factory.createChefLoginController();

        ChefBean chefCredentials = new ChefBean();
        chefCredentials.setUsername(usernameField.getText());
        chefCredentials.setPassword(passField.getText());

        boolean result = controller.attemptChefLogin(chefCredentials);

        //CODICE DI TEST
        if(result) System.out.println("Chef logged, username: "+ LoginController.chefLogged.getUsername()+", password: "+LoginController.chefLogged.getPassword()+", ID: "+LoginController.chefLogged.getId());
        else System.out.println("Login failed");
    }

    //TODO: eccezioni
    public void loadUI() throws IOException {
        uiLoader = new FXMLLoader(MainApp.class.getResource("ChefLoginView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        MainApp.primaryStage.setScene(scene);
    }

}
