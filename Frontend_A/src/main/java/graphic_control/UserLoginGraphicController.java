package graphic_control;

import beans.UserCredBean;
import control.UserLoginController;
import factories.UserLoginControllerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.view.MainApp;
import utilities.AlertBox;

import java.io.IOException;

//controller grafico della schermata di login per l'utente

public class UserLoginGraphicController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passField;

    private Scene scene;
    private static final String ERROR_BOX_TITLE = "Login Failed";

    //alla pressione del tasto di login si raccolgono i dati dai campi dell'interfaccia nel bean, dopodichè si tenta il login passando il bean al controller applicativo
    @FXML
    private void onLoginButtonPression() throws IOException {
        String username = usernameField.getText();
        String password = passField.getText();
        UserLoginControllerFactory controllerFactory = new UserLoginControllerFactory();
        UserLoginController loginController = controllerFactory.createUserLoginController();

        UserCredBean credBean = new UserCredBean();

        //se le credenziali inserite sono considerate illegali nel sistema si avverte subito l'utente, invece che provare il login
        try {
            credBean.setUsername(username);
            credBean.setPassword(password);
        }catch(IllegalArgumentException e){
            AlertBox.display(ERROR_BOX_TITLE,"Login failed: incorrect credentials");
            return;
        }

        if(loginController.attemptUserLogin(credBean)){
            UserHomeGraphicController controller = new UserHomeGraphicController();
            controller.loadUI();
        }else{
            AlertBox.display(ERROR_BOX_TITLE,"Incorrect credentials");
        }
    }

    //passaggio alla schermata di registrazione
    @FXML
    private void onRegButtonPression() throws IOException {
        UserRegistrationGraphicController controller = new UserRegistrationGraphicController();
        controller.loadUI();
    }


    //passaggio alla schermata di login per lo chef
    @FXML
    private void onChefLoginButtonPression() throws IOException {
        ChefLoginGraphicController chefController = new ChefLoginGraphicController();
        chefController.loadUI();
    }

    //caricamento e visualizzazione dell'interfaccia
    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("UserLoginView.fxml"));
        uiLoader.setController(this);
        scene = new Scene(uiLoader.load(),1315,810);
        MainApp.getPrimaryStage().setScene(scene);
    }
}