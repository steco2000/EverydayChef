package model;

import java.io.IOException;
import java.io.Serializable;

public class UserCredentials implements UserCredBase, Serializable {

    private String username;
    private String password;
    private String email;
    private boolean rememberMe;
    private Inventory ingredientsInventory;

    public UserCredentials(String us, String pw, String email){
        this.username = us;
        this.password = pw;
        this.email = email;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
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

    public Inventory getIngredientsInventory() {
        return ingredientsInventory;
    }

    //TODO: controlla eccezione
    public void setIngredientsInventory(Inventory ingredientsInventory) {
        this.ingredientsInventory = ingredientsInventory;
        UserCredentialsDAO dao = new UserCredentialsDAO();
        try {
            dao.saveUser(this);
        } catch (IOException e) {
            return;
        }
    }

}
