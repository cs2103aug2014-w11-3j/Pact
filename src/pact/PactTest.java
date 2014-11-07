package pact;

import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import parser.Parser;
import utility.Keyword;


public class PactTest {
    @Test
    public void test() {
       
       
        testCommand("add a task", "Task was added successfully.","create go for lunch");
        testCommand("add a task","Task was added successfully.","create assignment --end 12112014");
        testCommand("delete a task", "Task was deleted successfully.","delete go for lunch");
        testCommand("update a task","Task was updated successfully.", "update assignment --name assignment 4");
        testCommand("undo an action", "Undo was successful.","undo");
        testCommand("add a task", "Task was added successfully.", "add homework --start 12112014 at 1200 --end 13112014 at 1200");
        testCommand("clear the file","All tasks cleared successfully.","clear");
        testCommand("display when text file is empty","There are no tasks in your task list", "display");
        
    }

    private void testCommand(String description, String expected, String command) {
        
        HashMap<Keyword, String> parsedCommand;
        ArrayList<String> result;
        Parser parser = new Parser();
        EventHandler eh = new EventHandler();
        
        
        try {
            parsedCommand = parser.parse(command);
            result = eh.determineCommand(parsedCommand);
            assertEquals(description,expected,result.get(0));
        
        }
        catch( Exception e) {
            
        }
       
    }
}
