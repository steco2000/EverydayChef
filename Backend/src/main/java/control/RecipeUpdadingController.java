package control;

import beans.RecipeBean;

public interface RecipeUpdadingController {

    void updateRecipe(String toUpdateName, RecipeBean recipe);
    void deleteRecipe(String toDeleteName);

}
