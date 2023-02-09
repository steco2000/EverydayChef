package model;

//observer astratto dell'entità Recipe

public abstract class RecipeObserver {

    public abstract void update();
    public abstract void setSubject(RecipeSubject subject);

}
