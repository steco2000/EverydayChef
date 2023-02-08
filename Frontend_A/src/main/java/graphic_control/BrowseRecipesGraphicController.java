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

//controller grafico delle schermate di navigazione delle ricette, info di una ricetta e info di uno chef

public class BrowseRecipesGraphicController {

    @FXML
    private Label mainLabel;

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
    private TableColumn<RecipeIngredientBean, String> missingIngredientQuantityCol;
    @FXML
    private TableColumn<RecipeIngredientBean, String> missingIngredientUnitCol;
    @FXML
    private TableView<RecipeIngredientBean> ingredientListTable;
    @FXML
    private TableColumn<RecipeIngredientBean, String> ingredientNamecol;
    @FXML
    private TableColumn<RecipeIngredientBean, String> ingredientQuantityCol;
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

    //questi attributi servono a salvare le informazioni legate a una ricetta quando questa viene selezionata dalla tabella
    private ChefBean recipeChef;
    private RecipeBean selectedRecipeBean;
    private List<RecipeIngredientBean> missingIngredients;

    //lista delle ricette consigliate visualizzate nella tabella
    private List<RecipeBrowsingTableBean> suggestedRecipes;

    /*
    Quando viene premuto il tasto di visualizzazione della ricetta e una ricetta è selezionata dalla tabella, questa deve essere caricata e mostrata. Viene quindi creato l'apposito controller
    applicativo che si occupa di recuperare i vari dati, incluse informazioni dello chef e ingredienti mancanti da mostrare nell'apposita tabella.
     */
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

    /*
    Alla pressione del tasto di ricerca per le ricette, viene salvato il contenuto della search bar e passato al controller applicativo che si occupa di recuperare le ricette legate
    alla ricerca digitata dall'utente. Il risultato della ricerca, se non vuoto, viene inserito nella tabella.
     */
    @FXML
    private void onSearchButtonPression(){
        mainLabel.setText("Search results");
        if(searchBar.getText().isEmpty()) return;
        RecipeSearchingControllerFactory factory = new RecipeSearchingControllerFactory();
        RecipeSearchingController controller = factory.createRecipeSearchingController();
        List<RecipeBrowsingTableBean> searchResult = controller.retrieveSearchResult(searchBar.getText());
        if(searchResult.isEmpty()){
            AlertBox.display("No results","No recipes found.");
        }else {
            ObservableList<RecipeBrowsingTableBean> beanObservableList = FXCollections.observableArrayList();
            beanObservableList.addAll(searchResult);
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            chefColumn.setCellValueFactory(new PropertyValueFactory<>("chefCompleteName"));
            chefUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("chefUsername"));
            recipeTable.setItems(beanObservableList);
        }
    }

    /*
    Se l'utente clicca sulla label con il nome dello chef, nella schermata di info della ricetta, devono essere mostrate le informazioni dello chef, recuperate precedentemente,
    attraverso l'apposita view.
     */
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

    //tasto back della schermata della ricetta, si ricarica la view di navigazione
    @FXML
    private void onRecipePageBackButtonPression() throws IOException {
        this.loadUI();
    }

    //tasto back della schermata di info dello chef, si ricarica la schermata della ricetta selezionata
    @FXML
    private void onChefPageBackButtonPression() throws IOException {
        this.loadRecipePage(selectedRecipeBean,missingIngredients);
    }

    //tasto back della schermata di navigazione, si ricarica la schermata home
    @FXML
    private void onBackButtonPression() throws IOException {
        UserHomeGraphicController homeGraphicController = new UserHomeGraphicController();
        homeGraphicController.loadUI();
    }

    /*
    Questo metodo serve a innescare l'algoritmo di ricerca delle ricette consigliate eseguito dall'apposito controller applicativo. Il risultato della ricerca viene inserito nella
    tabella delle ricette.
     */
    private void getSuggestedRecipes() {
        ObservableList<RecipeBrowsingTableBean> observableBeanList = FXCollections.observableArrayList();
        if(this.suggestedRecipes == null){
            BrowseRecipeControllerFactory factory = new BrowseRecipeControllerFactory();
            BrowseRecipeController browseRecipeController = factory.createBrowseRecipeController();
            this.suggestedRecipes = browseRecipeController.retrieveSuggestedRecipe();
        }
        observableBeanList.addAll(this.suggestedRecipes);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        chefColumn.setCellValueFactory(new PropertyValueFactory<>("chefCompleteName"));
        chefUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("chefUsername"));
        recipeTable.setItems(observableBeanList);
    }

    /*
    Questo metodo coordina il caricamento e la visualizzazione della schermata di informazioni di una ricetta ricetta, una volta che questa viene recuperata dal controller applicativo
    "RecipeInfoRetrieving". Vengono popolate le tabelle degli ingredienti richiesti e mancanti, più tutti gli altri campi.
     */
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

    //questo metodo serve a popolare la tabella degli ingredienti richiesti per la schermata di info di una ricetta
    private void setUpIngredientsTable(List<RecipeIngredientBean> ingredientList) {
        ingredientNamecol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ingredientQuantityCol.setCellValueFactory(new PropertyValueFactory<>("stringQuantity"));
        ingredientUnitCol.setCellValueFactory(new PropertyValueFactory<>("measureUnit"));
        ObservableList<RecipeIngredientBean> beanObservableList = FXCollections.observableArrayList();
        beanObservableList.addAll(ingredientList);
        ingredientListTable.setItems(beanObservableList);
    }

    //questo metodo serve a popolare la tabella degli ingredienti mancanti per la schermata di info di una ricetta
    private void setUpMissingIngredientsTable(List<RecipeIngredientBean> missingIngredients) {
        missingIngredientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        missingIngredientQuantityCol.setCellValueFactory(new PropertyValueFactory<>("stringQuantity"));
        missingIngredientUnitCol.setCellValueFactory(new PropertyValueFactory<>("measureUnit"));
        ObservableList<RecipeIngredientBean> beanObservableList = FXCollections.observableArrayList();
        beanObservableList.addAll(missingIngredients);
        missingIngredientsTable.setItems(beanObservableList);
    }

    /*
    Questo è il metodo che gestisce caricamento e visualizzazione della schermata di navigazione. L'operazione principale che viene eseguita è il recupero e la visualizzazione
    delle ricette consigliate sfruttando l'apposito metodo definito precedentemente. La colonna dell'username è fittizia, serve solo a mantenere un riferimento univoco allo chef se la
    ricetta viene selezionata. Viene perciò resa invisibile all'utente.
     */
    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("BrowseRecipesView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        this.getSuggestedRecipes();
        chefUsernameColumn.setVisible(false);
        MainApp.getPrimaryStage().setScene(scene);
        if(this.recipeTable.getItems().isEmpty()) AlertBox.display("No recommended recipe found","It seems that there aren't recipes that we can recommend to you.\n Remember to always update your ingredients inventory to get personalized recommendations on recipes you can cook!");
    }

}
