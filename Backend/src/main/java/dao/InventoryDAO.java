package dao;

import control.LoginController;
import model.InventoryBase;
import model.UserCredentials;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class InventoryDAO {

    protected UserCredentials user;
    protected final String lastUsedDaoFileName;

    protected InventoryDAO(){
        this.user = (UserCredentials) LoginController.getUserLogged();
        Path relativeLastDAOFilePath = Paths.get("Backend\\src\\main\\resources\\last_inventoryDAO_flags\\last_inventoryDAO_flag_"+user.getUsername()+".ser");
        lastUsedDaoFileName = relativeLastDAOFilePath.toAbsolutePath().toString();
    }

    public abstract void saveInventory(InventoryBase inventory);
    public abstract InventoryBase retrieveInventory();
    protected abstract void makeDataConsistent() throws IOException, ClassNotFoundException;

    protected void writeLastUsed(boolean value) throws IOException {
        FileOutputStream fileout = new FileOutputStream(lastUsedDaoFileName);
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        out.writeObject(value);
        out.close();
        fileout.close();
    }

}
