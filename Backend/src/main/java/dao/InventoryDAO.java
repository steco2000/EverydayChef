package dao;

import beans.InventoryTableDataBean;
import control.LoginController;
import factories.InventoryFactory;
import model.Inventory;
import model.InventoryBase;
import model.UserCredBase;
import model.UserCredentials;

import java.io.*;

public class InventoryDAO {

    private String inventoryFileName;
    private static UserCredentials user;

    public InventoryDAO(){
        inventoryFileName = "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\"+LoginController.getUserLogged().getUsername()+"-inventory.ser";
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
                ex.printStackTrace();
                return null;
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            InventoryFactory inventoryFactory = new InventoryFactory();
            currInv = inventoryFactory.createInventory((UserCredentials) LoginController.getUserLogged());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        InventoryTableDataBean observer = InventoryTableDataBean.getSingletonInstance();
        currInv.attach(observer);
        observer.setSubject(currInv);
        return currInv;
    }

}
