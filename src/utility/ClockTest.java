package utility;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClockTest {

    @Test
    public void test1() throws Exception {
        assertEquals("23/12/1995 at 11:30", "23/12/1995 11:30", new Clock().parse("23 Dec 1995", "1130"));
    }
    @Test
    public void test2() throws Exception {
        assertEquals("23/12/1995 at 11:30", "23/12/1995 23:30", new Clock().parse("23 12 1995", "1130PM"));
    }
    @Test
    public void test3() throws Exception {
        assertEquals("23/12/1995 at 11:30", "23/12/1995 23:30", new Clock().parse("23/12/1995", "2330"));
    }
    @Test
    public void test4() throws Exception {
        assertEquals("23/12/1995 at 11:30", "23/12/1995 11:30", new Clock().parse("23/Dec/1995", "1130am"));
    }
    @Test
    public void test5() throws Exception {
        assertEquals("23/12/1995 at 11:30", "23/12/1995 23:30", new Clock().parse("23-Dec-1995", "11:30PM"));
    }
    @Test
    public void test6() throws Exception {
        assertEquals("23/12/1995 at 11:30", "23/12/1995 11:30", new Clock().parse("Dec 23 1995", "1130am"));
    }

}
