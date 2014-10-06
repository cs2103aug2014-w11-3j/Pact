package pact;

import java.util.ArrayList;

public class Parser {
        
    public ArrayList<String> parse(String userInput) {
        ArrayList<String> result = new ArrayList<String>();
        String[] splitString = userInput.trim().split(" ", 2);
        CommandType code = CommandType.getCommandType(splitString[0]);
        if (code == CommandType.CREATE) {
            ParserForCREATE subParser = new 
            //CREATE
        } else if (code == CommandType.READ) {
            //READ
        } else if (code == CommandType.UPDATE) {
            //UPDATE
        } else if (code == CommandType.DELETE) {
            //DELETE
        } else if (code == CommandType.EXIT) {
            //EXIT
        } else if (code == CommandType.INVALID) {
            //NOT A COMMAND
        }
        return result;
    }
    
    public static void main(String args[]) throws Exception {
        Parser parser = new Parser();
        parser.parse("add Duong Dat to");
        parser.parse("show Duong Dat to");
        parser.parse("display Duong Dat to");
        parser.parse("clgt Duong Dat to");
    }
}
