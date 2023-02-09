package factories;

import control.RecipeSearchingApplicativeController;
import control.RecipeSearchingController;

//factory adibita alla creazione di istanze di RecipeSearchingApplicativeController

public class RecipeSearchingControllerFactory {

    public RecipeSearchingController createRecipeSearchingController(){
        return new RecipeSearchingApplicativeController();
    }

}
