package control;

import beans.*;

import java.util.List;

public interface RecipeInfoRetrievingController {

    RecipeBean retrieveRecipeInfo(RecipeBrowsingTableBean recipe);
    ChefBean retrieveChefInfo(String chefUsername);
    List<RecipeIngredientBean> retrieveMissingIngredients(RecipeBrowsingTableBean recipe);

}
