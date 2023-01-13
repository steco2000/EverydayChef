package dao;

import control.LoginController;
import model.Chef;
import model.ChefBase;

import java.io.*;

//TODO: eccezioni

public class ChefDAO {

    private static final String CHEF_FILE_NAME = "C:\\Users\\darkd\\OneDrive\\Desktop\\Progetto ISPW\\EverydayChef\\Backend\\src\\main\\resources\\chef_data_";
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
            filein = new FileInputStream(CHEF_FILE_NAME+username+".ser");
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

    public void saveChef(ChefBase chef) throws IOException {
        FileOutputStream fileout = new FileOutputStream(CHEF_FILE_NAME+chef.getUsername()+".ser", true);
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

    public boolean credentialsAreCorrect(String username, String password) {
        Chef currChef;
        FileInputStream filein = null;

        try {
            filein = new FileInputStream(CHEF_FILE_NAME + username + ".ser");
            ObjectInputStream inputObjStream = new ObjectInputStream(filein);
            currChef = (Chef) inputObjStream.readObject();
            System.out.println("Inserite: "+username+" "+password);
            System.out.println("Lette da file: "+currChef.getUsername()+" "+currChef.getPassword());
            if ((currChef.getPassword().equals(password)) && (currChef.getUsername().equals(username))) {
                LoginController.setChefLogged(currChef);
                filein.close();
                return true;
            }
            return false;
        }catch (FileNotFoundException e) {
            return false;
        } catch (EOFException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }


    public ChefBase retrieveChef(String chefUsername) {
        FileInputStream filein;
        try {
            filein = new FileInputStream(CHEF_FILE_NAME + chefUsername + ".ser");
            ObjectInputStream inputObjStream = new ObjectInputStream(filein);
            return (ChefBase) inputObjStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
