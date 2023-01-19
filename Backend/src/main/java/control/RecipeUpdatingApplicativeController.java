package control;

import beans.RecipeBean;
import dao.RecipeDAO;

import java.io.IOException;

public class RecipeUpdatingApplicativeController implements RecipeUpdadingController{

    @Override
    public void updateRecipe(String toUpdateName, RecipeBean recipe) {
        this.deleteRecipe(toUpdateName);
        RecipeSharingApplicativeController sharingController = new RecipeSharingApplicativeController();
        sharingController.shareRecipe(recipe);
    }

    @Override
    public void deleteRecipe(String toDeleteName) {
        RecipeDAO recipeDAO = new RecipeDAO();
        recipeDAO.deleteRecipe(toDeleteName);
        try {
            recipeDAO.saveChanges();
        } catch (IOException ignored) {
            "".isEmpty(); //eccezione ignorata
        }
    }

}
