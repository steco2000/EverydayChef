package graphic_control;

import beans.RecipeBean;
import beans.RecipeTableDataBean;
import control.LoginController;
import control.RecipeSharingController;
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
import view.MainApp;
import java.io.IOException;
import java.util.List;

//TODO: eccezioni

public class ChefHomeGraphicController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<RecipeBean> recipeTable;

    @FXML
    private TableColumn<RecipeBean, String> recipesColumn;

    private RecipeTableDataBean dataBean;
    private static final String ERROR_BOX_TITLE = "Error";

    public ChefHomeGraphicController(){
        RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
        RecipeSharingController sharingController = controllerFactory.createRecipeSharingController();
        sharingController.setUpRecipesObserver(LoginController.getChefLogged().getUsername());
        this.dataBean = RecipeTableDataBean.getSingletonInstance();
    }

    private void setUpRecipeTable(){
        recipesColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        recipeTable.setItems(this.getObservableTableData());
    }

    private ObservableList<RecipeBean> getObservableTableData() {
        ObservableList<RecipeBean> observableList = FXCollections.observableArrayList();
        List<RecipeBean> dataList;
        dataList = dataBean.getTableData();
        if(dataList.isEmpty()) return null;
        observableList.addAll(dataList);
        return observableList;
    }

    @FXML
    private void onCreateButtonPression() throws IOException {
        RecipeSharingGraphicController controller = new RecipeSharingGraphicController(this);
        controller.loadUI(LoginController.getChefLogged().getUsername());
    }

    @FXML
    private void onUpdateButtonPression() throws IOException {
        if(recipeTable.getSelectionModel().getSelectedItem() == null){
            AlertBox.display(ERROR_BOX_TITLE,"No recipe selected.");
            return;
        }
        RecipeSharingGraphicController controller = new RecipeSharingGraphicController(this);
        controller.loadUpdateUI(LoginController.getChefLogged().getUsername(), recipeTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void onSaveButtonPression(){
        RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
        RecipeSharingController controller = controllerFactory.createRecipeSharingController();
        controller.saveChanges();
    }

    @FXML
    private void onBackButtonPression() throws IOException {
        RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
        RecipeSharingController controller = controllerFactory.createRecipeSharingController();
        controller.saveChanges();
        this.loadHomeUI();
    }

    @FXML
    private void onManageRecipesButtonPression() throws IOException {
        this.loadRecipeUI();
    }

    @FXML
    private void onRecipeStatsButtonPression(){
        //TODO: implementa
    }

    @FXML
    private void onLogOutButtonPression() throws IOException {
        System.gc();
        ChefLoginGraphicController controller = new ChefLoginGraphicController();
        controller.loadUI();
    }

    public void loadRecipeUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("RecipeManagmentView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        this.setUpRecipeTable();
        MainApp.getPrimaryStage().setScene(scene);
    }

    public void loadHomeUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("ChefHomeView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        welcomeLabel.setText(welcomeLabel.getText()+", "+LoginController.getChefLogged().getUsername()+"!");
        MainApp.getPrimaryStage().setScene(scene);
    }

}
