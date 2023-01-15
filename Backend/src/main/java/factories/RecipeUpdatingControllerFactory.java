package factories;

import control.RecipeUpdadingController;
import control.RecipeUpdatingApplicativeController;

public class RecipeUpdatingControllerFactory {

    public RecipeUpdadingController createRecipeUpdatingController(){ return new RecipeUpdatingApplicativeController();}

}
