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

    public RecipeManagementView(String chefUsername){
        this.chefUsername = chefUsername;
        this.sc = new Scanner(System.in);
        RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
        RecipeSharingController sharingController = controllerFactory.createRecipeSharingController();
        sharingController.setUpRecipesObserver(LoginController.getChefLogged().getUsername());
        this.dataBean = RecipeTableDataBean.getSingletonInstance();
    }

    public void display(){
        System.out.println();
        System.out.println("Recipe Management");
        System.out.println();
        System.out.println("Those are your reipes: ");

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
                ShareRecipeView shareRecipeView = new ShareRecipeView(this);
                shareRecipeView.display(this.chefUsername);
            }
            case 2 -> {
                //todo update
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
        for(RecipeBean r: recipeList){
            System.out.println(r.getName());
        }
        System.out.println();
    }

}
