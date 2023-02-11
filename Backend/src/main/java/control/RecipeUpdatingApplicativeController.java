package control;

import beans.RecipeBean;
import dao.RecipeDAO;
import exceptions.PersistentDataAccessException;

import java.io.IOException;

//controller applicativo che gestisce la modifica di una ricetta esistente

public class RecipeUpdatingApplicativeController implements RecipeUpdadingController{

    @Override
    public void updateRecipe(String toUpdateName, RecipeBean recipe) throws PersistentDataAccessException {
        this.deleteRecipe(toUpdateName);
        RecipeSharingApplicativeController sharingController = new RecipeSharingApplicativeController();
        sharingController.shareRecipe(recipe);
    }

    @Override
    public void deleteRecipe(String toDeleteName) throws PersistentDataAccessException {
        RecipeDAO recipeDAO = new RecipeDAO();
        recipeDAO.deleteRecipe(toDeleteName);
        try {
            recipeDAO.saveChanges();
        } catch (IOException e) {
            throw new PersistentDataAccessException(e);
        }
    }

}
