package control;

import beans.IngredientBean;

//interfaccia del controller applicativo "InventoryApplicativeController" esposta alla UI

public interface InventoryController {

    boolean addIngredient(IngredientBean ingredient);
    void removeIngredient(IngredientBean ingredient);
    boolean updateIngredient(String toUpdate, IngredientBean updates);
    void saveCurrentInventory();

}
