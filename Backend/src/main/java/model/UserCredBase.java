package model;

import java.io.Serializable;

public interface UserCredBase extends Serializable {

    String getUsername();
    void setUsername(String username);
    String getPassword();
    void setPassword(String password);
    boolean checkCredentials(String us, String pw);
    String getEmail();
    void setEmail(String email);
    void setRememberMe(boolean rememberMe);
    boolean getRememberMe();

}
