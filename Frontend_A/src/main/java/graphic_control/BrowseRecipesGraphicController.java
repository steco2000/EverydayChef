package graphic_control;

import beans.ChefBean;
import beans.RecipeBean;
import beans.RecipeBrowsingTableBean;
import beans.RecipeIngredientBean;
import control.BrowseRecipeController;
import control.RecipeInfoRetrievingController;
import control.RecipeSearchingController;
import factories.BrowseRecipeControllerFactory;
import factories.RecipeInfoRetrievingControllerFactory;
import factories.RecipeSearchingControllerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utilities.AlertBox;
import main.view.MainApp;

import java.io.IOException;
import java.util.List;

public class BrowseRecipesGraphicController {

    //schermata ricette consigliate
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

    //schermata info ricetta
    @FXML
    private Label recipeNameLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label prepTimeLabel;
    @FXML
    private Label servingsLabel;
    @FXML
    private Label chefLabel;
    @FXML
    private TextArea preparationArea;
    @FXML
    private TableView<RecipeIngredientBean> missingIngredientsTable;
    @FXML
    private TableColumn<RecipeIngredientBean, String> missingIngredientNameCol;
    @FXML
    private TableColumn<RecipeIngredientBean, Double> missingIngredientQuantityCol;
    @FXML
    private TableColumn<RecipeIngredientBean, String> missingIngredientUnitCol;
    @FXML
    private TableView<RecipeIngredientBean> ingredientListTable;
    @FXML
    private TableColumn<RecipeIngredientBean, String> ingredientNamecol;
    @FXML
    private TableColumn<RecipeIngredientBean, Double> ingredientQuantityCol;
    @FXML
    private TableColumn<RecipeIngredientBean, String> ingredientUnitCol;

    //schermata info chef
    @FXML
    private Label chefPageNameLabel;
    @FXML
    private Label chefPageBirthDateLabel;
    @FXML
    private Label chefPageEmailLabel;
    @FXML
    private TextArea chefPageBioArea;

    private ChefBean recipeChef;
    private RecipeBean selectedRecipeBean;
    private List<RecipeIngredientBean> missingIngredients;
    private List<RecipeBrowsingTableBean> suggestedRecipes;

    @FXML
    private void onShowRecipeButtonPression() throws IOException {
        RecipeBrowsingTableBean recipeSelected = recipeTable.getSelectionModel().getSelectedItem();
        if(recipeSelected == null){
            AlertBox.display("Error","No recipe selected.");
            return;
        }
        RecipeInfoRetrievingControllerFactory factory = new RecipeInfoRetrievingControllerFactory();
        RecipeInfoRetrievingController controller = factory.createRecipeInfoRetrievingController();
        selectedRecipeBean = controller.retrieveRecipeInfo(recipeSelected);
        this.recipeChef = controller.retrieveChefInfo(recipeSelected.getChefUsername());
        missingIngredients = controller.retrieveMissingIngredients(recipeSelected);
        this.loadRecipePage(selectedRecipeBean,missingIngredients);
    }

    @FXML
    private void onSearchButtonPression(){
        if(searchBar.getText().isEmpty()) return;
        RecipeSearchingControllerFactory factory = new RecipeSearchingControllerFactory();
        RecipeSearchingController controller = factory.createRecipeSearchingController();
        List<RecipeBrowsingTableBean> searchResult = controller.retrieveSearchResult(searchBar.getText());
        ObservableList<RecipeBrowsingTableBean> beanObservableList = FXCollections.observableArrayList();
        beanObservableList.addAll(searchResult);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        chefColumn.setCellValueFactory(new PropertyValueFactory<>("chefCompleteName"));
        chefUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("chefUsername"));
        recipeTable.setItems(beanObservableList);
    }

    @FXML
    private void onChefLabelClick() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("ChefInfoView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        chefPageNameLabel.setText(recipeChef.getName()+" "+recipeChef.getSurname());
        chefPageBirthDateLabel.setText(recipeChef.getBirthDate());
        chefPageEmailLabel.setText(recipeChef.getEmail());
        chefPageBioArea.setText(recipeChef.getInfo());
        chefPageBioArea.setEditable(false);
        MainApp.getPrimaryStage().setScene(scene);
    }

    @FXML
    private void onRecipePageBackButtonPression() throws IOException {
        this.loadUI();
    }

    @FXML
    private void onChefPageBackButtonPression() throws IOException {
        this.loadRecipePage(selectedRecipeBean,missingIngredients);
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
        if(this.suggestedRecipes == null){
            this.suggestedRecipes = browseRecipeController.retrieveSuggestedRecipe();
        }
        observableBeanList.addAll(this.suggestedRecipes);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        chefColumn.setCellValueFactory(new PropertyValueFactory<>("chefCompleteName"));
        chefUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("chefUsername"));
        recipeTable.setItems(observableBeanList);
    }

    private void loadRecipePage(RecipeBean recipe, List<RecipeIngredientBean> missingIngredients) throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("RecipePageView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        this.setUpMissingIngredientsTable(missingIngredients);
        this.setUpIngredientsTable(recipe.getIngredientList());
        Text chefName = new Text(recipeChef.getName()+" "+recipeChef.getSurname());
        chefName.setFill(Color.BLUE);
        chefName.setUnderline(true);
        Font chefNameFont = new Font(18);
        chefName.setFont(chefNameFont);
        chefLabel.setText("");
        chefLabel.setGraphic(chefName);
        recipeNameLabel.setText(recipe.getName());
        difficultyLabel.setText(recipe.getDifficulty());
        prepTimeLabel.setText(recipe.getPreparationTime());
        servingsLabel.setText(String.valueOf(recipe.getServings()));
        preparationArea.setText(recipe.getPreparationProcedure());
        preparationArea.setEditable(false);
        MainApp.getPrimaryStage().setScene(scene);
    }

    private void setUpIngredientsTable(List<RecipeIngredientBean> ingredientList) {
        ingredientNamecol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ingredientQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        ingredientUnitCol.setCellValueFactory(new PropertyValueFactory<>("measureUnit"));
        ObservableList<RecipeIngredientBean> beanObservableList = FXCollections.observableArrayList();
        beanObservableList.addAll(ingredientList);
        ingredientListTable.setItems(beanObservableList);
    }

    private void setUpMissingIngredientsTable(List<RecipeIngredientBean> missingIngredients) {
        missingIngredientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        missingIngredientQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        missingIngredientUnitCol.setCellValueFactory(new PropertyValueFactory<>("measureUnit"));
        ObservableList<RecipeIngredientBean> beanObservableList = FXCollections.observableArrayList();
        beanObservableList.addAll(missingIngredients);
        missingIngredientsTable.setItems(beanObservableList);
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
