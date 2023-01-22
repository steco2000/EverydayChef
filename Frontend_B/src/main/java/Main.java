import java.io.Console;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Console console = System.console();
        Scanner scanner = new Scanner(console.reader());
        (new UserLoginView(console, scanner)).display();
    }

}
