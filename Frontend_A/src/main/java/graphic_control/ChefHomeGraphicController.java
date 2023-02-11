package graphic_control;

import beans.RecipeBean;
import beans.RecipeTableDataBean;
import control.LoginController;
import control.RecipeSharingController;
import exceptions.PersistentDataAccessException;
import factories.RecipeSharingControllerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utilities.AlertBox;
import main.view.MainApp;
import java.io.IOException;
import java.util.List;

//controller grafico che gestisce la schermata home per l'utente chef più la schermata di overview delle ricette salvate

public class ChefHomeGraphicController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<RecipeBean> recipeTable;

    @FXML
    private TableColumn<RecipeBean, String> recipesColumn;

    private RecipeTableDataBean dataBean;
    private static final String ERROR_BOX_TITLE = "Error";
    private static boolean recipeObserverIsSet = false;

    /*
    Nel costruttore di questo controller, nel caso in cui non sia già stato fatto, viene impostato il RecipeTableDataBean a observer del RecipeDAO, grazie all'apposito metodo del
    controller applicativo. In questo modo l'interfaccia potrà, attraverso il bean, avere sempre i dati aggiornati sulle ricette create dallo chef.
    */
    public ChefHomeGraphicController(){
        if(!recipeObserverIsSet) startRecipesObservation();
        this.dataBean = RecipeTableDataBean.getSingletonInstance();
    }

    private static void startRecipesObservation() {
        try {
            RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
            RecipeSharingController sharingController = controllerFactory.createRecipeSharingController();
            sharingController.setUpRecipesObserver(LoginController.getChefLogged().getUsername());
            recipeObserverIsSet = true;
        }catch(PersistentDataAccessException e){
            AlertBox.display(ERROR_BOX_TITLE, e.getMessage());
        }
    }

    /*
    Questo metodo si occupa di impostare gli elementi della tabella delle ricette create dallo chef, recuperati dal metodo "getObservableTableData"
     */
    private void setUpRecipeTable(){
        recipesColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        recipeTable.setItems(this.getObservableTableData());
    }

    /*
    Questo metodo recupera le ricette create dallo chef per poter successivamente visualizzarle nella tabella tramite un array di ogetti osservabili
     */
    private ObservableList<RecipeBean> getObservableTableData() {
        ObservableList<RecipeBean> observableList = FXCollections.observableArrayList();
        List<RecipeBean> dataList;
        dataList = dataBean.getTableData();
        if(dataList.isEmpty()) return null;
        observableList.addAll(dataList);
        return observableList;
    }

    //alla pressione del tasto di creazione di una nuova ricetta viene caricata la relativa schermata
    @FXML
    private void onCreateButtonPression() throws IOException {
        RecipeSharingGraphicController controller = new RecipeSharingGraphicController();
        controller.loadUI(LoginController.getChefLogged().getUsername());
    }

    //alla pressione del tasto di modifica di una ricetta esistente viene caricata la relativa schermata
    @FXML
    private void onUpdateButtonPression() throws IOException {
        if(recipeTable.getSelectionModel().getSelectedItem() == null){
            AlertBox.display(ERROR_BOX_TITLE,"No recipe selected.");
            return;
        }
        RecipeSharingGraphicController controller = new RecipeSharingGraphicController();
        controller.loadUpdateUI(LoginController.getChefLogged().getUsername(), recipeTable.getSelectionModel().getSelectedItem());
    }

    //alla pressione del tasto save vengono salvate le modifiche effettuate invocando il metodo del controller applicativo
    @FXML
    private void onSaveButtonPression(){
        try {
            RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
            RecipeSharingController controller = controllerFactory.createRecipeSharingController();
            controller.saveChanges();
        }catch (PersistentDataAccessException e){
            AlertBox.display(ERROR_BOX_TITLE, e.getMessage());
        }
    }

    /*
    Il tasto back è presente nell'overview delle ricette inserite, alla sua pressione si deve caricare la schermata home
     */
    @FXML
    private void onBackButtonPression() throws IOException {
        try {
            RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
            RecipeSharingController controller = controllerFactory.createRecipeSharingController();
            controller.saveChanges();
            this.loadHomeUI();
        }catch (PersistentDataAccessException e){
            AlertBox.display(ERROR_BOX_TITLE, e.getMessage());
        }
    }

    /*
    Alla pressione del tasto relativo alla gestione delle ricette, invece, deve essere caricata la schermata di overview con la tabella
     */
    @FXML
    private void onManageRecipesButtonPression() throws IOException {
        this.loadRecipeUI();
    }

    //alla pressione relativo alle statistiche delle ricette viene caricata la relativa schermata
    @FXML
    private void onRecipeStatsButtonPression() throws IOException {
        RecipeStatisticsGraphicController statisticsGraphicController = new RecipeStatisticsGraphicController(LoginController.getChefLogged().getUsername());
        statisticsGraphicController.loadUI();
    }

    //alla pressione del tasto di log out basta ricaricare la schermata di login, una successiva autenticazione sovrascriverà le variabili di identificazione dello chef loggato
    @FXML
    private void onLogOutButtonPression() throws IOException {
        ChefLoginGraphicController controller = new ChefLoginGraphicController();
        controller.loadUI();
    }

    //metodo che gestisce caricamento e visualizzazione della schermata di overview delle ricette caricate
    public void loadRecipeUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("RecipeManagementView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        this.setUpRecipeTable();
        MainApp.getPrimaryStage().setScene(scene);
    }

    //metodo che gestisce caricamento e visualizzazione della schermata home generale
    public void loadHomeUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("ChefHomeView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        welcomeLabel.setText(welcomeLabel.getText()+", "+LoginController.getChefLogged().getUsername()+"!");
        MainApp.getPrimaryStage().setScene(scene);
    }

}
