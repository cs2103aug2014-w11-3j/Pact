package pact;

import parser.Parser;
import utility.Keyword;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Scanner;

public class CommandLineInterface {

    private Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        Parser commandParser = new Parser();
        EventHandler logic = new EventHandler();
        HashMap<Keyword, String> parsedCommand = new HashMap<Keyword, String>();
        System.out.println("Welome to PACT");
        System.out.println("Available commands : \"create\", \"update\", \"delete\", \"search\", \"display\"");
        while (true) {
            try{
                parsedCommand = commandParser.parse(cli.getUserCommand());
                logic.determineCommand(parsedCommand);
            }
            catch(ParseException pe){
                System.out.println("Please try again: Invalid command!");
                System.out.println("Available commands : \"create\", \"update\", \"delete\", \"search\", \"display\"");
            } catch (Exception pe) {
                System.out.println("Invalid format!");
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