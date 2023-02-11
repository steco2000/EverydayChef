package control;

import beans.*;
import exceptions.PersistentDataAccessException;

import java.util.List;

//interfaccia del controller applicativo "RecipeInfoRetrievingApplicativeController" esposta alla UI

public interface RecipeInfoRetrievingController {

    RecipeBean retrieveRecipeInfo(RecipeBrowsingTableBean recipe) throws PersistentDataAccessException;
    ChefBean retrieveChefInfo(String chefUsername) throws PersistentDataAccessException;
    List<RecipeIngredientBean> retrieveMissingIngredients(RecipeBrowsingTableBean recipe);

}
