package control;

import beans.IngredientBean;
import dao.InventoryDAO;
import exceptions.PersistentDataAccessException;
import factories.IngredientFactory;
import factories.InventoryDAOFactory;
import model.IngredientBase;
import model.InventoryBase;

import java.io.IOException;
import java.sql.SQLException;

/*
Controller applicativo che per il caso d'uso "Gestisci inventario ingredienti".
 */

public class InventoryApplicativeController implements InventoryController{

    private final InventoryBase currentInventory;

    //nel costruttore si effettua il caching dell'inventario dell'utente loggato
    public InventoryApplicativeController() throws PersistentDataAccessException {
        try {
            InventoryDAOFactory daoFactory = new InventoryDAOFactory();
            InventoryDAO dao = daoFactory.createInventoryDAO();
            currentInventory = dao.retrieveInventory();
        } catch (IOException | SQLException e) {
            throw new PersistentDataAccessException(e);
        }
    }

    //aggiunta ingrediente, grazie alla relativa factory si inseriscono direttamente le informazioni dal bean in fase di creazione
    @Override
    public boolean addIngredient(IngredientBean ingredient) {
        for(IngredientBase i: currentInventory.getState()){
            if(i.getName().equals(ingredient.getName())) return false;
        }
        IngredientFactory ingredientFactory = new IngredientFactory();
        IngredientBase newIngredient = ingredientFactory.createIngredient(ingredient.getName(),ingredient.getQuantity(),ingredient.getMeasureUnit(),ingredient.getExpirationDate(),ingredient.getNotes());
        currentInventory.addIngredient(newIngredient);
        return true;
    }

    //rimozione dell'ingrediente dall'inventario in cache
    @Override
    public void removeIngredient(IngredientBean ingredient) {
        currentInventory.removeIngredient(ingredient.getName());
    }

    //la modifica di un ingrediente consiste, in sostanza nella sovrascrittura dell'ingrediente modificato con uno nuovo contenente le modifiche
    @Override
    public boolean updateIngredient(String toUpdate, IngredientBean updates) {
        for(IngredientBase i: currentInventory.getState()){
            if(i.getName().equals(toUpdate)) continue;
            if(i.getName().equals(updates.getName())) return false;
        }
        currentInventory.removeIngredient(toUpdate);
        IngredientFactory factory = new IngredientFactory();
        IngredientBase updated = factory.createIngredient(updates.getName(),updates.getQuantity(),updates.getMeasureUnit(),updates.getExpirationDate(),updates.getNotes());
        currentInventory.addIngredient(updated);
        return true;
    }

    //salvataggio delle modifiche all'inventario in persistenza, attraverso il DAO
    @Override
    public void saveCurrentInventory() throws PersistentDataAccessException {
        try {
            InventoryDAOFactory daoFactory = new InventoryDAOFactory();
            InventoryDAO dao = daoFactory.createInventoryDAO();
            dao.saveInventory(currentInventory);
        }catch (IOException | SQLException e){
            throw new PersistentDataAccessException(e);
        }
    }

}
