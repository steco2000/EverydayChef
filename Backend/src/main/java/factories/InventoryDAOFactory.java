package factories;

import dao.DBMSInventoryDAO;
import dao.FileSystemInventoryDAO;
import dao.InventoryDAO;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

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

    private static void decideDAOType() throws NoSuchAlgorithmException {
        if(rand == null){
            rand = SecureRandom.getInstanceStrong();
        }
        double decider = rand.nextDouble();

        int randomValue;
        if(decider >= 0.5) randomValue = 1;
        else randomValue = 0;

        if (randomValue != 0){
            runtimeSelectedDA0 = true;
            System.out.println("File system InventoryDAO started");
        } else{
            runtimeSelectedDA0 = false;
            System.out.println("DBMS InventoryDAO started");
        }

        daoSelected = true;
    }

    public InventoryDAO createInventoryDAO(){
        if(runtimeSelectedDA0) return new FileSystemInventoryDAO();
        else return new DBMSInventoryDAO();
    }

}
