package dao;

import beans.InventoryTableDataBean;
import control.LoginController;
import factories.InventoryFactory;
import model.Inventory;
import model.InventoryBase;
import model.UserCredentials;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemInventoryDAO extends InventoryDAO{

    private final String inventoryFileName;

    public FileSystemInventoryDAO(){
        super();
        Path relativeInventoryFilePath = Paths.get("Backend\\src\\main\\resources\\"+LoginController.getUserLogged().getUsername()+"-inventory.ser");
        inventoryFileName = relativeInventoryFilePath.toAbsolutePath().toString();
        try {
            this.makeDataConsistent();
        } catch (IOException | ClassNotFoundException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    @Override
    public void saveInventory(InventoryBase inventory) {
        try {
            FileOutputStream fileout = new FileOutputStream(inventoryFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileout);

            this.user.setIngredientsInventory((Inventory) inventory);

            out.writeObject(inventory);
            out.close();
            fileout.close();
        } catch (IOException e) {
            assert(true); //non salvo nulla
        }

        try {
            this.writeLastUsed(true);
        } catch (IOException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    @Override
    public InventoryBase retrieveInventory(){
        Inventory currInv;
        FileInputStream filein = null;

        try{
            filein = new FileInputStream(inventoryFileName);
            ObjectInputStream inputObjStream = new ObjectInputStream(filein);
            currInv = (Inventory) inputObjStream.readObject();
            inputObjStream.close();
            filein.close();
        }catch (FileNotFoundException e) {
            InventoryFactory inventoryFactory = new InventoryFactory();
            currInv = inventoryFactory.createInventory((UserCredentials) LoginController.getUserLogged());
        } catch(ClassNotFoundException | IOException e){
            return null;
        }
        InventoryTableDataBean observer = InventoryTableDataBean.getSingletonInstance();
        currInv.attach(observer);
        observer.setSubject(currInv);
        return currInv;
    }

    @Override
    protected void makeDataConsistent() throws IOException, ClassNotFoundException {
        try {
            FileInputStream filein = new FileInputStream(this.lastUsedDaoFileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(filein);
            boolean lastWriter = (boolean) objectInputStream.readObject();

            //se l'ultimo dao ad aver scritto era di tipo file system, come quello corrente, non faccio nulla

            if (!lastWriter) {
                //qui al contrario devo copiare i dati dal db
                DBMSInventoryDAO dbmsDao = new DBMSInventoryDAO();
                this.saveInventory(dbmsDao.retrieveInventory());
            }
        }catch (FileNotFoundException e){
            assert(true); //sel il file non c'è non devo fare nulla
        }
    }

}
