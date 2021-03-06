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
    private static final String ANNOUNCEMENT_SUGGESTION = "Do you mean ";
    private static final String ANNOUNCEMENT_NO_EMPTY_SLOT = "There is no empty slot for this day :(";
    
    private final String[] dayName = new String[]{ "","Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}; 
    
    private DataHandler dataHandler = new DataHandler();
    private ArrayList<String> result = new ArrayList<String>();
    
    //@author A0101331H
    /**
     * Use the parameters and call the appropriate methods
     * @param parameters
     * @return result
     * @throws Exception
     */
    public ArrayList<String> determineCommand(HashMap<Keyword, String> parameters) 
                throws Exception {
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
    //@author A0101331H
    private void completeTask(HashMap<Keyword, String> parameters, String done) {
        parameters.put(Keyword.COMPLETED, done);
        updateTask(parameters);
    }
    //@author A0101331H
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
        ArrayList<Task> queryResult = dataHandler.readTask("", false, "", 
                                                           "", false, false);
        for (Task i : queryResult) {
            result.add(i.getDisplayedString());
        }
    }
    
    //@author A0101331H
    /**
     * Calls the dataHandler to read/search and store the information obtained 
     * into result
     * @param parameters
     */
    private void readTask(HashMap<Keyword, String> parameters) {
        String key = parameters.get(Keyword.CONTENT);
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
        
        ArrayList<Task> queryResult = dataHandler.readTask(key, isExact, 
                                                           start, end, 
                                                           isArchivedIncluded, 
                                                           isCompletedIncluded);
        
        String firstOutputLine = "";

        if (!dataHandler.getNearMatchString().equals("")) {
            firstOutputLine += ANNOUNCEMENT_SUGGESTION 
                  + dataHandler.getNearMatchString() + "?\n";
        }
        
        if (queryResult.isEmpty()) {
            firstOutputLine += ANNOUNCEMENT_NOT_FOUND;
        } else {
            firstOutputLine += ANNOUNCEMENT_READ;
        }
        
        result.add(firstOutputLine);
     
        if ((parameters.containsKey(Keyword.SORT))) {
        	Keyword sortKey = Keyword.getMeaning(parameters.get(Keyword.SORT));
        	sortTasks(queryResult, sortKey, true);
        }
        for (int i = 0; i < queryResult.size(); i++) {
            result.add(queryResult.get(i).getDisplayedString());
        }
        
    }
    //@author A0101331H
    /**
     * Calls the dataHandler to delete Task or clear all Tasks
     * @param parameters
     * @param clear
     */
    private void deleteTask(HashMap<Keyword, String> parameters, 
                            boolean clear) {
        String key = parameters.get(Keyword.CONTENT);
        boolean isDeleting = parameters.containsKey(Keyword.FOREVER);
        
        ArrayList<Task> queryResult = dataHandler.deleteTask(key, isDeleting);
        
        String firstOutputLine = "";

        if (!dataHandler.getNearMatchString().equals("")) {
            firstOutputLine += ANNOUNCEMENT_SUGGESTION 
                  + dataHandler.getNearMatchString() + "?\n";
        }
        
        if (queryResult.isEmpty()) {
            firstOutputLine += ANNOUNCEMENT_NOT_FOUND;
            result.add(firstOutputLine);
            return;
        }
        if (clear == false) {
            firstOutputLine += ANNOUNCEMENT_DELETE;
        } else {
            firstOutputLine += ANNOUNCEMENT_CLEAR;
        }

        result.add(firstOutputLine);
        
        for (Task i : queryResult) {
            result.add(i.getDisplayedString());
        }
    }
    //@author A0101331H
    /**
     * Calls the dataHandler to update Task
     * @param parameters
     */
    private void updateTask(HashMap<Keyword, String> parameters) {
        String start = "";
        String end = "";
        String newContent = "";
        String isCompleted = "";
        String key = parameters.get(Keyword.CONTENT);
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
        ArrayList<Task> queryResult = dataHandler.updateTask(key, newContent, 
                                                             start, end, 
                                                             isCompleted);
        
        String firstOutputLine = "";

        if (!dataHandler.getNearMatchString().equals("")) {
            firstOutputLine += ANNOUNCEMENT_SUGGESTION 
                  + dataHandler.getNearMatchString() + "?\n";
        }
        
        if (queryResult.isEmpty()) {
            firstOutputLine += ANNOUNCEMENT_NOT_FOUND;
            result.add(firstOutputLine);
            return;
        }
        firstOutputLine += ANNOUNCEMENT_UPDATE;
        result.add(firstOutputLine);

        for (Task i : queryResult) {
            result.add(i.getDisplayedString());
        }
    }
    
    //@author A0113012J
    
    private int compareTo(Task taskOne, Task taskTwo, Keyword key) {
        //time comparison must be handled separately
    	if (key.equals(Keyword.START) || key.equals(Keyword.END)) {
    		Clock clock = new Clock();
    		String taskOneTime = taskOne.getValue(key);
    		String taskTwoTime = taskTwo.getValue(key);
    		long taskOneTimeLong = clock.parseFromCommonFormat(taskOneTime);
    		long taskTwoTimeLong = clock.parseFromCommonFormat(taskTwoTime);
    		//compare the integer
    		if (taskOneTimeLong < taskTwoTimeLong) {
    			return -1;
    		} else if (taskOneTimeLong == taskTwoTimeLong) {
    			return 0;
    		} else {
    			return 1;
    		}
    	}
    	return taskOne.getValue(key).compareTo(taskTwo.getValue(key));
    }
    
    //@author A0113012J
    private void sortTasks(ArrayList<Task> tasksList,Keyword sortKey,
                           boolean isAscending) {
        for (int i = 0; i < tasksList.size(); ++i) {
            for (int j = i; j < tasksList.size(); ++j) {
                if (compareTo(tasksList.get(i),tasksList.get(j),sortKey) > 0) {
                    Collections.swap(tasksList, i, j);
                }
            }
        }
        if (!isAscending) {
            Collections.reverse(tasksList);
        }
    }
    
    private ArrayList<String> processADayEmptySlot(ArrayList<Task> onThisDay) 
            throws Exception {
        ArrayList<String> resultThisDay = new ArrayList<String>();
        String freeTimeNow = "00:00";
        Clock clock = new Clock();
        for (int j = 0; j < onThisDay.size(); ++j) {
            if (onThisDay.get(j).getValue(Keyword.START).equals("")) {
                continue;
            }
            Task task = onThisDay.get(j);
            String startTime = clock.getTime(task.getValue(Keyword.START));
            String endTime = clock.getTime(task.getValue(Keyword.END));
            if (freeTimeNow.compareTo(startTime) < 0) {
                resultThisDay.add(freeTimeNow + " to " + startTime);
            }
            if (freeTimeNow.compareTo(endTime) < 0) {
                freeTimeNow = endTime;
            }
        }
        if (freeTimeNow.compareTo("23:59") < 0) {
            resultThisDay.add(freeTimeNow + " to 23:59");
        }
        if (resultThisDay.size() == 0) {
            resultThisDay.add(ANNOUNCEMENT_NO_EMPTY_SLOT);
        }
        return resultThisDay;
    }
    
    //@author A0113012J
    private void searchEmptySlot(HashMap<Keyword, String> parameters) 
            throws Exception {   
        String start = parameters.get(Keyword.START);
        String end = parameters.get(Keyword.END);
        Clock clock = new Clock();
        int startDay = clock.getDay(start);
        for (int i = startDay; ; ++i) {
            String startSearch = String.valueOf(i);
            while (startSearch.length() < 2) {
                startSearch = '0' + startSearch;
            }
            startSearch = startSearch + start.substring(2, start.length());
            startSearch = clock.normalize(startSearch);
            if (clock.parseFromCommonFormat(startSearch) > 
                clock.parseFromCommonFormat(end)) {
                break;
            }
            String endSearch = clock.getDate(startSearch) + " 23:59";
            ArrayList<Task> onThisDay = dataHandler.readTask("", false, 
                                                             startSearch, 
                                                             endSearch, 
                                                             false, false);
            sortTasks(onThisDay,Keyword.START, true);
            
            ArrayList<String> resultThisDay = processADayEmptySlot(onThisDay);
            
            String date = clock.getDate(startSearch);
            String day = dayName[clock.getDayOfTheWeek(date)];
            result.add("\nOn " + day + " " + clock.getDay(startSearch) + " " + 
                       clock.getMonth(clock.getDate(startSearch)) 
                       + " free from:" );
            for (int j = 0; j < resultThisDay.size(); ++j) {
                result.add(String.valueOf(j + 1) + ". " + resultThisDay.get(j));
            }
            
        }
    }

    //@author A0101331H
    /**
     * Calls the dataHandler to undo to the previous data
     * @throws Exception
     */
    private void undo() throws Exception {
        dataHandler.undo();
        result.add(ANNOUNCEMENT_UNDO);
    }
}
