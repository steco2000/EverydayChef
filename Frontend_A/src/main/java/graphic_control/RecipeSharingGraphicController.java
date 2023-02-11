package graphic_control;

import beans.RecipeBean;
import beans.RecipeIngredientBean;
import beans.RecipeTableDataBean;
import control.RecipeSharingController;
import control.RecipeUpdadingController;
import exceptions.PersistentDataAccessException;
import exceptions.RecipeIngredientQuantityException;
import factories.RecipeSharingControllerFactory;
import factories.RecipeUpdatingControllerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utilities.AlertBox;
import utilities.ConfirmBox;
import main.view.MainApp;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

//controller grafico che gestisce la schermata di creazione della ricetta e di informazioni dello chef

public class RecipeSharingGraphicController {

    @FXML
    private TextField nameField;
    @FXML
    private ChoiceBox<String> difficultyBox;
    @FXML
    private TextField prepTimeField;
    @FXML
    private ChoiceBox<String> timeUnitBox;
    @FXML
    private TextField servingsField;
    @FXML
    private TextArea preparationArea;
    @FXML
    private TextField ingredientNameField;
    @FXML
    private TextField ingredientQuantityField;
    @FXML
    private RadioButton justEnoughButton;
    @FXML
    private ChoiceBox<String> unitBox;
    @FXML
    private TableView<RecipeIngredientBean> ingredientTable;
    @FXML
    private TableColumn<RecipeIngredientBean, String> ingredientNameColumn;
    @FXML
    private TableColumn<RecipeIngredientBean, String> ingredientQuantityColumn;
    @FXML
    private TableColumn<RecipeIngredientBean, String> unitColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button shareButton;

    private static final String ERROR_BOX_TITLE = "Error";
    private ObservableList<RecipeIngredientBean> ingredientTableList = FXCollections.observableArrayList();
    private String chefUsername;    //l'username dello chef è necessario per recuperare le sue informazioni con il controller applicativo

    private boolean onUpdateMode = false;   //questo attributo serve a sapere se la schermata è stata avviata in modalità di modifica
    private String toUpdateName;    //nome della ricetta da modificare in tal caso

    /*
    Alla pressione del tasto aggiungi ingrediente viene creato il bean corrispondente, dove vengono incapsulati i dati dai campi della schermata. Per gli ingredienti della ricetta
    è possibile evitare di specificare la quantità di un ingrediente attivando il radio button "Just Enough" ossia "quanto basta". In tal caso i campi di quantità e unità di misura
    verrano disattivati.
     */
    @FXML
    private void onAddIngredientButtonPression(){
        RecipeIngredientBean newIngredient = new RecipeIngredientBean();
        try {
            newIngredient.setName(ingredientNameField.getText());
            for(RecipeIngredientBean i: ingredientTableList){
                if(i.getName().equals(newIngredient.getName())){
                    AlertBox.display(ERROR_BOX_TITLE,"This ingredient is already present.");
                    return;
                }
            }
            if(!justEnoughButton.isSelected()){
                newIngredient.setMeasureUnit(unitBox.getValue());
                newIngredient.setQuantity(ingredientQuantityField.getText(),false);
            }else{
                newIngredient.setQuantity("J. E.",true);
                newIngredient.setMeasureUnit("");
            }
        } catch (ParseException e) {
            AlertBox.display(ERROR_BOX_TITLE,"Invalid quantity.");
            return;

            /*l'eccezione del prossimo catch viene lanciata nel caso in cui il bean riceva quantità nulla, allora viene chiesto all'utente se desidera continuare comunque e che,
            in caso di risposta affermativa, l'ingrediente verrà salvato con la quantità settata a just enough.
             */
        } catch (RecipeIngredientQuantityException e){
            boolean answer = ConfirmBox.display(newIngredient.getName(), "Are you sure that you want to save the ingredient \""+newIngredient.getName()+"\" without a specified quantity? It will be set as just enough");
            if(!answer) return;
            else {
                try {
                    newIngredient.setQuantity("J. E.",true);
                    newIngredient.setMeasureUnit("");
                } catch (ParseException | RecipeIngredientQuantityException ignored) {
                    assert(true); //eccezione ignorata dato che la quantità è settata in questo modo appositamente
                }
            }
        } catch(IllegalArgumentException e){
            AlertBox.display(ERROR_BOX_TITLE,"Some values in fields are missing or not valid.");
            return;
        }
        ingredientTableList.add(newIngredient);
        ingredientNameField.clear();
        ingredientNameField.setDisable(false);
        ingredientQuantityField.clear();
        ingredientNameField.setDisable(false);
        justEnoughButton.setSelected(false);
        unitBox.setValue("Kg");
        unitBox.setDisable(false);
    }

    /*ogni volta che il bottone just enough viene premuto si controlla se è selezionato o meno. Nel primo caso vanno disattivati i campi di quantità e unità di misura, altrimenti vanno
    attivati
     */
    @FXML
    private void onJustEnoughButtonPression(){
        if(justEnoughButton.isSelected()){
            ingredientQuantityField.setDisable(true);
            unitBox.setDisable(true);
        }else{
            ingredientQuantityField.setDisable(false);
            unitBox.setDisable(false);
        }
    }

    //alla pressione del bottone di rimozione di un ingrediente si elimina l'ingrediente selezionato dalla tabella
    @FXML
    private void onRemoveButtonPression(){
        ingredientTableList.remove(ingredientTable.getSelectionModel().getSelectedItem());
    }

    //alla pressione del tasto back si ricarica la home dello chef
    @FXML
    private void onBackButtonPression() throws IOException {
        ChefHomeGraphicController homeGraphicController = new ChefHomeGraphicController();
        homeGraphicController.loadRecipeUI();
    }

    /*
    Quando l'utente preme il tasto share, la ricetta è confermata. A seguito di alcuni controlli preliminari, viene crato il bean che incapsula i dati inseriti dall'utente da passare
    poi al controller applicativo. Nel caso in cui la schermata si trovi in modalità di modifica (flag a true) verrà invocato il metodo dell'UpdatingController, altrimenti dello
    SharingController
     */
    @FXML
    private void onShareButtonPression() throws IOException {
        try {
            if (ingredientTableList.isEmpty()) {
                AlertBox.display(ERROR_BOX_TITLE, "A recipe must have at least one ingredient.");
                return;
            }
            RecipeBean updates = this.getRecipeData();
            if (updates == null) {
                return;
            }
            if (onUpdateMode) {
                RecipeUpdatingControllerFactory updatingControllerFactory = new RecipeUpdatingControllerFactory();
                RecipeUpdadingController updadingController = updatingControllerFactory.createRecipeUpdatingController();
                updadingController.updateRecipe(toUpdateName, updates);
                toUpdateName = null;
            } else {
                RecipeSharingControllerFactory factory = new RecipeSharingControllerFactory();
                RecipeSharingController controller = factory.createRecipeSharingController();
                controller.shareRecipe(updates);
            }
            ChefHomeGraphicController homeGraphicController = new ChefHomeGraphicController();
            homeGraphicController.loadRecipeUI();
        }catch (PersistentDataAccessException e){
            AlertBox.display(ERROR_BOX_TITLE, e.getMessage());
        }
    }

    /*
    Il pulsante delete, riferito alla ricetta, sarà visibile solo in modalità update. Se viene premuto e viene confermata la cancellazione si procede a invocare il relativo metodo del
    controller applicativo.
     */
    @FXML
    private void onDeleteButtonPression() throws IOException {
        try {
            if (ConfirmBox.display("Warning", "Are you sure you want to delete this recipe?")) {
                RecipeUpdatingControllerFactory updatingControllerFactory = new RecipeUpdatingControllerFactory();
                RecipeUpdadingController updadingController = updatingControllerFactory.createRecipeUpdatingController();
                updadingController.deleteRecipe(toUpdateName);
                toUpdateName = null;
                ChefHomeGraphicController homeGraphicController = new ChefHomeGraphicController();
                homeGraphicController.loadRecipeUI();
            }
        }catch (PersistentDataAccessException e){
            AlertBox.display(ERROR_BOX_TITLE, e.getMessage());
        }
    }

    /*
    Questo metodo crea e popola il bean che incapsula i dati di una nuova ricetta o di una esistente che viene modificata. Vengono raccolti tutti i dati dai campi della schermata e
    nel bean avvengono i controlli sintattici.
     */
    private RecipeBean getRecipeData(){
        RecipeBean newRecipe = new RecipeBean();
        try{
            newRecipe.setName(nameField.getText());
            newRecipe.setChefUsername(chefUsername);
            newRecipe.setDifficulty(difficultyBox.getValue());
            double prepTimeValue = Double.parseDouble(prepTimeField.getText());
            if(prepTimeValue <= 0){
                AlertBox.display(ERROR_BOX_TITLE,"Invalid preparation time value");
                return null;
            }
            newRecipe.setPreparationTime(prepTimeField.getText() + " " + timeUnitBox.getValue());
            newRecipe.setServings(servingsField.getText());
            newRecipe.setPreparationProcedure(preparationArea.getText());
            List<RecipeIngredientBean> ingredientList = new ArrayList<>();
            ingredientList.addAll(ingredientTableList);
            newRecipe.setIngredientList(ingredientList);
            return newRecipe;
        } catch (NumberFormatException e) {
            AlertBox.display(ERROR_BOX_TITLE, "Some values in fields are invalid.");
            return null;
        } catch(IllegalArgumentException e){
            AlertBox.display(ERROR_BOX_TITLE, "Some required fields are missing.");
            return null;
        }
    }

    /*
    Quando la schermata è caricata in modalità modifica, vengono compilati tutti i campi dell'interfaccia con lo stato della ricetta che si intende modificare. Questi ultimi vengono
    recuperati grazie all'apposito bean che, essendo observer del RecipeDAO, possiede sempre i dati aggiornati. Inoltre, solo in questo caso, viene reso visibile il pulsante "Delete
    Recipe".
     */
    public void loadUpdateUI(String username, RecipeBean toUpdate) throws IOException {
        onUpdateMode = true;
        this.loadUI(username);
        deleteButton.setVisible(true);
        shareButton.setText("Update Recipe");
        RecipeTableDataBean dataBean = RecipeTableDataBean.getSingletonInstance();
        toUpdateName = toUpdate.getName();
        RecipeBean recipe = dataBean.getRecipe(toUpdateName);
        nameField.setText(recipe.getName());
        difficultyBox.setValue(recipe.getDifficulty());
        String prepTime = recipe.getPreparationTime();
        int valueEnd = prepTime.indexOf(" ");
        prepTimeField.setText(prepTime.substring(0,valueEnd));
        timeUnitBox.setValue(prepTime.substring(valueEnd+1));
        servingsField.setText(String.valueOf(recipe.getServings()));
        preparationArea.setText(recipe.getPreparationProcedure());
        ingredientTableList = FXCollections.observableArrayList();
        ingredientTableList.addAll(recipe.getIngredientList());
        ingredientTable.setItems(ingredientTableList);
    }

    /*
    Questo metodo gestisce caricamento e visualizzazione della schermata di creazione e condivisione della ricetta. I vari choicebox vengono popolati con i valori considerati legali
    nel sistema. Il tasto "Delete" non viene reso visibile.
     */
    public void loadUI(String username) throws IOException {
        chefUsername = username;
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("ShareRecipeView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        deleteButton.setVisible(false);
        difficultyBox.getItems().addAll("Hard","Medium","Easy");
        difficultyBox.setValue("Easy");
        timeUnitBox.getItems().addAll("H","min");
        timeUnitBox.setValue("min");
        unitBox.getItems().addAll("Kg","L","");
        unitBox.setValue("Kg");
        ingredientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ingredientQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("stringQuantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("measureUnit"));
        ingredientTable.setItems(ingredientTableList);
        MainApp.getPrimaryStage().setScene(scene);
    }

}
