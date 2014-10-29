package pact;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import parser.Parser;
import utility.Keyword;


public class PactTest {
    @Test
    public void test() {
        //testCommand("add a task", "")
        testCommand("display when text file is empty","Sorry, nothing found.", "display");
    }

    private void testCommand(String description, String expected, String command) {
        ArrayList<String> expectedA = new ArrayList<String>();
        HashMap<Keyword, String> parsedCommand;
        ArrayList<String> result;
        Parser parser = new Parser();
        EventHandler eh = new EventHandler();
        expectedA.add(expected);
        
        try {
            parsedCommand = parser.parse(command);
            result = eh.determineCommand(parsedCommand);
            assert(expectedA.equals(result));
        }
        catch( Exception e) {
            
        }
        expectedA.clear(); 
    }





}
