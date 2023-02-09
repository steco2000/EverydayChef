package factories;

import model.*;

import java.util.List;

//factory adibita alla creazione di istanze di Recipe

public class RecipeFactory {

    public RecipeBase createRecipe(String name, ChefBase chef, String difficulty, String preparationTime, int servings, List<RecipeIngredient> ingredientList, String preparationProcedure){
        return new Recipe(name, chef, difficulty, preparationTime, servings, ingredientList, preparationProcedure);
    }

}
