package beans;

import exceptions.RecipeIngredientQuantityException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/*
Bean che incapsula dati delle istanze di Ingredient (kind of RecipeIngredient)
 */

public class RecipeIngredientBean {

    private String name;
    private double quantity;
    private String stringQuantity;
    private String measureUnit;

    public String getName(){
        return name;
    }

    public String getStringQuantity() {
        return stringQuantity;
    }

    //Le parole presenti nel nome vengono tutte capitalizzate per evitare ambiguità nell'algoritmo di selezione delle ricette consigliate
    private String toTitleCase(String givenName) {
        String[] arr = givenName.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String s : arr) {
            sb.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    //nomi vuoti non sono ammessi
    public void setName(String name) throws IllegalArgumentException{
        if(name.length() == 0) throw new IllegalArgumentException();
        this.name = toTitleCase(name);
    }

    public double getQuantity() {
        return quantity;
    }

    /*
    Per gli ingredienti delle ricette la quantità può essere settata a "J. E." (just enough). Si mantengono perciò due attributi, string e double, da utilizzare nel contesto UI o
    interno. L'attributo quantity della entity, però, è un double. Quindi, nel caso di j. e., viene settato a -1, per convenzione del sistema.
    Il metodo deve gestire l'inserimento della quantità sia da UI che da controller. Il parametro booleano serve a gestire il secondo caso e a distinguere i casi di input illegali
    (quantità inserite minori di 0), dal caso j. e.
     */
    public void setQuantity(String quantity, boolean justEnough) throws ParseException, RecipeIngredientQuantityException {
        if(quantity.length() == 0) throw new RecipeIngredientQuantityException();
        if(quantity.equals("J. E.")){

            //se ci troviamo qui dentro siamo sicuramente nella UI, dato che la stringa in ingresso è esattamente "J. E.". La quantità double va settata a -1
            this.quantity = -1;
            this.stringQuantity = quantity;

        }else {

            //se ci troviamo qui non è possibile stabilire in che strato siamo, prima si converte la quantità
            double givenQuantity;
            try {
                givenQuantity = Double.parseDouble(quantity);
            } catch (NumberFormatException e) {
                NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                Number number = format.parse(quantity);
                givenQuantity = number.doubleValue();
            }

            //se la quantità è <= 0 e non siamo in caso di j.e. -> eccezione
            if (givenQuantity <= 0 && !justEnough) throw new IllegalArgumentException();

            //in base a se ci troviamo nel caso j.e. o no si settano gli attributi nel modo corrispondente
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
