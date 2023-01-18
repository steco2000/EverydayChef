package graphic_control;

import beans.RecipeBrowsingTableBean;
import control.BrowseRecipeController;
import factories.BrowseRecipeControllerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import view.MainApp;

import java.io.IOException;

public class BrowseRecipesGraphicController {

    @FXML
    private TableView<RecipeBrowsingTableBean> recipeTable;
    @FXML
    private TableColumn<RecipeBrowsingTableBean, String> nameColumn;
    @FXML
    private TableColumn<RecipeBrowsingTableBean, String> chefColumn;
    @FXML
    private TableColumn<RecipeBrowsingTableBean, String> chefUsernameColumn;

    @FXML
    private TextField searchBar;

    @FXML
    private void onShowRecipeButtonPression(){
        //TODO...
    }

    @FXML
    private void onSearchButtonPression(){
        //TODO...
    }

    @FXML
    private void onBackButtonPression() throws IOException {
        UserHomeGraphicController homeGraphicController = new UserHomeGraphicController();
        homeGraphicController.loadUI();
    }

    private void getSuggestedRecipes() {
        BrowseRecipeControllerFactory factory = new BrowseRecipeControllerFactory();
        BrowseRecipeController browseRecipeController = factory.createBrowseRecipeController();
        ObservableList<RecipeBrowsingTableBean> observableBeanList = FXCollections.observableArrayList();
        observableBeanList.addAll(browseRecipeController.retrieveSuggestedRecipe());
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        chefColumn.setCellValueFactory(new PropertyValueFactory<>("chefCompleteName"));
        chefUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("chefUsername"));
        recipeTable.setItems(observableBeanList);
    }

    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("BrowseRecipesView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        this.getSuggestedRecipes();
        chefUsernameColumn.setVisible(false);
        MainApp.getPrimaryStage().setScene(scene);
    }

}
