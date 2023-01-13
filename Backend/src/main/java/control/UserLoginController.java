package control;

import beans.UserCredBean;

public interface UserLoginController {
    
    public boolean attemptUserLogin(UserCredBean credentials);
    public boolean registerUser(UserCredBean credentials);
    
}
