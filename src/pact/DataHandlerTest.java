package pact;

import static org.junit.Assert.*;

import utility.Keyword;
import org.junit.Test;
import org.junit.Before;

public class DataHandlerTest {
    private DataHandler dh;

    @Before
    public void setUp() throws Exception {
        dh = new DataHandler();
    }
    
    @Test
    public void test() {
        dh.deleteTask("",true);
    }

}




