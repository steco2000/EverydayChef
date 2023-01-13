package beans;

import dao.RecipeDAO;
import model.*;

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
