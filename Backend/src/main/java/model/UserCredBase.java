package model;

import java.io.Serializable;

public interface UserCredBase extends Serializable {

    String getUsername();
    void setUsername(String username);
    String getPassword();
    void setPassword(String password);
    String getEmail();
    void setEmail(String email);

}
