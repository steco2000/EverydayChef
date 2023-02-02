package beans;

import exceptions.RecipeIngredientQuantityException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class RecipeIngredientBean {

    private String name;
    private double quantity;
    private String stringQuantity;
    private String measureUnit;

    public String getName() throws IllegalArgumentException{
        if(name == null || name.length() == 0) throw new IllegalArgumentException();
        return name;
    }

    public String getStringQuantity() {
        return stringQuantity;
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

    public void setName(String name) throws IllegalArgumentException{
        if(name.length() == 0) throw new IllegalArgumentException();
        this.name = toTitleCase(name);
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity, boolean justEnough) throws ParseException, RecipeIngredientQuantityException {
        if(quantity.length() == 0) throw new RecipeIngredientQuantityException();
        if(quantity.equals("J. E.")){
            this.quantity = -1;
            this.stringQuantity = quantity;
        }else {
            double givenQuantity;
            try {
                givenQuantity = Double.parseDouble(quantity);
            } catch (NumberFormatException e) {
                NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                Number number = format.parse(quantity);
                givenQuantity = number.doubleValue();
            }

            if (givenQuantity <= 0 && !justEnough) throw new IllegalArgumentException();

            if(givenQuantity == -1 && justEnough){
                this.quantity = givenQuantity;
                this.stringQuantity = "J. E.";
            }else {
                this.quantity = givenQuantity;
                this.stringQuantity = String.valueOf(givenQuantity);
            }
        }
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }
}
