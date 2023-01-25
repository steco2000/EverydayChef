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

}
