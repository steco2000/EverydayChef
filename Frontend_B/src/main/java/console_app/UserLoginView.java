package console_app;

import java.io.Console;
import java.util.Scanner;

public class UserLoginView {

    private final Scanner scanner;
    private final Console console;

    public UserLoginView(Console console, Scanner scanner){
        this.scanner = scanner;
        this.console = console;
    }

    public void display(){
        this.console.printf("EverydayChef");
    }

}
