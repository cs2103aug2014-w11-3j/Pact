package pact;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;

import utility.Keyword;

import org.junit.Test;
import org.junit.Before;


public class EventHandlerTest {
    private EventHandler ev;
    @Before
    public void setUp() throws Exception {
        ev = new EventHandler();
    }

    @Test
    public void test2() {
        HashMap<Keyword, String> hm = new HashMap<Keyword, String>();
        ArrayList<String> result = new ArrayList<String>();
        hm.put(Keyword.METHOD, "add");
        hm.put(Keyword.CONTENT, "Visit the zoo");
        try {
            result = ev.determineCommand(hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(result.get(0), "Added Successfully.");
        result.clear();
        hm.clear();

        hm.put(Keyword.METHOD, "add");
        hm.put(Keyword.CONTENT, "Visit the zoo1");
        hm.put(Keyword.ALLDAY, "false");
        hm.put(Keyword.TYPE, "deadline");
        try {
            result = ev.determineCommand(hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(result.get(0), "Added Successfully.");

        result.clear();
        hm.clear();
        hm.put(Keyword.METHOD, "undo");

        try {
            result = ev.determineCommand(hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(result.get(0) , "Undo Successfully.");

        result.clear();
        hm.clear();
        hm.put(Keyword.METHOD, "display");
        hm.put(Keyword.CONTENT, "");
        try {
            result = ev.determineCommand(hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(result.get(0), "1. Visit the zoo");
    }

}
