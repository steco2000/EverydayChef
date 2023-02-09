package factories;

import control.LoginController;
import control.UserLoginController;

//factory adibita alla creazione di istanze di LoginController

public class UserLoginControllerFactory {

    public UserLoginController createUserLoginController(){
        return new LoginController();
    }

}
