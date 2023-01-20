package control;

import beans.RecipeStatisticsTableBean;

import java.util.List;

public interface RecipeStatisticsController {

    List<RecipeStatisticsTableBean> getRecipesStatistics(String chefUsername);

}
