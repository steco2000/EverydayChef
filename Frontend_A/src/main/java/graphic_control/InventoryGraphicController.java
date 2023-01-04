package graphic_control;

import view.MainApp;
import control.IngredientBean;
import control.InventoryController;
import control.InventoryControllerFactory;
import control.InventoryTableDataBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import utilities.AlertBox;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

//TODO: eccezioni

public class InventoryGraphicController {

    private InventoryTableDataBean dataBean;
    private InventoryController applController;
    private static String toUpdateName;
    private static final String ERROR_BOX_TITLE = "Error";

    @FXML
    private TextField nameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField unitField;
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

    public InventoryGraphicController(){
        InventoryControllerFactory controllerFactory = new InventoryControllerFactory();
        applController = controllerFactory.createInventoryController();
        dataBean = InventoryTableDataBean.getSingletonInstance();
    }

    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("InventoryView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        this.setUpTable();
        MainApp.getPrimaryStage().setScene(scene);
    }

    private void setUpTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("measureUnit"));
        expDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        inventoryTable.setItems(this.getObservableTableData());
    }

    private ObservableList<IngredientBean> getObservableTableData() {
        ObservableList<IngredientBean> observableList = FXCollections.observableArrayList();
        List<IngredientBean> dataList = dataBean.getTableData();
        if(dataList.isEmpty()) return null;
        for(IngredientBean i: dataList){
            observableList.add(i);
        }
        return observableList;
    }

    @FXML
    public void onBackButtonPression() throws IOException {
        applController.saveCurrentInventory();
        UserHomeGraphicController controller = new UserHomeGraphicController();
        controller.loadUI();
    }

    @FXML
    public void onAddIngredientButtonPression() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("AddIngredientView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        MainApp.getPrimaryStage().setScene(scene);
    }

    @FXML
    public void onUpdateButtonPression() throws IOException {
        IngredientBean ingredientToUpdate = inventoryTable.getSelectionModel().getSelectedItem();
        toUpdateName = ingredientToUpdate.getName();

        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("UpdateIngredientView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);

        nameField.setText(ingredientToUpdate.getName());
        quantityField.setText(String.valueOf(ingredientToUpdate.getQuantity()));
        unitField.setText(ingredientToUpdate.getMeasureUnit());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateValue = dateFormat.format(ingredientToUpdate.getExpirationDate());
        dateField.setText(dateValue);
        notesField.setText(ingredientToUpdate.getNotes());

        MainApp.getPrimaryStage().setScene(scene);
    }

    @FXML
    public void onUpdateConfirmationButtonPression() throws IOException {
        IngredientBean updates = new IngredientBean();
        updates.setName(nameField.getText());
        updates.setQuantity(Double.parseDouble(quantityField.getText()));
        updates.setMeasureUnit(unitField.getText());
        updates.setExpirationDate(dateField.getText());
        updates.setNotes(notesField.getText());
        if(!applController.updateIngredient(toUpdateName,updates)){
            AlertBox.display(ERROR_BOX_TITLE, "This ingredient is already present in the inventory!");
            return;
        }
        this.loadUI();
    }

    @FXML
    public void onRemoveButtonPression(){
        IngredientBean toRemove = inventoryTable.getSelectionModel().getSelectedItem();
        applController.removeIngredient(toRemove);
        this.setUpTable();
    }

    @FXML
    public void onIngredientConfirmationButtonPression() throws IOException {
        IngredientBean ingredientBean = new IngredientBean();
        ingredientBean.setName(nameField.getText());
        ingredientBean.setQuantity(Double.parseDouble(quantityField.getText()));        //TODO: double validation
        ingredientBean.setMeasureUnit(unitField.getText());
        if(!ingredientBean.setExpirationDate(dateField.getText())){
            AlertBox.display(ERROR_BOX_TITLE,"Date format incorrect");
            return;
        }
        ingredientBean.setNotes(notesField.getText());
        if(applController.addIngredient(ingredientBean)) {
            this.loadUI();
        }else{
            AlertBox.display(ERROR_BOX_TITLE,"Ingredient already present in the inventory, please update it instead of adding a new one.");
        }
    }

    @FXML
    public void onIngredientPageBackButtonPression() throws IOException {
        this.loadUI();
    }

    @FXML
    public void onSaveButtonPression(){
        applController.saveCurrentInventory();
    }

}
