package model;

import control.InventoryTableDataBean;
import control.LoginController;

import java.io.*;

public class InventoryDAO {

    private static String INVENTORY_FILE_NAME = null;

    public InventoryDAO(){
        INVENTORY_FILE_NAME = "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\"+LoginController.userLogged.getUsername()+"-inventory.ser";
    }

    public void saveInventory(InventoryBase inventory) throws IOException {
        FileOutputStream fileout = new FileOutputStream(INVENTORY_FILE_NAME);
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        out.writeObject(inventory);
        out.close();
        fileout.close();
    }

    public InventoryBase retrieveInventory(){
        Inventory currInv;
        FileInputStream filein = null;
        String username = LoginController.userLogged.getUsername();

        try{
            filein = new FileInputStream(INVENTORY_FILE_NAME);
            while(true){
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currInv = (Inventory) inputObjStream.readObject();
                if(username.equals(currInv.getUser().getUsername())) break;
            }
        }catch (EOFException e) {
            try {
                filein.close();
                InventoryFactory inventoryFactory = new InventoryFactory();
                currInv = inventoryFactory.createInventory((UserCredentials) LoginController.userLogged);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            InventoryFactory inventoryFactory = new InventoryFactory();
            currInv = inventoryFactory.createInventory((UserCredentials) LoginController.userLogged);
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
