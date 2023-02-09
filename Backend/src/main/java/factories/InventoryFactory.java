package factories;

import model.Inventory;
import model.UserCredentials;

//factory adibita alla creazione di istanze di Inventory

public class InventoryFactory {

    public Inventory createInventory(UserCredentials user){
        return new Inventory(user);
    }

}
