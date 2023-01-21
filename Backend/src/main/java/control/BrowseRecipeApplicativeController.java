package control;

import beans.RecipeBrowsingTableBean;
import dao.ChefDAO;
import dao.RecipesBrowsingDAO;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BrowseRecipeApplicativeController implements BrowseRecipeController{

    private final UserCredentials currentUser;

    public BrowseRecipeApplicativeController(){
        this.currentUser = (UserCredentials) LoginController.getUserLogged();
    }

    @Override
    public List<RecipeBrowsingTableBean> retrieveSuggestedRecipe() {
        Inventory inventory = this.currentUser.getIngredientsInventory();
        RecipeInfoRetrievingApplicativeController infoController = new RecipeInfoRetrievingApplicativeController();
        infoController.setInventoryList(inventory.getIngredientList());
        ChefDAO chefDAO = new ChefDAO();
        List<RecipeBase> suggestedRecipes = new ArrayList<>();

        int lastId = 1;
        try {
            lastId = chefDAO.getLastId();
        } catch (IOException | ClassNotFoundException ignored) {
            System.out.println("Got exception from id retrieval");
            assert(true); //eccezione ignorata
        }


        List<RecipeBase> currentChefRecipeList;
        RecipesBrowsingDAO dao = new RecipesBrowsingDAO();

        System.out.println("Iterating chef ids from 1 to last: "+lastId);

        for (int i = 1; i < lastId + 1; i++) {
            currentChefRecipeList = dao.getRecipeList(i);
            if (currentChefRecipeList.isEmpty()) continue;

            System.out.println("Got this recipes list from chef with id: "+i);
            for(RecipeBase r: currentChefRecipeList) System.out.println(r.getName());

            for (Ingredient ingr : (inventory.getIngredientList())) {
                for(RecipeBase rec: currentChefRecipeList){
                    if(rec.getIngredientList().stream().anyMatch(o -> o.getName().contains(ingr.getName()) || ingr.getName().contains(o.getName()))){
                        suggestedRecipes.add(rec);
                    }
                }
            }
        }

        List<RecipeBrowsingTableBean> beanList = new ArrayList<>();
        for(RecipeBase r: suggestedRecipes){
            RecipeBrowsingTableBean bean = new RecipeBrowsingTableBean();
            bean.setName(r.getName());
            bean.setChefCompleteName(r.getChef().getName()+" "+r.getChef().getSurname());
            bean.setChefUsername(r.getChef().getUsername());
            beanList.add(bean);
        }

        return beanList;
    }

}

