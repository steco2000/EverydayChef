package factories;

import control.RecipeInfoRetrievingApplicativeController;
import control.RecipeInfoRetrievingController;

public class RecipeInfoRetrievingControllerFactory {

    public RecipeInfoRetrievingController createRecipeInfoRetrievingController(){
        return new RecipeInfoRetrievingApplicativeController();
    }

}
