package beans;

import exceptions.RecipeIngredientQuantityException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class RecipeIngredientBean {

    private String name;
    private double quantity;
    private String measureUnit;

    public String getName() throws IllegalArgumentException{
        if(name == null || name.length() == 0) throw new IllegalArgumentException();
        return name;
    }

    private String toTitleCase(String givenName) {
        String[] arr = givenName.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
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

    public void setQuantity(String quantity, boolean ...notSpecified) throws ParseException, RecipeIngredientQuantityException {
        //Per acluni ingredienti come olio e sale la quantità può non essere specificata, tuttavia l'utente viene avvertito
        if(quantity.length() == 0 && notSpecified.length == 0) throw new RecipeIngredientQuantityException();
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
}
