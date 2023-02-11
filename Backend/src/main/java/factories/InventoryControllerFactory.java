package factories;

import control.InventoryApplicativeController;
import control.InventoryController;
import exceptions.PersistentDataAccessException;

//factory adibita alla creazione di istanze di InventoryApplicativeController

public class InventoryControllerFactory {

    public InventoryController createInventoryController() throws PersistentDataAccessException {
        return new InventoryApplicativeController();
    }

}
