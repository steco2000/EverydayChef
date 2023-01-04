package model;

import java.util.ArrayList;
import java.util.List;

public abstract class InventorySubject {

    private List<InventoryObserver> observerList;

    protected InventorySubject(){
        observerList = new ArrayList<>();
    }

    public void attach(InventoryObserver newObserver){
        observerList.add(newObserver);
    }

    public void detach(InventoryObserver toDetach){
        observerList.remove(toDetach);
    }

    public void notifyObservers(){
        for(InventoryObserver o: observerList){
            o.update();
        }
    }

}
