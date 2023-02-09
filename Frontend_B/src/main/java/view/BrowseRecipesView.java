package view;

import beans.RecipeBrowsingTableBean;
import code_reuse.InputReusableUtilities;
import control.BrowseRecipeController;
import control.RecipeSearchingController;
import factories.BrowseRecipeControllerFactory;
import factories.RecipeSearchingControllerFactory;

import java.util.List;
import java.util.Scanner;

//view che gestisce la navigazione tra le ricette consigliate

public class BrowseRecipesView {

    private Scanner sc;

    //ricette consigliate o risultati di una ricerca
    private List<RecipeBrowsingTableBean> suggestedRecipes;
    private List<RecipeBrowsingTableBean> searchResult;

    public BrowseRecipesView() {
        this.sc = new Scanner(System.in);
    }

    //display della view e raccolta azioni utente
    public void display(boolean fromSearch, String ...search){
        System.out.println();
        System.out.println("Browse Recipes");
        System.out.println();

        if(!fromSearch) this.displaySuggestedRecipesTable();
        else this.displaySearchResults(search[0]);

        System.out.println();
        System.out.println("Press:");
        System.out.println("0) To go back home");
        System.out.println("1) To show a recipe from the list");
        System.out.println("2) To search a specific recipe");
        int answer = InputReusableUtilities.getAnswer(this.sc,0,2);

        switch (answer){
            case -1 -> {
                assert(true); //errore nella risposta, non faccio nulla e proseguo
            }
            case 0 -> {
                UserHomeView userHomeView = new UserHomeView();
                userHomeView.display();
            }
            case 1 -> {
                this.manageShowRecipe(fromSearch);
            }
            default -> {
                System.out.println();
                System.out.println("Digit your search below:");
                String searchValue = sc.nextLine();
                this.display(true,searchValue);
            }
        }
    }

    //metodo che gestisce come visualizzare le ricette, se siamo in ricerca o in fase di consiglio delle ricette consigliate
    private void manageShowRecipe(boolean fromSearch) {
        while(true){
            if(fromSearch) this.fromSearchRecipeBrowsing();
            else this.suggestedRecipesBrowsing();
            break;
        }
    }

    //gestione azioni utente in modalità di ricerca
    private void fromSearchRecipeBrowsing(){
        int answer;
        if(this.searchResult.isEmpty()){
            System.out.println();
            System.out.println("There are no results from your search, try again. Press enter to continue");
            this.sc.nextLine();
            this.display(false);
        }
        System.out.println();

        //ogni ricetta della lista viene associata a un indice numerico, lo stesso dell'array + 1. In questo modo è possibile accedervi direttamente
        System.out.println("Digit the recipe's index");
        answer = InputReusableUtilities.getAnswer(this.sc,1,this.searchResult.size());
        if(answer == -1){
            System.out.println();
            System.out.println("Invalid answer, press enter to continue");
            this.sc.nextLine();
            return;
        }

        RecipeBrowsingTableBean recipeSelected = this.searchResult.get(answer-1);
        RecipePageView recipePageView = new RecipePageView(recipeSelected);
        recipePageView.display();
    }

    //gestione azioni utente in modalità di consiglio ricette
    private void suggestedRecipesBrowsing() {
        int answer;
        if(this.suggestedRecipes.isEmpty()){
            System.out.println();
            System.out.println("There are no suggested recipes. Make sure to keep your inventory up to date. You can also try to search something. Press enter to continue");
            this.sc.nextLine();
            this.display(false);
        }
        System.out.println();
        System.out.println("Digit the recipe's index");
        answer = InputReusableUtilities.getAnswer(this.sc,1,this.suggestedRecipes.size());
        if(answer == -1){
            System.out.println();
            System.out.println("Invalid answer, press enter to continue");
            this.sc.nextLine();
            return;
        }

        RecipeBrowsingTableBean recipeSelected = this.suggestedRecipes.get(answer-1);
        RecipePageView recipePageView = new RecipePageView(recipeSelected);
        recipePageView.display();
    }

    //recupero e visualizzazione dei risultati di una ricerca
    private void displaySearchResults(String search) {
        if(search.isEmpty()){
            this.display(false);
        }else{
            RecipeSearchingControllerFactory factory = new RecipeSearchingControllerFactory();
            RecipeSearchingController controller = factory.createRecipeSearchingController();
            this.searchResult = controller.retrieveSearchResult(search);

            if(this.searchResult.isEmpty()) return;

            System.out.println("Search results:");
            this.displayTable(this.searchResult);
        }
    }

    //recupero e visualizzazione ricette consigliate
    private void displaySuggestedRecipesTable() {
        if(this.suggestedRecipes == null){
            BrowseRecipeControllerFactory factory = new BrowseRecipeControllerFactory();
            BrowseRecipeController browseRecipeController = factory.createBrowseRecipeController();
            this.suggestedRecipes = browseRecipeController.retrieveSuggestedRecipe();
        }

        if(this.suggestedRecipes.isEmpty()) return;

        System.out.println("Suggested Recipes:");
        this.displayTable(this.suggestedRecipes);
    }

    //metodo per stampare una lista di ricette
    private void displayTable(List<RecipeBrowsingTableBean> data){
        int index = 1;
        for(RecipeBrowsingTableBean r: data){
            System.out.println(index+") "+r.getName()+", by "+r.getChefCompleteName());
            index++;
        }
    }

}

