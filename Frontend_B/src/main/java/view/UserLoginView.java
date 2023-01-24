package view;

import beans.UserCredBean;
import code_reuse.InputReusableUtilities;
import control.UserLoginController;
import factories.UserLoginControllerFactory;

import java.util.Scanner;

public class UserLoginView {

    private Scanner sc;
    private UserCredBean credBean;

    public UserLoginView(){
        this.sc = new Scanner(System.in);
    }

    public void display() {
        while(true) {
            System.out.println("EverydayChef");
            System.out.println("Log In as user");
            System.out.println("");
            System.out.println("Press:");
            System.out.println("1) To log in");
            System.out.println("2) To register");
            System.out.println("3) To Sign up as Chef");
            int answer = InputReusableUtilities.getAnswer(this.sc,1,3);

            switch (answer) {
                case -1 -> {
                    assert(true); //errore nella risposta, non faccio niente e passo alla prossima iterazione
                }
                case 1 -> {
                    if(this.loginAttempt()){
                        UserHomeView homeView = new UserHomeView();
                        homeView.display();
                    }else{
                        System.out.println("Login failed, incorrect credentials");
                        System.out.println("Press enter or digit something to continue");
                        sc.nextLine();
                    }
                }
                case 2 -> {
                    System.out.println();
                    UserRegisterView regView = new UserRegisterView();
                    regView.display();
                }
                default -> {
                    System.out.println();
                    ChefLoginView chefLoginView = new ChefLoginView();
                    chefLoginView.display();
                }
            }
        }
    }

    private boolean loginAttempt() {
        System.out.println("Log in");
        String[] credentials = InputReusableUtilities.getCredentials(this.sc);

        this.credBean = new UserCredBean();
        credBean.setUsername(credentials[0]);
        credBean.setPassword(credentials[1]);

        UserLoginControllerFactory factory = new UserLoginControllerFactory();
        UserLoginController loginController = factory.createUserLoginController();

        return loginController.attemptUserLogin(credBean);
    }

}
