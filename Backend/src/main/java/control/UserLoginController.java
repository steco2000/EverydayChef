package control;

import beans.UserCredBean;
import exceptions.PersistentDataAccessException;

//Interfaccia del controller applicativo "LoginController" esposta alla UI dell'utente base

public interface UserLoginController {
    
    boolean attemptUserLogin(UserCredBean credentials);
    boolean registerUser(UserCredBean credentials) throws PersistentDataAccessException;
    
}
