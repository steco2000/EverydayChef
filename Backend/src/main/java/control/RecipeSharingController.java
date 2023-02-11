package control;

import beans.RecipeBean;
import exceptions.PersistentDataAccessException;

//interfaccia del controller applicativo "RecipeSharingApplicativeController" esposta alla UI

public interface RecipeSharingController {
    void shareRecipe(RecipeBean recipe) throws PersistentDataAccessException;
    void setUpRecipesObserver(String chefUsername) throws PersistentDataAccessException;
    void saveChanges() throws PersistentDataAccessException;
}
