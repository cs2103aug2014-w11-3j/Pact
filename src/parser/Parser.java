package parser;

import java.util.HashMap;

import utility.Clock;
import utility.Keyword;

public class Parser {

    public HashMap<Keyword, String> parameters = new HashMap<Keyword, String>();
        
    public void getParameters(Keyword method, String input) throws Exception {
        String key, value;
        Keyword argType;
        String[] argument = input.trim().split("--");
        for (int i = 0; i < argument.length; ++i) {
            if (i == 0) {
                key = "content";
                value = argument[0].trim();
            } else {
                String[] tmp = argument[i].trim().split(" ", 2);
                key = tmp[0].trim();
                value = tmp[1].trim();
            }
            argType = Keyword.getMeaning(key);
            if (argType.equals(Keyword.START) || argType.equals(Keyword.END)) {
                if (value.contains("at")) {
                    String[] tmp = value.trim().split("at", 2);
                    value = new Clock().parse(tmp[0], tmp[1]);
                } else {
                    if (argType.equals(Keyword.START)) {
                        value = new Clock().parse(value, "0000");
                    } else {
                        value = new Clock().parse(value, "2359");
                    }
                }
            }
            parameters.put(argType, value);
        }
    }

    public HashMap<Keyword, String> parse(String userInput) throws Exception {
        parameters.clear();
        String[] splitString = userInput.trim().split(" ", 2);
        Keyword code = Keyword.getMeaning(splitString[0]);
        getParameters(code, splitString[1]);
        return parameters;
    }
    
    //------------------Remove when complete-----------------
    public static void main(String args[]) throws Exception {
        String command = "add submit assignment to Ms.Isabel --start 08/10/2014 --end 10-Oct-2014 at 23:59";
        System.out.println(new Parser().parse(command));
    }
    //-------------------------------------------------------

}
