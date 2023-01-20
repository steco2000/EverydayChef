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

public class InventoryDAO {

    private final String inventoryFileName;
    private final UserCredentials user;

    public InventoryDAO(){
        //Percorso file "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\"+LoginController.getUserLogged().getUsername()+"-inventory.ser";
        Path relativeInventoryFilePath = Paths.get("Backend\\src\\main\\resources");
        inventoryFileName = relativeInventoryFilePath.toAbsolutePath()+"\\"+LoginController.getUserLogged().getUsername()+"-inventory.ser";
        user = (UserCredentials) LoginController.getUserLogged();
    }

    public void saveInventory(InventoryBase inventory) throws IOException {
        FileOutputStream fileout = new FileOutputStream(inventoryFileName);
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        user.setIngredientsInventory((Inventory) inventory);

        out.writeObject(inventory);
        out.close();
        fileout.close();
    }

    public InventoryBase retrieveInventory(){
        Inventory currInv;
        FileInputStream filein = null;
        String username = user.getUsername();

        try{
            filein = new FileInputStream(inventoryFileName);
            while(true){
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currInv = (Inventory) inputObjStream.readObject();
                if(username.equals(currInv.getUser().getUsername())) break;
            }
        }catch (EOFException e) {
            try {
                filein.close();
                InventoryFactory inventoryFactory = new InventoryFactory();
                currInv = inventoryFactory.createInventory((UserCredentials) LoginController.getUserLogged());
            } catch (IOException ex) {
                return null;
            }
        } catch (FileNotFoundException e) {
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

}
