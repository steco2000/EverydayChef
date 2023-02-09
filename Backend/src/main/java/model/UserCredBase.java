package model;

import java.io.Serializable;

//interfaccia dell'entità UserCRedentials esposta allo strato di controllo

public interface UserCredBase extends Serializable {

    String getUsername();
    void setUsername(String username);
    String getPassword();
    void setPassword(String password);
    String getEmail();
    void setEmail(String email);

}
