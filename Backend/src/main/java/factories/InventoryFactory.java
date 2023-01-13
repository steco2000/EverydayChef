package factories;

import model.Inventory;
import model.UserCredentials;

public class InventoryFactory {

    public Inventory createInventory(UserCredentials user){
        return new Inventory(user);
    }

}
