package control;

import beans.RecipeBean;

//interfaccia del controller applicativo "RecipeUpdatingApplicativeController" esposta alla UI

public interface RecipeUpdadingController {

    void updateRecipe(String toUpdateName, RecipeBean recipe);
    void deleteRecipe(String toDeleteName);

}
