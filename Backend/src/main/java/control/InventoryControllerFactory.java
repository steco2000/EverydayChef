package control;

public class InventoryControllerFactory {

    public InventoryController createInventoryController(){
        return new InventoryApplicativeController();
    }

}
