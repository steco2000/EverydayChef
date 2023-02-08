package beans;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//bean che incapsula i dati di istanze di tipo InventoryIngredient

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

    /*
    Ogni volta che viene impostato un nome a un ingrediente, tutte le parole vengono per capitalizzate per convenzione del sistema. Questo per non creare ambiguità tra i nomi
    nell'algoritmo di selezione delle ricette consigliate.
     */
    private String toTitleCase(String givenName) {
        String[] arr = givenName.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String s : arr) {
            sb.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    //nomi vuoti non sono consentiti
    public void setName(String name) throws IllegalArgumentException {
        if(name == null || name.length() == 0) throw new IllegalArgumentException();
        this.name = toTitleCase(name);
    }

    public double getQuantity() {
        return quantity;
    }

    /*
    Il bean si aspetta la quantità in formato stringa, per poi provare a effettuare la conversione a double, lanciando eccezioni in caso di problemi. E' inoltre presente un sistema
    in grado di non fare distinzioni tra double scritti con la virgola o con il punto.
     */
    public void setQuantity(String quantity) throws ParseException, IllegalArgumentException {
        if(quantity == null || quantity.length() == 0) throw new IllegalArgumentException();
        double givenQuantity;
        try{
            givenQuantity = Double.parseDouble(quantity);
        }catch(NumberFormatException e){

            //se siamo finiti qui il numero potrebbe essere stato scritto con la virgola, si prova quindi a convertirlo
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            Number number = format.parse(quantity);
            givenQuantity = number.doubleValue();

        }
        if(givenQuantity < 0) throw new IllegalArgumentException();
        this.quantity = givenQuantity;
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

    //la data di scadenza può essere impostata via stringa o direttamente nel formato interno se, ad esempio, sia un controller applicativo a chiamare il metodo di set

    public void setExpirationDateInternalFormat(Date expirationDate){
        this.expirationDate = expirationDate;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.stringExpDate = dateFormat.format(expirationDate);
    }

    /*
    Il metodo che accetta la data in formato stringa deve effettuare la conversione, lanciando eccezioni in caso di problemi. In questo caso sono accettate date nel passato, dato che
    potrei avere necessità di salvare comunque nell'inventario un ingrediente scaduto.
     */
    public void setExpirationDate(String expirationDate) throws ParseException, IllegalArgumentException {
        if(expirationDate.isEmpty()) throw new IllegalArgumentException();
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
