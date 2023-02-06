package dao;

import control.LoginController;
import model.Chef;
import model.ChefBase;
import model.UserCredentials;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChefDAO {

    private final String chefFileName;
    private final String lastIdFileName;

    public ChefDAO(){
        Path relativeChefFilePath = Paths.get("Backend\\src\\main\\resources\\chef_data\\chef_data_");
        chefFileName = relativeChefFilePath.toAbsolutePath().toString();
        Path relativeLastIdFilePath = Paths.get("Backend\\src\\main\\resources\\chef_data\\last_chef_id.ser");
        lastIdFileName = relativeLastIdFilePath.toAbsolutePath().toString();
    }

    public int getLastId() throws IOException, ClassNotFoundException {
        FileInputStream filein = new FileInputStream(lastIdFileName);
        ObjectInputStream in = new ObjectInputStream(filein);

        return (int) in.readObject();
    }

    public boolean chefNotExists(String email, String username){
        Chef currChef;
        FileInputStream filein;
        ObjectInputStream objStream;

        File folder = new File(Paths.get("Backend\\src\\main\\resources\\chef_data\\").toAbsolutePath().toString());
        File[] listOfFiles = folder.listFiles();

        try{
            for(File f: listOfFiles){
                if(f.getName().equals(".placeholder") || f.getName().equals("last_chef_id.ser")) continue;
                filein = new FileInputStream(Paths.get("Backend\\src\\main\\resources\\chef_data\\"+f.getName()).toAbsolutePath().toString());
                objStream = new ObjectInputStream(filein);
                currChef = (Chef) objStream.readObject();
                if((currChef.getEmail().equals(email)) || (currChef.getUsername().equals(username))){
                    filein.close();
                    return false;
                }
            }
            return true;
        } catch(ClassNotFoundException | IOException e){
            return false;
        }
    }

    public void saveChef(ChefBase chef, boolean overwrite) throws IOException {
        FileOutputStream fileout = new FileOutputStream(chefFileName+chef.getUsername()+".ser", true);
        ObjectOutputStream out = new ObjectOutputStream(fileout);

        if(!overwrite) {
            FileOutputStream fileoutID = new FileOutputStream(lastIdFileName);
            ObjectOutputStream outID = new ObjectOutputStream(fileoutID);

            outID.writeObject(chef.getId());
            outID.close();
            fileoutID.close();
        }

        out.writeObject(chef);
        out.close();
        fileout.close();
    }

    public boolean credentialsAreCorrect(String username, String password) {
        Chef currChef;
        FileInputStream filein = null;

        try {
            filein = new FileInputStream(chefFileName + username + ".ser");
            ObjectInputStream inputObjStream = new ObjectInputStream(filein);
            currChef = (Chef) inputObjStream.readObject();
            if ((currChef.getPassword().equals(password)) && (currChef.getUsername().equals(username))) {
                LoginController.setChefLogged(currChef);
                filein.close();
                return true;
            }
            return false;
        } catch (ClassNotFoundException | IOException e) {
            return false;
        }
    }


    public ChefBase retrieveChef(String chefUsername) {
        FileInputStream filein;
        try {
            filein = new FileInputStream(chefFileName + chefUsername + ".ser");
            ObjectInputStream inputObjStream = new ObjectInputStream(filein);
            return (ChefBase) inputObjStream.readObject();
        } catch (ClassNotFoundException | IOException ignored) {
            return null;
        }
    }

    public int retrieveChefId(String chefUsername){
        Chef chef = (Chef) this.retrieveChef(chefUsername);
        return chef.getId();
    }

}
