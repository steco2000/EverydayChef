package model;

import java.io.Serializable;
import java.util.Date;

public class Ingredient implements IngredientBase, Serializable {

    private String name;
    private double quantity;
    private String measureUnit;
    private Date expirationDate;
    private String notes;

    public Ingredient(String name, double quantity, String measureUnit, Date expirationDate, String notes){
        this.name = name;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.expirationDate = expirationDate;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
