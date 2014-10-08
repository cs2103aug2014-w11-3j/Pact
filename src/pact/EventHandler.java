package pact;

import java.util.ArrayList;
import java.text.ParseException;

import utility.Task;
import utility.Task.TASK_TYPE;

public class EventHandler {
  
	public static ArrayList<String> array;
    private static Task task;
    private static int value;
    private static int status;
    private static int error = -1;      
    private static int success = 0;
    private static int indexOfFirstItem = 0;
    private static int indexOfSecondVariable = 2;
    private static int indexOfSecondValue = 3;
    private static int indexOfThirdValue = 5;
    private static int noOfUpdateParameters = 8;
    private static boolean searchWholeTrue = true;
    private static boolean searchWholeFalse = false;
    private static java.text.SimpleDateFormat formatter;
    private static ArrayList<Task> searchResult;
    private static DataHandler datahandler;
    
    
    //public static void main(String[] args) throws ParseException {
    //}
    
    public EventHandler() {
        initialize();
    }
    
    public int initialize() {
        array = new ArrayList<String>();
        datahandler = new DataHandler();
        task = new Task();
        searchResult = new ArrayList<Task>();
        formatter = new java.text.SimpleDateFormat("ddMMyyyy HHmm");
        return 0;
    }
    public int determineCommand(ArrayList<String> parameters) throws Exception {
        int commandType = Integer.parseInt(parameters.get(1));

        switch(commandType) {            
        case 0:
            status = addTask(parameters);
            break;
        case 1:
            status = readFile(parameters);
            break;
        case 2:
            status = updateFile(parameters);
            break;
        case 3: 
            status = archiveTask(parameters);
            break;
        case 4:
            status = searchTask(parameters);
            break;
        case 5:
        	status = undo();
        	break; 
        default:
            status = error;
            break;
        }
        searchResult.clear();
                    
        return status;          
    }


	private int addTask(ArrayList<String> parameters) throws ParseException {
        task = new Task();
        
        int noOfParameter = parameters.size();
        
        for (int index = 2; index < noOfParameter; index+=2) {
            value = index;
            
            switch(parameters.get(value)) {
            case "taskName":
                task.taskName = parameters.get(++value);
                break;
            case "startTime":
                task.startTime = formatter.parse(parameters.get(++value));
                break;
            case "endTime":
                task.endTime = formatter.parse(parameters.get(++value));
                break;          
            case "type":
                task.type = TASK_TYPE.valueOf(parameters.get(++value).toUpperCase());
                break;
            default:
                return error;
            }
        }
        status = datahandler.addTask(task);
        return status;
        
    }
    private int readFile(ArrayList<String> parameters) throws Exception {
        String display = "";
        status = datahandler.searchTask(display, searchResult, searchWholeFalse);
        for (int i = 0; i < searchResult.size(); ++i)
            System.out.println(searchResult.get(i).taskName);
        return status;
        
    }
    private int updateFile(ArrayList<String> parameters) throws Exception {
        String field = null;
        String changeToValue = null;
        String nameOfTaskToBeUpdated = "";
        
        if (parameters.size() != noOfUpdateParameters) {
            return error;
        }
        
        for (int index = 2; index < noOfUpdateParameters; index += 2) {
            value = index;          
            switch(parameters.get(value)) {
            case "taskName":
                nameOfTaskToBeUpdated = parameters.get(++value);
                break;
            case "field":
                field = parameters.get(++value);
                break;
            case "changeToValue":
                changeToValue = parameters.get(++value);
                break;
            default:
                return error;
            }           
        }
        
        datahandler.searchTask(nameOfTaskToBeUpdated, searchResult, searchWholeTrue);
    
        Task editedTask = new Task(searchResult.get(indexOfFirstItem));
        
        switch(field) {
        case "taskName":
            editedTask.taskName = changeToValue;
            break;
        case "startTime":
            editedTask.startTime = formatter.parse(changeToValue);
            break;
        case "endTime":
            editedTask.endTime = formatter.parse(changeToValue);
            break;
        case "isCompleted":
            editedTask.isCompleted = Boolean.parseBoolean(changeToValue);
            break;
        case "isArchived":
            editedTask.isArchived = Boolean.parseBoolean(changeToValue);
            break;
        default:
            return error;
        }
              
        
        if (datahandler.archiveTask(searchResult.get(indexOfFirstItem)) == success &&
            datahandler.addTask(editedTask) == success) {
            
            status = success;
        } else {
            status = error;
        }
        return status;
        
    }

    private int archiveTask(ArrayList<String> parameters) throws Exception {
    	boolean archiveAll;  
        String nameOfTaskToBeDeleted;
        
        if(parameters.get(indexOfSecondVariable).equals("deleteAll")){
    		archiveAll = Boolean.parseBoolean(parameters.get(indexOfSecondValue));
    		nameOfTaskToBeDeleted = parameters.get(indexOfThirdValue);
    	}else{
    		archiveAll = Boolean.parseBoolean(parameters.get(indexOfThirdValue));
    		nameOfTaskToBeDeleted = parameters.get(indexOfSecondValue);
    	}
        if(archiveAll == false){
        	datahandler.searchTask(nameOfTaskToBeDeleted, searchResult, searchWholeTrue);
        	for(int i = 0; i < searchResult.size(); i++){
        		status = datahandler.archiveTask(searchResult.get(indexOfFirstItem));
        	}
        }else{
        	datahandler.searchTask(nameOfTaskToBeDeleted, searchResult, searchWholeFalse);
        	for(int i = 0; i < searchResult.size(); i++){
        		status = datahandler.archiveTask(searchResult.get(indexOfFirstItem));
        	}
        }
        return status; 
        
    }
    private int searchTask(ArrayList<String> parameters) throws Exception {
    	boolean searchWhole;
    	String keyword;
    	if(parameters.get(indexOfSecondVariable).equals("searchWhole")){
    		searchWhole = Boolean.parseBoolean(parameters.get(indexOfSecondValue));
    		keyword = parameters.get(indexOfThirdValue);
    	}else{
    		searchWhole = Boolean.parseBoolean(parameters.get(indexOfThirdValue));
    		keyword = parameters.get(indexOfSecondValue);
    	}

        status = datahandler.searchTask(keyword, searchResult, searchWhole);
        
        for (int i = 0; i < searchResult.size(); ++i){
           System.out.println(searchResult.get(i).taskName);
        }
        return status;
        
    }
    private int undo() throws Exception {
		status = datahandler.undo();
		return status;
	} 
}