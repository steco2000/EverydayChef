package view;

import beans.ChefBean;
import beans.RecipeBean;
import beans.RecipeBrowsingTableBean;
import beans.RecipeIngredientBean;
import code_reuse.InputReusableUtilities;
import control.RecipeInfoRetrievingController;
import exceptions.PersistentDataAccessException;
import factories.RecipeInfoRetrievingControllerFactory;

import java.util.List;
import java.util.Scanner;

//view che gestisce la pagina di informazioni della ricetta

public class RecipePageView {

    private Scanner sc;
    private RecipeBean recipeSelected;
    private ChefBean chef;
    private List<RecipeIngredientBean> missingIngredients;

    //nel costruttore recupero tutte le info necessarie
    public RecipePageView(RecipeBrowsingTableBean recipe) throws PersistentDataAccessException {
        this.sc = new Scanner(System.in);
        RecipeInfoRetrievingControllerFactory factory = new RecipeInfoRetrievingControllerFactory();
        RecipeInfoRetrievingController controller = factory.createRecipeInfoRetrievingController();
        this.recipeSelected = controller.retrieveRecipeInfo(recipe);
        this.chef = controller.retrieveChefInfo(recipe.getChefUsername());
        this.missingIngredients = controller.retrieveMissingIngredients(recipe);
    }

    //display della ricetta e raccolta azioni utente
    public void display(){
        System.out.println();
        System.out.println(this.recipeSelected.getName());
        System.out.println("Difficulty: "+this.recipeSelected.getDifficulty());
        System.out.println("Preparation time: "+this.recipeSelected.getPreparationTime());
        System.out.println("Number of servings: "+this.recipeSelected.getServings());
        System.out.println("Created by: "+this.chef.getName()+" "+this.chef.getSurname());
        System.out.println("Preparation procedure:");
        System.out.println(this.recipeSelected.getPreparationProcedure());
        System.out.println();
        System.out.println("Ingredient list:");
        this.displayIngredientTable(this.recipeSelected.getIngredientList());

        if(!missingIngredients.isEmpty()){
            System.out.println();
            System.out.println("Missing ingredients (with missing quantity):");
            this.displayIngredientTable(this.missingIngredients);
        }

        System.out.println();
        System.out.println("Press:");
        System.out.println("0) To go back");
        System.out.println("1) To view chef info");
        int answer = InputReusableUtilities.getAnswer(this.sc,0,1);

        if (answer == 0) {
            BrowseRecipesView browseRecipesView = new BrowseRecipesView();
            browseRecipesView.display(false);
        } else {
            this.displayChefPage();
        }
    }

    //stampa informazioni dello chef
    private void displayChefPage() {
        System.out.println();
        System.out.println("Chef Page:");
        System.out.println(this.chef.getName()+" "+this.chef.getSurname());
        System.out.println("Born: "+this.chef.getBirthDate());
        System.out.println("Email: "+this.chef.getEmail());
        System.out.println("Bio informations:");
        System.out.println(this.chef.getInfo());
        System.out.println();
        System.out.println("Press enter to go back");
        this.sc.nextLine();
        this.display();
    }

    //stampa le tabelle degli ingredienti (della ricetta e mancanti)
    private void displayIngredientTable(List<RecipeIngredientBean> data) {
        for(RecipeIngredientBean i: data){
            System.out.println("- "+i.getName()+", "+i.getStringQuantity()+" "+i.getMeasureUnit());
        }
    }

}
