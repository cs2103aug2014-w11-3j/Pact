package pact;

import java.util.Scanner;

public class CommandLineInterface {

    private Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        
    }
    
    public int printToUser(String message) {
        System.out.println(message);
        return 0;
    }
    
    private String getUserCommand() { //get from Scanner or something else
        String command;
        do {
            command = scanner.nextLine();
        } while (command.isEmpty());
        return command;
    }
}
