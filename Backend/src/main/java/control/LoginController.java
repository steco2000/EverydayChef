package control;

import beans.ChefBean;
import beans.UserCredBean;
import dao.ChefDAO;
import dao.UserCredentialsDAO;
import factories.ChefFactory;
import factories.UserCredentialsFactory;
import model.*;

import java.io.IOException;

public class LoginController implements UserLoginController, ChefLoginController{

    private static ChefBase chefLogged = null;
    private static UserCredBase userLogged = null;

    public static UserCredBase getUserLogged(){ return userLogged; }
    public static void setUserLogged(UserCredBase user){ userLogged = user; }

    public static ChefBase getChefLogged(){ return chefLogged; }
    public static void setChefLogged(ChefBase chef){ chefLogged = chef; }

    @Override
    public boolean attemptChefLogin(ChefBean credentials) {
        ChefDAO dao = new ChefDAO();
        return dao.credentialsAreCorrect(credentials.getUsername(),credentials.getPassword());
    }

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
                dao.saveChef(chef);
                return true;
            }else{
                return false;
            }
        }catch(IOException e){
            return false;
        }
    }

    //TODO: gestire remember me
    @Override
    public boolean attemptUserLogin(UserCredBean credentials) {
        UserCredentialsDAO dao = new UserCredentialsDAO();
        return dao.credentialsAreCorrect(credentials.getUsername(),credentials.getPassword());
    }

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
