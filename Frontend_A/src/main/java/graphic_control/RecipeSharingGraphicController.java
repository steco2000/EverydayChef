package graphic_control;

import beans.RecipeBean;
import beans.RecipeIngredientBean;
import beans.RecipeTableDataBean;
import control.RecipeSharingController;
import control.RecipeUpdadingController;
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
    private ChoiceBox<String> unitBox;
    @FXML
    private TableView<RecipeIngredientBean> ingredientTable;
    @FXML
    private TableColumn<RecipeIngredientBean, String> ingredientNameColumn;
    @FXML
    private TableColumn<RecipeIngredientBean, Double> ingredientQuantityColumn;
    @FXML
    private TableColumn<RecipeIngredientBean, String> unitColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button shareButton;

    private static final String ERROR_BOX_TITLE = "Error";
    private ObservableList<RecipeIngredientBean> ingredientTableList = FXCollections.observableArrayList();
    private String chefUsername;
    private ChefHomeGraphicController homeGraphicController;
    private boolean onUpdateMode = false;
    private String toUpdateName;

    public RecipeSharingGraphicController(ChefHomeGraphicController homeGraphicController){
        this.homeGraphicController = homeGraphicController;
    }

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
            newIngredient.setMeasureUnit(unitBox.getValue());
            newIngredient.setQuantity(ingredientQuantityField.getText());
        } catch (ParseException e) {
            AlertBox.display(ERROR_BOX_TITLE,"Invalid quantity.");
            return;
        } catch (RecipeIngredientQuantityException e){
            boolean answer = ConfirmBox.display(newIngredient.getName(), "Are you sure that you want to save the ingredient \""+newIngredient.getName()+"\" without a specified quantity?");
            if(!answer) return;
        } catch(IllegalArgumentException e){
            AlertBox.display(ERROR_BOX_TITLE,"Some values in fields are missing or not valid.");
            return;
        }
        ingredientTableList.add(newIngredient);
        ingredientNameField.clear();
        ingredientQuantityField.clear();
        unitBox.setValue("Kg");
    }

    @FXML
    private void onRemoveButtonPression(){
        ingredientTableList.remove(ingredientTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void onBackButtonPression() throws IOException {
        homeGraphicController.loadRecipeUI();
    }

    @FXML
    private void onShareButtonPression() throws IOException {
        if(ingredientTableList.isEmpty()){
            AlertBox.display(ERROR_BOX_TITLE,"A recipe must have at least one ingredient.");
            return;
        }
        RecipeBean updates = this.getRecipeData();
        if(updates == null){
            return;
        }
        if(onUpdateMode){
            RecipeUpdatingControllerFactory updatingControllerFactory = new RecipeUpdatingControllerFactory();
            RecipeUpdadingController updadingController = updatingControllerFactory.createRecipeUpdatingController();
            updadingController.updateRecipe(toUpdateName,updates);
            toUpdateName = null;
            homeGraphicController.loadRecipeUI();
        }else {
            RecipeSharingControllerFactory factory = new RecipeSharingControllerFactory();
            RecipeSharingController controller = factory.createRecipeSharingController();
            controller.shareRecipe(updates);
            homeGraphicController.loadRecipeUI();
        }
    }

    @FXML
    private void onDeleteButtonPression() throws IOException {
        if(ConfirmBox.display("Warning","Are you sure you want to delete this recipe?")){
            RecipeUpdatingControllerFactory updatingControllerFactory = new RecipeUpdatingControllerFactory();
            RecipeUpdadingController updadingController = updatingControllerFactory.createRecipeUpdatingController();
            updadingController.deleteRecipe(toUpdateName);
            toUpdateName = null;
            homeGraphicController.loadRecipeUI();
        }
    }

    private RecipeBean getRecipeData(){
        RecipeBean newRecipe = new RecipeBean();
        try{
            newRecipe.setName(nameField.getText());
            newRecipe.setChefUsername(chefUsername);
            newRecipe.setDifficulty(difficultyBox.getValue());
            Double.parseDouble(prepTimeField.getText());
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
        ingredientQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("measureUnit"));
        ingredientTable.setItems(ingredientTableList);
        MainApp.getPrimaryStage().setScene(scene);
    }

}
