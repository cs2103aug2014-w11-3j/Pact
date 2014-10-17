package parser;

import java.util.HashMap;

import utility.Clock;
import utility.Clock.TimeType;
import utility.Keyword;

public class Parser {

    public HashMap<Keyword, String> parameters = new HashMap<Keyword, String>();
    
    public TimeType startType;
    public TimeType endType;
    
    private void setAllDay(boolean isAllDay) {
        parameters.put(Keyword.ALLDAY, Boolean.toString(isAllDay));
    }
    private void setType(Keyword typeOfTask) {
        parameters.put(Keyword.TYPE, typeOfTask.toString());
    }
    
    public void configCreate() throws Exception {
        if (!startType.equals(TimeType.NONE) && !endType.equals(TimeType.NONE)) { 
            //have both start and end
            if (!startType.equals(endType)) {
                throw new Exception("start and end must have same format");
            }
            setType(Keyword.TIMED);
            setAllDay(endType.equals(TimeType.DATE));
        } else {
            //don't have start or don't have end
            if (!startType.equals(TimeType.NONE)) {
                throw new Exception("must have end time if have start time");
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
            if (argType.equals(Keyword.START)) {
                value = new Clock().parse(value, "0000");
                startType = TimeType.DATE;
            } else {
                value = new Clock().parse(value, "2359");
                endType = TimeType.DATE;
            }
        }
        return value;

    }
    
    public void getParameters(Keyword method, String input) throws Exception {
        parameters.put(Keyword.METHOD, method.toString().toLowerCase());
        startType = TimeType.NONE;
        endType = TimeType.NONE;
        String key, value;
        Keyword argType;
        String[] argument = input.trim().split("--");
        
        for (int i = 0; i < argument.length; ++i) {
           
            if (i == 0) {
                key = "content";
                value = argument[0].trim();
            
            } else {
                argument[i] = argument[i].trim() + " ";
                String[] tmp = argument[i].split(" ", 2);
                key = tmp[0].trim();
                value = tmp[1].trim();
            }
            argType = Keyword.getMeaning(key);
            if (!Keyword.isProperArgument(method, argType)) {
                throw new Exception("not a proper argument");
            }
            if (argType.equals(Keyword.START) || argType.equals(Keyword.END)) {
                value = parseTimeArg(argType, value);
            }
            parameters.put(argType, value);
        }
        if (method.equals(Keyword.CREATE)) {
            configCreate();
        }
    }

    public HashMap<Keyword, String> parse(String userInput) throws Exception {
        parameters.clear();
        userInput = userInput.trim();
        userInput = userInput + " ";
        String[] splitString = userInput.split(" ", 2);
       
        Keyword code = Keyword.getMeaning(splitString[0].trim());
        if (!Keyword.isCommand(code)) {
            throw new Exception("not a method");
        }
        getParameters(code, splitString[1].trim());
        return parameters;
    }
    
}
