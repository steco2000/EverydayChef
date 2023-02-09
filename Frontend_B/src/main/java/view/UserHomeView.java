package view;

import code_reuse.InputReusableUtilities;
import control.LoginController;

import java.util.Scanner;

//view per la gestione della home per l'utente base

public class UserHomeView {

    private Scanner sc;

    public UserHomeView(){
        this.sc = new Scanner(System.in);
    }

    //display della schermata e raccolta azioni utente. In base all'azione scelta si carica la rispettiva schermata
    public void display(){
        while(true) {
            System.out.println();
            System.out.println("Welcome to EverydayChef, " + LoginController.getUserLogged().getUsername() + "!");
            System.out.println("Press:");
            System.out.println("0) To exit");
            System.out.println("1) To manage your ingredients inventory");
            System.out.println("2) To browse through the recipes");
            int answer = InputReusableUtilities.getAnswer(this.sc,0,2);

            switch (answer) {
                case -1 -> {
                    assert(true); //errore nella risposta, non faccio niente e vado alla prossima iterazione
                }
                case 0 -> {
                    UserLoginView loginView = new UserLoginView();
                    loginView.display();
                }
                case 1 -> {
                    InventoryView inventoryView = new InventoryView();
                    inventoryView.display();
                }
                default -> {
                    BrowseRecipesView browseRecipesView = new BrowseRecipesView();
                    browseRecipesView.display(false);
                }
            }

        }
    }

}
