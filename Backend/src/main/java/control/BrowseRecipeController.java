package control;

import beans.RecipeBrowsingTableBean;

import java.util.List;

//Interfaccia del controller applicativo "BrowseRecipeApplicativeController" esposta alle UI

public interface BrowseRecipeController {

    List<RecipeBrowsingTableBean> retrieveSuggestedRecipe();

}
