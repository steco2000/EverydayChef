package factories;

import model.Chef;
import model.ChefBase;
import dao.ChefDAO;

import java.io.FileNotFoundException;
import java.io.IOException;

//factory adibita alla creazione di istanze di Chef

public class ChefFactory {

    //dallo ChefDAO viene recuperato l'ultimo id assegnato per incrementarlo e assegnarlo al nuovo chef
    public ChefBase createChef() throws IOException {
        Chef newChef = new Chef();
        ChefDAO dao = new ChefDAO();
        int id;
        try {
            id = dao.getLastId();
            newChef.setId(id+1);
        } catch (ClassNotFoundException ignored) {
            assert(true); //ignorata, il tipo di dato che si sta leggendo è primitivo
        } catch(FileNotFoundException e){
            newChef.setId(1);   //non esiste alcuno chef
        }
        return newChef;
    }

}
