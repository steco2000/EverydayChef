package dao;

import exceptions.ExistingRecipeException;
import model.Chef;
import model.Recipe;
import model.RecipeBase;
import model.RecipeSubject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO extends RecipeSubject {

    private static final String RECIPE_FILE_NAME = "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\recipes\\recipes_";
    private static Chef chef;
    private static List<Recipe> recipeList;

    public RecipeDAO(){
    }

    public RecipeDAO(String chefUsername){
        ChefDAO chefDAO = new ChefDAO();
        chef = (Chef) chefDAO.retrieveChef(chefUsername);
        try {
            this.updateState(chefUsername);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateState(String chefUsername) throws IOException {
        ChefDAO chefDao = new ChefDAO();
        Chef thisChef = (Chef) chefDao.retrieveChef(chefUsername);
        try {
            FileInputStream filein = new FileInputStream(RECIPE_FILE_NAME+thisChef.getId()+".ser");
            ObjectInputStream inputStream = new ObjectInputStream(filein);
            recipeList = (ArrayList<Recipe>) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            return;
        }catch(FileNotFoundException e){
            recipeList = new ArrayList<>();
        }
    }


    public void saveChanges() throws IOException {
        FileOutputStream fileout = new FileOutputStream(RECIPE_FILE_NAME+chef.getId()+".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        ChefDAO chefDao = new ChefDAO();
        chefDao.saveChef(chef);

        this.notifyObservers();

        out.writeObject(recipeList);
        out.close();
        fileout.close();
    }

    public void saveRecipe(RecipeBase recipe) throws IOException, ExistingRecipeException {
        if(recipeAlreadyExists((Recipe) recipe)) throw new ExistingRecipeException();
        recipeList.add((Recipe) recipe);
        chef.addRecipe((Recipe) recipe);
        this.notifyObservers();
    }

    public void deleteRecipe(String recipeName) {
        if(recipeList == null || recipeList.isEmpty()) return;
        Recipe toRemove = null;
        for(Recipe r: recipeList){
            if(r.getName().equals(recipeName)) toRemove = r;
        }
        chef.removeRecipe(toRemove);
        recipeList.remove(toRemove);
        this.notifyObservers();
    }

    private boolean recipeAlreadyExists(Recipe recipe) {
        try{
            for(Recipe r: recipeList){
                if(recipe.getName().equals(r.getName())) return true;
            }
        }catch(NullPointerException e){
            return false;
        }
        return false;
    }

    public List<Recipe> getState() {
        return recipeList;
    }

}
