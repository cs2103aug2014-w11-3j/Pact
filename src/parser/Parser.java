package parser;

import java.util.ArrayList;

import pact.CommandType;

public class Parser {
        
    public ArrayList<String> parse(String userInput) {
        ParserForAll subParser = null;
        String[] splitString = userInput.trim().split(" ", 2);
        CommandType code = CommandType.getCommandType(splitString[0]);
        if (code == CommandType.CREATE) {
            subParser = new ParserForCreate();
            
        } else if (code == CommandType.READ) {
            subParser = new ParserForRead();
            
        } else if (code == CommandType.UPDATE) {
            subParser = new ParserForUpdate();
            
        } else if (code == CommandType.DELETE) {
            subParser = new ParserForDelete();
            
        } else if (code == CommandType.EXIT) {
            
        } else if (code == CommandType.UNDO) {
            
        } else if (code == CommandType.INVALID) {
            //maybe throw an Exception?
        }

        subParser.getParameters(splitString[1]);
        return subParser.parameters;
    }
    
    public static void main(String args[]) throws Exception {
        Parser parser = new Parser();
        parser.parse("add Duong Dat to");
    }
}
