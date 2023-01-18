package dao;

import model.RecipeBase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class RecipesBrowsingDAO {

    private static String RECIPE_FILE_NAME_PREFIX = "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\recipes\\recipes_";

    public List<RecipeBase> getRecipeList(int chefId) {
        try {
            FileInputStream filein = new FileInputStream(RECIPE_FILE_NAME_PREFIX+chefId+".ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(filein);
            return (List<RecipeBase>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

}