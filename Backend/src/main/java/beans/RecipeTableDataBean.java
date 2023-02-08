package beans;

import dao.RecipeDAO;
import exceptions.RecipeIngredientQuantityException;
import model.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/*
bean in pull model per l'interfaccia di gestione delle ricette. Rende disponibili all'interfaccia i dati sulle ricette inserite sempre aggiornati,
implementando sul RecipeDAO il pattern observer. Il RecipeDAO, infatti, quando viene creato effettua un caching delle ricette dello chef loggato, quindi le ha sempre a disposizione
e sa quando cambiano. Il bean è singleton, dato che è in continuo ascolto di aggiornamenti dal DAO, perciò l'interfaccia deve sempre essere sicura
di relazionarsi con la stessa istanza per avere i dati sempre aggiornati.
*/

public class RecipeTableDataBean extends RecipeObserver {

    private RecipeDAO subject;
    private List<Recipe> recipeList;
    private static RecipeTableDataBean singletonInstance;

    /*
   Questo metodo incapsula i dati del model nei bean che verranno poi passati all'interfaccia, dove verranno visualizzati nella tabella.
    */
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

    /*
    Questo metodo fornisce all'interfaccia i dati completi di una ricetta quando questa è selezionata per essere modificata
     */
    public RecipeBean getRecipe(String name) {
        Recipe toReturn = null;
        for (Recipe r : recipeList) {
            if (r.getName().equals(name)) {
                toReturn = r;
                break;
            }
        }
        RecipeBean beanToReturn = new RecipeBean();
        beanToReturn.setName(toReturn.getName());
        beanToReturn.setDifficulty(toReturn.getDifficulty());
        beanToReturn.setPreparationTime(toReturn.getPreparationTime());
        beanToReturn.setServings(String.valueOf(toReturn.getServings()));


        List<RecipeIngredientBean> ingredientBeanList = new ArrayList<>();
        for (RecipeIngredient i : toReturn.getIngredientList()) {
            RecipeIngredientBean ingredientBean = new RecipeIngredientBean();
            ingredientBean.setName(i.getName());
            try {
                ingredientBean.setQuantity(String.valueOf(i.getQuantity()),i.getQuantity()==-1);
            } catch (ParseException | RecipeIngredientQuantityException ignored){
                assert(true); //eccezione ignorata dato che i dati provenienti dalla memoria sono sempre validi
            }
            ingredientBean.setMeasureUnit(i.getMeasureUnit());
            ingredientBeanList.add(ingredientBean);
        }

        beanToReturn.setIngredientList(ingredientBeanList);
        beanToReturn.setPreparationProcedure(toReturn.getPreparationProcedure());
        return beanToReturn;
    }

    //get dell'istanza singleton
    public static RecipeTableDataBean getSingletonInstance(){
        if(singletonInstance == null) singletonInstance = new RecipeTableDataBean();
        return singletonInstance;
    }

    //metodo per linkare al bean observer il DAO da osservare
    @Override
    public void setSubject(RecipeSubject subject){
        this.subject = (RecipeDAO) subject;
        this.recipeList = this.subject.getState();
    }

    //metodo di update chiamato dal subject per notificare all'observer il cambiamento di stato
    @Override
    public void update() {
        this.recipeList = subject.getState();
    }

}
