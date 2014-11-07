package parser;

import java.util.HashMap;

import utility.Clock;
import utility.Clock.TimeType;
import utility.Keyword;

public class Parser {

    private static final String MISSING_ENDDATE = "End date required\n";
    private static final String NO_ARGUMENTS_ADD = "Arguments required for adding a task\n\n";
    private static final String NO_ARGUMENTS_UPDATE = "Arguments required for updating a task\n\n";
    private static final String NO_ARGUMENTS_DELETE = "Arguments required for deleting a task\n\n";
    private static final String NO_ARGUMENTS_COMPLETED = "Arguments are required for completing tasks\n\n";
    private static final String NO_ARGUMENTS_INCOMPLETE = "Arguments are required for incomplete task command\n\n";
    private static final String NO_ARGUMENTS_EMPTYSLOT = "Arguments are required to search for empty slots\n\n";
    private static final String COMPLETED_TASK_FORMAT = "Format for complete task : completed <taskName>\n";
    private static final String INCOMPLETE_TASK_FORMAT = "Format for incomplete task : incomplete <taskName>\n";
    private static final String TIMED_TASK_FORMAT = "Format for Time Task : add <taskName> --st <startDate> at <time> --en <endDate> at <time>\n";
    private static final String DEADLINE_TASK_FORMAT = "Format for Deadline Task : add <taskName> --en <endDate> at <time>\n";
    private static final String FlOATING_TASK_FORMAT = "Format for Floating Task : add <taskName>\n";
    private static final String EMPTYSLOT_TASK_FORMAT ="Format for EmptySlot : emptyslot --after <startDate> --before <endDate>\n";
    private static final String UPDATE_FORMAT = "Format for update/change : update/change <taskName> --<field> <changeToValue>\n";
    private static final String DELETE_FORMAT = "Format for delete : delete <taskName>\n";
    private static final String INVALID_COMMAND = "Invalid Command\n\n";
    private static final String AVAILABLE_COMMANDS = "Available commands : \"create\", \"update\", \"delete\", \"search\", \"display\", \"undo\", \"completed\", \"exit\", \"qexit\" \n";
    private static final String NON_MATCHING_FORMAT = "Start and end time must have the same format\n";
    private static final String IMPROPER_ARGUMENT = "Argument is invalid\n";
    private static final String INCORRECT_TIME_FORMAT = "Incorrect format to add time\n";
    private static final String EXCEEDED_CHAR_LIMIT = "Task description has exceeded 30 characters";
    private static final String INVALID_DATE = "You have entered an invalid date.\n\n";
    private static final String EXCEED_DATE_LIMIT ="Maximum search period for emptyslot is 7 days\n\n";
    private static final String HELP = "Valid Formats:\n";
    private HashMap<Keyword, String> parameters = new HashMap<Keyword, String>();
    
    private TimeType startType;
    private TimeType endType;
    
    /**
     * Insert isAllDay parameter into HashMap
     * @param isAllDay
     */
    private void setAllDay(boolean isAllDay) {
        parameters.put(Keyword.ALLDAY, Boolean.toString(isAllDay));
    }
    /**
     * Insert typeOfTask parameter into HashMap
     * @param typeOfTask
     */
    private void setType(Keyword typeOfTask) {
        parameters.put(Keyword.TYPE, typeOfTask.toString());
    }
    
    /**
     * set the type of Task : timed, floating, deadline 
     * @throws Exception
     */
    private void configCreate() throws Exception {
        if (!startType.equals(TimeType.NONE) && !endType.equals(TimeType.NONE)) { 
            //have both start and end
            if (!startType.equals(endType)) {
                throw new Exception(NON_MATCHING_FORMAT);
            }
            setType(Keyword.TIMED);
            setAllDay(endType.equals(TimeType.DATE));
        } else {
            //don't have start or don't have end
            if (!startType.equals(TimeType.NONE)) {
                throw new Exception(MISSING_ENDDATE + HELP + TIMED_TASK_FORMAT);
            }
            if (!endType.equals(TimeType.NONE)) {
                //have end
                setType(Keyword.DEADLINE);
                setAllDay(endType.equals(TimeType.DATE));
            } else {
                //don't have end
                setType(Keyword.FLOATING);
            }
        }
    }
    private void configEmptySlot() throws Exception {
        if (startType.equals(TimeType.NONE)) { 
            throw new Exception(NO_ARGUMENTS_EMPTYSLOT + HELP + EMPTYSLOT_TASK_FORMAT);
        }
        if(endType.equals(TimeType.NONE)){
            throw new Exception(NO_ARGUMENTS_EMPTYSLOT + HELP + EMPTYSLOT_TASK_FORMAT);
        }
        
    }
    
    /**
     * Parse the string and set the TIMETYPE for the Task
     * @param argType START/END
     * @param value
     * @return parsed time 
     * @throws Exception
     */
    private String parseTimeArg(Keyword argType, String value) throws Exception {
        if (value.contains("at")) {
            String[] tmp = value.trim().split("at", 2);
            value = new Clock().parse(tmp[0], tmp[1]);
            if (argType.equals(Keyword.START)) {
                startType = TimeType.TIME;
            } else {
                endType = TimeType.TIME;
            }
        } else {
            String delims = "[ ]+";
            String[] tmp = value.trim().split(delims);
            if (tmp.length > 3) {
                throw new Exception(INCORRECT_TIME_FORMAT + HELP + TIMED_TASK_FORMAT + DEADLINE_TASK_FORMAT);
            }
            if (argType.equals(Keyword.START)) {
                value = new Clock().parse(value, "0000");
                startType = TimeType.DATE;
            } else {
                value = new Clock().parse(value, "2359");
                endType = TimeType.DATE;
            }
        }
        String[] check = value.split(" ");
        //System.out.println(check[0]);
        if (check[0].length() > 10) {
            throw new Exception(INVALID_DATE + HELP + FlOATING_TASK_FORMAT + DEADLINE_TASK_FORMAT + TIMED_TASK_FORMAT);
        }
        return value;
    }
    
    
    /**
     * Process string array to get parameters and put in HashMap
     * @param method
     * @param argument
     * @throws Exception
     */
    private void getParameters(Keyword method, String[] argument) throws Exception {
        parameters.put(Keyword.METHOD, method.toString().toLowerCase());
        startType = TimeType.NONE;
        endType = TimeType.NONE;
        String key, value;
        Keyword argType;
        
        for (int i = 0; i < argument.length; ++i) {
           
            if (i == 0) {
                key = "content";
                value = argument[0].trim();
                if (value.length() > 29) {
                    throw new Exception(EXCEEDED_CHAR_LIMIT);
                }
            
            } else {
                argument[i] = argument[i].trim() + " ";
                String[] tmp = argument[i].split(" ", 2);
                key = tmp[0].trim();
                value = tmp[1].trim();
            }
            argType = Keyword.getMeaning(key);
            if (!Keyword.isProperArgument(method, argType)) {
                throw new Exception(IMPROPER_ARGUMENT);
            }
            if (argType.equals(Keyword.START) || argType.equals(Keyword.END)) {
                value = parseTimeArg(argType, value);
            }
            parameters.put(argType, value);
        }
        if (method.equals(Keyword.CREATE)) {
            configCreate();
        }
        if (method.equals(Keyword.EMPTYSLOT)) {
            configEmptySlot();
        }
    }
    
    
    

    private void checkExceptions(Keyword userCode, String values) throws Exception {
        if (!Keyword.isCommand(userCode)) {
            throw new Exception(INVALID_COMMAND + AVAILABLE_COMMANDS);
        }
        if (userCode.equals(Keyword.QEXIT)) {
            throw new Exception("exit from program");
        }
        if (values.equals("") || (values.charAt(0) == '-' && values.charAt(1) == '-')) {
            if (userCode.equals(Keyword.CREATE)) {
                throw new Exception(NO_ARGUMENTS_ADD + HELP + FlOATING_TASK_FORMAT + DEADLINE_TASK_FORMAT + TIMED_TASK_FORMAT);
            } else if (userCode.equals(Keyword.UPDATE)) {
                throw new Exception(NO_ARGUMENTS_UPDATE + HELP + UPDATE_FORMAT);
            } else if (userCode.equals(Keyword.DELETE)) {
                throw new Exception(NO_ARGUMENTS_DELETE + HELP + DELETE_FORMAT);
            } else if (userCode.equals(Keyword.COMPLETED)) {
                throw new Exception(NO_ARGUMENTS_COMPLETED + HELP + COMPLETED_TASK_FORMAT);
            } else if (userCode.equals(Keyword.INCOMPLETE)) {
                throw new Exception(NO_ARGUMENTS_INCOMPLETE + HELP + INCOMPLETE_TASK_FORMAT);
            }
        }

        
        if(userCode.equals(Keyword.EMPTYSLOT)){
            
            String[] check = values.trim().split("--");
           
            
            if(values.equals("")||values.charAt(0) != '-'){
                 throw new Exception(NO_ARGUMENTS_EMPTYSLOT + HELP + EMPTYSLOT_TASK_FORMAT);
             }
            if(check.length<3){
                throw new Exception(NO_ARGUMENTS_EMPTYSLOT + HELP + EMPTYSLOT_TASK_FORMAT);
            }
            
             
        }
         
    }

    public void checkDateLimit() throws Exception{
       Clock clock = new Clock();
    	String st = parameters.get(Keyword.START);
    	String en = parameters.get(Keyword.END);
        if(Integer.parseInt(clock.getDateDifference(st, en)) >7){
           throw new Exception(EXCEED_DATE_LIMIT);
       }
    }
    
    /**
     * Parse the userInput and calls appropriate methods
     * @param userInput
     * @return parameters HashMap<Keyword, String>
     * @throws Exception
     */
    public HashMap<Keyword, String> parse(String userInput) throws Exception {
        parameters.clear();
        userInput = userInput.trim();
        userInput = userInput + " ";
        String[] splitString = userInput.split(" ", 2);
        Keyword code = Keyword.getMeaning(splitString[0].trim());
        userInput = splitString[1].trim();
        checkExceptions(code, userInput);
        String[] argument = userInput.trim().split("--");
        getParameters(code, argument);
        if (code.equals(Keyword.EMPTYSLOT)) {
            checkDateLimit();
        }
        return parameters;
    }
} 
