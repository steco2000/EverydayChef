package factories;

import dao.DBMSInventoryDAO;
import dao.FileSystemInventoryDAO;
import dao.InventoryDAO;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

//factory adibita alla creazione di istanze di InventoryDAO

public class InventoryDAOFactory {

    private static boolean daoSelected = false;
    private static boolean runtimeSelectedDA0 = true;
    private static Random rand;

    //nel costruttore viene stabilito in modo casuale se verranno creati dao dbms o file system, se "runtimeSelectedDAO" è true allora verranno creati solo dao file system, altrimenti dbms

    public InventoryDAOFactory(){
       if(!daoSelected) {
           try {
               decideDAOType();
           } catch (NoSuchAlgorithmException e) {
               randomExceptionThrown();
           }
       }
    }

    private static void randomExceptionThrown() {
        daoSelected = true; //se l'algoritmo per il random non è disponibile viene in automatico selezionato il dao file system
    }

    //Questo è il metodo che estrae il tipo di DAO da creare per la sessione corrente
    private static void decideDAOType() throws NoSuchAlgorithmException {

        //viene estratto un numero casuale tra 0 e 1
        if(rand == null){
            rand = SecureRandom.getInstanceStrong();
        }
        double decider = rand.nextDouble();

        //qui si approssima il valore estratto
        int randomValue;
        if(decider >= 0.5) randomValue = 1;
        else randomValue = 0;

        //qui in base al valore si sceglie il flag del tipo di DAO da istanziare
        if (randomValue != 0){
            runtimeSelectedDA0 = true;
        } else{
            runtimeSelectedDA0 = false;
        }

        daoSelected = true;
    }

    /*
    il metodo di creazione associerà alle variabili di tipo InventoryDAO l'istanza del tipo deciso in modo polimorfico
     */
    public InventoryDAO createInventoryDAO(){
        if(runtimeSelectedDA0){
            return new FileSystemInventoryDAO();
        }else{
            return new DBMSInventoryDAO();
        }
    }

}
