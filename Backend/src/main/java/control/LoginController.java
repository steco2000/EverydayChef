package control;

import model.*;

import java.io.EOFException;
import java.io.FileNotFoundException;
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

    private int getChefId(ChefDAO dao) throws IOException, ClassNotFoundException {
        int id;
        try{
            id = dao.getLastId();
        }catch(EOFException | FileNotFoundException e){
            id = 0;
        }
        return id;
    }

    @Override
    public boolean registerChef(ChefBean chefInfo) {
        ChefDAO dao = new ChefDAO();
        try{
            if(dao.chefNotExists(chefInfo.getEmail(),chefInfo.getUsername())){
                int id = getChefId(dao);
                ChefFactory factory = new ChefFactory();
                Chef chef = (Chef) factory.createChef();
                chef.setId(id+1);
                chef.setName(chefInfo.getName());
                chef.setSurname(chefInfo.getSurname());
                chef.setBirthDate(chefInfo.getBirthDateInternalFormat());
                chef.setInfo(chefInfo.getInfo());
                chef.setUsername(chefInfo.getUsername());
                chef.setEmail(chefInfo.getEmail());
                chef.setPassword(chefInfo.getPassword());
                System.out.println("Register chef use case: username "+chef.getUsername()+", password "+chef.getPassword());
                dao.saveChef(chef);
                return true;
            }else{
                return false;
            }
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    //TODO: gestire remember me
    @Override
    public boolean attemptUserLogin(UserCredBean credentials) {
        UserCredentialsDAO dao = new UserCredentialsDAO();
        if(dao.credentialsAreCorrect(credentials.getUsername(),credentials.getPassword())){
            return true;
        }
        return false;
    }

    @Override
    public boolean registerUser(UserCredBean credentials) {
        //TODO: controlla eccezioni
        UserCredentialsDAO dao = new UserCredentialsDAO();
        try {
            if(dao.userNotExists(credentials.getEmail(),credentials.getUsername())) {
                UserCredentialsFactory factory = new UserCredentialsFactory();
                UserCredBase user = factory.createUserCredentials(credentials.getUsername(), credentials.getPassword(), credentials.getEmail());
                dao.saveUser(user);
                return true;
            }else return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
