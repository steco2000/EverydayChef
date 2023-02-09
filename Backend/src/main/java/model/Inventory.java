package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//entità che incapsula i dati dell'inventario ingredienti, lo stato è l'utente e la lista degli ingredienti da inventario

public class Inventory extends InventorySubject implements InventoryBase, Serializable  {

    private UserCredentials user;
    private List<InventoryIngredient> ingredientList;

    //quando l'inventario viene creato si associa all'utente
    public Inventory(UserCredentials user){
        this.ingredientList = new ArrayList<>();
        this.user = user;
        user.setIngredientsInventory(this);
    }

    public void setUser(UserCredentials user) {
        this.user = user;
    }

    public UserCredentials getUser() {
        return this.user;
    }

    //L'inventario è osservato dal bean in pull model per l'interfaccia di gestione, ogni modifica deve essere notificata

    @Override
    public void addIngredient(IngredientBase ingredient){
        this.ingredientList.add((InventoryIngredient) ingredient);
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

    //metodo usato dal bean in pull model per ottenere i dati aggiornati
    @Override
    public List<IngredientBase> getState(){
        List<IngredientBase> state = new ArrayList<>();
        state.addAll(ingredientList);
        return state;
    }

    public List<InventoryIngredient> getIngredientList() {
        return ingredientList;
    }
}
