package control;

import java.lang.reflect.MalformedParametersException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void setUsername(String username) {
        if(username.length() == 0) throw new IllegalArgumentException();
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException{
        if(name.length() == 0) throw new IllegalArgumentException();
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) throws IllegalArgumentException{
        if(surname.length() == 0) throw new IllegalArgumentException();
        this.surname = surname;
    }

    public String getBirthDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(this.birthDate);
    }

    public Date getBirthDateInternalFormat(){ return this.birthDate; }

    public void setBirthDate(String birthDate) throws ParseException {
        Date javaDate;
        if (birthDate.trim().equals("")) {
        }
        else{
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

    public void setPassword(String password) {
        if(password.length() == 0) throw new IllegalArgumentException();
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    private boolean isValid(String email){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean setEmail(String email) throws MalformedParametersException{
        if(!isValid(email)) throw new MalformedParametersException();
        this.email = email;
        return true;
    }
}
