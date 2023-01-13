package control;

import beans.RecipeBean;
import beans.RecipeIngredientBean;
import beans.RecipeTableDataBean;
import dao.ChefDAO;
import dao.RecipeDAO;
import exceptions.ExistingRecipeException;
import factories.RecipeFactory;
import factories.RecipeIngredientFactory;
import model.ChefBase;
import model.RecipeBase;
import model.RecipeIngredient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeSharingApplicativeController implements RecipeSharingController{

    @Override
    public void setUpRecipesObserver(String chefUsername){
        RecipeDAO recipeDAO = new RecipeDAO(chefUsername);
        recipeDAO.attach(RecipeTableDataBean.getSingletonInstance());
        RecipeTableDataBean.getSingletonInstance().setSubject(recipeDAO);
    }

    @Override
    public void saveChanges() {
        RecipeDAO recipeDAO = new RecipeDAO();
        try {
            recipeDAO.saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shareRecipe(RecipeBean recipe){
        RecipeFactory factory = new RecipeFactory();
        ChefDAO chefDAO = new ChefDAO();
        ChefBase chef = chefDAO.retrieveChef(recipe.getChefUsername());

        RecipeIngredientFactory ingredientFactory = new RecipeIngredientFactory();
        List<RecipeIngredient> ingredientList = new ArrayList<>();
        for(RecipeIngredientBean i: recipe.getIngedientList()){
            RecipeIngredient ingredient = ingredientFactory.createRecipeIngredient(i.getName(),i.getQuantity(),i.getMeasureUnit());
            ingredientList.add(ingredient);
        }

        RecipeBase newRecipe = factory.createRecipe(
                recipe.getName(),
                chef,
                recipe.getDifficulty(),
                recipe.getPreparationTime(),
                recipe.getServings(),
                ingredientList,
                recipe.getPreparationProcedure()
        );

        RecipeDAO recipeDAO = new RecipeDAO();
        int i=1;
        String name = newRecipe.getName();

        retry:
        while(true){
            try {
                if(i==1){
                    recipeDAO.saveRecipe(newRecipe);
                    break;
                }
                else{
                    newRecipe.setName(name+" "+i);
                    recipeDAO.saveRecipe(newRecipe);
                    break;
                }
            } catch (ExistingRecipeException e) {
                System.out.println("Duplicate recipe detected");
                i++;
            }catch (IOException e){
                e.printStackTrace();
                break;
            }
        }

    }

}
