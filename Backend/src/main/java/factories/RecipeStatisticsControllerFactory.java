package factories;

import control.RecipeStatisticsApplicativeController;
import control.RecipeStatisticsController;

public class RecipeStatisticsControllerFactory {

    public RecipeStatisticsController createRecipeStatisticsController(){
        return new RecipeStatisticsApplicativeController();
    }

}
