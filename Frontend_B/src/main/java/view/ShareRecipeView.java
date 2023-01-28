package view;

import beans.RecipeBean;
import beans.RecipeIngredientBean;
import code_reuse.InputReusableUtilities;
import control.RecipeSharingController;
import exceptions.RecipeIngredientQuantityException;
import factories.RecipeSharingControllerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShareRecipeView {

    private Scanner sc;
    private RecipeBean newRecipe;
    private RecipeManagementView recipeManagementView;

    public ShareRecipeView(RecipeManagementView recipeManagementView){
        this.recipeManagementView = recipeManagementView;
        this.sc = new Scanner(System.in);
        this.newRecipe = new RecipeBean();
    }

    public void display(String chefUsername){
        this.newRecipe.setChefUsername(chefUsername);
        while(true) {
            System.out.println();
            System.out.println("Recipe sharing");
            System.out.print("Recipe name: ");
            try {
                this.newRecipe.setName(sc.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println();
                System.out.println("Name can't be empty, press enter to continue");
                this.sc.nextLine();
                continue;
            }
            System.out.println("Difficulty:");
            this.newRecipe.setDifficulty(InputReusableUtilities.difficultyInput(this.sc));
            System.out.println();
            this.newRecipe.setPreparationTime(InputReusableUtilities.preparationTimeInput(this.sc));
            System.out.println();
            System.out.print("Number of servings: ");
            try {
                this.newRecipe.setServings(this.sc.nextLine());
            } catch (NumberFormatException e){
                System.out.println();
                System.out.println("Invalid value, press enter to continue");
                this.sc.nextLine();
                continue;
            }
            System.out.println("Preparation procedure:");
            this.newRecipe.setPreparationProcedure(this.sc.nextLine());

            System.out.println();
            this.newRecipe.setIngredientList(this.getIngredientList());

            if(this.newRecipe.getIngredientList().isEmpty()){
                System.out.println("A recipe must have at least one ingredient, press enter to continue");
                this.sc.nextLine();
                continue;
            }

            RecipeSharingControllerFactory factory = new RecipeSharingControllerFactory();
            RecipeSharingController controller = factory.createRecipeSharingController();
            controller.shareRecipe(this.newRecipe);
            break;

        }

        this.recipeManagementView.display();

    }

    private List<RecipeIngredientBean> getIngredientList() {
        List<RecipeIngredientBean> ingredientList = new ArrayList<>();
        while (true) {
            System.out.println("Ingredient List:");
            this.displayIngredientList(ingredientList);
            System.out.println("Press:");
            System.out.println("1) To add an ingredient");
            System.out.println("2) To remove an ingredient");
            System.out.println("3) To confirm the list");
            int ans = InputReusableUtilities.getAnswer(this.sc, 1, 3);

            switch (ans) {
                case -1 -> {
                    assert (true); //errore nella risposta, non faccio nulla
                }
                case 1 -> ingredientList.add(InputReusableUtilities.getIngredientData(this.sc));
                case 2 -> {
                    if (ingredientList.isEmpty()) continue;
                    System.out.println();
                    System.out.println("Digit the index of the ingredient to remove");
                    int ingrIdx = InputReusableUtilities.getAnswer(this.sc, 1, ingredientList.size());
                    if (ingrIdx == -1) {
                        continue;
                    }
                    ingredientList.remove(ingrIdx - 1);
                }
                default -> {
                    return ingredientList;
                }
            }
        }
    }

    private void displayIngredientList(List<RecipeIngredientBean> ingredientList){
        if(ingredientList.isEmpty()) return;
        int idx = 1;
        for(RecipeIngredientBean i: ingredientList){
            System.out.println(idx+") "+i.getName()+", "+i.getQuantity()+" "+i.getMeasureUnit());
            idx++;
        }
        System.out.println();
    }

}