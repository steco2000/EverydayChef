package view;

import beans.UserCredBean;
import code_reuse.InputReusableUtilities;
import control.UserLoginController;
import factories.UserLoginControllerFactory;

import java.lang.reflect.MalformedParametersException;
import java.util.Scanner;

public class UserRegisterView {

    private Scanner sc;

    public UserRegisterView(){
        this.sc = new Scanner(System.in);
    }

    public void display(){
        System.out.println("Register as user");
        System.out.println();
        System.out.print("Email: ");
        String email = this.sc.nextLine();
        System.out.print("Username: ");
        String username = this.sc.nextLine();

        String password;
        String passwordConf;

        while(true){
            System.out.print("Password: ");
            password = this.sc.nextLine();
            System.out.print("Confirm password: ");
            passwordConf = this.sc.nextLine();
            if(!password.equals(passwordConf)){
                System.out.println("Password doesn't match, digit something to continue");
                sc.nextLine();
            }else break;
        }

        System.out.println();
        System.out.println("Entered data:");
        System.out.println("Email: "+email);
        System.out.println("Username "+username);
        System.out.println("Password: "+password);
        System.out.println("Are you sure to proceed? (y/n)");

        if(InputReusableUtilities.yes(this.sc)) {
            UserCredBean credBean = new UserCredBean();
            credBean.setUsername(username);
            credBean.setPassword(password);

            try{
                credBean.setEmail(email);
            }catch (MalformedParametersException e){
                System.out.println("Invalid email, digit something to continue");
                sc.nextLine();
                this.display();
            }

            UserLoginControllerFactory factory = new UserLoginControllerFactory();
            UserLoginController controller = factory.createUserLoginController();

            if (controller.registerUser(credBean)) {
                UserLoginView loginView = new UserLoginView();
                loginView.display();
            } else {
                System.out.println("Unable to register, username or email already used, digit something to continue");
                sc.nextLine();
                this.display();
            }
        }else{
            UserLoginView loginView = new UserLoginView();
            loginView.display();
        }

    }

}
