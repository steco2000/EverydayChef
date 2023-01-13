package factories;

import control.RecipeSharingApplicativeController;
import control.RecipeSharingController;

public class RecipeSharingControllerFactory {

    public RecipeSharingController createRecipeSharingController(){
        return new RecipeSharingApplicativeController();
    }

}
