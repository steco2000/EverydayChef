package factories;

import model.Inventory;
import model.UserCredentials;

import java.io.IOException;

//factory adibita alla creazione di istanze di Inventory

public class InventoryFactory {

    public Inventory createInventory(UserCredentials user) throws IOException {
        return new Inventory(user);
    }

}
