package utility;

import java.util.Arrays;

public enum Keyword {
    CREATE      (new String[] { "add", "create", "new" }),
    READ        (new String[] { "show", "display", "search", "find", "read" }),
    UPDATE      (new String[] { "update", "change" }), 
    DELETE      (new String[] { "delete", "remove" }), 
    EXIT        (new String[] { "exit", "quit", "halt" }), 
    UNDO        (new String[] { "undo", "cancel" }),
    CLEAR       (new String[] {"clear", "deleteAll", "removeAll" }),
    
    METHOD      (new String[] { "method" }), 
    CONTENT     (new String[] { "content"}), 
    NEWCONTENT  (new String[] { "description", "name", "task" }), 
    START       (new String[] { "start", "begin", "from", "after" }), 
    END         (new String[] { "end", "due", "finish", "to", "until", "on", "before" }), 
    EXACT       (new String[] { "exact" }),
    TYPE        (new String[] { "type" }),
    ALLDAY      (new String[] { "allday" }),
    COMPLETED   (new String[] { "completed" }),
    ARCHIVED    (new String[] { "archived" }),
    FOREVER     (new String[] { "forever", "permanent", "force" }),

    FLOATING    (new String[] { "floating" }),
    TIMED       (new String[] { "timed" }),
    DEADLINE    (new String[] { "deadline" }),

    INVALID     (new String[] {});

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
        Keyword[] commandList = {CREATE, READ, DELETE, UPDATE, EXIT, UNDO,CLEAR};
        return Arrays.asList(commandList).contains(input);
    }
    
    public static boolean isProperArgument(Keyword method, Keyword key) {
        Keyword[] argList = {};
        if (method.equals(CREATE)) {
            argList = new Keyword[] { CONTENT, START, END };
        } else if (method.equals(READ)) {
            argList = new Keyword[] { CONTENT, EXACT, START, END };
        } else if (method.equals(UPDATE)) {
            argList = new Keyword[] { CONTENT, NEWCONTENT, START, END, FOREVER };
        } else if (method.equals(DELETE)) {
            argList = new Keyword[] { CONTENT, FOREVER };
        } else if (method.equals(UNDO)) {
            argList = new Keyword[] {CONTENT};
        } else if (method.equals(CLEAR)) {
            argList = new Keyword[] {CONTENT};
        }
        return Arrays.asList(argList).contains(key);
    }
    
}
