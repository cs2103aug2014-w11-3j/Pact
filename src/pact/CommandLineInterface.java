package pact;

import parser.Parser;
import utility.Keyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CommandLineInterface {

    private Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        Parser commandParser = new Parser();
        EventHandler logic = new EventHandler();
        HashMap<Keyword, String> parsedCommand;
        ArrayList<String> result;
        System.out.println("Welome to PACT");
        System.out.println("Available commands : \"create\", \"update\", \"delete\", \"search\", \"display\", \"undo\"");
        while (true) {
            try {
                parsedCommand = commandParser.parse(cli.getUserCommand());
                result = logic.determineCommand(parsedCommand);
                for (String st : result) {
                    System.out.println(st);
                }
            } catch (Exception pe) {
                System.out.println("Please try again: Invalid command!");
              //System.out.println("Available commands : \"create\", \"update\", \"delete\", \"search\", \"display\", \"undo\"");
                System.out.println(pe.getMessage());
            }
        }
    }
    
    public void printToUser(String message) {
        System.out.println(message);
    }
    
    private String getUserCommand() { //get from Scanner or something else
        String command;
        do {
            command = scanner.nextLine();
            command = command.trim();
        } while (command.isEmpty());
        return command;
    }
}