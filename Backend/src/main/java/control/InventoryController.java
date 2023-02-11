package control;

import beans.IngredientBean;
import exceptions.PersistentDataAccessException;

//interfaccia del controller applicativo "InventoryApplicativeController" esposta alla UI

public interface InventoryController {

    boolean addIngredient(IngredientBean ingredient);
    void removeIngredient(IngredientBean ingredient);
    boolean updateIngredient(String toUpdate, IngredientBean updates);
    void saveCurrentInventory() throws PersistentDataAccessException;

}
