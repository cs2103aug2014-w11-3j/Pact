package utility;

import java.util.Arrays;

public enum Keyword {
    CREATE  (new String[]{ "add", "create", "new" }),
    READ    (new String[]{ "show", "display", "search", "find" }),
    UPDATE  (new String[]{ "update", "change" }), 
    DELETE  (new String[]{ "delete", "remove" }), 
    EXIT    (new String[]{ "exit", "quit", "halt" }), 
    UNDO    (new String[]{ "undo", "cancel" }), 
    CONTENT (new String[]{ "content", "description", "name", "task" }), 
    START   (new String[]{ "start", "begin" }), 
    END     (new String[]{ "end", "due", "finish" }), 
    METHOD  (new String[]{ "method" }), 
    INVALID (new String[]{});

    public String[] dict;

    private Keyword(String[] otherOptions) {
        this.dict = otherOptions;  
    }
    
    public static Keyword getMeaning(String code) {
        code = code.toLowerCase().trim();
        for (Keyword command : Keyword.values()) {
            if (command != INVALID) {
                if (Arrays.asList(command.dict).contains(code)) {
                    return command;
                }
            }
        }
        return INVALID;
    }
    
    public static boolean isCommand(Keyword input) {
        Keyword[] commandList = {CREATE, READ, DELETE, UPDATE, EXIT, UNDO};
        if (Arrays.asList(commandList).contains(input)) {
            return true;
        } else {
            return false;
        }
    }
    
}
