package model;

import java.util.ArrayList;
import java.util.List;

public abstract class RecipeSubject {

    private List<RecipeObserver> observers;

    protected RecipeSubject(){
        this.observers = new ArrayList<>();
    }

    public void attach(RecipeObserver newObserver){
        this.observers.add(newObserver);
    }

    public void detach(RecipeObserver toDetach){
        this.observers.remove(toDetach);
    }

    public void notifyObservers(){
        for(RecipeObserver o: observers){
            o.update();
        }
    }

}
