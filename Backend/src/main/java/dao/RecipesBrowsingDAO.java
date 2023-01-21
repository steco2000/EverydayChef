package dao;

import model.RecipeBase;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    private List<RecipeBase> getSearchMatchings(String searchValue, int chefId){
        try{
            FileInputStream filein = new FileInputStream(recipeFileName+chefId+".ser");
            ObjectInputStream inputStream = new ObjectInputStream(filein);
            List<RecipeBase> currentRecipeList = (List<RecipeBase>) inputStream.readObject();
            List<RecipeBase> matchings = new ArrayList<>();
            matchings.addAll(currentRecipeList.stream().filter(o -> (o.getName().contains(searchValue) || searchValue.contains(o.getName()))).collect(Collectors.toList()));
            return matchings;
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public List<RecipeBase> getSearchResult(String searchValue){
        ChefDAO chefDAO = new ChefDAO();
        List<RecipeBase> searchResult = new ArrayList<>();
        try {
            int lastChefId = chefDAO.getLastId();
            for(int i=1; i<lastChefId+1; i++){
                List<RecipeBase> matchings = this.getSearchMatchings(searchValue,i);
                if(!matchings.isEmpty()) searchResult.addAll(matchings);
            }
        } catch (IOException | ClassNotFoundException ignored) {
            assert(true);   //eccezione ignorata
        }
        return searchResult;
    }

}
