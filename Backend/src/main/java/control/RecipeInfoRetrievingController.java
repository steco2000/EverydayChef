package control;

import beans.*;

import java.util.List;

//interfaccia del controller applicativo "RecipeInfoRetrievingApplicativeController" esposta alla UI

public interface RecipeInfoRetrievingController {

    RecipeBean retrieveRecipeInfo(RecipeBrowsingTableBean recipe);
    ChefBean retrieveChefInfo(String chefUsername);
    List<RecipeIngredientBean> retrieveMissingIngredients(RecipeBrowsingTableBean recipe);

}
