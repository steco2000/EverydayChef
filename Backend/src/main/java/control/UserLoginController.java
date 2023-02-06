package control;

import beans.UserCredBean;

public interface UserLoginController {
    
    boolean attemptUserLogin(UserCredBean credentials);
    boolean registerUser(UserCredBean credentials);
    
}
