package factories;

import control.BrowseRecipeApplicativeController;
import control.BrowseRecipeController;

public class BrowseRecipeControllerFactory {

    public BrowseRecipeController createBrowseRecipeController() {
        return new BrowseRecipeApplicativeController();
    }

}
