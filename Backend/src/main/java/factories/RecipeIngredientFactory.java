package factories;

import model.Ingredient;
import model.RecipeIngredient;

//factory adibita alla creazione di istanze di Ingredient (legate a variabili di tipo RecipeIngredient)

public class RecipeIngredientFactory {

    public RecipeIngredient createRecipeIngredient(String name, double quantity, String measureUnit){
        return new Ingredient(name,quantity,measureUnit);
    }

}
