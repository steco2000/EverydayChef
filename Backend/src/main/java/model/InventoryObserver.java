package model;

//observer astratto dell'inventario

public abstract class InventoryObserver {

    public abstract void update();
    public abstract void setSubject(InventorySubject subject);

}
