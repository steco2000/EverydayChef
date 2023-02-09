package model;

import dao.UserCredentialsDAO;

import java.io.IOException;
import java.io.Serializable;

//entità che incapsula i dati degli utenti

public class UserCredentials implements UserCredBase, Serializable {

    private String username;
    private String password;
    private String email;
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

    public Inventory getIngredientsInventory(){ return this.ingredientsInventory; }

    //quando si lega un inventario all'utente questo chiede al DAO di esssre salvato
    public void setIngredientsInventory(Inventory ingredientsInventory) {
        this.ingredientsInventory = ingredientsInventory;
        UserCredentialsDAO dao = new UserCredentialsDAO();
        try {
            dao.saveUser(this);
        } catch (IOException ignored) {
            assert(true); //eccezione ignorata, se l'utente esiste esiste anche il file
        }
    }

}
