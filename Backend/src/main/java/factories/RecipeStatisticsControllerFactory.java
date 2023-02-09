package factories;

import control.RecipeStatisticsApplicativeController;
import control.RecipeStatisticsController;

//factory adibita alla creazione di istanze di RecipeStatisticsApplicativeController

public class RecipeStatisticsControllerFactory {

    public RecipeStatisticsController createRecipeStatisticsController(){
        return new RecipeStatisticsApplicativeController();
    }

}
