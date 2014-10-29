package pact;

import java.util.ArrayList;
import java.util.HashMap;
import utility.Keyword;
import utility.Task;

public class EventHandler {
    
    private static final String ANNOUNCEMENT_NOT_FOUND = "Sorry, nothing found.";
    private static final String ANNOUNCEMENT_CREATE = "Task was added successfully.";
    private static final String ANNOUNCEMENT_READ = "Search was successful.";
    private static final String ANNOUNCEMENT_DELETE = "Task was deleted successfully.";
    private static final String ANNOUNCEMENT_UPDATE = "Task was updated successfully.";
    private static final String ANNOUNCEMENT_UNDO = "Undo was successful.";
    private static final String ANNOUNCEMENT_CLEAR = "All tasks cleared successfully.";
    private DataHandler dataHandler = new DataHandler();
    private ArrayList<String> result = new ArrayList<String>();
    
    
    /**
     * Use the parameters and call the appropriate methods
     * @param parameters
     * @return result
     * @throws Exception
     */
    public ArrayList<String> determineCommand(HashMap<Keyword, String> parameters) throws Exception {
        String codeString = parameters.get(Keyword.METHOD);
        Keyword code = Keyword.getMeaning(codeString);
        result.clear();
        
        if (code.equals(Keyword.CREATE)) {
            createTask(parameters);
        } else if (code.equals(Keyword.READ)) {
            readTask(parameters);
        } else if (code.equals(Keyword.UPDATE)) {
            updateTask(parameters);
        } else if (code.equals(Keyword.DELETE)) {
            deleteTask(parameters, false);
        } else if (code.equals(Keyword.CLEAR)) {
        	deleteTask(parameters, true);  
        } else if (code.equals(Keyword.UNDO)) {
            undo();
        } else if(code.equals(Keyword.EXIT)){
        	readTask(parameters);
        }       
        return result;
    }

    /**
     * Creates a new Task and calls the dataHandler to store the information
     * @param parameters
     */
    private void createTask(HashMap<Keyword, String> parameters) {
        String value;
        Task task = new Task();
        for (Keyword key : parameters.keySet()) {
            value = parameters.get(key); 
            if (key.equals(Keyword.CONTENT)) {
                task.setValue(key, value);
            } else if (key.equals(Keyword.START)) {
            	task.setValue(key, value);
            } else if (key.equals(Keyword.END)) {
            	task.setValue(key, value);
            } else if (key.equals(Keyword.ALLDAY)) {
            	task.setValue(key, value);
            } else if (key.equals(Keyword.TYPE)) {
            	task.setValue(key, value);
            }
        }
        dataHandler.createTask(task);
       
        result.add(ANNOUNCEMENT_CREATE);
    }

    /**
     * Calls the dataHandler to read/search and store the information obtained into result
     * @param parameters
     */
    private void readTask(HashMap<Keyword, String> parameters) {
        boolean isExact = parameters.containsKey(Keyword.EXACT);
        //int counter = 0;
        String start = "";
        String end = "";
        boolean isArchivedIncluded = false; 
        
        if (parameters.containsKey(Keyword.START)) {
            start = parameters.get(Keyword.START);
        }
        if (parameters.containsKey(Keyword.END)) {
            end = parameters.get(Keyword.END);
        }
        if (parameters.containsKey(Keyword.ARCHIVED)) {
        	isArchivedIncluded = Boolean.valueOf(parameters.get(Keyword.ARCHIVED));
        }
        
        ArrayList<Task> queryResult = dataHandler.readTask(parameters.get(Keyword.CONTENT), isExact, start, end, isArchivedIncluded, true);
        if (queryResult.isEmpty()) {
            result.add(ANNOUNCEMENT_NOT_FOUND);
        }else{
        	result.add(ANNOUNCEMENT_READ);
        }
        /*for (Task i : queryResult) {
            ++counter;
            result.add(Integer.toString(counter) + ". " + i.getDisplayedString());
        }*/
        for (Task i : queryResult) {
            result.add(i.getDisplayedString());
        }
        
    }

    /**
     * Calls the dataHandler to delete Task or clear all Tasks
     * @param parameters
     * @param clear
     */
    private void deleteTask(HashMap<Keyword, String> parameters, Boolean clear) {
        //int counter = 0;
        ArrayList<Task> queryResult = dataHandler.deleteTask(parameters.get(Keyword.CONTENT), parameters.containsKey(Keyword.FOREVER));
        if (queryResult.isEmpty()) {
            result.add(ANNOUNCEMENT_NOT_FOUND);
            return;
        }
        if (clear == false) {
        	result.add(ANNOUNCEMENT_DELETE);
        } else {
        	result.add(ANNOUNCEMENT_CLEAR);
        }
      /*  for (Task i : queryResult) {
            ++counter;
            result.add(Integer.toString(counter) + ". " + i.getDisplayedString());
        }*/
        for (Task i : queryResult) {
            result.add(i.getDisplayedString());
        }
    }

    /**
     * Calls the dataHandler to update Task
     * @param parameters
     */
    private void updateTask(HashMap<Keyword, String> parameters) {
        //int counter = 0;
        String start = "";
        String end = "";
        String newContent = "";
        String isCompleted = "";
        if (parameters.containsKey(Keyword.NEWCONTENT)) {
            newContent = parameters.get(Keyword.NEWCONTENT);
        }
        if (parameters.containsKey(Keyword.COMPLETED)) {
        	isCompleted = parameters.get(Keyword.COMPLETED);
        }
        if (parameters.containsKey(Keyword.START)) {
            start = parameters.get(Keyword.START);
        }
        if (parameters.containsKey(Keyword.END)) {
            end = parameters.get(Keyword.END);
        }
        ArrayList<Task> queryResult = dataHandler.updateTask(parameters.get(Keyword.CONTENT), newContent, start, end, isCompleted);
        if (queryResult.isEmpty()) {
            result.add(ANNOUNCEMENT_NOT_FOUND);
            return;
        }
        result.add(ANNOUNCEMENT_UPDATE);
       /* for (Task i : queryResult) {
            ++counter;
            result.add(Integer.toString(counter) + ". " + i.getDisplayedString());
        }*/
        for (Task i : queryResult) {
            result.add(i.getDisplayedString());
        }
    }


    /**
     * Calls the dataHandler to undo to the previous data
     * @throws Exception
     */
    private void undo() throws Exception {
    	dataHandler.undo();
    	result.add(ANNOUNCEMENT_UNDO);
   
    }
}