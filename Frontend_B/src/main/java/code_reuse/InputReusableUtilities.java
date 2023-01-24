package code_reuse;

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

}
