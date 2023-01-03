package model;

import java.util.List;

public interface InventoryBase {

    void addIngredient(IngredientBase ingredient);
    void removeIngredient(String ingredient);
    IngredientBase getIngredient(String name);
    List<IngredientBase> getState();

}
