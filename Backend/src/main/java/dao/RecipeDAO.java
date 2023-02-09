package dao;

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

/*
dao che si occupa della scrittura in persistenza delle istanze di tipo ricetta. Ogni volta che uno chef intende gestire le proprie ricette, questo dao effettua a runtime un caching
delle stesse, per poi salvare i dati in persistenza quando lo chef decide di aver finito con le modifiche.
 */

public class RecipeDAO extends RecipeSubject {

    private static Chef chef;
    private static List<Recipe> recipeList;
    private final String recipeFileName;

    /*
    nel costruttore di default si calcolano i percorsi assoluti dei file ".ser". Questi file sono del tipo "recipes_*chef_id*.ser".
     */
    public RecipeDAO(){
        Path relativeRecipeFilePath = Paths.get("Backend\\src\\main\\resources\\recipes\\recipes_");
        recipeFileName = relativeRecipeFilePath.toAbsolutePath().toString();
    }

    /*
    Nel costruttore non di default si accetta l'username dello chef che si suppone essere attualmente loggato, per effettuare il caching delle ricette. Se lo chef non è presente ok,
    se è presente ma è diverso da quello salvato dobbiamo cambiarlo, perchè vuol dire che a runtime quello chef ha effettuato il logout e un altro si è autenticato. Dopodiché si effettua
    il caching delle ricette
     */
    public RecipeDAO(String chefUsername){
        this();
        if(chef == null || !(chef.getUsername().equals(chefUsername))) setChef(chefUsername);
        try {
            updateState(chefUsername, this.recipeFileName);
        } catch (IOException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    //questo metodo recupera i dati dello chef legato al dao
    private static void setChef(String chefUsername) {
        ChefDAO chefDAO = new ChefDAO();
        chef = (Chef) chefDAO.retrieveChef(chefUsername);
    }

    /*
    Questo metodo recupera tutte le ricette salvate dallo chef, effettuando il caching delle stesse.
     */
    private static void updateState(String chefUsername, String recipeFileName) throws IOException {
        ChefDAO chefDao = new ChefDAO();
        Chef thisChef = (Chef) chefDao.retrieveChef(chefUsername);
        try {
            FileInputStream filein = new FileInputStream(recipeFileName+thisChef.getId()+".ser");
            ObjectInputStream inputStream = new ObjectInputStream(filein);
            recipeList = (ArrayList<Recipe>) inputStream.readObject();
        } catch (ClassNotFoundException ignored) {
            assert(true); //eccezione ignorata
        }catch(FileNotFoundException e){
            recipeList = new ArrayList<>(); //se il file non esiste lo chef non ha salvato ancora ricette, si istanzia l'array vuoto
        }
    }


    /*
    Questo metodo salva in persistenza le modifiche effettuate dallo chef sulle ricette. Il RecipeDAO è osservato dal bean in pull model per l'interfaccia, quindi quando salva
    notifica gli observer.
     */
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

    //quando lo chef conferma una ricetta questa si aggiunge alla lista in cache. Anche qui si notificano gli observer. Se già esiste si lancia l'apposita eccezione
    public void saveRecipe(RecipeBase recipe) throws ExistingRecipeException {
        if(recipeAlreadyExists((Recipe) recipe)) throw new ExistingRecipeException();
        recipeList.add((Recipe) recipe);
        chef.addRecipe((Recipe) recipe);
        this.notifyObservers();
    }

    //quando invece una ricetta è eliminata si rimuove dalla lista e si notifica l'observer
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

    //questo metodo controlla se una ricetta esiste già nella lista
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

    //il metodo sfruttato dagli observer per ottenere i dati aggiornati
    public List<Recipe> getState() {
        return recipeList;
    }

    //metodo che serve a incrementare le visualizzazioni di una ricetta quando viene aperta. In questo caso salviamo subito dato che non ha senso attendere modifiche
    public void incrementRecipeViews(String recipe) {
        Recipe toIncrement = recipeList.stream().filter(o -> o.getName().equals(recipe)).findFirst().orElse(null);
        try {
            toIncrement.incrementViews();
            this.saveChanges();
        } catch (IOException | NullPointerException ignored) {
            assert(true); //eccezione ignorata, deve per forza esistere la ricetta, perché viene selezionata proprio dalla lista
        }
    }
}
