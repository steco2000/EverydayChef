package view;

import beans.UserCredBean;
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
            int answer = 0;
            try{
                answer = Integer.parseInt(sc.nextLine());
            }catch(NumberFormatException e){
                System.out.println("Invalid answer, digit something to continue to continue");
                this.sc.nextLine();
                continue;
            }

            if(answer < 1 || answer > 3){
                System.out.println("Invalid answer, digit something to continue to continue");
                this.sc.nextLine();
            }

            switch (answer) {
                case 1 -> {
                    if(this.loginAttempt()){
                        UserHomeView homeView = new UserHomeView(this.credBean);
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
        System.out.print("Username: ");
        String username = this.sc.nextLine();
        System.out.print("Password: ");
        String password = this.sc.nextLine();

        this.credBean = new UserCredBean();
        credBean.setUsername(username);
        credBean.setPassword(password);

        UserLoginControllerFactory factory = new UserLoginControllerFactory();
        UserLoginController loginController = factory.createUserLoginController();

        return loginController.attemptUserLogin(credBean);
    }

}
