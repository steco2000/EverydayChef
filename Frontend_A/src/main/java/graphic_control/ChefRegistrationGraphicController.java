package graphic_control;

import control.ChefBean;
import control.ChefLoginController;
import control.ChefLoginControllerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.MainApp;
import utilities.AlertBox;

import java.io.IOException;

public class ChefRegistrationGraphicController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField birthDateField;

    @FXML
    private TextArea infoField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordConfirmationField;

    private boolean windowFlag = false;
    private Scene sceneA;
    private Scene sceneB;
    private ChefBean newChef;

    //TODO: eccezioni
    @FXML
    private void onBackButtonPression() throws IOException {
        if(windowFlag){
            MainApp.primaryStage.setScene(sceneA);
            windowFlag = false;
        }else{
            ChefLoginGraphicController logController = new ChefLoginGraphicController();
            logController.loadUI();
        }
    }

    @FXML
    private void onNextButtonPression() throws IOException {
        newChef = new ChefBean();
        newChef.setName(nameField.getText());
        newChef.setSurname(surnameField.getText());

        if(newChef.setBirthDate(birthDateField.getText())) {
            newChef.setInfo(infoField.getText());
            FXMLLoader windowBLoader = new FXMLLoader(MainApp.class.getResource("ChefRegistrationView_B.fxml"));
            windowBLoader.setController(this);
            sceneB = new Scene(windowBLoader.load(), 1315, 810);
            windowFlag = true;
            MainApp.primaryStage.setScene(sceneB);
        }else{
            AlertBox.display("Error","Birth Date incorrect!");
        }
    }

    //TODO: eccezioni
    @FXML
    private void onRegisterButtonPression() throws IOException {
        newChef.setUsername(usernameField.getText());
        if(newChef.setEmail(emailField.getText())){
            String pw = passwordField.getText();
            String pwConf = passwordConfirmationField.getText();
            if(pw.equals(pwConf)){
                //TODO: registra chef
                newChef.setPassword(pw);
                ChefLoginControllerFactory factory = new ChefLoginControllerFactory();
                ChefLoginController controller = factory.createChefLoginController();
                if(controller.registerChef(newChef)){
                    ChefLoginGraphicController graphicController = new ChefLoginGraphicController();
                    graphicController.loadUI();
                }else{
                    AlertBox.display("Error","Unable to register, username or email already exists!");
                }
            }else{
                AlertBox.display("Error","Password doesn't match!");
            }
        }else{
            AlertBox.display("Error","Email incorrect!");
        }
    }

    public void loadUI() throws IOException {
        FXMLLoader windowALoader = new FXMLLoader(MainApp.class.getResource("ChefRegistrationView_A.fxml"));
        windowALoader.setController(this);
        sceneA = new Scene(windowALoader.load(),1315,810);
        MainApp.primaryStage.setScene(sceneA);
    }

}
