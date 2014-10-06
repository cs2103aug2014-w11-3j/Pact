package pact;

import java.text.ParseException;

public class ParserForUpdate {
    public void getUpdateParameters() throws ParseException{
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
        parameters.add(newValues[1].trim());
        
        parameters.add(descriptionParameter);
        parameters.add(newValues[0].trim());
        
        //for (int i = 0; i < parameters.size(); ++i)
        //    System.out.println(parameters.get(i));
    }

}
