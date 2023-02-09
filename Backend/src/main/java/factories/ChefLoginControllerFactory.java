package factories;

import control.ChefLoginController;
import control.LoginController;

//factory adibita alla creazione di istanze di LoginController legate a variabili di tipo ChefLoginController

public class ChefLoginControllerFactory {

    public ChefLoginController createChefLoginController(){
        return new LoginController();
    }

}
