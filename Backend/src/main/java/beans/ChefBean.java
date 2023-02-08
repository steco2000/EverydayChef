package beans;

import java.lang.reflect.MalformedParametersException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//bean che incapsula i dati relativi a istanze di chef

public class ChefBean {

    private String name;
    private String surname;
    private Date birthDate;
    private String info;
    private String username;
    private String password;
    private int id;
    private String email;

    public String getUsername() {
        return username;
    }

    //non è ammesso un username vuoto, in tal caso viene lanciata un'eccezione
    public void setUsername(String username) {
        if(username == null || username.length() == 0) throw new IllegalArgumentException();
        this.username = username;
    }

    public String getName() {
        return name;
    }

    //stessa cosa per il nome
    public void setName(String name) throws IllegalArgumentException{
        if(name == null || name.length() == 0) throw new IllegalArgumentException();
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    //e per il cognome
    public void setSurname(String surname) throws IllegalArgumentException{
        if(surname == null || surname.length() == 0) throw new IllegalArgumentException();
        this.surname = surname;
    }

    //nel bean la data è trattata in formato stringa per i metodi standard
    public String getBirthDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(this.birthDate);
    }

    //tuttavia si può richiedere nel formato interno scelto
    public Date getBirthDateInternalFormat(){ return this.birthDate; }

    /*
    Il metodo per settare la data di nascita si aspetta una stringa nel giusto formato, per poi convertirla nel tipo "Date". Ovviamente se il formato non è corretto verrà lanciata
    la ParseException. Nel caso in cui la data di nascita sia nel futuro, cosa impossibile, viene lanciata L'IllegalArgumentException
     */
    public void setBirthDate(String birthDate) throws ParseException {
        Date javaDate;
        if (!birthDate.trim().equals("")) {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
            sdfrmt.setLenient(false);
            javaDate = sdfrmt.parse(birthDate);
            long millis = System.currentTimeMillis();
            Date now = new Date(millis);
            if(javaDate.after(now)) throw new IllegalArgumentException();
            this.birthDate = javaDate;
        }
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPassword() { return password; }

    //anche la password non può essere vuota
    public void setPassword(String password) {
        if(password == null || password.length() == 0) throw new IllegalArgumentException();
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    /*
    Per la validazione delle email si utilizza una espressione regolare del formato atteso e si controlla che ciò che si è ricevuto in input matchi con l'espressione. E' bene
    notare però che questo metodo non controlla se l'email esiste realmente, ma solo se sia corretta sintatticamente.
     */
    private boolean isValid(String email){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //nel caso in cui l'email sia non valida viene lanciata un'eccezione
    public void setEmail(String email) throws MalformedParametersException{
        if(!isValid(email)) throw new MalformedParametersException();
        this.email = email;
    }
}
