package factories;

import control.RecipeSharingApplicativeController;
import control.RecipeSharingController;

//factory adibita alla creazione di istanze di RecipeSharingApplicativeController

public class RecipeSharingControllerFactory {

    public RecipeSharingController createRecipeSharingController(){
        return new RecipeSharingApplicativeController();
    }

}
