package pact;

import java.util.ArrayList;

public class Parser {
    private static String commandType;
    private static String userCommand = "";
    private static String commandCreateTask ="0";
    private static String commandRead = "1";
    private static String commandUpdate = "2";
    private static String commandDelete = "3";
    private static String commandSearch = "4";
    private static String commandParameter = "commandType";
    private static String typeParameter = "type";//for create task: floating/deadline/timed
    private static String descriptionParameter = "description";
    private static String startTimeParameter = "startTime";
    private static String endTimeParameter = "endTime";
    private static String fieldParameter = "field"; //for update command: field to be updated
    private static String newValueParameter = "value";
    private static String searchKeyParameter= "searchKey";
    
    
    private static String timedTaskKey = "from";
    private static String deadlineTaskKey = "due on";
    
    private static  ArrayList<String> parameters;
    
    public ArrayList<String> parse(String userInput) {
        userCommand = userInput;
        parameters  = new ArrayList<String>();
        commandType = determineCommandType(getCommandType());

        if (commandType.equals("createTask")) {
            parameters.add(commandParameter);
            parameters.add(commandCreateTask);
            getCreateParameters();
            
        } else if (commandType.equals("read")) {
            parameters.add(commandParameter);
            parameters.add(commandRead);
        } else if (commandType.equals("update")) {
            parameters.add(commandParameter);
            parameters.add(commandUpdate);
            getUpdateParameters();
        } else if (commandType.equals("delete")) {
            parameters.add(commandParameter);
            parameters.add(commandDelete);
            getDeleteParameters();
        } else if (commandType.equals("search")) {
            parameters.add(commandParameter);
            parameters.add(commandSearch);
            getSearchParameters();
        } else {
            //invalid command-what to do? return error or parameters.add("invalid")
        }
         
   
    
    return parameters;
    
    }

    public String getCommandType() {
        String[] splitString = splitUserCommand();
        return splitString[0];
        
     }
    
    public String[] splitUserCommand() {
        String[] words = userCommand.trim().split(" ", 2);
        return words;
    }
    public String determineCommandType(String userCommandType) {
        if (userCommandType.equalsIgnoreCase("add") || userCommandType.equalsIgnoreCase("create")) {
            return "createTask";
        } else if (userCommandType.equalsIgnoreCase("searchAll") || userCommandType.equalsIgnoreCase("read")||userCommandType.equalsIgnoreCase("display")) {
            return "read";
        } else if (userCommandType.equalsIgnoreCase("update") || userCommandType.equalsIgnoreCase("edit")) {
            return "update";
        } else if (userCommandType.equalsIgnoreCase("delete") || userCommandType.equalsIgnoreCase("remove")) {
            return "delete";
        } else if (userCommandType.equalsIgnoreCase("search") || userCommandType.equalsIgnoreCase("get")||userCommandType.equalsIgnoreCase("find")) {
            return "search";
        } else {
            return "invalid";
        }
        
    }

    public void getCreateParameters() {
        String[] userParameters  = splitUserCommand();
        String createType = getCreateType(userParameters[1]);
        parameters.add(typeParameter);
        parameters.add(createType);
        
        
        parameters.add(descriptionParameter);
        if (createType.equals("timed")) {
            String[] array2 = userParameters[1].split("from",2);
            parameters.add(array2[0]);
            String[] array3 = array2[1].trim().split("to",2);
            parameters.add(startTimeParameter);
            parameters.add(array3[0]);
            parameters.add(endTimeParameter);
            parameters.add(array3[1]);
            
        } else if (createType.equals("deadline")) {
            String[] array1 =userParameters[1].trim().split("due on",2);
            parameters.add(endTimeParameter);
            parameters.add(array1[1]);
        } else {
            parameters.add(userParameters[1]);
        }
        
        
    }

    public void getUpdateParameters() {
        String words[] = splitUserCommand();
        parameters.add(fieldParameter);

        String[] array1 = words[1].trim().split(" ",2);
        if (array1[0].equalsIgnoreCase("startTime")) {
            parameters.add(startTimeParameter);
        
        } else if (array1[0].equalsIgnoreCase("endTime")) {
            parameters.add(endTimeParameter);
        } else {
            parameters.add(descriptionParameter);
        }
        
        parameters.add(newValueParameter);
        String[] newValues = array1[1].trim().split("to",2);
        parameters.add(newValues[1]);
        
        parameters.add(descriptionParameter);
        parameters.add(newValues[0]);

    }
    
    public void getDeleteParameters() {
        String[] words = splitUserCommand();
        parameters.add(descriptionParameter);
        parameters.add(words[1]);
    }
    
    public void getSearchParameters() {
        String[] words = splitUserCommand();
        parameters.add(searchKeyParameter);
        parameters.add(words[1]);
        
    }
    
    public String getCreateType (String userValues) {
        String taskType="";
        int indexDeadline = userValues.toLowerCase().indexOf(deadlineTaskKey);
        int indexTimed = userValues.toLowerCase().indexOf(timedTaskKey);
        if (indexDeadline != -1) {
            taskType = "deadline";
        } else if (indexTimed!=-1) {
            taskType = "timed";
        } else {
            taskType= "floating";
        }
        return taskType;
    }
    
}
