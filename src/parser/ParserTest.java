package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParserException() {
        
        Parser p = new Parser();
        String command = "put";
        String description = "check if parser throws exception for invlaid command";
        String expected = "Available commands : \"create\", \"update\", \"delete\", \"search\", \"display\", \"undo\" \n";
        try{
            p.parse(command);
        } catch(Exception e){
            assertEquals(description,expected,e.getMessage());
            
        }
    }

}
