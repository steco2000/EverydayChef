package factories;

import dao.DBMSInventoryDAO;
import dao.FileSystemInventoryDAO;
import dao.InventoryDAO;

import java.util.Random;

public class InventoryDAOFactory {

    private static boolean daoSelected = false;
    private static boolean runtimeSelectedDA0;

    //nel costruttore viene stabilito in modo casuale se verranno creati dao dbms o file system, se "runtimeSelectedDAO" è true allora verranno creati solo dao file system, altrimenti dbms

    public InventoryDAOFactory(){
        if(!daoSelected){
            Random rand = new Random();
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
    }

    public InventoryDAO createInventoryDAO(){
        if(runtimeSelectedDA0) return new FileSystemInventoryDAO();
        else return new DBMSInventoryDAO();
    }

}
