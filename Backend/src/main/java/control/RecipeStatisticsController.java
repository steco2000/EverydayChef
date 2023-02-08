package control;

import beans.RecipeStatisticsTableBean;

import java.util.List;

//interfaccia del controller applicativo "RecipeStatisticsApplicativeController" esposta alla UI

public interface RecipeStatisticsController {

    List<RecipeStatisticsTableBean> getRecipesStatistics(String chefUsername);

}
