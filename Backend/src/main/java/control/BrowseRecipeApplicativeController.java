package control;

import beans.RecipeBrowsingTableBean;
import dao.ChefDAO;
import dao.InventoryDAO;
import dao.RecipesBrowsingDAO;
import exceptions.PersistentDataAccessException;
import factories.InventoryDAOFactory;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
Controller applicativo per la gestione della navigazione tra le ricette consigliate
 */

public class BrowseRecipeApplicativeController implements BrowseRecipeController{

    /*
    Metodo che incapsula i dati delle ricette consigliate ottenute nei bean da restituire alla UI e da visualizzare nella tabella
     */
    private List<RecipeBrowsingTableBean> setUpSuggestedBeanList(List<RecipeBase> suggestedRecipes){
        List<RecipeBrowsingTableBean> beanList = new ArrayList<>();
        for(RecipeBase r: suggestedRecipes){
            RecipeBrowsingTableBean bean = new RecipeBrowsingTableBean();
            bean.setName(r.getName());
            bean.setChefCompleteName(r.getChef().getName()+" "+r.getChef().getSurname());
            bean.setChefUsername(r.getChef().getUsername());
            beanList.add(bean);
        }
        return beanList;
    }

    /*
    Questo è il metodo chiave del caso d'uso, controlla se una ricetta contiene l'ingrediente dell'inventario e da ciò restituisce true o false. La ricerca è fatta attraverso uno
    stream dell'array degli ingredienti, dove si cerca una corrispondenza per predicato. Si controlla se il nome dell'ingrediente dell'inventario contiene quello di uno della ricetta,
    e viceversa. Inoltre si escludono gli ingredienti a quantità just enough, per scelta progettuale. Si controlla anche che la ricetta non sia già stata selezionata
     */
    private boolean recipeContainsIngredient(RecipeBase rec, Ingredient ingr, List<RecipeBase> suggestedRecipes){
        return (rec.getIngredientList().stream().anyMatch(o -> ((o.getName().contains(ingr.getName()) || ingr.getName().contains(o.getName())) && o.getQuantity()!=-1)) && !suggestedRecipes.contains(rec));
    }

    /*
    Metodo esposto alla UI per recuperare le ricette consigliate in base all'inventario dell'utente loggato
     */
    @Override
    public List<RecipeBrowsingTableBean> retrieveSuggestedRecipe() throws PersistentDataAccessException {
        try {
            //recupero inventario utente
            InventoryDAOFactory inventoryDAOFactory = new InventoryDAOFactory();
            InventoryDAO inventoryDAO = inventoryDAOFactory.createInventoryDAO();
            Inventory inventory = (Inventory) inventoryDAO.retrieveInventory();
            if (inventory == null) return new ArrayList<>(); //inventario vuoto -> no suggerimenti

            //Una volta recuperato viene passato ai controller delegati alle info della ricetta, ad esmpio per il calcolo degli ingredienti mancanti
            RecipeInfoRetrievingApplicativeController.setInventoryList(inventory.getIngredientList());

            List<RecipeBase> suggestedRecipes = new ArrayList<>();

            //si recupera l'ultimo id chef salvato per controllare e scorrere le ricette di tutti gli chef
            ChefDAO chefDAO = new ChefDAO();
            int lastId = 1;
            try {
                lastId = chefDAO.getLastId();
            } catch (ClassNotFoundException ignored) {
                assert (true); //eccezione ignorata dato che l'id salvato su un file è un tipo primitivo java
            }

            List<RecipeBase> currentChefRecipeList;
            RecipesBrowsingDAO dao = new RecipesBrowsingDAO();

        /*
        Dal DAO si recupera la lista delle ricette per ogni chef. Poi si scorrono tutti gli ingredienti nell'inventario e si controlla se l'ingrediente è presente. Se si, si
        aggiunge la ricetta alla lista da restituire alla UI
         */
            for (int i = 1; i < lastId + 1; i++) {
                currentChefRecipeList = dao.getRecipeList(i);
                if (currentChefRecipeList.isEmpty()) continue;

                for (Ingredient ingr : (inventory.getIngredientList())) {
                    for (RecipeBase rec : currentChefRecipeList) {
                        if (recipeContainsIngredient(rec, ingr, suggestedRecipes)) suggestedRecipes.add(rec);
                    }
                }
            }

            return this.setUpSuggestedBeanList(suggestedRecipes);
        }  catch (IOException | SQLException e) {
            throw new PersistentDataAccessException(e);
        }
    }

}

