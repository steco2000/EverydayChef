package model;

public abstract class InventoryObserver {

    public abstract void update();
    public abstract void setSubject(InventorySubject subject);

}
