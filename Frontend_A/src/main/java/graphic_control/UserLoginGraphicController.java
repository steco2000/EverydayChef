package graphic_control;

import control.UserCredBean;
import control.UserLoginController;
import control.UserLoginControllerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.MainApp;

import java.io.IOException;

public class UserLoginGraphicController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passField;

    @FXML
    private CheckBox rememberMeCheckbox;

    private Scene scene;

    @FXML
    private void onLoginButtonPression(){
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

    }

    @FXML
    private void onRegButtonPression() throws IOException {
        UserRegistrationGraphicController controller = new UserRegistrationGraphicController();
        controller.loadUI();
        /*
        System.out.println("Registration button pressed");
        FXMLLoader regViewLoader = new FXMLLoader(MainApp.class.getResource("UserRegView.fxml"));
        Scene regView = new Scene(regViewLoader.load(),1315,810);
        MainApp.primaryStage.setScene(regView);
        */
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