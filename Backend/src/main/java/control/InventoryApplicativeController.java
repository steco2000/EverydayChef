package control;

import beans.IngredientBean;
import dao.InventoryDAO;
import factories.IngredientFactory;
import factories.InventoryDAOFactory;
import model.*;

public class InventoryApplicativeController implements InventoryController{

    private final InventoryBase currentInventory;

    public InventoryApplicativeController(){
        InventoryDAOFactory daoFactory = new InventoryDAOFactory();
        InventoryDAO dao = daoFactory.createInventoryDAO();
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

    @Override
    public void saveCurrentInventory() {
        InventoryDAOFactory daoFactory = new InventoryDAOFactory();
        InventoryDAO dao = daoFactory.createInventoryDAO();
        dao.saveInventory(currentInventory);
    }

}
