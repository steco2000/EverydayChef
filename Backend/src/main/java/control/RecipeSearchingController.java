package control;

import beans.RecipeBrowsingTableBean;
import model.RecipeBase;

import java.util.List;

public interface RecipeSearchingController {

    List<RecipeBrowsingTableBean> retrieveSearchResult(String search);

}
