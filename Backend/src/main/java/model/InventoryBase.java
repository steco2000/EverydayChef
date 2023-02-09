package model;

import java.io.Serializable;
import java.util.List;

//interfaccia dell'entità inventario esposta allo strato di controllo

public interface InventoryBase extends Serializable {

    void addIngredient(IngredientBase ingredient);
    void removeIngredient(String ingredient);
    IngredientBase getIngredient(String name);
    List<IngredientBase> getState();

}
