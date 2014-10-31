package utility;

import java.util.Arrays;

public enum Keyword {
    CREATE      (new String[] { "add", "create", "new" }),
    READ        (new String[] { "show", "display", "search", "find", "read" }),
    UPDATE      (new String[] { "update", "change" }), 
    DELETE      (new String[] { "delete", "remove" }), 
    EXIT        (new String[] { "exit", "quit", "logout" }), 
    QEXIT       (new String[] { "qexit", "qe", "halt" }),
    UNDO        (new String[] { "undo", "cancel" }),
    CLEAR       (new String[] { "clear", "deleteAll", "removeAll" }),
    
    METHOD      (new String[] { "method" }), 
    CONTENT     (new String[] { "content"}), 
    NEWCONTENT  (new String[] { "description", "name", "task" }), 
    START       (new String[] { "start", "begin", "from", "after", "st" }), 
    END         (new String[] { "end", "due", "finish", "to", "until", "on", "before", "en" }), 
    EXACT       (new String[] { "exact" }),
    TYPE        (new String[] { "type" }),
    ALLDAY      (new String[] { "allday" }),
    COMPLETED   (new String[] { "completed", "complete" }),
    ARCHIVED    (new String[] { "archived" }),
    FOREVER     (new String[] { "forever", "permanent", "force" }),
    SORT		(new String[] { "sort", "organise" }),

    FLOATING    (new String[] { "floating" }),
    TIMED       (new String[] { "timed" }),
    DEADLINE    (new String[] { "deadline" }),

    INVALID     (new String[] {});
 
    private String[] dictionary;

    /**
     * Use a particular dictionary
     * @param otherOptions
     */
    private Keyword(String[] otherOptions) {
        this.dictionary = otherOptions;  
    }
    
    /**
     * Obtain the meaning of the command string
     * @param code
     * @return Keyword
     */
    public static Keyword getMeaning(String code) {
        code = code.toLowerCase().trim();
        for (Keyword command : Keyword.values()) {
            if (command != INVALID) {
                if (Arrays.asList(command.dictionary).contains(code)) {
                    return command;
                }
            }
        }
        return INVALID;
    }
    
    /**
     * Check if the keyword is a command
     * @param input
     * @return boolean
     */
    public static boolean isCommand(Keyword input) {
        Keyword[] commandList = {CREATE, READ, DELETE, UPDATE, EXIT, QEXIT, UNDO, CLEAR};
        return Arrays.asList(commandList).contains(input);
    }
    
    /**
     * Check if the input arguments are valid
     * @param method
     * @param key
     * @return
     */
    public static boolean isProperArgument(Keyword method, Keyword key) {
        Keyword[] argList = {};
        if (method.equals(CREATE)) {
            argList = new Keyword[] { CONTENT, START, END };
        } else if (method.equals(READ)) {
<<<<<<< HEAD
            argList = new Keyword[] { CONTENT, EXACT, START, END ,SORT,COMPLETED,ARCHIVED};
=======
            argList = new Keyword[] { CONTENT, EXACT, START, END ,SORT , COMPLETED};
>>>>>>> effc81f7fa8edeeac6ecbd6941bae376fccf94a3
        } else if (method.equals(UPDATE)) {
            argList = new Keyword[] { CONTENT, NEWCONTENT, START, END, FOREVER, COMPLETED};
        } else if (method.equals(DELETE)) {
            argList = new Keyword[] { CONTENT, FOREVER };
        } else if (method.equals(UNDO)) {
            argList = new Keyword[] { CONTENT};
        } else if (method.equals(CLEAR)) {
            argList = new Keyword[] { CONTENT,FOREVER};
        } else if (method.equals(EXIT)) {
        	argList = new Keyword[] { CONTENT };
        }
        return Arrays.asList(argList).contains(key);
    }
    
}
