package model;

import java.io.Serializable;

public class UserCredentials implements UserCredBase, Serializable {

    private String username;
    private String password;
    private String email;
    private boolean rememberMe;

    public UserCredentials(String us, String pw, String email){
        this.username = us;
        this.password = pw;
        this.email = email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean checkCredentials(String us, String pw){
        if(this.username.equals(us) && this.password.equals(pw)) return true;
        return false;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public boolean getRememberMe() {
        return this.rememberMe;
    }

}
