package model;

import java.io.Serializable;
import java.util.Date;

public class InventoryIngredient extends Ingredient implements IngredientBase, Serializable {

    private Date expirationDate;
    private String notes;

    public InventoryIngredient(String name, double quantity, String measureUnit, Date expirationDate, String notes){
        this.name = name;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.expirationDate = expirationDate;
        this.notes = notes;
    }

    @Override
    public Date getExpirationDate() {
        return expirationDate;
    }

    @Override
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
