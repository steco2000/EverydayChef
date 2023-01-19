package beans;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IngredientBean {

    private String name;
    private double quantity;
    private String measureUnit;
    private Date expirationDate;
    private String stringExpDate;
    private String notes;

    public String getName() {
        return name;
    }

    private String toTitleCase(String givenName) {
        String[] arr = givenName.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String s : arr) {
            sb.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public void setName(String name) throws IllegalArgumentException {
        if(name == null || name.length() == 0) throw new IllegalArgumentException();
        this.name = toTitleCase(name);
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) throws ParseException, IllegalArgumentException {
        if(quantity == null || quantity.length() == 0) throw new IllegalArgumentException();
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

    public String getStringExpDate() {
        return stringExpDate;
    }

    public void setExpirationDateInternalFormat(Date expirationDate){
        this.expirationDate = expirationDate;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.stringExpDate = dateFormat.format(expirationDate);
    }

    public void setExpirationDate(String expirationDate) throws ParseException {
        Date javaDate;
        this.stringExpDate = expirationDate;
        if(!expirationDate.trim().equals("")){
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
