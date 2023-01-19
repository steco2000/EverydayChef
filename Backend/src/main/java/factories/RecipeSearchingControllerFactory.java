package factories;

import control.RecipeSearchingApplicativeController;
import control.RecipeSearchingController;

public class RecipeSearchingControllerFactory {

    public RecipeSearchingController createRecipeSearchingController(){
        return new RecipeSearchingApplicativeController();
    }

}
