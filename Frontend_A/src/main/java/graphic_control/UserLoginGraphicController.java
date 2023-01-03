package graphic_control;

import control.UserCredBean;
import control.UserLoginController;
import control.UserLoginControllerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import View.MainApp;
import utilities.AlertBox;

import java.io.IOException;

public class UserLoginGraphicController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passField;

    @FXML
    private CheckBox rememberMeCheckbox;

    private Scene scene;

    //TODO: eccezioni
    @FXML
    private void onLoginButtonPression() throws IOException {
        String username = usernameField.getText();
        String password = passField.getText();
        System.out.println("Username: "+username+", Password: "+password);
        UserLoginControllerFactory controllerFactory = new UserLoginControllerFactory();
        UserLoginController loginController = controllerFactory.createUserLoginController();

        UserCredBean credBean = new UserCredBean();
        credBean.setUsername(username);
        credBean.setPassword(password);
        credBean.setRememberMe(rememberMeCheckbox.isSelected());

        boolean result = loginController.attemptUserLogin(credBean);
        System.out.println(result);
        if(result){
            UserHomeGraphicController controller = new UserHomeGraphicController();
            controller.loadUI();
        }else{
            AlertBox.display("Login Failed","Login failed: credentials incorrect!");
        }
    }

    @FXML
    private void onRegButtonPression() throws IOException {
        UserRegistrationGraphicController controller = new UserRegistrationGraphicController();
        controller.loadUI();
    }


    //TODO: eccezioni
    @FXML
    private void onChefLoginButtonPression() throws IOException {
        ChefLoginGraphicController chefController = new ChefLoginGraphicController();
        chefController.loadUI();
    }

    //TODO: eccezioni
    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("UserLoginView.fxml"));
        uiLoader.setController(this);
        scene = new Scene(uiLoader.load(),1315,810);
        MainApp.primaryStage.setScene(scene);
    }
}