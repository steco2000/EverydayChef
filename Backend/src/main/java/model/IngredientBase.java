package model;

import java.util.Date;

public interface IngredientBase {

    void setName(String name);
    String getName();
    void setQuantity(double quantity);
    double getQuantity();
    void setMeasureUnit(String measureUnit);
    String getMeasureUnit();
    void setExpirationDate(Date expirationDate);
    Date getExpirationDate();
    void setNotes(String notes);
    String getNotes();

}
