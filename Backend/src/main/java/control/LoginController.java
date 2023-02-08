package control;

import beans.ChefBean;
import beans.UserCredBean;
import dao.ChefDAO;
import dao.UserCredentialsDAO;
import factories.ChefFactory;
import factories.UserCredentialsFactory;
import model.*;

import java.io.IOException;

/*
Controller applicativo che gestisce login e registrazione di entrambi gli utenti. Sia il riferimento all'utente loggato che quello dello chef sono salvati in attributi di classe
di questo controller.
 */

public class LoginController implements UserLoginController, ChefLoginController{

    private static ChefBase chefLogged = null;
    private static UserCredBase userLogged = null;

    //metodi per salvare/reperire i riferimenti agli utenti loggati
    public static UserCredBase getUserLogged(){ return userLogged; }
    public static void setUserLogged(UserCredBase user){ userLogged = user; }

    public static ChefBase getChefLogged(){ return chefLogged; }
    public static void setChefLogged(ChefBase chef){ chefLogged = chef; }

    //metodo che gestisce il tentativo di login dello chef. In sostanza consiste in una query al DAO per il check delle credenziali inserite
    @Override
    public boolean attemptChefLogin(ChefBean credentials) {
        ChefDAO dao = new ChefDAO();
        return dao.credentialsAreCorrect(credentials.getUsername(),credentials.getPassword());
    }

    /*
    Registrazione dello chef. Viene prima controllato se esiste già uno chef con gli identificativi inseriti. Se no si procede alla registrazione
     */
    @Override
    public boolean registerChef(ChefBean chefInfo) {
        ChefDAO dao = new ChefDAO();
        try{
            if(dao.chefNotExists(chefInfo.getEmail(),chefInfo.getUsername())){
                ChefFactory factory = new ChefFactory();
                ChefBase chef = factory.createChef();
                chef.setName(chefInfo.getName());
                chef.setSurname(chefInfo.getSurname());
                chef.setBirthDate(chefInfo.getBirthDateInternalFormat());
                chef.setInfo(chefInfo.getInfo());
                chef.setUsername(chefInfo.getUsername());
                chef.setEmail(chefInfo.getEmail());
                chef.setPassword(chefInfo.getPassword());
                dao.saveChef(chef, false);
                return true;
            }else{
                return false;
            }
        }catch(IOException e){
            return false;
        }
    }

    //tentativo di login per utente, analogo a quello dello chef
    @Override
    public boolean attemptUserLogin(UserCredBean credentials) {
        UserCredentialsDAO dao = new UserCredentialsDAO();
        return dao.credentialsAreCorrect(credentials.getUsername(),credentials.getPassword());
    }

    //registrazione utente, analoga allo chef
    @Override
    public boolean registerUser(UserCredBean credentials) {
        UserCredentialsDAO dao = new UserCredentialsDAO();
        try {
            if(dao.userNotExists(credentials.getEmail(),credentials.getUsername())) {
                UserCredentialsFactory factory = new UserCredentialsFactory();
                UserCredBase user = factory.createUserCredentials(credentials.getUsername(), credentials.getPassword(), credentials.getEmail());
                dao.saveUser(user);
                return true;
            }else return false;
        } catch (IOException e) {
            return false;
        }
    }
}
