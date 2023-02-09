package view;

import beans.RecipeStatisticsTableBean;
import control.RecipeStatisticsController;
import factories.RecipeStatisticsControllerFactory;

import java.util.List;
import java.util.Scanner;

//view per la gestione della schermata di statistiche delle ricette

public class RecipeStatisticsView {

    private Scanner sc;
    private String chefUsername;

    public RecipeStatisticsView(String chefUsername){
        this.sc = new Scanner(System.in);
        this.chefUsername = chefUsername;
    }

   //recupero informazioni e display della schermata
    public void display(){
        RecipeStatisticsController controller = (new RecipeStatisticsControllerFactory()).createRecipeStatisticsController();
        List<RecipeStatisticsTableBean> beanList = controller.getRecipesStatistics(this.chefUsername);
        System.out.println();
        System.out.println("Recipe Statistics:");
        for(int i=0; i<beanList.size(); i++){
            System.out.println(i+1+") "+beanList.get(i).getRecipeName()+": "+beanList.get(i).getViews()+" views");
        }
        System.out.println();
        System.out.println("Press enter to go back home");
        this.sc.nextLine();
        ChefHomeView chefHomeView = new ChefHomeView();
        chefHomeView.display();
    }

}
