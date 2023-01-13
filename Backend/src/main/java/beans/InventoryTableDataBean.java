package beans;

import model.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class InventoryTableDataBean extends InventoryObserver {

    private Inventory subject;
    private List<IngredientBase> ingredientList;
    private static InventoryTableDataBean singletonInstance;

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

    @Override
    public void update() {
        this.ingredientList = this.subject.getState();
    }

    @Override
    public void setSubject(InventorySubject subject) {
        this.subject = (Inventory) subject;
        this.ingredientList = this.subject.getState();
    }

    public static InventoryTableDataBean getSingletonInstance(){
        if(singletonInstance == null) singletonInstance = new InventoryTableDataBean();
        return singletonInstance;
    }

}
