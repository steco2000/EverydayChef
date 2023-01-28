package dao;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;

import beans.InventoryTableDataBean;
import control.LoginController;
import factories.IngredientFactory;
import factories.InventoryFactory;
import model.*;

public class DBMSInventoryDAO extends InventoryDAO{

    private static final String USER = "admin";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventoryDB";
    private final String pwPath;
    private static String pass;

    public DBMSInventoryDAO(){
        super();

        this.pwPath = Paths.get("Backend\\src\\main\\resources\\inventoryDB\\dbpass.ser").toAbsolutePath().toString();
        try{
            FileInputStream filein = new FileInputStream(pwPath);
            ObjectInputStream objIn = new ObjectInputStream(filein);
            pass = (String) objIn.readObject();
            filein.close();
            objIn.close();
        }catch(FileNotFoundException e){
            this.storePassword();
        } catch (IOException | ClassNotFoundException ignored) {
            assert(true); //eccezione ignorata
        }

        try {
            this.makeDataConsistent();
        } catch (IOException | ClassNotFoundException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    private void storePassword() {
        try {
            FileOutputStream filein = new FileOutputStream(pwPath);
            ObjectOutputStream objOut = new ObjectOutputStream(filein);
            objOut.writeObject(USER);
            filein.close();
            objOut.close();
        } catch (IOException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    @Override
    public void saveInventory(InventoryBase inventory){
        user.setIngredientsInventory((Inventory) inventory);
        Statement stmt = null;
        Connection conn;

        try{
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, pass);
            stmt = conn.createStatement();
            stmt.execute("insert into UserCredentials(username,password,email) values('"+user.getUsername()+"','"+user.getPassword()+"','"+user.getEmail()+"')");
            stmt.execute("insert into inventory(user) values('"+user.getUsername()+"')");
            for(InventoryIngredient i : ((Inventory) inventory).getIngredientList()){
                java.sql.Date sqlDate = new java.sql.Date(i.getExpirationDate().getTime());
                stmt.execute("insert into InventoryIngredient(name,inventory,quantity,measureUnit,expirationDate,notes) values('"+i.getName()+"','"+user.getUsername()+"','"+i.getQuantity()+"','"+i.getMeasureUnit()+"','"+sqlDate+"','"+i.getNotes()+"')");
            }
        } catch (SQLIntegrityConstraintViolationException e){
            this.updateExistingInventory(stmt,inventory,user);
        } catch (ClassNotFoundException | SQLException e) {
            assert(true); //impossibile salvare
        }

        try {
            this.writeLastUsed(false);
        } catch (IOException ignored) {
            assert(true); //eccezione ignorata
        }
    }

    private void updateExistingInventory(Statement stmt, InventoryBase inventory, UserCredentials user) {
        try {
            stmt.execute("delete from inventoryingredient where inventory = '"+user.getUsername()+"'");
            for (InventoryIngredient i : ((Inventory) inventory).getIngredientList()) {
                java.sql.Date sqlDate = new java.sql.Date(i.getExpirationDate().getTime());
                stmt.execute("insert into InventoryIngredient(name,inventory,quantity,measureUnit,expirationDate,notes) values('" + i.getName() + "','" + user.getUsername() + "','" + i.getQuantity() + "','" + i.getMeasureUnit() + "','" + sqlDate + "','" + i.getNotes() + "')");
            }
        } catch (SQLException e) {
            assert(true); //non c'è nulla da aggiornare
        }
    }

    @Override
    public InventoryBase retrieveInventory(){
        Statement stmt = null;
        Connection conn = null;
        Inventory currInv;

        try{
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, pass);
            stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("select * from InventoryIngredient where inventory = '"+user.getUsername()+"'");
            if(!result.isBeforeFirst()){
                InventoryFactory inventoryFactory = new InventoryFactory();
                currInv = inventoryFactory.createInventory((UserCredentials) LoginController.getUserLogged());
                user.setIngredientsInventory(currInv);
            }else{
                InventoryFactory inventoryFactory = new InventoryFactory();
                currInv = inventoryFactory.createInventory(user);
                while(result.next()){
                    IngredientFactory ingredientFactory = new IngredientFactory();
                    IngredientBase ingredient = ingredientFactory.createIngredient(result.getString(1),Double.parseDouble(result.getString(3)),result.getString(4),result.getDate(5),result.getString(6));
                    currInv.addIngredient(ingredient);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }

        InventoryTableDataBean observer = InventoryTableDataBean.getSingletonInstance();
        currInv.attach(observer);
        observer.setSubject(currInv);
        return currInv;
    }

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
