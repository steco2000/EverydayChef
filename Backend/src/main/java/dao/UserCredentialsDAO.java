package dao;

import control.LoginController;
import model.UserCredBase;
import model.UserCredentials;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

//dao che gestisce il salvataggio in persistenza di istanze di dipo UserCredentials

public class UserCredentialsDAO {

    private final String usersFileName;

    //nel costruttore si calcola il percorso assoluto dei file che contengono le istanze. Questi file sono del tipo "user_credentials_*username*.ser"
    public UserCredentialsDAO(){
        Path relativeUsersFilePath = Paths.get("Backend\\src\\main\\resources\\user_credentials\\user_credentials_");
        usersFileName = relativeUsersFilePath.toAbsolutePath().toString();
    }

    /*
    Questo metodo cerca una corrispondenza tra le credenziali inserite dall'utente e quelle salvate in memoria, per l'autenticazione al momento del login. Dato che due chef o utenti
    non possono avere lo stesso username basta controllare la coppia username e password
    */
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

    //metodo che scrive sul file un'istanza di utente al momento della registrazione
    public void saveUser(UserCredBase user) throws IOException {
        FileOutputStream fileout = new FileOutputStream(usersFileName+user.getUsername()+".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        out.writeObject(user);
        out.close();
        fileout.close();
    }

    /*
    Questo metodo controlla che un utente non esista al momento della registrazione. Vengono aperti e letti tutti i file contententi le istanze, e controllato che il valore di username
    e email non sia già stato usato.
     */
    public boolean userNotExists(String email, String username){
        UserCredentials currUser;
        FileInputStream filein;
        ObjectInputStream objStream;

        File folder = new File(Paths.get("Backend\\src\\main\\resources\\user_credentials\\").toAbsolutePath().toString());
        File[] listOfFiles = folder.listFiles();

        try{
            for(File f: listOfFiles){

                //i .placeholder sono file senza un significato logico, presenti solo per marcare le directory e permettere che vengano incluse in github
                if(f.getName().equals(".placeholder")) continue;
                filein = new FileInputStream(Paths.get("Backend\\src\\main\\resources\\user_credentials\\"+f.getName()).toAbsolutePath().toString());
                objStream = new ObjectInputStream(filein);
                currUser = (UserCredentials) objStream.readObject();
                if((currUser.getEmail().equals(email)) || (currUser.getUsername().equals(username))){
                    filein.close();
                    return false;
                }
            }
            return true;
        } catch(ClassNotFoundException | IOException e){
            return false;
        }
    }

}
