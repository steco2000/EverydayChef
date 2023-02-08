package beans;

import java.lang.reflect.MalformedParametersException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//bean che incapsula i dati di istanze di tipi UserCredentials

public class UserCredBean {

    private String username;
    private String password;
    private String email;

    public String getUsername() {
        return username;
    }

    //username vuoti non sono ammessi
    public void setUsername(String username) throws IllegalArgumentException{
        if(username == null || username.length() == 0) throw new IllegalArgumentException();
        this.username = username;
    }

    public String getPassword() { return password; }

    //stessa cosa per le password
    public void setPassword(String password) throws IllegalArgumentException{
        if(password == null || password.length() == 0) throw new IllegalArgumentException();
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    //nel caso in cui l'email sia non valida viene lanciata un'eccezione
    public void setEmail(String email) throws MalformedParametersException{
        if(!isValid(email)) throw new MalformedParametersException();
        this.email = email;
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

}
