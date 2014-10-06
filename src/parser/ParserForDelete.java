package parser;

import java.text.ParseException;

public class ParserForDelete extends ParserForAll {

    public void getParameters(String input) {
    }

    public void getDeleteParameters() throws ParseException {
        String[] words = splitUserCommand();
        parameters.add(descriptionParameter);
        parameters.add(words[1]);
    }
    

}
