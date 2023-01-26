package code_reuse;

import beans.RecipeIngredientBean;
import exceptions.RecipeIngredientQuantityException;

import java.text.ParseException;
import java.util.Scanner;

public class InputReusableUtilities {

    public static boolean yes(Scanner sc){
        String answer = sc.nextLine();
        return (answer.equals("y") || answer.equals("Y"));
    }

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

    public static String[] getCredentials(Scanner sc){
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        String[] creds = {username, password};
        return creds;
    }

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
                case 2 -> measureUnit = "H";
            }
        }
        while(true){
            if(value > 0) break;
            System.out.println("How many?");
            try{
                value = Double.parseDouble(sc.nextLine());
                if(value <= 0){
                    System.out.println("Invalid value, press enter to continue");
                    sc.nextLine();
                }
            }catch(NumberFormatException e){
                System.out.println("Invalid value, press enter to continue");
                sc.nextLine();
            }
        }
        return value+" "+measureUnit;
    }

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

    public static RecipeIngredientBean getIngredientData(Scanner sc) {
        RecipeIngredientBean newIngredient = new RecipeIngredientBean();
        while(true){
            try{
                System.out.println();
                System.out.print("Name: ");
                newIngredient.setName(sc.nextLine());
                System.out.print("Quantity (don't type anything if you don't want to specify it): ");
                String quantity = sc.nextLine();
                if(quantity.length() == 0) newIngredient.setQuantity("",true);
                else newIngredient.setQuantity(quantity);
                newIngredient.setMeasureUnit(InputReusableUtilities.measureUnitInput(sc));
                System.out.println("Are you sure to proceed? (y/n)");
                if(InputReusableUtilities.yes(sc)) return newIngredient;
            }catch(IllegalArgumentException e){
                System.out.println();
                System.out.println("Invalid value, press enter to continue");
                sc.nextLine();
            } catch (ParseException | RecipeIngredientQuantityException e) {
                System.out.println();
                System.out.println("Invalid quantity, press enter to continue");
                sc.nextLine();
            }

        }
    }

}
