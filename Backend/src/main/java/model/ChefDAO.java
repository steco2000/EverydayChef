package model;

import control.LoginController;

import java.io.*;

//TODO: eccezioni

public class ChefDAO {

    private static final String CHEF_FILE_NAME = "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\chefs.ser";
    private static final String LAST_ID_FILE_NAME = "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\last_chef_id.ser";

    public int getLastId() throws IOException, ClassNotFoundException {
        FileInputStream filein = new FileInputStream(LAST_ID_FILE_NAME);
        ObjectInputStream in = new ObjectInputStream(filein);

        return (int) in.readObject();
    }

    public boolean chefNotExists(String email, String username){
        Chef currChef;
        FileInputStream filein = null;

        try{
            filein = new FileInputStream(CHEF_FILE_NAME);
            while(true){
                ObjectInputStream inputObjStream = new ObjectInputStream(filein);
                currChef = (Chef) inputObjStream.readObject();
                if((currChef.getEmail().equals(email)) || (currChef.getUsername().equals(username))){
                    filein.close();
                    return false;
                }
            }
        }catch (EOFException e) {
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
                if ((currChef.getPassword().equals(password)) && (currChef.getUsername().equals(username))) {
                    LoginController.setChefLogged(currChef);
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
