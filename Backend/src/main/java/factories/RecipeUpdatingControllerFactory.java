package factories;

import control.RecipeUpdadingController;
import control.RecipeUpdatingApplicativeController;

//factory adibita alla creazione di istanze di RecipeUpdatingApplicativeController

public class RecipeUpdatingControllerFactory {

    public RecipeUpdadingController createRecipeUpdatingController(){ return new RecipeUpdatingApplicativeController();}

}
