package model;

public class ChefFactory {

    public static int nextChefId = 0;

    public ChefFactory(){
        //TODO: una volta salvato l'ultimo id creato in persistenza, recuperarlo e salvarlo nella variabile statica per le successive create
    }

    public ChefBase createChef(){
        Chef newChef = new Chef();
        //settaggio id corretto
        return newChef;
    }

}
