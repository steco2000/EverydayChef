package control;

import beans.UserCredBean;

//Interfaccia del controller applicativo "LoginController" esposta alla UI dell'utente base

public interface UserLoginController {
    
    boolean attemptUserLogin(UserCredBean credentials);
    boolean registerUser(UserCredBean credentials);
    
}
