package factories;

import model.Chef;
import model.ChefBase;
import dao.ChefDAO;

import java.io.IOException;

//factory adibita alla creazione di istanze di Chef

public class ChefFactory {

    //dallo ChefDAO viene recuperato l'ultimo id assegnato per incrementarlo e assegnarlo al nuovo chef
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
