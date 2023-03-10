package view;

import beans.RecipeBean;
import beans.RecipeTableDataBean;
import code_reuse.InputReusableUtilities;
import control.LoginController;
import control.RecipeSharingController;
import exceptions.PersistentDataAccessException;
import factories.RecipeSharingControllerFactory;

import java.util.List;
import java.util.Scanner;

//view che controlla la schermata di gestione delle ricette

public class RecipeManagementView {

    private Scanner sc;
    private RecipeTableDataBean dataBean;
    private List<RecipeBean> recipeList;
    private String chefUsername;
    private static boolean observerIsSet = false;

    //nel costruttore si inizializzano gli attributi della view e si controlla se l'observer sul RecipeDAO è stato impostato
    public RecipeManagementView(String chefUsername) throws PersistentDataAccessException {
        if(!observerIsSet) startRecipesObservation();
        this.chefUsername = chefUsername;
        this.sc = new Scanner(System.in);
        this.dataBean = RecipeTableDataBean.getSingletonInstance();
    }

    //metodo per impostare l'observer sul RecipeDAO
    private static void startRecipesObservation() throws PersistentDataAccessException {
        RecipeSharingControllerFactory controllerFactory = new RecipeSharingControllerFactory();
        RecipeSharingController sharingController = controllerFactory.createRecipeSharingController();
        sharingController.setUpRecipesObserver(LoginController.getChefLogged().getUsername());
        observerIsSet = true;
    }

    //metodo per interrompere l'osservazione delle ricette al momento del logout dello chef
    public static void stopRecipesObservation(){ observerIsSet = false; }

    //display della schermata e raccolta azioni utente. In base all'azione scelta viene caricata la relativa schermata
    public void display(){
        try {
            System.out.println();
            System.out.println("Recipe Management");
            System.out.println();

            this.displayRecipeTable();

            System.out.println("Press:");
            System.out.println("0) To go back home");
            System.out.println("1) To share a new recipe");
            System.out.println("2) To update a recipe from your list");
            System.out.println("3) To save changes");
            int answer = InputReusableUtilities.getAnswer(this.sc, 0, 3);

            switch (answer) {
                case -1 -> {
                    assert (true); //errore nella risposta, non faccio nulla
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
                    int ans = InputReusableUtilities.getAnswer(this.sc, 1, this.recipeList.size());
                    if (ans == -1) this.display();
                    RecipeBean toUpdate = this.recipeList.get(ans - 1);
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
        }catch(PersistentDataAccessException e){
            System.out.println("Error: "+e.getMessage()+" Press enter to continue");
            this.sc.nextLine();
        }
    }

    //metodo che stampa la lista delle ricette salvate dallo chef
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
