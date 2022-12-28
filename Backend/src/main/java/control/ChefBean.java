package control;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(this.birthDate);
    }

    public Date getBirthDateInternalFormat(){ return this.birthDate; }

    public boolean setBirthDate(String birthDate) {
        Date javaDate;
        if (birthDate.trim().equals("")) return true;
        else{
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
            sdfrmt.setLenient(false);
            try{
                javaDate = sdfrmt.parse(birthDate);
            }catch (ParseException e){
                return false;
            }
            this.birthDate = javaDate;
            return true;
        }
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
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

    public boolean setEmail(String email) {
        if(!isValid(email)) return false;
        this.email = email;
        return true;
    }
}
