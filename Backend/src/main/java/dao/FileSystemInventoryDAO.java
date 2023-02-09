package dao;

import beans.InventoryTableDataBean;
import control.LoginController;
import factories.InventoryFactory;
import model.Inventory;
import model.InventoryBase;
import model.UserCredentials;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

//dao in versione file system per il salvataggio di istanze di tipo inventario

public class FileSystemInventoryDAO extends InventoryDAO{

    private final String inventoryFileName;

    /*
    Nel costruttore viene calcolato il percorso assoluto dei file ".ser" che contengono istanze di tipo inventario. Inoltre viene garantito che all'avvio i dati siano aggiornati
     */
    public FileSystemInventoryDAO(){
        super();
        Path relativeInventoryFilePath = Paths.get("Backend\\src\\main\\resources\\inventories\\"+LoginController.getUserLogged().getUsername()+"-inventory.ser");
        inventoryFileName = relativeInventoryFilePath.toAbsolutePath().toString();
        try {
            this.makeDataConsistent();
        } catch (IOException | ClassNotFoundException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    //Questo metodo scrive sui file le istanze di tipo inventario
    @Override
    public void saveInventory(InventoryBase inventory) {
        try {
            FileOutputStream fileout = new FileOutputStream(inventoryFileName);
            ObjectOutputStream out = new ObjectOutputStream(fileout);

            this.user.setIngredientsInventory((Inventory) inventory);

            out.writeObject(inventory);
            out.close();
            fileout.close();
        } catch (IOException e) {
            assert(true); //non salvo nulla
        }

         /*
        Siccome si sta scrivendo dati aggiornati, va scritto anche il flag relativo all'ultima versione di InventoryDAO che li ha salvati (true file system, false altrimenti).
        Lo si fa su un file apposito. In questo modo, quando verrà lanciato un InventoryDAO in versione DBMS, saprà che l'ultimo DAO ad aver scritto era di tipo file system, e che quindi i dati sui file
        devono essere aggiornati da quelli presenti sul DB.
         */
        try {
            this.writeLastUsed(true);
        } catch (IOException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    /*
    Questo metodo recupera dai file l'istanza di inventario relativa all'utente correntemente loggato.
     */
    @Override
    public InventoryBase retrieveInventory(){
        Inventory currInv;
        FileInputStream filein = null;

        try{
            filein = new FileInputStream(inventoryFileName);
            ObjectInputStream inputObjStream = new ObjectInputStream(filein);
            currInv = (Inventory) inputObjStream.readObject();
            inputObjStream.close();
            filein.close();
        }catch (FileNotFoundException e) {
            InventoryFactory inventoryFactory = new InventoryFactory();
            currInv = inventoryFactory.createInventory((UserCredentials) LoginController.getUserLogged());
        } catch(ClassNotFoundException | IOException e){
            return null;
        }

        /*
        L'InventoryDAO è la prima classe del sistema che entra in contatto diretto con l'istanza di inventario dell'utente corrente. Per questo motivo è ad esso assegnata la responsabilità
        di settare il bean in pull model come observer dell'inventario.
         */
        InventoryTableDataBean observer = InventoryTableDataBean.getSingletonInstance();
        currInv.attach(observer);
        observer.setSubject(currInv);
        return currInv;
    }

    /*
    Questo metodo serve a rendere consistenti i dati tra file system e DBMS. Legge il flag dell'ultimo tipo di inventoryDAO ad aver scritto in memoria
     */
    @Override
    protected void makeDataConsistent() throws IOException, ClassNotFoundException {
        try {
            FileInputStream filein = new FileInputStream(this.lastUsedDaoFileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(filein);
            boolean lastWriter = (boolean) objectInputStream.readObject();

            //se l'ultimo dao ad aver scritto era di tipo file system, come quello corrente, non faccio nulla

            if (!lastWriter) {
                //qui al contrario devo copiare i dati dal db
                DBMSInventoryDAO dbmsDao = new DBMSInventoryDAO();
                this.saveInventory(dbmsDao.retrieveInventory());
            }
        }catch (FileNotFoundException e){
            assert(true); //sel il file non c'è non devo fare nulla
        }
    }

}
