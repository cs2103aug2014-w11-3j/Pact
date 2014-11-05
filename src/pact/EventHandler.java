package pact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import utility.Clock;
import utility.Keyword;
import utility.Task;

public class EventHandler {
    
    private static final String ANNOUNCEMENT_NOT_FOUND = "There are no tasks in your task list";
    private static final String ANNOUNCEMENT_CREATE = "Task was added successfully.";
    private static final String ANNOUNCEMENT_READ = "You have the following tasks";
    private static final String ANNOUNCEMENT_DELETE = "Task was deleted successfully.";
    private static final String ANNOUNCEMENT_UPDATE = "Task was updated successfully.";
    private static final String ANNOUNCEMENT_UNDO = "Undo was successful.";
    private static final String ANNOUNCEMENT_CLEAR = "All tasks cleared successfully.";
    private DataHandler dataHandler = new DataHandler();
    private ArrayList<String> result = new ArrayList<String>();
    
    //@Hui Yi A0101331H
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
        } else if (code.equals(Keyword.EXIT)) {
            readTask(parameters);
        } else if (code.equals(Keyword.COMPLETED)) {
            completeTask(parameters, "true");
        } else if (code.equals(Keyword.INCOMPLETE)) {
            completeTask(parameters, "false");
        } else if (code.equals(Keyword.EMPTYSLOT)) {
            searchEmptySlot(parameters);
        }
        return result;
    }
    //@Hui Yi A0101331H
    private void completeTask(HashMap<Keyword, String> parameters, String done) {
        parameters.put(Keyword.COMPLETED, done);
        updateTask(parameters);
    }
    //@Hui Yi A0101331H
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
        parameters.clear();
        parameters.put(Keyword.CONTENT, "");
        ArrayList<Task> queryResult = dataHandler.readTask(parameters.get(Keyword.CONTENT), false, "", "", false, false);
        for (Task i : queryResult) {
            result.add(i.getDisplayedString());
        }
    }
    //@Hui Yi A0101331H
    /**
     * Calls the dataHandler to read/search and store the information obtained into result
     * @param parameters
     */
    private void readTask(HashMap<Keyword, String> parameters) {
        //for (Keyword key : parameters.keySet()) {
            //System.out.println(key + " " + parameters.get(key));
       // }
        boolean isExact = parameters.containsKey(Keyword.EXACT);
        Clock clock = new Clock();
        String start = clock.getCurrentDateAndTime();
        String end = "";
        boolean isArchivedIncluded = false; 
        boolean isCompletedIncluded = false;
        
        if (parameters.containsKey(Keyword.START)) {
            start = parameters.get(Keyword.START);
        }
        if (parameters.containsKey(Keyword.END)) {
            end = parameters.get(Keyword.END);
        }
        if (parameters.containsKey(Keyword.ARCHIVED)) {
            isArchivedIncluded = Boolean.valueOf(parameters.get(Keyword.ARCHIVED));
        }
        if (parameters.containsKey(Keyword.COMPLETED)) {
            isCompletedIncluded = true;
        }
        
        ArrayList<Task> queryResult = dataHandler.readTask(parameters.get(Keyword.CONTENT), isExact, start, end, isArchivedIncluded, isCompletedIncluded);
        if (queryResult.isEmpty()) {
            result.add(ANNOUNCEMENT_NOT_FOUND);
        } else {
            result.add(ANNOUNCEMENT_READ);
        }
        ArrayList<String> unsorted = new ArrayList<String>();
       
        for (Task i : queryResult) {
                 unsorted.add(i.getDisplayedString());
        }
        if ((parameters.containsKey(Keyword.SORT))) {
             Collections.sort(unsorted);
        }
        for (int i = 0; i < unsorted.size(); i++) {
            result.add(unsorted.get(i));
        }
        
    }
    //@Hui Yi A0101331H
    /**
     * Calls the dataHandler to delete Task or clear all Tasks
     * @param parameters
     * @param clear
     */
    private void deleteTask(HashMap<Keyword, String> parameters, Boolean clear) {
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
            result.add(i.getDisplayedString());
        }
    }
    //@Hui Yi A0101331H
    /**
     * Calls the dataHandler to update Task
     * @param parameters
     */
    private void updateTask(HashMap<Keyword, String> parameters) {
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

        for (Task i : queryResult) {
            result.add(i.getDisplayedString());
        }
    }
    //@Hui Yi A0101331H
    private void sortTasks(ArrayList<Task> tasksList,Keyword sortKey,boolean isAscending) {
        for (int i = 0; i < tasksList.size(); ++i) {
            for (int j = i; j < tasksList.size(); ++j) {
                if (tasksList.get(i).getValue(sortKey).compareTo(tasksList.get(j).getValue(sortKey)) > 0) {
                    Collections.swap(tasksList, i, j);
                }
            }
        }
        if (!isAscending) {
            Collections.reverse(tasksList);
        }
    }
    
    private void searchEmptySlot(HashMap<Keyword, String> parameters) throws Exception {   
        String start = parameters.get(Keyword.START);
        String end = parameters.get(Keyword.END);
        Clock clock = new Clock();
        int startDay = clock.getDay(start);
        for (int i = startDay; ; ++i) {
            ArrayList<String> resultThisDay = new ArrayList<String>();
            String startSearch = String.valueOf(i);
            while (startSearch.length() < 2) {
                startSearch = '0' + startSearch;
            }
            startSearch = startSearch + start.substring(2, start.length());
            startSearch = clock.normalize(startSearch);
            if (clock.parseFromCommonFormat(startSearch) > clock.parseFromCommonFormat(end)) {
                break;
            }
            String endSearch = clock.getDate(startSearch) + " 23:59";
            ArrayList<Task> onThisDay = dataHandler.readTask("", false, startSearch, endSearch, false, false);
            sortTasks(onThisDay,Keyword.START, true);
            String freeTimeNow = "00:00";
            for (int j = 0; j < onThisDay.size(); ++j) {
                if (onThisDay.get(j).getValue(Keyword.START).equals("")) {
                    continue;
                }
                if (freeTimeNow.compareTo(clock.getTime(onThisDay.get(j).getValue(Keyword.START))) < 0) {
                    resultThisDay.add(freeTimeNow + " to " + clock.getTime(onThisDay.get(j).getValue(Keyword.START)));
                }
                if (freeTimeNow.compareTo(clock.getTime(onThisDay.get(j).getValue(Keyword.END))) < 0) {
                    freeTimeNow = clock.getTime(onThisDay.get(j).getValue(Keyword.END)); 
                }
            }
            if (freeTimeNow.compareTo("23:59") < 0) {
                resultThisDay.add(freeTimeNow + " to 23:59");
            }
            //result.add("On " + clock.getDate(startSearch) );
            String[] array = new String[]{ "","Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}; 
            result.add("\nOn " + array[clock.getDayOfTheWeek(clock.getDate(startSearch))] + " " + clock.getDay(startSearch) + " " + clock.getMonth(clock.getDate(startSearch)) + " free from:" );
            for (int j = 0; j < resultThisDay.size(); ++j) {
                result.add(String.valueOf(j + 1) + ". " + resultThisDay.get(j));
            }
            
        }
    }

    //@Hui Yi A0101331H
    /**
     * Calls the dataHandler to undo to the previous data
     * @throws Exception
     */
    private void undo() throws Exception {
        dataHandler.undo();
        result.add(ANNOUNCEMENT_UNDO);
    }
    
    /*public static void main(String[] args) throws Exception {
        EventHandler e = new EventHandler();
        e.searchEmptySlot("20/12/2014 00:00","21/12/2014 23:59");
    }*/
}
