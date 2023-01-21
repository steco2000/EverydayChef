package dao;

import control.LoginController;
import exceptions.ExistingRecipeException;
import model.Chef;
import model.Recipe;
import model.RecipeBase;
import model.RecipeSubject;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO extends RecipeSubject {

    private static Chef chef;
    private static List<Recipe> recipeList;
    private final String recipeFileName;

    public RecipeDAO(){
        Path relativeRecipeFilePath = Paths.get("Backend\\src\\main\\resources\\recipes\\recipes_");
        recipeFileName = relativeRecipeFilePath.toAbsolutePath().toString();
    }

    public RecipeDAO(String chefUsername){
        this();
        if(chef == null || !(chef.getUsername().equals(chefUsername))) setChef(chefUsername);
        try {
            updateState(chefUsername);
        } catch (IOException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    private static void setChef(String chefUsername) {
        ChefDAO chefDAO = new ChefDAO();
        chef = (Chef) chefDAO.retrieveChef(chefUsername);
    }

    private void updateState(String chefUsername) throws IOException {
        ChefDAO chefDao = new ChefDAO();
        Chef thisChef = (Chef) chefDao.retrieveChef(chefUsername);
        try {
            FileInputStream filein = new FileInputStream(this.recipeFileName+thisChef.getId()+".ser");
            ObjectInputStream inputStream = new ObjectInputStream(filein);
            recipeList = (ArrayList<Recipe>) inputStream.readObject();
        } catch (ClassNotFoundException ignored) {
            assert(true); //eccezione ignorata
        }catch(FileNotFoundException e){
            recipeList = new ArrayList<>();
        }
    }


    public void saveChanges() throws IOException {
        FileOutputStream fileout = new FileOutputStream(recipeFileName+chef.getId()+".ser");
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        ChefDAO chefDao = new ChefDAO();
        chefDao.saveChef(chef,true);

        this.notifyObservers();

        out.writeObject(recipeList);
        out.close();
        fileout.close();
    }

    public void saveRecipe(RecipeBase recipe) throws ExistingRecipeException {
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

    public void incrementRecipeViews(String recipe) {
        Recipe toIncrement = recipeList.stream().filter(o -> o.getName().equals(recipe)).findFirst().orElse(null);
        try {
            toIncrement.incrementViews();
            this.saveChanges();
            System.out.println(toIncrement.getName()+" "+toIncrement.getViews());
        } catch (IOException | NullPointerException ignored) {
            assert(true); //eccezione ignorata
        }
    }
}
