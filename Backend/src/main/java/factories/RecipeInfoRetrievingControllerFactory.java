package factories;

import control.RecipeInfoRetrievingApplicativeController;
import control.RecipeInfoRetrievingController;

//factory adibita alla creazione di istanze di RecipeInfoRetrievingApplicativeController

public class RecipeInfoRetrievingControllerFactory {

    public RecipeInfoRetrievingController createRecipeInfoRetrievingController(){
        return new RecipeInfoRetrievingApplicativeController();
    }

}
