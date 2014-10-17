package pact;

import java.util.ArrayList;
import java.util.HashMap;
import utility.Keyword;
import utility.Task;

public class EventHandler {
    
    private static final String ANNOUNCEMENT_NOT_FOUND = "Sorry, nothing found.";
    private static final String ANNOUNCEMENT_CREATE = "Added Successfully.";
    private static final String ANNOUNCEMENT_DELETE = "Deleted Successfully.";
    private static final String ANNOUNCEMENT_UPDATE = "Updated Successfully.";
    private static final String ANNOUNCEMENT_UNDO = "Undo Successfully.";
    private static final String ANNOUNCEMENT_CLEAR = "Cleared Successfully.";
    
    private DataHandler dataHandler = new DataHandler();
    private ArrayList<String> result = new ArrayList<String>();
    
    public ArrayList<String> determineCommand(HashMap<Keyword, String> parameters) throws Exception {
        String codeString = parameters.get(Keyword.METHOD);
        Keyword code = Keyword.getMeaning(codeString);
        result.clear();
        
        if (code.equals(Keyword.CREATE)) {
            addTask(parameters);
        } else if (code.equals(Keyword.READ)) {
            searchTask(parameters);
        } else if (code.equals(Keyword.UPDATE)) {
            updateTask(parameters);
        } else if (code.equals(Keyword.DELETE)) {
            deleteTask(parameters, false);
        } else if (code.equals(Keyword.CLEAR)) {
        	deleteTask(parameters, true);  
        } else if (code.equals(Keyword.UNDO)) {
            undo();
        } else {

        }
        return result;
    }

    private void addTask(HashMap<Keyword, String> parameters) {
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
        dataHandler.addTask(task);
        result.add(ANNOUNCEMENT_CREATE);
    }

    private void searchTask(HashMap<Keyword, String> parameters) {
        boolean isExact = parameters.containsKey(Keyword.EXACT);
        int counter = 0;
        String start = "";
        String end = "";
        if (parameters.containsKey(Keyword.START)) {
            start = parameters.get(Keyword.START);
        }
        if (parameters.containsKey(Keyword.END)) {
            end = parameters.get(Keyword.END);
        }
        ArrayList<Task> queryResult = dataHandler.searchTask(parameters.get(Keyword.CONTENT), isExact, start, end, false);
        for (Task i : queryResult) {
            ++counter;
            result.add(Integer.toString(counter) + ". " + i.getDisplayedString());
        }
        if (queryResult.isEmpty()) {
            result.add(ANNOUNCEMENT_NOT_FOUND);
        }
    }

    private void deleteTask(HashMap<Keyword, String> parameters, Boolean clear) {
        int counter = 0;
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
        for (Task i : queryResult) {
            ++counter;
            result.add(Integer.toString(counter) + ". " + i.getDisplayedString());
        }
    }

    private void updateTask(HashMap<Keyword, String> parameters) {
        int counter = 0;
        String start = "";
        String end = "";
        String newContent = "";
        if (parameters.containsKey(Keyword.NEWCONTENT)) {
            newContent = parameters.get(Keyword.NEWCONTENT);
        }
        if (parameters.containsKey(Keyword.START)) {
            start = parameters.get(Keyword.START);
        }
        if (parameters.containsKey(Keyword.END)) {
            end = parameters.get(Keyword.END);
        }
        ArrayList<Task> queryResult = dataHandler.updateTask(parameters.get(Keyword.CONTENT), newContent, start, end);
        if (queryResult.isEmpty()) {
            result.add(ANNOUNCEMENT_NOT_FOUND);
            return;
        }
        result.add(ANNOUNCEMENT_UPDATE);
        for (Task i : queryResult) {
            ++counter;
            result.add(Integer.toString(counter) + ". " + i.getDisplayedString());
        }
    }


    private void undo() throws Exception {
    	 dataHandler.undo();
    	 result.add(ANNOUNCEMENT_UNDO);
    }
}