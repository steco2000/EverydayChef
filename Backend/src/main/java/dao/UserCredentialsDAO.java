package dao;

import control.LoginController;
import model.UserCredBase;
import model.UserCredentials;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserCredentialsDAO {

    private final String usersFileName;

    public UserCredentialsDAO(){
        Path relativeUsersFilePath = Paths.get("Backend\\src\\main\\resources\\user_credentials_");
        usersFileName = relativeUsersFilePath.toAbsolutePath().toString();
    }

    public boolean credentialsAreCorrect(String username, String password) {
        UserCredentials currUser;
        FileInputStream filein;

        try {
            filein = new FileInputStream(usersFileName+username+".ser");
            while (true) {
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currUser = (UserCredentials) inputObjStream.readObject();
                if ((currUser.getPassword().equals(password)) && (currUser.getUsername().equals(username))) {
                    filein.close();
                    LoginController.setUserLogged(currUser);
                    return true;
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            return false;
        }
    }

    public void saveUser(UserCredBase user) throws IOException {
        FileOutputStream fileout = new FileOutputStream(usersFileName+user.getUsername()+".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        out.writeObject(user);
        out.close();
        fileout.close();
    }

    public boolean userNotExists(String email, String username){
        UserCredentials currUser;
        FileInputStream filein = null;

        try{
            filein = new FileInputStream(usersFileName+username+".ser");
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
                return false;
            }
            return true;
        } catch (FileNotFoundException e) {
            return true;
        } catch(ClassNotFoundException | IOException e){
            return false;
        }
    }

}
