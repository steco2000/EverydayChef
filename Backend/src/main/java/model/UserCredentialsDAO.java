package model;

import control.LoginController;

import java.io.*;

public class UserCredentialsDAO {

    private static final String USERS_FILE_NAME = "C:\\Users\\darkd\\OneDrive\\Desktop\\ispw\\progetto\\implementazione\\EverydayChef\\ECBackEnd\\src\\main\\resources\\user_credentials.ser";

    //TODO: questo metodo è molto simile a userNotExists, vedi se puoi fare riuso di codice
    public boolean credentialsAreCorrect(String username, String password) {
        UserCredentials currUser;
        FileInputStream filein = null;

        try {
            filein = new FileInputStream(USERS_FILE_NAME);
            while (true) {
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currUser = (UserCredentials) inputObjStream.readObject();
                System.out.println("Current email: " + currUser.getEmail() + ", current username: " + currUser.getUsername() + ", current pw: " + currUser.getPassword());
                if ((currUser.getPassword().equals(password)) && (currUser.getUsername().equals(username))) {
                    filein.close();
                    LoginController.userLogged = currUser;
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
    public void saveUser(UserCredBase user) throws IOException, ClassNotFoundException {
        FileOutputStream fileout = new FileOutputStream(USERS_FILE_NAME, true);
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        out.writeObject(user);
        out.close();
        fileout.close();
    }

    public boolean userNotExists(String email, String username){
        UserCredentials currUser;
        FileInputStream filein = null;

        try{
            filein = new FileInputStream(USERS_FILE_NAME);
            while(true){
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currUser = (UserCredentials) inputObjStream.readObject();
                System.out.println("Current email: "+currUser.getEmail()+", current username: "+currUser.getUsername()+", current pw: "+currUser.getPassword());
                if((currUser.getEmail().equals(email)) || (currUser.getUsername().equals(username))){
                    filein.close();
                    return false;
                }
            }
        }catch (EOFException e) {
            System.out.println("End of file");
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
