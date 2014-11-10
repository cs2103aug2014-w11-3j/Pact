package pact;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import utility.Keyword;
import utility.Task;

public class DataHandlerTest {
    private DataHandler dh;
    private Task sampleTaskOne, sampleTaskTwo, sampleTaskThree;

    private ArrayList<String> ExpectedOutputZero, ExpectedOutputOne, ExpectedOutputTwo, ExpectedOutputThree, ExpectedOutputFour, ExpectedOutputFive;

    private void setUpTasks() {
        sampleTaskOne = new Task();
        sampleTaskTwo = new Task();
        sampleTaskThree = new Task();

        sampleTaskOne.setValue(Keyword.CONTENT,"test one");
        sampleTaskTwo.setValue(Keyword.CONTENT,"test two");
        sampleTaskThree.setValue(Keyword.CONTENT,"test three");
    }

    private void setUpExpectedOutput() {
        ExpectedOutputZero = new ArrayList<String>();

        ExpectedOutputOne = new ArrayList<String>();
        ExpectedOutputOne.add(sampleTaskOne.getValue(Keyword.CONTENT));

        ExpectedOutputTwo = new ArrayList<String>();
        ExpectedOutputTwo.add(sampleTaskOne.getValue(Keyword.CONTENT));
        ExpectedOutputTwo.add(sampleTaskTwo.getValue(Keyword.CONTENT));

        ExpectedOutputThree = new ArrayList<String>();
        ExpectedOutputThree.add(sampleTaskOne.getValue(Keyword.CONTENT));
        ExpectedOutputThree.add(sampleTaskTwo.getValue(Keyword.CONTENT));
        ExpectedOutputThree.add(sampleTaskThree.getValue(Keyword.CONTENT));

        ExpectedOutputFour = new ArrayList<String>();
        ExpectedOutputFour.add(sampleTaskOne.getValue(Keyword.CONTENT));
        ExpectedOutputFour.add(sampleTaskTwo.getValue(Keyword.CONTENT));
        ExpectedOutputFour.add(sampleTaskOne.getValue(Keyword.CONTENT));

        ExpectedOutputFive = new ArrayList<String>();
        ExpectedOutputFive.add(sampleTaskThree.getValue(Keyword.CONTENT));
        ExpectedOutputFive.add(sampleTaskTwo.getValue(Keyword.CONTENT));
        ExpectedOutputFive.add(sampleTaskThree.getValue(Keyword.CONTENT));
    }

    public ArrayList<String> getAllData() {
        ArrayList<Task> data = dh.readTask("", false, "", "", false,false);
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < data.size(); ++i) {
            result.add(data.get(i).getValue(Keyword.CONTENT));
        }
        return result;
    }

    @Before
    public void setUp() throws Exception {
        dh = new DataHandler();
        setUpTasks();
        setUpExpectedOutput();
    }

    public void testDelete() {
        dh.deleteTask("",true);     //clear the data first, start the test with empty data
        assertEquals(getAllData(),ExpectedOutputZero); //make sure it is cleared
    }

    public void testCreate() {
        dh.createTask(sampleTaskOne);
        assertEquals(getAllData(),ExpectedOutputOne); 

        dh.createTask(sampleTaskTwo);
        assertEquals(getAllData(),ExpectedOutputTwo);

        dh.createTask(sampleTaskThree);
        assertEquals(getAllData(),ExpectedOutputThree);
    }

    public void testUndo() {
        dh.undo();
        assertEquals(getAllData(),ExpectedOutputTwo);

        dh.undo();
        assertEquals(getAllData(),ExpectedOutputThree); //make sure the redo also works
    }

    public void testUpdate() {
        dh.updateTask(sampleTaskThree.getValue(Keyword.CONTENT),sampleTaskOne.getValue(Keyword.CONTENT),"","","");
        assertEquals(getAllData(),ExpectedOutputFour);

        dh.updateTask(sampleTaskOne.getValue(Keyword.CONTENT),"test three","","","");
        assertEquals(getAllData(),ExpectedOutputFive);
    }

    @Test
    public void test() {
        testDelete();
        testCreate();
        testUndo();
        testUpdate();
    }

}