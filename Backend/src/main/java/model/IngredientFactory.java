package model;

import java.util.Date;

public class IngredientFactory {

    public IngredientBase createIngredient(String name, double quantity, String measureUnit, Date expirationDate, String notes){
        return new Ingredient(name,quantity,measureUnit,expirationDate,notes);
    }

}
