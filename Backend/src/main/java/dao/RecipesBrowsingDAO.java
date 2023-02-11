package dao;

import model.RecipeBase;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//dao che gestisce tutte le operazioni in lettura sulle ricette

public class RecipesBrowsingDAO {

    private final String recipeFileName;

    /*
   nel costruttore di default si calcolano i percorsi assoluti dei file ".ser". Questi file sono del tipo "recipes_*chef_id*.ser".
    */
    public RecipesBrowsingDAO(){
        Path relativeRecipeFilePath = Paths.get("Backend\\src\\main\\resources\\recipes\\recipes_");
        recipeFileName = relativeRecipeFilePath.toAbsolutePath().toString();
    }

    //metodo che recupera la lista delle ricette di uno chef a partire dall'id. Serve a scorrere tutte le ricette caricate per chef
    public List<RecipeBase> getRecipeList(int chefId) {
        try {
            FileInputStream filein = new FileInputStream(recipeFileName+chefId+".ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(filein);
            return (List<RecipeBase>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    //metodo che recupera le informazioni di una ricetta a partire dagli identificativi univoci della stessa, nome e username dello chef
    public RecipeBase getRecipeInfo(String recipeName, String chefUsername) throws IOException {
        ChefDAO chefDAO = new ChefDAO();
        int chefId = chefDAO.retrieveChefId(chefUsername);
        List<RecipeBase> recipeList = this.getRecipeList(chefId);
        for(RecipeBase r: recipeList){
            if(r.getName().equals(recipeName)) return r;
        }
        return null;
    }

    /*
    Metodo che recupera, per chef, i risultati di una ricerca dell'utente. Il metodo richiede anche l'id dello chef perché la ricerca avviene lista per lista,
    dato che le ricette sono organizzate in liste associate ai singoli chef.
     */
    private List<RecipeBase> getSearchMatchings(String searchValue, int chefId) {
        try{
            FileInputStream filein = new FileInputStream(recipeFileName+chefId+".ser");
            ObjectInputStream inputStream = new ObjectInputStream(filein);
            List<RecipeBase> currentRecipeList = (List<RecipeBase>) inputStream.readObject();
            List<RecipeBase> matchings = new ArrayList<>();

            //una ricetta è considerata conforme alla ricerca se il proprio nome è contenuto nell'input dell'utente o viceversa
            matchings.addAll(currentRecipeList.stream().filter(o -> (o.getName().contains(searchValue) || searchValue.contains(o.getName()))).collect(Collectors.toList()));

            return matchings;
        } catch (ClassNotFoundException | IOException e) {
            return new ArrayList<>();
        }
    }

    //metodo che recupera i risultati di una ricerca dell'utente.
    public List<RecipeBase> getSearchResult(String searchValue)  {
        ChefDAO chefDAO = new ChefDAO();
        List<RecipeBase> searchResult = new ArrayList<>();
        try {
            int lastChefId = chefDAO.getLastId();
            for(int i=1; i<lastChefId+1; i++){
                List<RecipeBase> matchings = this.getSearchMatchings(searchValue,i);
                if(!matchings.isEmpty()) searchResult.addAll(matchings);
            }
        } catch (ClassNotFoundException | IOException ignored) {
            assert(true);   //eccezione ignorata
        }
        return searchResult;
    }

}
