package pact;

import static org.junit.Assert.*;
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

	/*
	 * @Test public void test() { EventHandler ev = new EventHandler();
	 * HashMap<Keyword, String> hm = new HashMap<Keyword, String>();
	 * ArrayList<String> result = new ArrayList<String>();
	 * hm.put(Keyword.METHOD, "add"); hm.put(Keyword.CONTENT, "Visit the zoo");
	 * try { 
	 * result = ev.determineCommand(hm); 
	 * } catch (Exception e) { 
	 * Auto-generated catch block e.printStackTrace(); 
	 * }
	 * assertTrue(result.get(0).equals("Added Successfully.")); }
	 * 
	 * @Test public void test1() { 
	 * EventHandler ev = new EventHandler();
	 * HashMap<Keyword, String> hm = new HashMap<Keyword, String>();
	 * ArrayList<String> result = new ArrayList<String>();
	 * hm.put(Keyword.METHOD, "add");
	 *  hm.put(Keyword.CONTENT, "Visit the zoo");
	 * hm.put(Keyword.DEADLINE, "true"); 
	 * hm.put(Keyword.END,"10/10/2014 23:59");
	 *  try { result = ev.determineCommand(hm); 
	 *  } catch (Exception e) { 
	 *  e.printStackTrace(); 
	 *  }
	 * assertTrue(result.get(0).equals("Added Successfully.")); 
	 * }
	 */
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
		assertTrue(result.get(0).equals("Added Successfully."));
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
		assertTrue(result.get(0).equals("Added Successfully."));

		result.clear();
		hm.clear();
		hm.put(Keyword.METHOD, "undo");

		try {
			result = ev.determineCommand(hm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(result.get(0).equals("Undo Successfully."));
		
		result.clear();
		hm.clear();
		hm.put(Keyword.METHOD, "display");
		hm.put(Keyword.CONTENT, "");
		try {
			result = ev.determineCommand(hm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(result.get(0).equals("1. Visit the zoo"));
		
	}

}
