package beans;

import java.lang.reflect.MalformedParametersException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCredBean {

    private String username;
    private String password;
    private String email;
    private boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws IllegalArgumentException{
        if(username.length() == 0) throw new IllegalArgumentException();
        this.username = username;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) throws IllegalArgumentException{
        if(password.length() == 0) throw new IllegalArgumentException();
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(!isValid(email)) throw new MalformedParametersException();
        this.email = email;
    }

    private boolean isValid(String email){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
