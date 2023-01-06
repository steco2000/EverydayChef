package model;

import java.io.IOException;

public class ChefFactory {

    public ChefBase createChef(){
        Chef newChef = new Chef();
        ChefDAO dao = new ChefDAO();
        int id;
        try {
            id = dao.getLastId();
        } catch (IOException | ClassNotFoundException e) {
            id = 0;
        }
        newChef.setId(id+1);
        return newChef;
    }

}
