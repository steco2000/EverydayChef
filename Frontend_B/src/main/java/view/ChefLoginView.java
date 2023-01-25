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
            int answer = InputReusableUtilities.getAnswer(this.sc,1,3);

            switch (answer) {
                case -1 ->{
                    assert(true); //errore nella risposta, non faccio niente e passo alla prossima iterazione;
                }
                case 1 -> {
                    if(this.chefLoginAttempt()){
                        ChefHomeView chefHomeView = new ChefHomeView();
                        chefHomeView.display();
                    }else {
                        System.out.println();
                        System.out.println("Login failed, incorrect credentials");
                        System.out.println("Press enter or digit something to continue");
                        sc.nextLine();
                    }
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
