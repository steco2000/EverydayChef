package view;

import beans.RecipeBean;
import beans.RecipeTableDataBean;
import code_reuse.InputReusableUtilities;
import control.LoginController;
import control.RecipeSharingController;
import factories.RecipeSharingControllerFactory;

import java.util.List;
import java.util.Scanner;

public class RecipeManagementView {

    private Scanner sc;
    private RecipeTableDataBean dataBean;
    private List<RecipeBean> recipeList;
    private String chefUsername;
    private static boolean observerIsSet = false;

    public RecipeManagementView(String chefUsername){
        if(!observerIsSet) startRecipesObservation();
        this.chefUsername = chefUsername;
        this.sc = new Scanner(System.in);
        this.dataBean = RecipeTableDataBean.getSingletonInstance();
    }

    private static void startRecipesObservation() {
        RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
        RecipeSharingController sharingController = controllerFactory.createRecipeSharingController();
        sharingController.setUpRecipesObserver(LoginController.getChefLogged().getUsername());
        observerIsSet = true;
    }

    public void display(){
        System.out.println();
        System.out.println("Recipe Management");
        System.out.println();

        this.displayRecipeTable();

        System.out.println("Press:");
        System.out.println("0) To go back home");
        System.out.println("1) To share a new recipe");
        System.out.println("2) To update a recipe from your list");
        System.out.println("3) To save changes");
        int answer = InputReusableUtilities.getAnswer(this.sc,0,3);

        switch (answer){
            case -1 -> {
                assert(true); //errore nella risposta, non faccio nulla
            }
            case 0 -> {
                RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
                RecipeSharingController controller = controllerFactory.createRecipeSharingController();
                controller.saveChanges();
                ChefHomeView chefHomeView = new ChefHomeView();
                chefHomeView.display();
            }
            case 1 -> {
                ShareRecipeView shareRecipeView = new ShareRecipeView();
                shareRecipeView.display(this.chefUsername);
            }
            case 2 -> {
                System.out.println("Digit the index of the recipe to update");
                int ans = InputReusableUtilities.getAnswer(this.sc,1,this.recipeList.size());
                if(ans == -1) this.display();
                RecipeBean toUpdate = this.recipeList.get(ans-1);
                toUpdate = dataBean.getRecipe(toUpdate.getName());
                UpdateRecipeView updateRecipeView = new UpdateRecipeView(this.chefUsername);
                updateRecipeView.display(toUpdate);
            }
            default -> {
                RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
                RecipeSharingController controller = controllerFactory.createRecipeSharingController();
                controller.saveChanges();
                System.out.println("Save completed, press enter to continue");
                this.sc.nextLine();
                this.display();
            }
        }
    }

    private void displayRecipeTable() {
        recipeList = dataBean.getTableData();
        if(recipeList.isEmpty()) return;
        int i = 1;
        System.out.println("Those are your reipes: ");
        for(RecipeBean r: recipeList){
            System.out.println(i+") "+r.getName());
            i++;
        }
        System.out.println();
    }

}
