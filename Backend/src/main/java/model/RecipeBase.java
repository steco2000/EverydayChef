package model;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface RecipeBase extends Serializable {

    String getName();
    void setName(String name);
    Chef getChef();
    void setChef(Chef chef);
    String getDifficulty();
    void setDifficulty(String difficulty);
    String getPreparationTime();
    void setPreparationTime(String preparationTime);
    int getServings();
    void setServings(int servings);
    List<RecipeIngredient> getIngredientList();
    void setIngredientList(List<RecipeIngredient> ingredientList);
    String getPreparationProcedure();
    void setPreparationProcedure(String preparationProcedure);

}
