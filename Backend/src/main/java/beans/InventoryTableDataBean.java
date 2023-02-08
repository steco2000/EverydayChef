package beans;

import model.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/*
bean in pull model per l'interfaccia dell'inventario ingredienti. Rende disponibili all'interfaccia i dati dell'inventario sempre aggiornati,
implementando sul model il pattern observer. E' singleton dato che è in continuo ascolto di aggiornamenti dal model, perciò l'interfaccia deve sempre essere sicura
di relazionarsi con la stessa istanza per avere i dati sempre aggiornati.
*/

public class InventoryTableDataBean extends InventoryObserver {

    private Inventory subject;
    private List<IngredientBase> ingredientList;
    private static InventoryTableDataBean singletonInstance;

    /*
    Questo metodo incapsula i dati del model nei bean che verranno poi passati all'interfaccia, dove verranno visualizzati.
     */
    public List<IngredientBean> getTableData() throws ParseException {
        List<IngredientBean> beanList = new ArrayList<>();
        if (ingredientList == null) return beanList;
        for (IngredientBase i : ingredientList) {
            IngredientBean bean = new IngredientBean();
            bean.setName(i.getName());
            bean.setQuantity(String.valueOf(i.getQuantity()));
            bean.setMeasureUnit(i.getMeasureUnit());
            bean.setExpirationDateInternalFormat(i.getExpirationDate());
            bean.setNotes(i.getNotes());
            beanList.add(bean);
        }
        return beanList;
    }

    //metodo update invocato dal model per aggiornare lo stato dell'observer
    @Override
    public void update() {
        this.ingredientList = this.subject.getState();
    }

    //metodo che serve a linkare il bean observer al suo subject
    @Override
    public void setSubject(InventorySubject subject) {
        this.subject = (Inventory) subject;
        this.ingredientList = this.subject.getState();
    }

    //restituzione dell'istanza singleton
    public static InventoryTableDataBean getSingletonInstance(){
        if(singletonInstance == null) singletonInstance = new InventoryTableDataBean();
        return singletonInstance;
    }

}
