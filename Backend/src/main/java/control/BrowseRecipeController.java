package control;

import beans.RecipeBrowsingTableBean;

import java.util.List;

public interface BrowseRecipeController {

    List<RecipeBrowsingTableBean> retrieveSuggestedRecipe();

}
