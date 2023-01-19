package beans;

import java.text.ParseException;
import java.util.List;

public class RecipeBean {

    private String name;
    private String chefUsername;
    private String difficulty;
    private String preparationTime;
    private int servings;
    private List<RecipeIngredientBean> ingredientList;
    private String preparationProcedure;

    public String getName() {return name; }

    public void setName(String name) throws IllegalArgumentException{
        if(name == null || name.length() == 0) throw new IllegalArgumentException();
        this.name = name;
    }

    public String getChefUsername() {
        return chefUsername;
    }

    public void setChefUsername(String chefUsername) {
        this.chefUsername = chefUsername;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(String servings) throws NumberFormatException {
        this.servings = Integer.parseInt(servings);
    }

    public List<RecipeIngredientBean> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<RecipeIngredientBean> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public String getPreparationProcedure() {
        return preparationProcedure;
    }

    public void setPreparationProcedure(String preparationProcedure) {
        this.preparationProcedure = preparationProcedure;
    }
}
