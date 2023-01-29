package graphic_control;

import beans.RecipeStatisticsTableBean;
import control.RecipeStatisticsController;
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

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecipeStatisticsGraphicController {

    @FXML
    private TableView<RecipeStatisticsTableBean> recipeTable;
    @FXML
    private TableColumn<RecipeStatisticsTableBean, String> nameColumn;
    @FXML
    private TableColumn<RecipeStatisticsTableBean, Integer> viewsColumn;

    private final ChefHomeGraphicController homeGraphicController;
    private final String chefUsername;

    public RecipeStatisticsGraphicController(ChefHomeGraphicController chefHomeGraphicController, String chefUsername){
        this.homeGraphicController = chefHomeGraphicController;
        this.chefUsername = chefUsername;
    }

    @FXML
    private void onBackButtonPression() throws IOException {
        homeGraphicController.loadHomeUI();
    }

    public void loadUI() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(MainApp.class.getResource("RecipeStatisticsView.fxml"));
        uiLoader.setController(this);
        Scene scene = new Scene(uiLoader.load(),1315,810);
        RecipeStatisticsController controller = (new RecipeStatisticsControllerFactory()).createRecipeStatisticsController();
        List<RecipeStatisticsTableBean> beanList = controller.getRecipesStatistics(this.chefUsername);
        ObservableList<RecipeStatisticsTableBean> beanObservableList = FXCollections.observableArrayList();
        beanObservableList.addAll(beanList);
        Collections.sort(beanObservableList, new Comparator<RecipeStatisticsTableBean>() {
            @Override
            public int compare(RecipeStatisticsTableBean o1, RecipeStatisticsTableBean o2) {
                return o1.getViews() > o2.getViews() ? 0: 1;
            }
        });
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("recipeName"));
        viewsColumn.setCellValueFactory(new PropertyValueFactory<>("views"));
        recipeTable.setItems(beanObservableList);
        MainApp.getPrimaryStage().setScene(scene);
    }

}
