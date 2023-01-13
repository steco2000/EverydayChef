package control;

import beans.IngredientBean;

public interface InventoryController {

    boolean addIngredient(IngredientBean ingredient);
    void removeIngredient(IngredientBean ingredient);
    boolean updateIngredient(String toUpdate, IngredientBean updates);
    void saveCurrentInventory();

}
