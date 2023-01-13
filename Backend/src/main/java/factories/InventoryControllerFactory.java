package factories;

import control.InventoryApplicativeController;
import control.InventoryController;

public class InventoryControllerFactory {

    public InventoryController createInventoryController(){
        return new InventoryApplicativeController();
    }

}
