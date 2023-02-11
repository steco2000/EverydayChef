package control;

import beans.RecipeBrowsingTableBean;
import exceptions.PersistentDataAccessException;

import java.util.List;

//interfaccia del controller applicativo "RecipeSearchingApplicativeController" esposta alla UI

public interface RecipeSearchingController {

    List<RecipeBrowsingTableBean> retrieveSearchResult(String search);

}
