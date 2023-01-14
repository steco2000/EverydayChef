package factories;

import model.InventoryIngredient;
import model.IngredientBase;

import java.util.Date;

public class IngredientFactory {

    public IngredientBase createIngredient(String name, double quantity, String measureUnit, Date expirationDate, String notes){
        return new InventoryIngredient(name,quantity,measureUnit,expirationDate,notes);
    }

}