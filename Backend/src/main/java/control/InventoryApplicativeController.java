package control;

import beans.IngredientBean;
import dao.InventoryDAO;
import factories.IngredientFactory;
import model.*;

import java.io.IOException;

public class InventoryApplicativeController implements InventoryController{

    private final InventoryBase currentInventory;

    public InventoryApplicativeController(){
        InventoryDAO dao = new InventoryDAO();
        currentInventory = dao.retrieveInventory();
    }

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

    @Override
    public void removeIngredient(IngredientBean ingredient) {
        currentInventory.removeIngredient(ingredient.getName());
    }

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

    //TODO: controlla eccezione
    @Override
    public void saveCurrentInventory() {
        InventoryDAO dao = new InventoryDAO();
        try {
            dao.saveInventory(currentInventory);
        } catch (IOException ignored) {
            "".isEmpty(); //eccezione ignorata
        }
    }

}
