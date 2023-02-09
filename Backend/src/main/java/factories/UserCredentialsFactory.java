package factories;

import model.UserCredBase;
import model.UserCredentials;

//factory adibita alla creazione di istanze di UserCredentials

public class UserCredentialsFactory {

    public UserCredBase createUserCredentials(String username, String pw, String email){
        return new UserCredentials(username, pw, email);
    }

}
