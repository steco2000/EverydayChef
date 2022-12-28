package control;

public class UserLoginControllerFactory {

    public UserLoginController createUserLoginController(){
        return new LoginController();
    }

}
