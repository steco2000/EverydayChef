package view;

import beans.UserCredBean;
import control.UserLoginController;
import factories.UserLoginControllerFactory;

import java.util.Scanner;

public class UserLoginView {

    private Scanner sc;

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
                System.out.println("Invalid answer, digit something to continue to continue\n");
                this.sc.nextLine();
                continue;
            }

            if(answer < 1 || answer > 3){
                System.out.println("Invalid answer, digit something to continue to continue\n");
                this.sc.nextLine();
            }

            switch (answer){
                case 1:
                    boolean result = this.loginAttempt();
                    System.out.println(result); //todo: carica user home
                    break;
                case 2:
                    UserRegisterView regView = new UserRegisterView();
                    regView.display();
                    break;
                case 3:
                    //todo carica chef login
                    break;
            }
        }
    }

    private boolean loginAttempt() {
        System.out.println("Log in");
        System.out.print("Username: ");
        String username = this.sc.nextLine();
        System.out.print("Password: ");
        String password = this.sc.nextLine();

        UserCredBean credBean = new UserCredBean();
        credBean.setUsername(username);
        credBean.setPassword(password);

        UserLoginControllerFactory factory = new UserLoginControllerFactory();
        UserLoginController loginController = factory.createUserLoginController();

        if(loginController.attemptUserLogin(credBean)){
            return true;
        }
        return false;
    }

}
