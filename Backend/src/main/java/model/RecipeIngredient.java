package model;

import java.io.Serializable;

public interface RecipeIngredient extends Serializable {

    void setName(String name);
    String getName();
    void setQuantity(double quantity);
    double getQuantity();
    void setMeasureUnit(String unit);
    String getMeasureUnit();

}
