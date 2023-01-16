package control;

import beans.RecipeBean;

public interface RecipeSharingController {
    void shareRecipe(RecipeBean recipe);
    void setUpRecipesObserver(String chefUsername);
    void saveChanges();
}
