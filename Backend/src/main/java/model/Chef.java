package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//entità che incapsula i dati degli chef

public class Chef implements ChefBase, Serializable {

    private String name;
    private String surname;
    private Date birthDate;
    private String info;
    private List<Recipe> recipeList;
    private String username;
    private String password;
    private String email;
    private int id;

    public Chef(){
        recipeList = new ArrayList<>();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String getSurname() {
        return this.surname;
    }

    @Override
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public Date getBirthDate() {
        return this.birthDate;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public void setId(int id){ this.id = id; }

    public List<Recipe> getRecipes() {
        return this.recipeList;
    }

    public boolean addRecipe(Recipe recipe){
        for(Recipe r: recipeList){
            if(recipe.getName().equals(r.getName())) return false;
        }
        this.recipeList.add(recipe);
        return true;
    }

    public void removeRecipe(Recipe recipe){
        this.recipeList.remove(recipe);
    }

}
