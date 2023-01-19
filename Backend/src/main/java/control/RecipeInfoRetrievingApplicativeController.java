package control;

import beans.ChefBean;
import beans.RecipeBean;
import beans.RecipeBrowsingTableBean;
import beans.RecipeIngredientBean;
import dao.ChefDAO;
import dao.RecipesBrowsingDAO;
import exceptions.RecipeIngredientQuantityException;
import model.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecipeInfoRetrievingApplicativeController implements RecipeInfoRetrievingController{

    private static List<InventoryIngredient> inventoryList;
    private RecipeBean selectedRecipe;

    @Override
    public RecipeBean retrieveRecipeInfo(RecipeBrowsingTableBean recipeBrowsingBean) {
        RecipesBrowsingDAO recipesBrowsingDAO = new RecipesBrowsingDAO();
        RecipeBase recipe = recipesBrowsingDAO.getRecipeInfo(recipeBrowsingBean.getName(),recipeBrowsingBean.getChefUsername());
        RecipeBean toReturn = new RecipeBean();
        toReturn.setName(recipe.getName());
        toReturn.setServings(String.valueOf(recipe.getServings()));
        toReturn.setPreparationTime(recipe.getPreparationTime());
        toReturn.setDifficulty(recipe.getDifficulty());
        toReturn.setPreparationProcedure(recipe.getPreparationProcedure());
        List<RecipeIngredientBean> ingredientBeanList = new ArrayList<>();
        for(RecipeIngredient i: recipe.getIngredientList()){
            RecipeIngredientBean ingredientBean = new RecipeIngredientBean();
            ingredientBean.setName(i.getName());
            try {
                ingredientBean.setQuantity(String.valueOf(i.getQuantity()));
            } catch (ParseException | RecipeIngredientQuantityException ignored) {
                assert(true);
            }
            ingredientBean.setMeasureUnit(i.getMeasureUnit());
            ingredientBeanList.add(ingredientBean);
        }
        toReturn.setIngredientList(ingredientBeanList);
        this.selectedRecipe = toReturn;
        return toReturn;
    }

    @Override
    public ChefBean retrieveChefInfo(String chefUsername) {
        ChefDAO chefDAO = new ChefDAO();
        ChefBase chef = chefDAO.retrieveChef(chefUsername);
        ChefBean toReturn = new ChefBean();
        toReturn.setName(chef.getName());
        toReturn.setSurname(chef.getSurname());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            toReturn.setBirthDate(dateFormat.format(chef.getBirthDate()));
        }catch(NullPointerException | ParseException ignored){
            assert(true); //eccezione ignorata, dati da memoria già validati
        }
        toReturn.setInfo(chef.getInfo());
        toReturn.setEmail(chef.getEmail());
        return toReturn;
    }

    @Override
    public List<RecipeIngredientBean> retrieveMissingIngredients(RecipeBrowsingTableBean recipe) {
        List<RecipeIngredientBean> missingIngredients = new ArrayList<>();
        for(RecipeIngredientBean recipeIngredient: this.selectedRecipe.getIngredientList()){
            if(inventoryList.stream().noneMatch(o -> o.getName().equals(recipeIngredient.getName()))){
                //se l'ingrediente non è presente nell'inventario
                RecipeIngredientBean newMissing = new RecipeIngredientBean();
                newMissing.setName(recipeIngredient.getName());
                try {
                    newMissing.setQuantity(String.valueOf(recipeIngredient.getQuantity()));
                } catch (ParseException | RecipeIngredientQuantityException ignored) {
                    assert(true); //eccezione ignorata
                }
                newMissing.setMeasureUnit(recipeIngredient.getMeasureUnit());
                missingIngredients.add(newMissing);
            }else{
                //se l'ingrediente è presente
                for (InventoryIngredient inventoryIngredient : inventoryList) {
                    if ((recipeIngredient.getName().equals(inventoryIngredient.getName())) && (recipeIngredient.getQuantity() > inventoryIngredient.getQuantity())){
                        RecipeIngredientBean newMissing = new RecipeIngredientBean();
                        newMissing.setName(recipeIngredient.getName());
                        try {
                            double difference = (recipeIngredient.getQuantity()*100) - (inventoryIngredient.getQuantity()*100);
                            newMissing.setQuantity(String.valueOf(difference/100));
                        } catch (ParseException | RecipeIngredientQuantityException ignored) {
                            assert(true); //eccezione ignorata
                        }
                        newMissing.setMeasureUnit(recipeIngredient.getMeasureUnit());
                        missingIngredients.add(newMissing);
                    }
                }
            }
        }
        return missingIngredients;
    }

    public void setInventoryList(List<InventoryIngredient> inventoryList){
        RecipeInfoRetrievingApplicativeController.inventoryList = inventoryList;
    }

}
