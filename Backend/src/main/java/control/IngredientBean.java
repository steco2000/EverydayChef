package control;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatException;
import java.util.Locale;

public class IngredientBean {

    private String name;
    private double quantity;
    private String measureUnit;
    private Date expirationDate;
    private String notes;

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if(name.length() == 0) throw new IllegalArgumentException();
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) throws ParseException, IllegalArgumentException {
        if(quantity.length() == 0) throw new IllegalArgumentException();
        try{
            this.quantity = Double.parseDouble(quantity);
        }catch(NumberFormatException e){
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            Number number = format.parse(quantity);
            this.quantity = number.doubleValue();
        }
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

    public void setExpirationDate(String expirationDate) throws ParseException {
        Date javaDate;
        if (expirationDate.trim().equals("")) return;
        else{
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
            sdfrmt.setLenient(false);
            javaDate = sdfrmt.parse(expirationDate);
            this.expirationDate = javaDate;
        }
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
