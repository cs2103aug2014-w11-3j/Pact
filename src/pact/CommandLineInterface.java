package pact;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import parser.Parser;

public class CommandLineInterface {

    private Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        Parser commandParser = new Parser();
        EventHandler logic = new EventHandler();
        ArrayList<String> parsedCommand;
        while (true) {
            parsedCommand = commandParser.parse(cli.getUserCommand());
            try {
                logic.determineCommand(parsedCommand);
            } catch (ParseException pe) {
            }
        }
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
