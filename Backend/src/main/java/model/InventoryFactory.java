package model;

import control.LoginController;

public class InventoryFactory {

    public Inventory createInventory(UserCredentials user){
        return new Inventory(user);
    }

}
