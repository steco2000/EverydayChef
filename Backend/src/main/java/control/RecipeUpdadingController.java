package control;

import beans.RecipeBean;
import exceptions.PersistentDataAccessException;

//interfaccia del controller applicativo "RecipeUpdatingApplicativeController" esposta alla UI

public interface RecipeUpdadingController {

    void updateRecipe(String toUpdateName, RecipeBean recipe) throws PersistentDataAccessException;
    void deleteRecipe(String toDeleteName) throws PersistentDataAccessException;

}
