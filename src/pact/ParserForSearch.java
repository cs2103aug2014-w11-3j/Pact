package pact;

import java.text.ParseException;

public class ParserForSearch {
    public void getSearchParameters() throws ParseException{
        String[] words = splitUserCommand();
        parameters.add(searchWholeParameter);
        parameters.add("false");
        parameters.add(searchKeyParameter);
        parameters.add(words[1].trim());
        
    }
    
    public void getSearchWholeParameters() throws ParseException{
       String[] words = userCommand.trim().split(" ",2);
       parameters.add(searchWholeParameter);
       parameters.add("true");
       parameters.add(searchKeyParameter);
       parameters.add(words[1].trim());
    }
    

}
