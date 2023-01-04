package model;

public class InventoryFactory {

    public Inventory createInventory(UserCredentials user){
        return new Inventory(user);
    }

}
