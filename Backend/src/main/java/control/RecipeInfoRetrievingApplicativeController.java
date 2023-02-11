package control;

import beans.ChefBean;
import beans.RecipeBean;
import beans.RecipeBrowsingTableBean;
import beans.RecipeIngredientBean;
import dao.ChefDAO;
import dao.RecipeDAO;
import dao.RecipesBrowsingDAO;
import exceptions.PersistentDataAccessException;
import exceptions.RecipeIngredientQuantityException;
import model.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/*
Controller applicativo che gestisce il recupero delle informazioni di una ricetta da rendere disponibile all'utente. Recupera anche le info dello chef e calcola gli ingredienti
mancanti in base all'inventario.
 */

public class RecipeInfoRetrievingApplicativeController implements RecipeInfoRetrievingController{

    private static List<InventoryIngredient> inventoryList;
    private RecipeBean selectedRecipe;

    //metodo che esegue il recupero delle informazioni di base della ricetta
    @Override
    public RecipeBean retrieveRecipeInfo(RecipeBrowsingTableBean recipeBrowsingBean) throws PersistentDataAccessException {
        try {
            //recupero della ricetta dal DAO di navigazione
            RecipesBrowsingDAO recipesBrowsingDAO = new RecipesBrowsingDAO();
            RecipeBase recipe = recipesBrowsingDAO.getRecipeInfo(recipeBrowsingBean.getName(), recipeBrowsingBean.getChefUsername());

            //incremento delle visualizzazioni. Quando questo metodo viene chiamato vuol dire che un utente ha cliccato sulla ricetta
            RecipeDAO recipeWriterDAO = new RecipeDAO(recipe.getChef().getUsername());
            recipeWriterDAO.incrementRecipeViews(recipe.getName());

            //costruzione e ritorno del bean con i dati della ricetta
            RecipeBean toReturn = new RecipeBean();
            toReturn.setName(recipe.getName());
            toReturn.setServings(String.valueOf(recipe.getServings()));
            toReturn.setPreparationTime(recipe.getPreparationTime());
            toReturn.setDifficulty(recipe.getDifficulty());
            toReturn.setPreparationProcedure(recipe.getPreparationProcedure());

            List<RecipeIngredientBean> ingredientBeanList = new ArrayList<>();
            for (RecipeIngredient i : recipe.getIngredientList()) {
                RecipeIngredientBean ingredientBean = new RecipeIngredientBean();
                ingredientBean.setName(i.getName());
                try {
                    if (i.getQuantity() != -1) {
                        ingredientBean.setQuantity(String.valueOf(i.getQuantity()), false);
                    } else {
                        ingredientBean.setQuantity("J. E.", true);
                    }
                } catch (ParseException | RecipeIngredientQuantityException ignored) {
                    assert(true);   //ignorata perché i dati da memoria sono già validati
                }

                ingredientBean.setMeasureUnit(i.getMeasureUnit());
                ingredientBeanList.add(ingredientBean);
            }

            toReturn.setIngredientList(ingredientBeanList);
            this.selectedRecipe = toReturn;
            return toReturn;
        } catch(IOException e){
            throw new PersistentDataAccessException(e);
        }
    }

    //metodo per il recupero e ritorno alla UI delle informazioni dello chef
    @Override
    public ChefBean retrieveChefInfo(String chefUsername) throws PersistentDataAccessException {
        try {
            ChefDAO chefDAO = new ChefDAO();
            ChefBase chef = chefDAO.retrieveChef(chefUsername);
            ChefBean toReturn = new ChefBean();
            toReturn.setName(chef.getName());
            toReturn.setSurname(chef.getSurname());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                toReturn.setBirthDate(dateFormat.format(chef.getBirthDate()));
            } catch (NullPointerException | ParseException ignored) {
                assert (true); //eccezione ignorata, dati da memoria già validati
            }
            toReturn.setInfo(chef.getInfo());
            toReturn.setEmail(chef.getEmail());
            return toReturn;
        }catch (IOException e){
            throw new PersistentDataAccessException(e);
        }
    }

    //Metodo che crea l'ingrediente mancante, se esiste, e che calcola la quantità mancante.
    private RecipeIngredientBean getMissingQuantityIngredient(RecipeIngredientBean recipeIngredient, InventoryIngredient inventoryIngredient){
        RecipeIngredientBean newMissing = new RecipeIngredientBean();
        newMissing.setName(recipeIngredient.getName());
        try {
            if (recipeIngredient.getQuantity() != -1) {

                //se la quantità dell'ingrediente della ricetta è definita, si calcola la differenza, riportando tutto a cifre elevate per evitare problemi di approssimazione
                double difference = (recipeIngredient.getQuantity() * 100) - (inventoryIngredient.getQuantity() * 100);
                newMissing.setQuantity(String.valueOf(difference / 100),false);

                //se la quantità è j. e. non dobbiamo calcolare nulla, perchè in questo caso vuol dire che l'ingrediente non è proprio presente nell'inventario
            }else newMissing.setQuantity("J. E.",true);

        }catch (ParseException | RecipeIngredientQuantityException ignored) {
            assert (true); //eccezione ignorata, i dati provengono dalla memoria e sono giò stati validati
        }
        newMissing.setMeasureUnit(recipeIngredient.getMeasureUnit());
        return newMissing;
    }

    /*
    Metodo che cerca gli ingredienti mancanti nell'inventario
     */
    @Override
    public List<RecipeIngredientBean> retrieveMissingIngredients(RecipeBrowsingTableBean recipe) {
        List<RecipeIngredientBean> missingIngredients = new ArrayList<>();
        for(RecipeIngredientBean recipeIngredient: this.selectedRecipe.getIngredientList()){
            if(inventoryList.stream().noneMatch(o -> o.getName().equals(recipeIngredient.getName()))){
                //se l'ingrediente non è presente nell'inventario lo aggiungo così com'è
                missingIngredients.add(recipeIngredient);
            }else{
                //se l'ingrediente è presente ma la quantità non basta, aggiungo il bean con la quantità mancante
                for (InventoryIngredient inventoryIngredient : inventoryList) {
                    if ((recipeIngredient.getName().equals(inventoryIngredient.getName())) && (recipeIngredient.getQuantity() > inventoryIngredient.getQuantity())){
                        RecipeIngredientBean newMissing = this.getMissingQuantityIngredient(recipeIngredient, inventoryIngredient);
                        missingIngredients.add(newMissing);
                    }
                }
            }
        }
        return missingIngredients;
    }

    //metodo che serve a impostare la lista degli ingredienti dell'inventario dell'utente loggato, per permettere alle istanze del controller di effettuare i confronti con le ricette
    public static void setInventoryList(List<InventoryIngredient> inventoryList){
        RecipeInfoRetrievingApplicativeController.inventoryList = inventoryList;
    }

}
