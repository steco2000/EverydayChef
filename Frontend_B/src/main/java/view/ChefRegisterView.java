package view;

import beans.ChefBean;
import control.ChefLoginController;
import factories.ChefLoginControllerFactory;

import java.lang.reflect.MalformedParametersException;
import java.text.ParseException;
import java.util.Scanner;

public class ChefRegisterView {

    private Scanner sc;

    public ChefRegisterView() {
        this.sc = new Scanner(System.in);
    }

    public void display() {
        ChefBean newChef = new ChefBean();

        System.out.println("Register as Chef");

        System.out.print("Name: ");
        newChef.setName(sc.nextLine());
        System.out.print("Surname: ");
        newChef.setSurname(sc.nextLine());
        while (true) {
            try {
                System.out.print("Birth Date: ");
                newChef.setBirthDate(sc.nextLine());
                break;
            } catch (ParseException e) {
                System.out.println("Invalid date, the correct format is DD/MM/YYYY");
                System.out.println("Press enter or digit something to continue");
                sc.nextLine();
            }
        }
        System.out.print("Bio informations (Do not press enter to start a new line!): ");
        newChef.setInfo(sc.nextLine());
        while (true) {
            try {
                System.out.print("Email: ");
                newChef.setEmail(sc.nextLine());
                break;
            } catch (MalformedParametersException e) {
                System.out.println("Invalid email, press enter or digit something to continue");
                sc.nextLine();
            }
        }
        System.out.print("Username: ");
        newChef.setUsername(sc.nextLine());

        String password;
        String passwordConf;

        while (true) {
            System.out.print("Password: ");
            password = this.sc.nextLine();
            System.out.print("Confirm password: ");
            passwordConf = this.sc.nextLine();
            if (!password.equals(passwordConf)) {
                System.out.println("Password doesn't match, press enter or digit something to continue");
                sc.nextLine();
            } else break;
        }
        newChef.setPassword(password);

        System.out.println();
        System.out.println("Entered data:");
        System.out.println("Name: " + newChef.getName());
        System.out.println("Surname: " + newChef.getSurname());
        System.out.println("Birth Date: " + newChef.getBirthDate());
        System.out.println("Info: " + newChef.getInfo());
        System.out.println("Email: " + newChef.getEmail());
        System.out.println("Username: " + newChef.getUsername());
        System.out.println("Password: " + newChef.getPassword());
        System.out.println("Are you sure to proceed?");

        this.manageRegistrationAnswer(sc.nextLine(),newChef);

    }

    private void manageRegistrationAnswer(String answer, ChefBean newChef){
        if (answer.equals("y") || answer.equals("Y")) {
            ChefLoginControllerFactory factory = new ChefLoginControllerFactory();
            ChefLoginController chefLoginController = factory.createChefLoginController();
            if (chefLoginController.registerChef(newChef)) {
                ChefLoginView loginView = new ChefLoginView();
                loginView.display();
            } else {
                System.out.println("Unable to register, username or email already used, digit something to continue");
                sc.nextLine();
                this.display();
            }
        } else {
            ChefLoginView chefLoginView = new ChefLoginView();
            chefLoginView.display();
        }
    }

}

