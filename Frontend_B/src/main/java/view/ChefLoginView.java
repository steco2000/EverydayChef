package view;

import beans.ChefBean;
import code_reuse.InputReusableUtilities;
import control.ChefLoginController;
import factories.ChefLoginControllerFactory;

import java.util.Scanner;

public class ChefLoginView {

    private Scanner sc;

    public ChefLoginView(){
        this.sc = new Scanner(System.in);
    }

    public void display(){
        while(true) {
            System.out.println();
            System.out.println("EverydayChef");
            System.out.println("Log In as chef");
            System.out.println("");
            System.out.println("Press:");
            System.out.println("1) To log in as chef");
            System.out.println("2) To register as chef");
            System.out.println("3) To Sign up as user");
            int answer;
            try{
                answer = InputReusableUtilities.getAnswer(this.sc,1,3);
            }catch(NumberFormatException e){
                System.out.println("Invalid answer, digit something to continue to continue");
                this.sc.nextLine();
                continue;
            }

            switch (answer) {
                case 1 -> {
                    boolean result = this.chefLoginAttempt();
                    System.out.println(result); //todo carica chef home
                }
                case 2 -> {
                    System.out.println();
                    ChefRegisterView regView = new ChefRegisterView();
                    regView.display();
                }
                default -> {
                    UserLoginView userLoginView = new UserLoginView();
                    userLoginView.display();
                }
            }
        }
    }

    private boolean chefLoginAttempt() {
        System.out.println("Log in as chef");
        String[] credentials = InputReusableUtilities.getCredentials(this.sc);

        ChefBean credBean = new ChefBean();
        credBean.setUsername(credentials[0]);
        credBean.setPassword(credentials[1]);

        ChefLoginControllerFactory factory = new ChefLoginControllerFactory();
        ChefLoginController loginController = factory.createChefLoginController();

        return loginController.attemptChefLogin(credBean);
    }

}
