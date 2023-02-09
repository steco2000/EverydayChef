package dao;

import control.LoginController;
import model.InventoryBase;
import model.UserCredentials;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

//classe astratta che rappresenta il tipo InventoryDAO

public abstract class InventoryDAO {

    //questi sono gli unici due attributi comuni ai due tipi di dao
    protected UserCredentials user;
    protected final String lastUsedDaoFileName;

    /*
    Nel costruttore si calcola il percorso assoluto del file contenente il flag dell'utlimo dao ad aver scritto
     */
    protected InventoryDAO(){
        this.user = (UserCredentials) LoginController.getUserLogged();
        Path relativeLastDAOFilePath = Paths.get("Backend\\src\\main\\resources\\last_inventoryDAO_flags\\last_inventoryDAO_flag_"+user.getUsername()+".ser");
        lastUsedDaoFileName = relativeLastDAOFilePath.toAbsolutePath().toString();
    }

    //i metodi esposti e il metodo per la consistenza dei dati variano in base al tipo di dao, quindi sono astratti
    public abstract void saveInventory(InventoryBase inventory);
    public abstract InventoryBase retrieveInventory();
    protected abstract void makeDataConsistent() throws IOException, ClassNotFoundException;

    //Il metodo di scrittura del flag identificativo è comune
    protected void writeLastUsed(boolean value) throws IOException {
        FileOutputStream fileout = new FileOutputStream(lastUsedDaoFileName);
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        out.writeObject(value);
        out.close();
        fileout.close();
    }

}
