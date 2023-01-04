package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Inventory extends InventorySubject implements InventoryBase, Serializable  {

    private UserCredentials user;
    private List<IngredientBase> ingredientList;

    public Inventory(UserCredentials user){
        this.ingredientList = new ArrayList<>();
        this.user = user;
    }

    public void setUser(UserCredentials user) {
        this.user = user;
    }

    public UserCredentials getUser() {
        return this.user;
    }

    @Override
    public void addIngredient(IngredientBase ingredient){
        this.ingredientList.add(ingredient);
        this.notifyObservers();
    }

    @Override
    public void removeIngredient(String ingredient) {
        IngredientBase toRemove = null;
        for(IngredientBase i: ingredientList){
            if(i.getName().equals(ingredient)){
                toRemove = i;
                break;
            }
        }
        if(toRemove != null) {
            this.ingredientList.remove(toRemove);
            this.notifyObservers();
        }
    }

    @Override
    public IngredientBase getIngredient(String name) {
        for(IngredientBase i: ingredientList){
            if(i.getName().equals(name)) return i;
        }
        return null;
    }

    @Override
    public List<IngredientBase> getState(){
        return ingredientList;
    }

}
