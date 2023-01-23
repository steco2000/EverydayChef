package view;

import beans.ChefBean;
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
            int answer = 0;
            try{
                answer = Integer.parseInt(sc.nextLine());
            }catch(NumberFormatException e){
                System.out.println("Invalid answer, digit something to continue to continue");
                this.sc.nextLine();
                continue;
            }

            if(answer < 1 || answer > 3){
                System.out.println("Invalid answer, digit something to continue to continue\n");
                this.sc.nextLine();
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
        System.out.print("Username: ");
        String username = this.sc.nextLine();
        System.out.print("Password: ");
        String password = this.sc.nextLine();

        ChefBean credBean = new ChefBean();
        credBean.setUsername(username);
        credBean.setPassword(password);

        ChefLoginControllerFactory factory = new ChefLoginControllerFactory();
        ChefLoginController loginController = factory.createChefLoginController();

        return loginController.attemptChefLogin(credBean);
    }

}
