package utility;

import java.util.ArrayList;
import java.util.Arrays;

public enum Keyword {
    
    CREATE      (new String[] { "add", "create", "new" }),
    READ        (new String[] { "show", "display", "search", "read" }),
    UPDATE      (new String[] { "update", "change" }), 
    DELETE      (new String[] { "delete", "remove" }), 
    EXIT        (new String[] { "exit", "quit", "logout" }), 
    QEXIT       (new String[] { "qexit", "qe", "halt" }),
    UNDO        (new String[] { "undo", "cancel" }),
    CLEAR       (new String[] { "clear", "deleteAll", "removeAll" }),
    COMPLETED   (new String[] { "completed", "complete", "finished", "done" }),
    INCOMPLETE  (new String[] { "incomplete", "not complete","not done", 
                                "uncomplete" }),
    EMPTYSLOT   (new String[] { "emptyslot", "find", "findempty", "fes", 
                                "findslot" }),
    
   
    METHOD      (new String[] { "method" }), 
    CONTENT     (new String[] { "content" }), 
    NEWCONTENT  (new String[] { "description", "name", "task" }), 
    START       (new String[] { "start", "begin", "from", "after", "st" }), 
    END         (new String[] { "end", "due", "finish", "to", "until", "on", 
                                "before", "en" }), 
    EXACT       (new String[] { "exact" }),
    TYPE        (new String[] { "type" }),
    ALLDAY      (new String[] { "allday" }),
    ARCHIVED    (new String[] { "archived" }),
    FOREVER     (new String[] { "forever", "permanent", "force" }),
    SORT        (new String[] { "sort", "organise" }),
    ALL         (new String[] { "all" , "everything" }),
    
    FLOATING    (new String[] { "floating" }),
    TIMED       (new String[] { "timed" }),
    DEADLINE    (new String[] { "deadline" }),

    INVALID     (new String[] {});
 
    private String[] dictionary;

    //@author A0119656W
    /**
     * Use a particular dictionary
     * @param otherOptions
     */
    private Keyword(String[] otherOptions) {
        this.dictionary = otherOptions;  
    }
    
    //@author A0119656W
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
    
    //@author A0119656W
    /**
     * Check if the keyword is a command
     * @param input
     * @return boolean
     */
    public static boolean isCommand(Keyword input) {
        Keyword[] commandList = { CREATE, READ, DELETE, UPDATE, EXIT, QEXIT, 
                                  UNDO, CLEAR, COMPLETED, INCOMPLETE, 
                                  EMPTYSLOT };
        return Arrays.asList(commandList).contains(input);
    }
    
    //@author A0119656W
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
            argList = new Keyword[] { CONTENT, EXACT, START, END , SORT ,
                                      COMPLETED, ARCHIVED };
        } else if (method.equals(UPDATE)) {
            argList = new Keyword[] { CONTENT, NEWCONTENT, START, END, FOREVER, 
                                      COMPLETED };
        } else if (method.equals(DELETE)) {
            argList = new Keyword[] { CONTENT, FOREVER };
        } else if (method.equals(COMPLETED)) {
            argList = new Keyword[] { CONTENT };
        } else if (method.equals(INCOMPLETE)) {
            argList = new Keyword[] { CONTENT };
        } else if (method.equals(UNDO)) {
            argList = new Keyword[] { CONTENT };
        } else if (method.equals(CLEAR)) {
            argList = new Keyword[] { CONTENT,FOREVER };
        } else if (method.equals(EXIT)) {
            argList = new Keyword[] { CONTENT };
        } else if (method.equals(EMPTYSLOT)) {
            argList = new Keyword[] { CONTENT, START, END };
        }
        return Arrays.asList(argList).contains(key);
    }
    
    //@author A0119656W
    public static String[] listDictionary(Keyword method) {
        return method.dictionary;
    }
    
    //@author A0113012J
    /**
     * List all available commands for autocomplete purposes
     * @return
     */
    public static String[] listAllCommands() {
        Keyword[] commandList = { CREATE, READ, DELETE, UPDATE, EXIT, QEXIT, 
                                  UNDO, CLEAR, COMPLETED, INCOMPLETE, 
                                  EMPTYSLOT };
        ArrayList<String> merged = new ArrayList<String>();
        for (int i = 0; i < commandList.length; ++i) {
            String[] dictionary = listDictionary(commandList[i]);
            merged.addAll(new ArrayList<String>(Arrays.asList(dictionary)));
        }
        
        String[] finalResult = new String[merged.size()];
        merged.toArray(finalResult);
        return finalResult;
    }
    
    //@author A0113012J
    /**
     * List all available arguments for autocomplete purposes
     * @return
     */
    public static String[] listAllArguments() {
        Keyword[] argumentList = { METHOD, CONTENT, NEWCONTENT, START, END, 
                                   EXACT, TYPE, ALLDAY, ARCHIVED, FOREVER, 
                                   SORT, ALL };
        ArrayList<String> mergedResult = new ArrayList<String>();
        for (int i = 0; i < argumentList.length; ++i) {
            ArrayList<String> dictionary = new ArrayList<String>(Arrays.asList(listDictionary(argumentList[i])));
            for (int j = 0; j < dictionary.size(); ++j) {
                dictionary.set(j,"--" + dictionary.get(j));
            }
            mergedResult.addAll(dictionary);
        }
        
        String[] finalResult = new String[mergedResult.size()];
        mergedResult.toArray(finalResult);
        return finalResult;
    }
    
}
