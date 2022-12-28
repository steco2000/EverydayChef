package model;

public interface UserCredBase {

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
