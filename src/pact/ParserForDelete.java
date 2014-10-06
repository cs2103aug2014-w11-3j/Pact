package pact;

import java.text.ParseException;

public class ParserForDelete {
    public void getDeleteParameters() throws ParseException {
        String[] words = splitUserCommand();
        parameters.add(descriptionParameter);
        parameters.add(words[1]);
    }
    

}
