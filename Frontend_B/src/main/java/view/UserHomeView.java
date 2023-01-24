package view;

import beans.UserCredBean;
import code_reuse.InputReusableUtilities;

import java.util.Scanner;

public class UserHomeView {

    private Scanner sc;
    private UserCredBean loggedUser;

    public UserHomeView(UserCredBean loggedUser){
        this.sc = new Scanner(System.in);
        this.loggedUser = loggedUser;
    }

    public void display(){
        while(true) {
            System.out.println();
            System.out.println("Welcome to EverydayChef, " + loggedUser.getUsername() + "!");
            System.out.println("Press:");
            System.out.println("0) To exit");
            System.out.println("1) To manage your ingredients inventory");
            System.out.println("2) To browse through the recipes");
            int answer;
            try {
                answer = InputReusableUtilities.getAnswer(this.sc,0,2);
            } catch (NumberFormatException e) {
                System.out.println("Invalid answer, digit something to continue to continue");
                this.sc.nextLine();
                continue;
            }

            switch (answer) {
                case 0 -> {
                    UserLoginView loginView = new UserLoginView();
                    loginView.display();
                }
                case 1 -> {
                    //todo: carica schermata ricette
                }
                default -> {
                    //todo: carica inventario
                }
            }

        }
    }

}
