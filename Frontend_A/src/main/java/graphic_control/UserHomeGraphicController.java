package graphic_control;

import main.view.MainApp;
import control.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

//controller grafico della schermata home per l'utente

public class UserHomeGraphicController {

    @FXML
    private Label welcomeLabel;

    //alla pressione del tasto relativo alla gestione dell'inventario si carica la relativa interfaccia
    @FXML
    private void onInventoryButtonPression() throws IOException {
        InventoryGraphicController controller = new InventoryGraphicController();
        controller.loadUI();
    }

    //alla pressione del tasto relativo alla navigazione delle ricette si carica la relativa interfaccia
    @FXML
    private void onRecipeBrowsingButtonPression() throws IOException {
        BrowseRecipesGraphicController browseController = new BrowseRecipesGraphicController();
        browseController.loadUI();
    }

    /*nel caso in cui venga cliccato il tasto di uscita basta ricaricare la schermata di login. Una successiva autenticazione sovrascriverà le variabili necessarie a identificare
    l'utente loggato.*/
    @FXML
    private void onLogOutButtonPression() throws IOException {
        UserLoginGraphicController controller = new UserLoginGraphicController();
        controller.loadUI();
    }

    //caricamento e visualizzazione della schermata home. La variabile di benvenuto viene personalizzata in base all'utente loggato
    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("UserHomeView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        welcomeLabel.setText(welcomeLabel.getText()+", "+LoginController.getUserLogged().getUsername()+"!");
        MainApp.getPrimaryStage().setScene(scene);
    }

}
