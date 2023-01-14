package dao;

import control.LoginController;
import model.UserCredBase;
import model.UserCredentials;

import java.io.*;

//TODO: eccezioni

public class UserCredentialsDAO {

    private static final String USERS_FILE_NAME = "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\user_credentials_";

    public boolean credentialsAreCorrect(String username, String password) {
        UserCredentials currUser;
        FileInputStream filein = null;

        try {
            filein = new FileInputStream(USERS_FILE_NAME+username+".ser");
            while (true) {
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currUser = (UserCredentials) inputObjStream.readObject();
                if ((currUser.getPassword().equals(password)) && (currUser.getUsername().equals(username))) {
                    filein.close();
                    LoginController.setUserLogged(currUser);
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (EOFException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

        //TODO: controlla eccezione
    public void saveUser(UserCredBase user) throws IOException {
        FileOutputStream fileout = new FileOutputStream(USERS_FILE_NAME+user.getUsername()+".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        out.writeObject(user);
        out.close();
        fileout.close();
    }

    public boolean userNotExists(String email, String username){
        UserCredentials currUser;
        FileInputStream filein = null;

        try{
            filein = new FileInputStream(USERS_FILE_NAME+username+".ser");
            while(true){
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currUser = (UserCredentials) inputObjStream.readObject();
                if((currUser.getEmail().equals(email)) || (currUser.getUsername().equals(username))){
                    filein.close();
                    return false;
                }
            }
        }catch (EOFException e) {
            try {
                filein.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}