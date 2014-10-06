package pact;

import java.util.Arrays;

public enum CommandType {
    CREATE(0), READ(1), UPDATE(2), DELETE(3), EXIT(4), UNDO(5), INVALID(-1);
    public String[] dict;
    private String[][] allDict = {{ "add", "create", "new" }, 
                                  { "show", "display", "search", "find" }, 
                                  { "update", "change" }, 
                                  { "delete", "remove" }, 
                                  { "exit", "quit", "halt" },
                                  { "undo", "cancel" } };

    private CommandType(int passcode) {
        if (passcode != -1) {
            this.dict = allDict[passcode];  
        }
    }
    
    public static CommandType getCommandType(String code) {
        code = code.toLowerCase().trim();
        for (CommandType command : CommandType.values()) {
            if (command != INVALID) {
                if (Arrays.binarySearch(command.dict, code) >=0) {
                    return command;
                }
            }
        }
        return INVALID;
    }
            
}
