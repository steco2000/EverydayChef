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

//controller applicativo deputato a salvataggio e condivisione delle ricette

public class RecipeSharingApplicativeController implements RecipeSharingController{

    //al controller applicativo è assegnata la responsabilità di legare il bean observer di presentazione al DAO da osservare, dato che conosce entrambi e si trova nel mezzo
    @Override
    public void setUpRecipesObserver(String chefUsername){
        RecipeDAO recipeDAO = new RecipeDAO(chefUsername);
        recipeDAO.attach(RecipeTableDataBean.getSingletonInstance());
        RecipeTableDataBean.getSingletonInstance().setSubject(recipeDAO);
    }

    //metodo per innescare il salvataggio dei cambiamenti sul DAO
    @Override
    public void saveChanges() {
        RecipeDAO recipeDAO = new RecipeDAO();
        try {
            recipeDAO.saveChanges();
        } catch (IOException ignored) {
            assert(true);
        }
    }

    //metodo che gestisce creazione e salvataggio della ricetta
    @Override
    public void shareRecipe(RecipeBean recipe){

        //vengono prima recuperati i dati dello chef che crea la ricetta
        RecipeFactory factory = new RecipeFactory();
        ChefDAO chefDAO = new ChefDAO();
        ChefBase chef = chefDAO.retrieveChef(recipe.getChefUsername());

        //viene creata la lista degli ingredienti
        RecipeIngredientFactory ingredientFactory = new RecipeIngredientFactory();
        List<RecipeIngredient> ingredientList = new ArrayList<>();
        for(RecipeIngredientBean i: recipe.getIngredientList()){
            RecipeIngredient ingredient = ingredientFactory.createRecipeIngredient(i.getName(),i.getQuantity(),i.getMeasureUnit());
            ingredientList.add(ingredient);
        }

        //si crea la ricetta
        RecipeBase newRecipe = factory.createRecipe(
                recipe.getName(),
                chef,
                recipe.getDifficulty(),
                recipe.getPreparationTime(),
                recipe.getServings(),
                ingredientList,
                recipe.getPreparationProcedure()
        );

        /*
        Da qui in poi si gestiscono i casi di omonimia tra ricette. Se si tenta di salvare una ricetta con stesso nome di un'altra, si aggiunge a fianco un identificativo
        numerico (es. Carbonara 2)
         */

        RecipeDAO recipeDAO = new RecipeDAO(recipe.getChefUsername());
        int i=1;
        String name = newRecipe.getName();

        while(true){
            try {
                if(i==1){
                    recipeDAO.saveRecipe(newRecipe);
                }
                else{
                    newRecipe.setName(name+" "+i);
                    recipeDAO.saveRecipe(newRecipe);
                }
                break;
            } catch (ExistingRecipeException e) {

                /*
                il metodo "saveRecipe" lancia l'eccezione di questo catch se già esiste una ricetta con quel nome. Ovviamente ne può esistere più di una, quindi si prova incrementando
                di volta in volta l'identificativo, fino a trovarne uno non utilizzato
                 */
                i++;

            }
        }

    }

}
