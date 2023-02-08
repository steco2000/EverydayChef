package control;

import beans.RecipeBean;

//interfaccia del controller applicativo "RecipeSharingApplicativeController" esposta alla UI

public interface RecipeSharingController {
    void shareRecipe(RecipeBean recipe);
    void setUpRecipesObserver(String chefUsername);
    void saveChanges();
}
