package parser;

import java.text.ParseException;

public class ParserForCreate extends ParserForAll {
    
    public void getParameters(String input) {
    }

    public void getCreateParameters() throws ParseException {
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
            parameters.add(array3[0].trim());
            parameters.add(endTimeParameter);
            parameters.add(array3[1].trim());
            
        } else if (createType.equals("deadline")) {
            String[] array1 =userParameters[1].trim().split("due on",2);
            parameters.add(array1[0].trim());
            parameters.add(endTimeParameter);
            parameters.add(array1[1].trim());
        } else {
            parameters.add(userParameters[1].trim());
        }
        
    }

    public String getCreateType (String userValues) throws ParseException {
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
