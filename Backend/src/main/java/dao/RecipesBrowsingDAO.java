package dao;

import model.RecipeBase;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RecipesBrowsingDAO {

    private final String recipeFileName;

    public RecipesBrowsingDAO(){
        Path relativeRecipeFilePath = Paths.get("Backend\\src\\main\\resources\\recipes\\recipes_");
        recipeFileName = relativeRecipeFilePath.toAbsolutePath().toString();
    }

    public List<RecipeBase> getRecipeList(int chefId) {
        try {
            FileInputStream filein = new FileInputStream(recipeFileName+chefId+".ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(filein);
            return (List<RecipeBase>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public RecipeBase getRecipeInfo(String recipeName, String chefUsername){
        ChefDAO chefDAO = new ChefDAO();
        int chefId = chefDAO.retrieveChefId(chefUsername);
        List<RecipeBase> recipeList = this.getRecipeList(chefId);
        for(RecipeBase r: recipeList){
            if(r.getName().equals(recipeName)) return r;
        }
        return null;
    }

    public List<RecipeBase> getSearchResult(String searchValue){
        ChefDAO chefDAO = new ChefDAO();
        List<RecipeBase> currentRecipeList;
        List<RecipeBase> searchResult = new ArrayList<>();
        try {
            int lastChefId = chefDAO.getLastId();
            for(int i=1; i<lastChefId+1; i++){
                try{
                    FileInputStream filein = new FileInputStream(recipeFileName+i+".ser");
                    ObjectInputStream inputStream = new ObjectInputStream(filein);
                    currentRecipeList = (List<RecipeBase>) inputStream.readObject();
                    List<RecipeBase> matchings = new ArrayList<>();
                    matchings.addAll(currentRecipeList.stream().filter(o -> (o.getName().contains(searchValue) || searchResult.contains(o.getName()))).collect(Collectors.toList()));
                    if(!matchings.isEmpty()) searchResult.addAll(matchings);
                } catch (IOException e) {
                    continue;
                }
            }
        } catch (IOException | ClassNotFoundException ignored) {
            System.out.println("Catch 2");
            assert(true);
        }
        return searchResult;
    }

}
