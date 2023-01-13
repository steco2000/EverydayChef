package factories;

import model.Ingredient;
import model.RecipeIngredient;

public class RecipeIngredientFactory {

    public RecipeIngredient createRecipeIngredient(String name, double quantity, String measureUnit){
        return new Ingredient(name,quantity,measureUnit);
    }

}
