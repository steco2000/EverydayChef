package model;

import java.io.Serializable;
import java.util.List;

public class Recipe implements RecipeBase, Serializable {

    private String name;
    private Chef chef;
    private String difficulty;
    private String preparationTime;
    private int servings;
    private List<RecipeIngredient> ingredientList;
    private String preparationProcedure;

    public Recipe(){
    }

    public Recipe(String name, ChefBase chef, String difficulty, String preparationTime, int servings, List<RecipeIngredient> ingredientList, String preparationProcedure){
        this.name = name;
        this.chef = (Chef) chef;
        this.difficulty = difficulty;
        this.preparationTime = preparationTime;
        this.servings = servings;
        this.ingredientList = ingredientList;
        this.preparationProcedure = preparationProcedure;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Chef getChef() {
        return chef;
    }

    @Override
    public void setChef(Chef chef) {
        this.chef = chef;
    }

    @Override
    public String getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String getPreparationTime() {
        return preparationTime;
    }

    @Override
    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    @Override
    public int getServings() {
        return servings;
    }

    @Override
    public void setServings(int servings) {
        this.servings = servings;
    }

    @Override
    public List<RecipeIngredient> getIngredientList() {
        return ingredientList;
    }

    @Override
    public void setIngredientList(List<RecipeIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    public String getPreparationProcedure() {
        return preparationProcedure;
    }

    @Override
    public void setPreparationProcedure(String preparationProcedure) {
        this.preparationProcedure = preparationProcedure;
    }
}
