package model;

import java.io.Serializable;
import java.util.Date;

public interface ChefBase extends Serializable {

    void setName(String name);
    String getName();
    void setSurname(String surname);
    String getSurname();
    void setBirthDate(Date birthDate);
    Date getBirthDate();
    void setUsername(String username);
    String getUsername();
    void setPassword(String password);
    String getPassword();
    void setEmail(String email);
    String getEmail();
    int getId();
    void setInfo(String info);
    String getInfo();

}
