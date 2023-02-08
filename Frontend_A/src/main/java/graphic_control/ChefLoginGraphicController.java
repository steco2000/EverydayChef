package graphic_control;

import beans.ChefBean;
import control.ChefLoginController;
import factories.ChefLoginControllerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utilities.AlertBox;
import main.view.MainApp;

import java.io.IOException;

//controller grafico della schermata di login dell'utente chef

public class ChefLoginGraphicController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passField;

    private static final String ERROR_BOX_TITLE = "Login Failed";

    //alla pressione del tasto di visualizzazione viene caricata la relativa schermata
    @FXML
    private void onRegButtonPression() throws IOException {
        ChefRegistrationGraphicController regController = new ChefRegistrationGraphicController();
        regController.loadUI();
    }

    //alla pressione del tasto "Log in as user" viene caricata la relativa schermata
    @FXML
    private void onUserLoginButtonPression() throws IOException {
        UserLoginGraphicController userLoginController = new UserLoginGraphicController();
        userLoginController.loadUI();
    }

    /*
    Alla pressione del tasto di login, i valori inseriti nei campi della schermata vengono incapsulati nel bean e passati al controller applicativo per il tentativo di login.
     */
    @FXML
    private void onLoginButtonPression() throws IOException {
        ChefLoginControllerFactory factory = new ChefLoginControllerFactory();
        ChefLoginController controller = factory.createChefLoginController();

        ChefBean chefCredentials = new ChefBean();

        //se il bean riconosce dati considerati illegali dal sistema verrà lanciata l'eccezione
        try {
            chefCredentials.setUsername(usernameField.getText());
            chefCredentials.setPassword(passField.getText());
        }catch(IllegalArgumentException e){
            AlertBox.display(ERROR_BOX_TITLE,"Incorrect credentials.");
            return;
        }

        //se il tentativo di login fallisce il metodo del controller ritorna falso
        if(controller.attemptChefLogin(chefCredentials)){
            ChefHomeGraphicController homeController = new ChefHomeGraphicController();
            homeController.loadHomeUI();
        }else{
            AlertBox.display(ERROR_BOX_TITLE,"Incorrect credentials");
        }

    }

    //metodo chegestisce caricamento e visualizzazione della schermata
    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("ChefLoginView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        MainApp.getPrimaryStage().setScene(scene);
    }

}
