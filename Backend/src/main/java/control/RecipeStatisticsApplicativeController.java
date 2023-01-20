package control;

import beans.RecipeStatisticsTableBean;
import dao.RecipeDAO;
import model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeStatisticsApplicativeController implements RecipeStatisticsController{

    @Override
    public List<RecipeStatisticsTableBean> getRecipesStatistics(String chefUsername) {
        List<Recipe> recipeList = (new RecipeDAO(chefUsername)).getState();
        List<RecipeStatisticsTableBean> tableBeanList = new ArrayList<>();
        for(Recipe r: recipeList){
            RecipeStatisticsTableBean bean = new RecipeStatisticsTableBean();
            bean.setRecipeName(r.getName());
            bean.setViews(r.getViews());
            tableBeanList.add(bean);
        }
        return tableBeanList;
    }

}
