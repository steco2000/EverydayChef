package code_reuse;

import beans.RecipeIngredientBean;
import exceptions.RecipeIngredientQuantityException;

import java.text.ParseException;
import java.util.Scanner;

//classe di utilities per input da console, con controlli sintattici di base

public class InputReusableUtilities {

    private static final String INVALID_VALUE_MESSAGE = "Invalid value, press enter to continue";

    private InputReusableUtilities(){
        assert(true); //costruttore privato per nascondere quello pubblico implicito
    }

    //metodo che serve a ricevere in input la risposta a una domanda con risposta del tipo si o no
    public static boolean yes(Scanner sc){
        String answer = sc.nextLine();
        return (answer.equals("y") || answer.equals("Y"));
    }

    /*
    metodo che serve a ricevere in input l'indice di una azione da eseguire o di qualcosa da selezionare da una lista. Deve essere un valore intero e si specificano quale è il primo
    ammesso e quale l'ultimo
     */
    public static int getAnswer(Scanner sc, int firstAllowed, int lastAllowed) throws NumberFormatException{
        int answer;
        try{
            answer = Integer.parseInt(sc.nextLine());
            if(answer < firstAllowed || answer > lastAllowed){
                System.out.println("Invalid answer value, press enter or digit something to continue");
                sc.nextLine();
                return -1;
            }
            return answer;
        }catch(NumberFormatException e){
            System.out.println("Invalid answer, press enter or digit something to continue");
            sc.nextLine();
            return -1;
        }
    }

    //metodo che gestisce l'input di qualunque tipo di credenziale in fase di login
    public static String[] getCredentials(Scanner sc){
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        return new String[] {username, password};
    }

    //metodo che gestisce l'input dell'unità di misura di un ingrediente. Ritorna solo i formati ammessi dal sistema
    public static String measureUnitInput(Scanner sc){
        while(true) {
            System.out.println("Measure Unit - press:");
            System.out.println("1) For Kg");
            System.out.println("2) For L");
            System.out.println("3) For not specified");
            int answer = InputReusableUtilities.getAnswer(sc, 1, 3);
            switch (answer) {
                case -1:
                    continue;
                case 1:
                    return "Kg";
                case 2:
                    return "L";
                default:
                    return "";
            }
        }
    }

    //metodo per la gestione dell'input del tempo di preparazione di una ricetta, formato e valore
    public static  String preparationTimeInput(Scanner sc) {
        String measureUnit = "";
        double value = -1;
        while(true){
            if(!measureUnit.isEmpty()) break;
            System.out.println("Preparation time:");
            System.out.println("This recipe requires minutes or hours? Press:");
            System.out.println("1) For minutes");
            System.out.println("2) For hours");
            int ans = InputReusableUtilities.getAnswer(sc,1,2);
            switch (ans){
                case -1 -> {
                    assert(true); //errore nella risposta, non faccio nulla
                }
                case 1 -> measureUnit = "min";
                default -> measureUnit = "H";
            }
        }
        while(true){
            if(value > 0) break;
            System.out.println("How many?");
            try{
                value = Double.parseDouble(sc.nextLine());
                if(value <= 0){
                    System.out.println(INVALID_VALUE_MESSAGE);
                    sc.nextLine();
                }
            }catch(NumberFormatException e){
                System.out.println(INVALID_VALUE_MESSAGE);
                sc.nextLine();
            }
        }
        return value+" "+measureUnit;
    }

    //metodo per la gestione dell'input della difficoltà di una ricetta. Ritorna solo i valori consentiti dal sistema
    public static String difficultyInput(Scanner sc) {
        while(true) {
            System.out.println("Press:");
            System.out.println("1) To set Easy");
            System.out.println("2) To set Medium");
            System.out.println("3) To set Hard");
            int diff = InputReusableUtilities.getAnswer(sc,1,3);

            switch (diff){
                case -1 -> {
                    assert(true); //errore nella risposta non faccio nulla
                }
                case 1 -> {
                    return "Easy";
                }
                case 2 -> {
                    return "Medium";
                }
                default -> {
                    return "Hard";
                }
            }
        }
    }

    //metodo per la gestione dell'input di un ingrediente della ricetta, con tutti i dati richiesti
    public static RecipeIngredientBean getIngredientData(Scanner sc) {
        RecipeIngredientBean newIngredient = new RecipeIngredientBean();
        while(true){
            try{
                System.out.println();
                System.out.print("Name: ");
                newIngredient.setName(sc.nextLine());
                System.out.print("Quantity (only value without measure unit, type -1 if you want to set \"Just Enough\"): ");
                String quantity = sc.nextLine();
                if(Double.parseDouble(quantity)!=-1) {
                    newIngredient.setQuantity(quantity,false);
                    newIngredient.setMeasureUnit(InputReusableUtilities.measureUnitInput(sc));
                }else{
                    newIngredient.setQuantity(quantity,true);
                    newIngredient.setMeasureUnit("");
                }
                System.out.println("Are you sure to proceed? (y/n)");
                if(InputReusableUtilities.yes(sc)) return newIngredient;
            }catch(IllegalArgumentException e){
                System.out.println();
                System.out.println(INVALID_VALUE_MESSAGE);
                sc.nextLine();
            } catch (ParseException | RecipeIngredientQuantityException e) {
                System.out.println();
                System.out.println("Invalid quantity, press enter to continue");
                sc.nextLine();
            }

        }
    }

}
