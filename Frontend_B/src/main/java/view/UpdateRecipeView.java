package view;

import beans.RecipeBean;
import beans.RecipeIngredientBean;
import code_reuse.InputReusableUtilities;
import control.RecipeUpdadingController;
import exceptions.ExistingRecipeException;
import exceptions.PersistentDataAccessException;
import factories.RecipeUpdatingControllerFactory;

import java.util.Scanner;

//view per la gestione della schermata di modifica di una ricetta

public class UpdateRecipeView {

    private Scanner sc;
    private RecipeBean updates;
    private String chefUsername;

    public UpdateRecipeView(String chefUsername){
        this.sc = new Scanner(System.in);
        this.chefUsername = chefUsername;
    }

    //display della schermata con visualizzazione delle info della ricetta + raccolta azioni utente
    public void display(RecipeBean toUpdate) {
        try {
            updates = new RecipeBean();
            updates.setName(toUpdate.getName());
            updates.setDifficulty(toUpdate.getDifficulty());
            updates.setPreparationTime(toUpdate.getPreparationTime());
            updates.setServings(String.valueOf(toUpdate.getServings()));
            updates.setPreparationProcedure(toUpdate.getPreparationProcedure());
            updates.setChefUsername(this.chefUsername);
            updates.setIngredientList(toUpdate.getIngredientList());
            while (true) {
                System.out.println();
                System.out.println("You selected this recipe to update:");
                System.out.println("Name: " + updates.getName());
                System.out.println("Difficulty: " + updates.getDifficulty());
                System.out.println("Preparation time: " + updates.getPreparationTime());
                System.out.println("Number of servings: " + updates.getServings());
                System.out.println("Preparation procedure:");
                System.out.println(updates.getPreparationProcedure());
                this.displayIngredientTable(updates);
                System.out.println("Press:");
                System.out.println("0) To cancel");
                System.out.println("1) To modify the name");
                System.out.println("2) To modify the difficulty");
                System.out.println("3) To modify the preparation time");
                System.out.println("4) To modify the number of servings");
                System.out.println("5) To modify the preparation procedure");
                System.out.println("6) To add an ingredient to the list");
                System.out.println("7) To remove an ingredient from the list");
                System.out.println("8) To delete the recipe");
                System.out.println("9) to confirm changes");
                int ans = InputReusableUtilities.getAnswer(this.sc, 0, 9);

                //gestione degli input dell'utente in base all'azione scelta
                switch (ans) {
                    case -1 -> {
                        assert (true); //errore nella risposta, non faccio nulla
                    }
                    case 0 -> {
                        RecipeManagementView recipeManagementView = new RecipeManagementView(this.chefUsername);
                        recipeManagementView.display();
                    }
                    case 1 -> {
                        System.out.print("New name: ");
                        try {
                            updates.setName(this.sc.nextLine());
                        } catch (IllegalArgumentException e) {
                            System.out.println("The name can't be empty, press enter to continue");
                            this.sc.nextLine();
                        }
                    }
                    case 2 -> updates.setDifficulty(InputReusableUtilities.difficultyInput(this.sc));
                    case 3 -> updates.setPreparationTime(InputReusableUtilities.preparationTimeInput(this.sc));
                    case 4 -> {
                        System.out.print("Number of servings: ");
                        try {
                            updates.setServings(this.sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid value, press enter to continue");
                            this.sc.nextLine();
                        }
                    }
                    case 5 -> {
                        System.out.println("Preparation procedure (do not press enter to start a new line!)");
                        updates.setPreparationProcedure(this.sc.nextLine());
                    }
                    case 6 -> updates.getIngredientList().add(InputReusableUtilities.getIngredientData(this.sc));
                    case 7 -> {
                        if (updates.getIngredientList().isEmpty()) continue;
                        System.out.println("Digit the index of the ingredient to remove");
                        int ingrIdx = InputReusableUtilities.getAnswer(this.sc, 1, updates.getIngredientList().size());
                        if (ingrIdx != -1) updates.getIngredientList().remove(ingrIdx - 1);
                    }
                    case 8 -> {
                        try {
                            System.out.println("Are you sure you want to delete this recipe? (y/n)");
                            if (InputReusableUtilities.yes(this.sc)) {
                                RecipeUpdatingControllerFactory updatingControllerFactory = new RecipeUpdatingControllerFactory();
                                RecipeUpdadingController updadingController = updatingControllerFactory.createRecipeUpdatingController();
                                updadingController.deleteRecipe(toUpdate.getName());
                                RecipeManagementView recipeManagementView = new RecipeManagementView(this.chefUsername);
                                recipeManagementView.display();
                                return;
                            }
                        }catch (PersistentDataAccessException e){
                            System.out.println("Error: "+e.getMessage()+" Press enter to continue");
                            this.sc.nextLine();
                        }
                    }
                    default -> {
                        RecipeUpdatingControllerFactory updatingControllerFactory = new RecipeUpdatingControllerFactory();
                        RecipeUpdadingController updadingController = updatingControllerFactory.createRecipeUpdatingController();
                        updadingController.updateRecipe(toUpdate.getName(), updates);
                        RecipeManagementView recipeManagementView = new RecipeManagementView(this.chefUsername);
                        recipeManagementView.display();
                    }
                }
            }
        }catch(PersistentDataAccessException e){
            System.out.println("Error: "+e.getMessage()+" Press enter to continue");
            this.sc.nextLine();
        }
    }

    //metodo che stampa la lista degli ingredienti
    private void displayIngredientTable(RecipeBean recipe) {
        if(recipe.getIngredientList() == null || recipe.getIngredientList().isEmpty()) return;
        System.out.println();
        System.out.println("Ingredient List:");
        int idx = 1;
        for(RecipeIngredientBean i: recipe.getIngredientList()){
            System.out.println(idx+") "+i.getName()+", "+i.getStringQuantity()+" "+i.getMeasureUnit());
            idx++;
        }
        System.out.println();
    }
}
