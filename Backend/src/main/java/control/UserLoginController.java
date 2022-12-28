package control;

public interface UserLoginController {
    
    public boolean attemptUserLogin(UserCredBean credentials);
    public boolean registerUser(UserCredBean credentials);
    
}
