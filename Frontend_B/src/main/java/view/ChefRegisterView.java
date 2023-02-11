package view;

import beans.ChefBean;
import code_reuse.InputReusableUtilities;
import control.ChefLoginController;
import exceptions.PersistentDataAccessException;
import factories.ChefLoginControllerFactory;

import java.lang.reflect.MalformedParametersException;
import java.text.ParseException;
import java.util.Scanner;

//view che gestisce la schermata di registrazione dello chef

public class ChefRegisterView {

    private Scanner sc;

    public ChefRegisterView() {
        this.sc = new Scanner(System.in);
    }

    //display della schermata e raccolta dati per la registrazione
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
        System.out.println("Are you sure to proceed? (y/n)");

        this.manageRegistrationAnswer(newChef);

    }

    //metodo che gestisce la risposta alla conferma di registrazione e la chiamata al metodo del controller
    private void manageRegistrationAnswer(ChefBean newChef){
        try{
            if (InputReusableUtilities.yes(this.sc)) {
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
        }catch(PersistentDataAccessException e){
            System.out.println("Error: "+e.getMessage()+" Press enter to continue");
            this.sc.nextLine();
        }
    }

}

