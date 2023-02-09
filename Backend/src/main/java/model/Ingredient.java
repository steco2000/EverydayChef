package model;

import java.io.Serializable;

//entità che incapsula i dati degli ingredienti base (quelli delle ricette)

public class Ingredient implements RecipeIngredient, Serializable {

    protected String name;
    protected double quantity;
    protected String measureUnit;

    public Ingredient(){
    }

    public Ingredient(String name, double quantity, String measureUnit){
        this.name = name;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getMeasureUnit() {
        return measureUnit;
    }

    @Override
    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }
}
