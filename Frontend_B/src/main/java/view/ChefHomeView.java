package view;

import beans.RecipeStatisticsTableBean;
import code_reuse.InputReusableUtilities;
import control.LoginController;

import java.util.Scanner;

public class ChefHomeView {

    private Scanner sc;

    public ChefHomeView(){
        this.sc = new Scanner(System.in);
    }

    public void display(){
        while(true) {
            System.out.println();
            System.out.println("Chef Dashboard:");
            System.out.println("Welcome to EverydayChef, " + LoginController.getChefLogged().getUsername() + "!");
            System.out.println("Press:");
            System.out.println("0) To exit");
            System.out.println("1) To manage your recipes");
            System.out.println("2) To view the statistics of your recipes");
            int answer = InputReusableUtilities.getAnswer(this.sc,0,2);

            switch (answer){
                case -1 -> {
                    assert(true); //errore nella risposta, non faccio nulla
                }
                case 0 -> {
                    ChefLoginView chefLoginView = new ChefLoginView();
                    chefLoginView.display();
                }
                case 1 -> {
                    RecipeManagementView recipeManagementView = new RecipeManagementView(LoginController.getChefLogged().getUsername());
                    recipeManagementView.display();
                }
                default -> {
                    RecipeStatisticsView recipeStatisticsView = new RecipeStatisticsView(this, LoginController.getChefLogged().getUsername());
                    recipeStatisticsView.display();
                }
            }
        }
    }

}
