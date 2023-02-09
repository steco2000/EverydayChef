package factories;

import control.BrowseRecipeApplicativeController;
import control.BrowseRecipeController;

//factory adibita alla creazione di istanze di BrowseRecipeApplicativeController

public class BrowseRecipeControllerFactory {

    public BrowseRecipeController createBrowseRecipeController() {
        return new BrowseRecipeApplicativeController();
    }

}
