package graphic_control;

import javafx.scene.control.*;
import main.view.MainApp;
import beans.IngredientBean;
import control.InventoryController;
import factories.InventoryControllerFactory;
import beans.InventoryTableDataBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import utilities.AlertBox;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

//controller grafico delle schermate di gestione dell'inventario ingredienti, aggiunta ingredienti all'inventario e modifica di ingredienti esistenti

public class InventoryGraphicController {

    private InventoryTableDataBean dataBean;
    private InventoryController applController;
    private String toUpdateName;
    private static final String ERROR_BOX_TITLE = "Error";

    @FXML
    private TextField nameField;
    @FXML
    private TextField quantityField;
    @FXML
    private ChoiceBox<String> unitBox;
    @FXML
    private TextField dateField;
    @FXML
    private TextArea notesField;

    @FXML
    private TableView<IngredientBean> inventoryTable;
    @FXML
    private TableColumn<IngredientBean, String> nameColumn;
    @FXML
    private TableColumn<IngredientBean, Double> quantityColumn;
    @FXML
    private TableColumn<IngredientBean, String> unitColumn;
    @FXML
    private TableColumn<IngredientBean, String> expDateColumn;
    @FXML
    private TableColumn<IngredientBean, String> notesColumn;

    /*
    Già nel costruttore di questo controllore viene avviata la procedura di caching dell'inventario dell'utente loggato. Viene creato il controller applicativo e si recupera il bean
    che fa da observer all'inventario per i dati da visualizzare sull'interfaccia. Per questo successive modifiche all'inventario necessitanto di essere salvate dal controller
    applicativo, a seguito di una richiesta esplicita da parte del controller grafico.
     */
    public InventoryGraphicController(){
        InventoryControllerFactory controllerFactory = new InventoryControllerFactory();
        applController = controllerFactory.createInventoryController();
        dataBean = InventoryTableDataBean.getSingletonInstance();
    }

    /*
    Caricamento e visualizzazione della schermata di gestione dell'inventario. La prima operazione pratica che viene eseguita è quella di recuperare i dati relativi all'inventario
    dell'utente corrente per renderli disponibili nella tabella.
     */
    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("InventoryView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        this.setUpTable();
        MainApp.getPrimaryStage().setScene(scene);
    }

    //con questo metodo si inizializza la tabella dell'inventario e si recuperano i dati da inserire in essa
    private void setUpTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("measureUnit"));
        expDateColumn.setCellValueFactory(new PropertyValueFactory<>("stringExpDate"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        inventoryTable.setItems(this.getObservableTableData());
    }

    //questo metodo sfrutta il bean "dataBean", che fa da observer all'inventario dell'utente, per recuperare i dati aggiornati che verranno inseriti nella tabella
    private ObservableList<IngredientBean> getObservableTableData() {
        ObservableList<IngredientBean> observableList = FXCollections.observableArrayList();
        List<IngredientBean> dataList = null;
        try {
            dataList = dataBean.getTableData();
        } catch (ParseException e) {
            AlertBox.display(ERROR_BOX_TITLE,"Data corrupted.");
        }
        if(dataList.isEmpty()) return null;
        for(IngredientBean i: dataList){
            observableList.add(i);
        }
        return observableList;
    }

    //tasto back premuto -> si ricarica la schermata home
    @FXML
    public void onBackButtonPression() throws IOException {
        applController.saveCurrentInventory();
        UserHomeGraphicController controller = new UserHomeGraphicController();
        controller.loadUI();
    }

    //quando l'utente preme il tasto per aggiungere un ingrediente si carica la relativa schermata
    @FXML
    public void onAddIngredientButtonPression() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("AddIngredientView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);

        //il choicebox relativo all'unità di misura degli ingredienti viene riempito con i valori consentiti dal sistema
        unitBox.getItems().addAll("Kg","L","");
        unitBox.setValue("Kg");

        MainApp.getPrimaryStage().setScene(scene);
    }

    /*
    Nel caso in cui l'utente prema il tasto per modificare un ingrediente già presente, oltre a caricare la schermata, devono essere recuperati e mostrati nei relativi campi
    tutti i dati riguardanti quell'ingrediente. Se non è stato selezionato alcun ingrediente dalla tabella non si fa nulla
    */
    @FXML
    public void onUpdateButtonPression() throws IOException {
        IngredientBean ingredientToUpdate = inventoryTable.getSelectionModel().getSelectedItem();
        if(ingredientToUpdate == null) return;
        toUpdateName = ingredientToUpdate.getName();

        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("UpdateIngredientView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);

        unitBox.getItems().addAll("Kg","L","");
        unitBox.setValue("Kg");

        nameField.setText(ingredientToUpdate.getName());
        quantityField.setText(String.valueOf(ingredientToUpdate.getQuantity()));
        unitBox.setValue(ingredientToUpdate.getMeasureUnit());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateValue;
        try {
            dateValue = dateFormat.format(ingredientToUpdate.getExpirationDate());
            dateField.setText(dateValue);
        }catch(NullPointerException e){
            dateField.setText("");
        }
        notesField.setText(ingredientToUpdate.getNotes());

        MainApp.getPrimaryStage().setScene(scene);
    }

    /*
    Questo metodo gestisce il passaggio dei dati di un ingrediente da aggiornare al controller applicativo. Ovviamente questi vengono incapsulati nel bean che effetua i controlli
    sintattici di rito e, in caso di problemi, lancia delle eccezioni. Se l'update non va a buon fine, perchè il nome dell'ingrediente modificato corrisponde a un ingrediente già presente
    nell'inventario, il metodo del controller applicativo ritornerà falso.
     */
    @FXML
    public void onUpdateConfirmationButtonPression() throws IOException {
        IngredientBean updates = new IngredientBean();
        try {
            updates.setName(nameField.getText());
            updates.setQuantity(quantityField.getText());
            updates.setMeasureUnit(unitBox.getValue());
            updates.setExpirationDate(dateField.getText());
            updates.setNotes(notesField.getText());
        }catch(IllegalArgumentException e){
            AlertBox.display(ERROR_BOX_TITLE,"Some required fields are missing.");
            return;
        }catch(ParseException e){
            AlertBox.display(ERROR_BOX_TITLE,"Some values in fields are not valid.");
            return;
        }
        if(!applController.updateIngredient(toUpdateName,updates)){
            AlertBox.display(ERROR_BOX_TITLE, "This ingredient is already present in the inventory!");
            return;
        }
        this.loadUI();
    }

    //sel l'utente preme il tasto per rimuovere un ingrediente si recupera quello selezionato dalla tabella e si passa al metodo corrispondente del controller applicativo
    @FXML
    public void onRemoveButtonPression(){
        IngredientBean toRemove = inventoryTable.getSelectionModel().getSelectedItem();
        if(toRemove == null) return;
        applController.removeIngredient(toRemove);
        this.setUpTable();
    }

    /*
    Alla pressione del tasto di conferma di un ingrediente da aggiungere, vengono incapsulati nel bean i dati dai campi dell'interfaccia e passati al controller applicativo.
    Se il bean riscontra problemi sintattici vengono lanciate eccezioni. Se il controller applicativo riscontra che l'ingrediente è già presente viene recapitato false dal metodo.
     */
    @FXML
    public void onIngredientConfirmationButtonPression() throws IOException {
        IngredientBean ingredientBean = new IngredientBean();
        try{
            ingredientBean.setName(nameField.getText());
            ingredientBean.setQuantity(quantityField.getText());
            ingredientBean.setMeasureUnit(unitBox.getValue());
            ingredientBean.setExpirationDate(dateField.getText());
            ingredientBean.setNotes(notesField.getText());
        }catch(IllegalArgumentException | ParseException e){
            AlertBox.display(ERROR_BOX_TITLE,"Some values in fields are missing or not valid.");
            return;
        }
        if(applController.addIngredient(ingredientBean)) {
            this.loadUI();
        }else{
            AlertBox.display(ERROR_BOX_TITLE,"Ingredient already present in the inventory, please update it instead of adding a new one.");
        }
    }

    //se l'utente preme il tasto back sulla schermata di aggiunta o modifica di un ingrediente viene ricaricata la schermata dell'inventario
    @FXML
    public void onIngredientPageBackButtonPression() throws IOException {
        this.loadUI();
    }

    //alla pressione del tasto "Save Changes" viene lanciato il relativo metodo del controller applicativo
    @FXML
    public void onSaveButtonPression(){
        applController.saveCurrentInventory();
    }

}
