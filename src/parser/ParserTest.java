package parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParserException() {
       
        
       //This is a equivalence test case for the invalid command partition
        testCommand("invalid method name","Available commands : \"create\", \"update\", \"delete\", \"search\", \"display\", \"undo\" \n","put");
        
        //This is a equivalence test case for invalid arguments partition for the create method
        testCommand("test for invalid arguments",("Arguments required\n"+ "Format for Floating Task : add <taskName>\n"+"Format for Deadline Task : add <taskName> --en <endDate>\n" +"Format for Time Task : add <taskName> --st <startDate> --en <endDate>\n"),"create");
        
        //This is test to check if an exception is thrown if the time or date format for start and end is different
        testCommand("test for different date and time formats","start and end must have same format","create assignment 1 --start 22 Oct 2014 at 1000 --end 23/10/2014 at 2359");
    
        //
    }
    private void testCommand(String description,String expected,String command){
        Parser parser = new Parser();
        try{
            parser.parse(command);
            
        } catch(Exception e){
            assertEquals(description,expected,e.getMessage() );
        }
    };
}
