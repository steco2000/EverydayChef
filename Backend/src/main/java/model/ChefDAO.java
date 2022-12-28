package model;

import control.LoginController;

import java.io.*;

public class ChefDAO {

    private static final String CHEF_FILE_NAME = "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\chefs.ser";
    private static final String LAST_ID_FILE_NAME = "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\last_chef_id.ser";

    //TODO: eccezioni
    public int getLastId() throws IOException, ClassNotFoundException {
        FileInputStream filein = new FileInputStream(LAST_ID_FILE_NAME);
        ObjectInputStream in = new ObjectInputStream(filein);

        int id = (int) in.readObject();
        return id;
    }

    public boolean chefNotExists(String email, String username){
        Chef currChef;
        FileInputStream filein = null;

        try{
            filein = new FileInputStream(CHEF_FILE_NAME);
            while(true){
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currChef = (Chef) inputObjStream.readObject();
                System.out.println("Current email: "+currChef.getEmail()+", current username: "+currChef.getUsername()+", current pw: "+currChef.getPassword());
                if((currChef.getEmail().equals(email)) || (currChef.getUsername().equals(username))){
                    filein.close();
                    return false;
                }
            }
        }catch (EOFException e) {
            System.out.println("End of file");
            try {
                filein.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //TODO: eccezioni
    public void saveChef(Chef chef) throws IOException {
        FileOutputStream fileout = new FileOutputStream(CHEF_FILE_NAME, true);
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        FileOutputStream fileoutID = new FileOutputStream(LAST_ID_FILE_NAME);
        ObjectOutputStream outID = new ObjectOutputStream(fileoutID);

        out.writeObject(chef);
        out.close();
        fileout.close();

        outID.writeObject(chef.getId());
        outID.close();
        fileoutID.close();
    }

    //TODO: eccezioni
    public ChefBase retrieveChef(int id){
        FileInputStream filein = null;
        Chef currChef = null;

        try {
            filein = new FileInputStream(CHEF_FILE_NAME);
            while (true) {
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currChef = (Chef) inputObjStream.readObject();
                if (currChef.getId() == id) {
                    filein.close();
                    return currChef;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (EOFException e) {
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean credentialsAreCorrect(String username, String password) {
        Chef currChef;
        FileInputStream filein = null;

        try {
            filein = new FileInputStream(CHEF_FILE_NAME);
            while (true) {
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currChef = (Chef) inputObjStream.readObject();
                System.out.println("Current email: " + currChef.getEmail() + ", current username: " + currChef.getUsername() + ", current pw: " + currChef.getPassword());
                System.out.println("Typed username: " + username + ", typed pw: " + password);
                if ((currChef.getPassword().equals(password)) && (currChef.getUsername().equals(username))) {
                    LoginController.chefLogged = currChef;
                    filein.close();
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (EOFException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
