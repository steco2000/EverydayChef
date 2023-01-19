package control;

import beans.RecipeBrowsingTableBean;
import dao.RecipesBrowsingDAO;
import model.RecipeBase;

import java.util.ArrayList;
import java.util.List;

public class RecipeSearchingApplicativeController implements RecipeSearchingController{

    @Override
    public List<RecipeBrowsingTableBean> retrieveSearchResult(String search) {
        RecipesBrowsingDAO browsingDAO = new RecipesBrowsingDAO();
        List<RecipeBase> searchResult = browsingDAO.getSearchResult(search);
        List<RecipeBrowsingTableBean> resultList = new ArrayList<>();
        if(searchResult.isEmpty()) return resultList;
        for(RecipeBase r: searchResult){
            RecipeBrowsingTableBean recipeBean = new RecipeBrowsingTableBean();
            recipeBean.setName(r.getName());
            recipeBean.setChefCompleteName(r.getChef().getName()+" "+r.getChef().getSurname());
            recipeBean.setChefUsername(r.getChef().getUsername());
            resultList.add(recipeBean);
        }
        return resultList;
    }

}
