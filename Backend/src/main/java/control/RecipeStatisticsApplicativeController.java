package control;

import beans.RecipeStatisticsTableBean;
import dao.RecipeDAO;
import exceptions.PersistentDataAccessException;
import model.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//controller applicativo che gestisce il recupero delle statistiche di una ricetta

public class RecipeStatisticsApplicativeController implements RecipeStatisticsController{

    /*
    Unico metodo che svolge da solo la funzionalità del controller. Si interroga il RecipeDAO, che salva in cache la lista delle ricette dello chef loggato. Tutte le ricette
    vengono poi incapsulate nei bean appositi che vengono restituiti alla UI.
     */
    @Override
    public List<RecipeStatisticsTableBean> getRecipesStatistics(String chefUsername) throws PersistentDataAccessException {
        try {
            List<Recipe> recipeList = (new RecipeDAO(chefUsername)).getState();
            List<RecipeStatisticsTableBean> tableBeanList = new ArrayList<>();
            for (Recipe r : recipeList) {
                RecipeStatisticsTableBean bean = new RecipeStatisticsTableBean();
                bean.setRecipeName(r.getName());
                bean.setViews(r.getViews());
                tableBeanList.add(bean);
            }
            return tableBeanList;
        }catch (IOException e){
            throw new PersistentDataAccessException(e);
        }
    }
}
