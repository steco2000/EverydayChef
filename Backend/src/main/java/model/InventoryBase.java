package model;

import java.io.Serializable;
import java.util.List;

public interface InventoryBase extends Serializable {

    void addIngredient(IngredientBase ingredient);
    void removeIngredient(String ingredient);
    IngredientBase getIngredient(String name);
    List<IngredientBase> getState();

}
