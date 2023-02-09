package factories;

import control.InventoryApplicativeController;
import control.InventoryController;

//factory adibita alla creazione di istanze di InventoryApplicativeController

public class InventoryControllerFactory {

    public InventoryController createInventoryController(){
        return new InventoryApplicativeController();
    }

}
