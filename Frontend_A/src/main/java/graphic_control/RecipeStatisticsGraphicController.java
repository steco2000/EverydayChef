package graphic_control;

import beans.RecipeStatisticsTableBean;
import control.RecipeStatisticsController;
import exceptions.PersistentDataAccessException;
import factories.RecipeStatisticsControllerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.view.MainApp;
import utilities.AlertBox;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//controller grafico che gestisce la schermata delle statistiche delle ricette

public class RecipeStatisticsGraphicController {

    @FXML
    private TableView<RecipeStatisticsTableBean> recipeTable;
    @FXML
    private TableColumn<RecipeStatisticsTableBean, String> nameColumn;
    @FXML
    private TableColumn<RecipeStatisticsTableBean, Integer> viewsColumn;

    private final String chefUsername;

    /*
    Questo controller grafico ha necessità di conoscere solamente l'username dello chef, dato che è l'unico parametro richiesto dal metodo del controller applicativo. Per questo
    motivo, questo parametro gli viene passato dall'home controller direttamente nel costruttore, evitando di inserire una nuova associazione con il login controller.
     */
    public RecipeStatisticsGraphicController(String chefUsername){
        this.chefUsername = chefUsername;
    }

    //back button -> si ricarica la home
    @FXML
    private void onBackButtonPression() throws IOException {
        ChefHomeGraphicController homeGraphicController = new ChefHomeGraphicController();
        homeGraphicController.loadHomeUI();
    }

    /*
    Quando la schermata viene caricata la tabella deve già contenere i dati, che quindi vengono subito recuperati grazie all'invocazione verso il controller applicativo. Inoltre,
    le ricette vengono ordinate nella tabella in base al numero di visualizzazioni
     */
    public void loadUI() throws IOException {
        try {
            FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("RecipeStatisticsView.fxml"));
            uiLoader.setController(this);
            Scene scene = new Scene(uiLoader.load(), 1315, 810);
            RecipeStatisticsController controller = (new RecipeStatisticsControllerFactory()).createRecipeStatisticsController();
            List<RecipeStatisticsTableBean> beanList = controller.getRecipesStatistics(this.chefUsername);
            ObservableList<RecipeStatisticsTableBean> beanObservableList = FXCollections.observableArrayList();
            beanObservableList.addAll(beanList);
            Collections.sort(beanObservableList, new Comparator<RecipeStatisticsTableBean>() {
                @Override
                public int compare(RecipeStatisticsTableBean o1, RecipeStatisticsTableBean o2) {
                    return o1.getViews() > o2.getViews() ? 0 : 1;
                }
            });
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("recipeName"));
            viewsColumn.setCellValueFactory(new PropertyValueFactory<>("views"));
            recipeTable.setItems(beanObservableList);
            MainApp.getPrimaryStage().setScene(scene);
        }catch(PersistentDataAccessException e){
            AlertBox.display("Error",e.getMessage());
        }
    }

}
