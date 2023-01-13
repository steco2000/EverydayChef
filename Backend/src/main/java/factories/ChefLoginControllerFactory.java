package factories;

import control.ChefLoginController;
import control.LoginController;

public class ChefLoginControllerFactory {

    public ChefLoginController createChefLoginController(){
        return new LoginController();
    }

}
