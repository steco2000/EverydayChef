package factories;

import control.LoginController;
import control.UserLoginController;

public class UserLoginControllerFactory {

    public UserLoginController createUserLoginController(){
        return new LoginController();
    }

}
