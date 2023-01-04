package control;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IngredientBean {

    private String name;
    private double quantity;
    private String measureUnit;
    private Date expirationDate;
    private String notes;

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

    public void setExpirationDateInternalFormat(Date expirationDate){
        this.expirationDate = expirationDate;
    }

    public boolean setExpirationDate(String expirationDate) {
        Date javaDate;
        if (expirationDate.trim().equals("")) return true;
        else{
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
            sdfrmt.setLenient(false);
            try{
                javaDate = sdfrmt.parse(expirationDate);
            }catch (ParseException e){
                return false;
            }
            this.expirationDate = javaDate;
            return true;
        }
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
