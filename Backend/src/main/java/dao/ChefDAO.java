package dao;

import control.LoginController;
import model.Chef;
import model.ChefBase;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

//dao che gestisce salvataggio e recupero dallo strato di persistenza di istanze di tipo chef

public class ChefDAO {

    private final String chefFileName;
    private final String lastIdFileName;

    /*
    Nel costruttore vengono calcolati i percorsi assoluti dei file ".ser" coinvolti. I file "chef_data_*chef_id*.ser" contengono le istanze di chef. Il file "last_chef_id.ser"
    contiene l'id più alto assegnato ad una istanza, da controllare per successive allocazioni
     */
    public ChefDAO(){
        Path relativeChefFilePath = Paths.get("Backend\\src\\main\\resources\\chef_data\\chef_data_");
        chefFileName = relativeChefFilePath.toAbsolutePath().toString();
        Path relativeLastIdFilePath = Paths.get("Backend\\src\\main\\resources\\chef_data\\last_chef_id.ser");
        lastIdFileName = relativeLastIdFilePath.toAbsolutePath().toString();
    }

    //questo metodo recupera il valore dell'ultimo id per l'assegnazione alle nuove istanze
    public int getLastId() throws IOException, ClassNotFoundException {
        FileInputStream filein = new FileInputStream(lastIdFileName);
        ObjectInputStream in = new ObjectInputStream(filein);

        return (int) in.readObject();
    }

    /*
    Questo metodo controlla che uno chef non esista al momento della registrazione. Vengono aperti e letti tutti i file contententi le istanze, e controllato che il valore di username
    e email non sia già stato usato.
     */
    public boolean chefNotExists(String email, String username){
        Chef currChef;
        FileInputStream filein;
        ObjectInputStream objStream;

        File folder = new File(Paths.get("Backend\\src\\main\\resources\\chef_data\\").toAbsolutePath().toString());
        File[] listOfFiles = folder.listFiles();

        try{
            for(File f: listOfFiles){

                //i .placeholder sono file senza un significato logico, presenti solo per marcare le directory e permettere che vengano incluse in github
                if(f.getName().equals(".placeholder") || f.getName().equals("last_chef_id.ser")) continue;
                filein = new FileInputStream(Paths.get("Backend\\src\\main\\resources\\chef_data\\"+f.getName()).toAbsolutePath().toString());
                objStream = new ObjectInputStream(filein);
                currChef = (Chef) objStream.readObject();
                if((currChef.getEmail().equals(email)) || (currChef.getUsername().equals(username))){
                    filein.close();
                    return false;
                }
            }
            return true;
        } catch(ClassNotFoundException | IOException e){
            return false;   //se non viene trovato alcun file significa che lo chef non esiste e può essere registrato
        }
    }

    /*
    Questo metodo gestisce il salvataggio su file di istanze di tipo chef. Se lo chef viene sovrascritto, ad esempio quando si aggiungono ad esso delle ricette, non va aggiornato il
    file dell'ultimo id, dato che non è un nuovo chef.
     */
    public void saveChef(ChefBase chef, boolean overwrite) throws IOException {
        FileOutputStream fileout = new FileOutputStream(chefFileName+chef.getUsername()+".ser", true);
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        if(!overwrite) {
            FileOutputStream fileoutID = new FileOutputStream(lastIdFileName);
            ObjectOutputStream outID = new ObjectOutputStream(fileoutID);

            outID.writeObject(chef.getId());
            outID.close();
            fileoutID.close();
        }

        out.writeObject(chef);
        out.close();
        fileout.close();
    }

    /*
    Questo metodo cerca una corrispondenza tra le credenziali inserite dall'utente e quelle salvate in memoria, per l'autenticazione al momento del login. Dato che due chef o utenti
    non possono avere lo stesso username basta controllare la coppia username e password
    */
    public boolean credentialsAreCorrect(String username, String password) {
        Chef currChef;
        FileInputStream filein = null;

        try {
            filein = new FileInputStream(chefFileName + username + ".ser");
            ObjectInputStream inputObjStream = new ObjectInputStream(filein);
            currChef = (Chef) inputObjStream.readObject();
            if ((currChef.getPassword().equals(password)) && (currChef.getUsername().equals(username))) {
                LoginController.setChefLogged(currChef);
                filein.close();
                return true;
            }
            return false;
        } catch (ClassNotFoundException | IOException e) {
            return false;
        }
    }


    //questo metodo recupera dalla memoria una istanza di chef. Come identificativo si usa l'username essendo univoco nel sistema
    public ChefBase retrieveChef(String chefUsername) {
        FileInputStream filein;
        try {
            filein = new FileInputStream(chefFileName + chefUsername + ".ser");
            ObjectInputStream inputObjStream = new ObjectInputStream(filein);
            return (ChefBase) inputObjStream.readObject();
        } catch (ClassNotFoundException | IOException ignored) {
            return null;
        }
    }

    //questo metodo recupera l'id di uno chef a partire dall'username. Serve ad esempio ad accedere al file delle ricette da lui salvate
    public int retrieveChefId(String chefUsername){
        Chef chef = (Chef) this.retrieveChef(chefUsername);
        return chef.getId();
    }

}
