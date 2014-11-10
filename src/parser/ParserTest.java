package parser;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import utility.Keyword;

public class ParserTest {
    //test if parser is throwing exceptions correctly
    @Test
    public void testParserException() {
         //String MISSING_ENDDATE = "End date required\n";
         String NO_ARGUMENTS_ADD = "Arguments required for adding a task\n\n";
         String NO_ARGUMENTS_UPDATE = "Arguments required for updating a task\n\n";
         String NO_ARGUMENTS_DELETE = "Arguments required for deleting a task\n\n";
         String NO_ARGUMENTS_COMPLETED = "Arguments are required for completing tasks\n\n";
         String NO_ARGUMENTS_INCOMPLETE = "Arguments are required for incomplete task command\n\n";
         String NO_ARGUMENTS_EMPTYSLOT = "Arguments are required to search for empty slots\n\n";
         String COMPLETED_TASK_FORMAT = "Format for complete task : completed <taskName>\n";
         String INCOMPLETE_TASK_FORMAT = "Format for incomplete task : incomplete <taskName>\n";
         String TIMED_TASK_FORMAT = "Format for Time Task : add <taskName> --st <startDate> at <time> --en <endDate> at <time>\n";
         String DEADLINE_TASK_FORMAT = "Format for Deadline Task : add <taskName> --en <endDate> at <time>\n";
         String FlOATING_TASK_FORMAT = "Format for Floating Task : add <taskName>\n";
         String EMPTYSLOT_TASK_FORMAT ="Format for EmptySlot : emptyslot --after <startDate> --before <endDate>\n";
         String UPDATE_FORMAT = "Format for update/change : update/change <taskName> --<field> <changeToValue>\n";
         String DELETE_FORMAT = "Format for delete : delete <taskName>\n";
         String INVALID_COMMAND = "Invalid Command\n\n";
         String AVAILABLE_COMMANDS = "Available commands : \"create\", \"update\", \"delete\", \"search\", \"display\", \"undo\", \"completed\", \"exit\", \"qexit\" \n";
         //String NON_MATCHING_FORMAT = "Start and end time must have the same format\n";
         //String IMPROPER_ARGUMENT = "Argument is invalid\n";
         String INCORRECT_TIME_FORMAT = "Incorrect format to add time\n";
         //String EXCEEDED_CHAR_LIMIT = "Task description has exceeded 30 characters";
         String INVALID_DATE = "You have entered an invalid date.\n\n";
         String EXCEED_DATE_LIMIT ="Maximum search period for emptyslot is 7 days\n\n";
         String HELP = "Valid Formats:\n";
       
        
        
       //This is a equivalence test case for the invalid command partition
        testCommandException("invalid command",INVALID_COMMAND + AVAILABLE_COMMANDS,"put");
        
        //This is a equivalence test case for invalid arguments partition for the create method
        testCommandException("Create: test for invalid arguments",NO_ARGUMENTS_ADD + HELP + FlOATING_TASK_FORMAT + DEADLINE_TASK_FORMAT + TIMED_TASK_FORMAT ,"create");
        
        //invalid arguments for update method
        testCommandException("Update: test for invalid argument",NO_ARGUMENTS_UPDATE + HELP + UPDATE_FORMAT,"update" );
        
        //invalid arguments for delete method
        testCommandException("Delete:test for invalid argument",NO_ARGUMENTS_DELETE + HELP + DELETE_FORMAT,"delete");
        
        //invalid arguments for complete method
        testCommandException("Complete: test for invalid argument",NO_ARGUMENTS_COMPLETED + HELP + COMPLETED_TASK_FORMAT,"complete");
        
        //invalid arguments for incomplete command
        testCommandException("inComplete: test for invalid argument",NO_ARGUMENTS_INCOMPLETE + HELP + INCOMPLETE_TASK_FORMAT ,"uncomplete");
        
        //incorrect format for empty slot
        testCommandException("Emptyslot:test for invalid arguments",NO_ARGUMENTS_EMPTYSLOT + HELP + EMPTYSLOT_TASK_FORMAT,"emptyslot 12122014");
       
        //This is test to check if an exception is thrown if user enters time without the "at" keyword
        testCommandException("No at keyword before adding time",INCORRECT_TIME_FORMAT + HELP + TIMED_TASK_FORMAT + DEADLINE_TASK_FORMAT , "create assignment 1 --start 22 Oct 2014 1000 --end 23/10/2014 2359");
    
        //invalid date
        testCommandException("invalid date",INVALID_DATE + HELP + FlOATING_TASK_FORMAT + DEADLINE_TASK_FORMAT + TIMED_TASK_FORMAT, "add task 5 --end 121212345");
        
        //emptyslot time frame exceeds 7 days
        testCommandException("Emptyslot: timeframe exceeds 7 days limit",EXCEED_DATE_LIMIT, "find --start 12112014 --end 20112014");
    }
    private void testCommandException(String description, String expected,String command) {
        Parser parser = new Parser();
        try {
            parser.parse(command);
            
        } catch (Exception e) {
            assertEquals(description,expected,e.getMessage() );
        }
    }
    @Test
    public void testParser() {
        testCommand("check create command","create","go for lunch","create go for lunch");
    }
    
    private void testCommand(String description,String expectedCommand,String expectedArgument, String command) {
        HashMap <Keyword, String> expected = new HashMap<Keyword, String>();
        expected.put(Keyword.METHOD,expectedCommand);
        expected.put(Keyword.CONTENT,expectedCommand);
        Parser p =new Parser();
        try {
            HashMap<Keyword, String> results =p.parse(command);
            assert(expected.equals(results));
        } catch (Exception e) {
        }
    }
}
