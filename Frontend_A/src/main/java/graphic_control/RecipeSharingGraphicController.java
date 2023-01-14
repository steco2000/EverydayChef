package graphic_control;

import beans.IngredientBean;
import beans.RecipeBean;
import beans.RecipeIngredientBean;
import beans.RecipeTableDataBean;
import control.LoginController;
import control.RecipeSharingController;
import exceptions.RecipeIngredientQuantityException;
import factories.RecipeSharingControllerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.IngredientBase;
import model.Recipe;
import utilities.AlertBox;
import utilities.ConfirmBox;
import view.MainApp;

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
    private static String chefUsername;
    private ChefHomeGraphicController homeGraphicController;
    private boolean onUpdateMode = false;

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
        } catch (RecipeIngredientQuantityException e){
            boolean answer = ConfirmBox.display(newIngredient.getName(), "Are you sure that you want to save the ingredient \""+newIngredient.getName()+"\" without a specified quantity?");
            if(!answer) return;
        } catch(IllegalArgumentException e){
            AlertBox.display(ERROR_BOX_TITLE,"Some required fields are missing.");
        }
        ingredientTableList.add(newIngredient);
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
    private void onShareButtonPression(){
        if(onUpdateMode){
            //TODO: implementare update da qui
        }else {
            RecipeBean newRecipe = new RecipeBean();
            try {
                newRecipe.setName(nameField.getText());
                newRecipe.setChefUsername(chefUsername);
                newRecipe.setDifficulty(difficultyBox.getValue());
                Double.parseDouble(prepTimeField.getText());
                newRecipe.setPreparationTime(prepTimeField.getText() + " " + timeUnitBox.getValue());
                newRecipe.setServings(servingsField.getText());
                newRecipe.setPreparationProcedure(preparationArea.getText());
                List<RecipeIngredientBean> ingredientList = new ArrayList<>();
                ingredientList.addAll(ingredientTableList);
                newRecipe.setIngedientList(ingredientList);
                RecipeSharingControllerFactory factory = new RecipeSharingControllerFactory();
                RecipeSharingController controller = factory.createRecipeSharingController();
                controller.shareRecipe(newRecipe);
                homeGraphicController.loadRecipeUI();
            } catch (ParseException e) {
                AlertBox.display(ERROR_BOX_TITLE, "Invalid preparation time value.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onDeleteButtonPression(){
    }

    public void loadUpdateUI(String username, RecipeBean toUpdate) throws IOException {
        onUpdateMode = true;
        this.loadUI(username);
        deleteButton.setVisible(true);
        shareButton.setText("Update Recipe");
        RecipeTableDataBean dataBean = RecipeTableDataBean.getSingletonInstance();
        RecipeBean recipe = dataBean.getRecipe(toUpdate.getName());
        nameField.setText(recipe.getName());
        difficultyBox.setValue(recipe.getDifficulty());
        String prepTime = recipe.getPreparationTime();
        int valueEnd = prepTime.indexOf(" ");
        prepTimeField.setText(prepTime.substring(0,valueEnd));
        timeUnitBox.setValue(prepTime.substring(valueEnd+1));
        servingsField.setText(String.valueOf(recipe.getServings()));
        preparationArea.setText(recipe.getPreparationProcedure());
        ObservableList<RecipeIngredientBean> observableBeanList = FXCollections.observableArrayList();
        observableBeanList.addAll(recipe.getIngedientList());
        ingredientTable.setItems(observableBeanList);
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
