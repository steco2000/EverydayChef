package dao;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;

import beans.InventoryTableDataBean;
import control.LoginController;
import factories.IngredientFactory;
import factories.InventoryFactory;
import model.*;

//dao in versione DBMS per il salvataggio di istanze di tipo inventario

public class DBMSInventoryDAO extends InventoryDAO{

    private static final String USERNAME = "admin";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventoryDB";
    private static String pwPath;
    private static String pass;

    /*
    La password di accesso al db non viene scritta in chiaro nel codice, ma salvata su un file e recuperata all'occorrenza. In questo caso il db serve solo come supporto per salvare
    alcuni dati, non abbiamo quindi bisogno di definire ruoli o controllare gli accessi. Perciò il db possiede un solo utente globale, usato semplicemente come punto di accesso al db.
    Oltre a questo viene garantito che all'avvio i dati siano aggiornati.
     */
    public DBMSInventoryDAO(){
        super();
        if(pass == null) makePassword();
        try {
            this.makeDataConsistent();
        } catch (IOException | ClassNotFoundException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    //questo metodo recupera la password dal file quando questa non esiste
    private static void makePassword() {
        pwPath = Paths.get("Backend\\src\\main\\resources\\inventoryDB\\dbpass.ser").toAbsolutePath().toString();
        try{
            FileInputStream filein = new FileInputStream(pwPath);
            ObjectInputStream objIn = new ObjectInputStream(filein);
            pass = (String) objIn.readObject();
            filein.close();
            objIn.close();
        }catch(FileNotFoundException e){
            storePassword();    //se il file non esiste scriviamo la password sul file
        } catch (IOException | ClassNotFoundException ignored) {
            assert(true); //eccezione ignorata, stiamo scrivendo un tipo di dato primitivo
        }
    }

    //questo è il metodo che scrive la password sul file
    private static void storePassword() {
        try {
            FileOutputStream filein = new FileOutputStream(pwPath);
            ObjectOutputStream objOut = new ObjectOutputStream(filein);
            objOut.writeObject(USERNAME);
            filein.close();
            objOut.close();
        } catch (IOException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    /*
    In questo metodo si gestisce il salvataggio delle istanze di inventario sul db. In sostanza vengono lanciate una serie di query sul db in modo tale da poter salvare i dati
    aggiornati di tutte le istanze coinvolte, in modo compatibile allo schema del db.
     */
    @Override
    public void saveInventory(InventoryBase inventory){
        user.setIngredientsInventory((Inventory) inventory);
        Statement stmt = null;
        Connection conn;

        try{
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USERNAME, pass);
            stmt = conn.createStatement();

            //salvataggio dell'utente
            stmt.execute("insert into UserCredentials(username,password,email) values('"+user.getUsername()+"','"+user.getPassword()+"','"+user.getEmail()+"')");

            //associazione dell'utente all'inventario
            stmt.execute("insert into inventory(user) values('"+user.getUsername()+"')");

            //salvataggio della lista degli ingredienti
            for(InventoryIngredient i : ((Inventory) inventory).getIngredientList()){
                java.sql.Date sqlDate = new java.sql.Date(i.getExpirationDate().getTime());
                stmt.execute("insert into InventoryIngredient(name,inventory,quantity,measureUnit,expirationDate,notes) values('"+i.getName()+"','"+user.getUsername()+"','"+i.getQuantity()+"','"+i.getMeasureUnit()+"','"+sqlDate+"','"+i.getNotes()+"')");
            }
        } catch (SQLIntegrityConstraintViolationException e){

            //se viene lanciata questa eccezione l'inventario è già presente, bisogna solo aggiornarlo
            this.updateExistingInventory(stmt,inventory,user);

        } catch (ClassNotFoundException | SQLException e) {
            assert(true); //Se si verifica questa eccezione non c'è modo di salvare sul db
        }

        /*
        Siccome si sta scrivendo dati aggiornati, va scritto anche il flag relativo all'ultima versione di InventoryDAO che li ha salvati (true file system, false altrimenti).
        Lo si fa su un file apposito. In questo modo, quando verrà lanciato un InventoryDAO in versione file system, saprà che l'ultimo DAO ad aver scritto era di tipo DBMS, e che quindi i dati sui file
        devono essere aggiornati da quelli presenti sul DB.
         */
        try {
            this.writeLastUsed(false);
        } catch (IOException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    /*
    Se l'inventario che stiamo provando a scrivere esiste già, va solo modificato. Per fare ciò si fa prima a eliminare tutti gli ingredienti e a reinserirli completamente
     */
    private void updateExistingInventory(Statement stmt, InventoryBase inventory, UserCredentials user) {
        try {
            stmt.execute("delete from inventoryingredient where inventory = '"+user.getUsername()+"'");
            for (InventoryIngredient i : ((Inventory) inventory).getIngredientList()) {
                java.sql.Date sqlDate = new java.sql.Date(i.getExpirationDate().getTime());
                stmt.execute("insert into InventoryIngredient(name,inventory,quantity,measureUnit,expirationDate,notes) values('" + i.getName() + "','" + user.getUsername() + "','" + i.getQuantity() + "','" + i.getMeasureUnit() + "','" + sqlDate + "','" + i.getNotes() + "')");
            }
        } catch (SQLException e) {
            assert(true); //se si verifica questa eccezione non c'è modo di scrivere sul db
        }
    }

    /*
    Questo metodo recupera dal db l'istanza di inventario relativa all'utente correntemente loggato. I dati recuperati dal db non sono organizzati esattamente come è definita
    la rappresentazione nel model del sistema, perciò vanno "convertiti".
     */
    @Override
    public InventoryBase retrieveInventory(){
        Statement stmt = null;
        Connection conn = null;
        Inventory currInv;

        try{
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USERNAME, pass);
            stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("select * from InventoryIngredient where inventory = '"+user.getUsername()+"'");
            if(!result.isBeforeFirst()){

                //se si esegue questo blocco vuo dire che l'inventario non esiste, e viene quindi creato
                InventoryFactory inventoryFactory = new InventoryFactory();
                currInv = inventoryFactory.createInventory((UserCredentials) LoginController.getUserLogged());
                user.setIngredientsInventory(currInv);

            }else{

                //qui invece esiste, ma dobbiamo copiare i dati del db in una nuova istanza interna del sistema, facendo una apposita "conversione"
                InventoryFactory inventoryFactory = new InventoryFactory();
                currInv = inventoryFactory.createInventory(user);
                while(result.next()){
                    IngredientFactory ingredientFactory = new IngredientFactory();
                    IngredientBase ingredient = ingredientFactory.createIngredient(result.getString(1),Double.parseDouble(result.getString(3)),result.getString(4),result.getDate(5),result.getString(6));
                    currInv.addIngredient(ingredient);
                }

            }
        } catch (ClassNotFoundException | SQLException e) {
            return null;    //in questo caso si verifica un errore e si restituisce quindi null
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

            //se l'ultimo dao ad aver scritto era di tipo dbms, come quello corrente, non faccio nulla

            if (lastWriter) {
                //qui, al contrario, devo copiare i dati dai file
                FileSystemInventoryDAO fsDao = new FileSystemInventoryDAO();
                this.saveInventory(fsDao.retrieveInventory());
            }
        }catch(FileNotFoundException e){
            assert(true); //se il file non c'è non devo fare nulla
        }
    }

}
