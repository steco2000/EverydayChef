package beans;

import dao.RecipeDAO;
import exceptions.RecipeIngredientQuantityException;
import model.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RecipeTableDataBean extends RecipeObserver {

    private RecipeDAO subject;
    private List<Recipe> recipeList;
    private static RecipeTableDataBean singletonInstance;

    public List<RecipeBean> getTableData(){
        recipeList = subject.getState();
        List<RecipeBean> recipeBeanList = new ArrayList<>();
        if(recipeList == null) return recipeBeanList;
        for(Recipe r: recipeList) {
            RecipeBean recipeBean = new RecipeBean();
            recipeBean.setName(r.getName());
            recipeBeanList.add(recipeBean);
        }
        return recipeBeanList;
    }

    public RecipeBean getRecipe(String name){
        Recipe toReturn = null;
        for(Recipe r: recipeList){
            if(r.getName().equals(name)){
                toReturn = r;
                break;
            }
        }
        RecipeBean beanToReturn = new RecipeBean();
        beanToReturn.setName(toReturn.getName());
        beanToReturn.setDifficulty(toReturn.getDifficulty());
        beanToReturn.setPreparationTime(toReturn.getPreparationTime());

        try {
            beanToReturn.setServings(String.valueOf(toReturn.getServings()));
        } catch (ParseException ignored){}

        List<RecipeIngredientBean> ingredientBeanList = new ArrayList<>();
        for(RecipeIngredient i: toReturn.getIngredientList()){
            RecipeIngredientBean ingredientBean = new RecipeIngredientBean();
            ingredientBean.setName(i.getName());
            try {
                ingredientBean.setQuantity(String.valueOf(i.getQuantity()));
            } catch (ParseException | RecipeIngredientQuantityException ignored){}
            ingredientBean.setMeasureUnit(i.getMeasureUnit());
            ingredientBeanList.add(ingredientBean);
        }

        beanToReturn.setIngedientList(ingredientBeanList);
        beanToReturn.setPreparationProcedure(toReturn.getPreparationProcedure());
        return beanToReturn;
    }

    public void setSubject(RecipeSubject subject){
        this.subject = (RecipeDAO) subject;
        this.recipeList = this.subject.getState();
    }


    public static RecipeTableDataBean getSingletonInstance(){
        if(singletonInstance == null) singletonInstance = new RecipeTableDataBean();
        return singletonInstance;
    }

    @Override
    public void update() {
        this.recipeList = subject.getState();
    }

}
