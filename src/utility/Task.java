package utility;

import java.util.Scanner;

public class Task {
    
    private String content;
    private String start;
    private String end;
    private Keyword type;
    private boolean isAllDayTask;
    private boolean isCompleted;
    private boolean isArchived;
    
    
    /**
     * Construct a new Task object
     */
    public Task() {
        this.content = "";
        this.start = "";
        this.end = "";
        this.type = Keyword.FLOATING;
        this.isAllDayTask = false;
        this.isCompleted = false;
        this.isArchived = false;
    }
    
    public Task(Task copy) {
        this.content = copy.getValue(Keyword.CONTENT);
        this.start = copy.getValue(Keyword.START);
        this.end = copy.getValue(Keyword.END);
        this.type = Keyword.getMeaning(copy.getValue(Keyword.TYPE));
        this.isCompleted = Boolean.parseBoolean(copy.getValue(Keyword.COMPLETED));
        this.isArchived = Boolean.parseBoolean(copy.getValue(Keyword.ARCHIVED));
        this.isAllDayTask = Boolean.parseBoolean(copy.getValue(Keyword.ALLDAY));
    }
    
    /* 
     * Construct string to print contents of Task
     * @return constructed string
     */
    public String toString() {
        String result = "";
        String TAB = "    ";
        result = result + TAB + Keyword.CONTENT + ": " + this.content + "\n";
        if (!this.type.equals(Keyword.FLOATING)) {
            result = result + TAB + Keyword.END + ": " + this.end + "\n";
            if (!this.type.equals(Keyword.DEADLINE)) {
                result = result + TAB + Keyword.START + ": " + this.start + "\n";
            }
        }
        result = result + TAB + Keyword.TYPE + ": " + this.type + "\n";
        result = result + TAB + Keyword.COMPLETED + ": " + this.isCompleted + "\n";
        result = result + TAB + Keyword.ARCHIVED + ": " + this.isArchived + "\n";
        result = result + TAB + Keyword.ALLDAY + ": " + this.isAllDayTask + "\n";
        return result;
    }
    
    /**
     * Construct string to put into result
     * @return 
     */
   /* public String getDisplayedString() {
        String result = content;
        if (type.equals(Keyword.TIMED)) {
            String[] splitStart = start.split(" ", 2);
            String[] splitEnd = end.split(" ", 2);
            if (!isAllDayTask) {
                result = result + ":" + String.valueOf(isCompleted) + ":" + splitStart[0] + " " + splitStart[1];
                result = result + " " + splitEnd[0] + " " + splitEnd[1];
            } else {
                result = result + ":" + String.valueOf(isCompleted) + ":" + splitStart[0];
                result = result + " " + splitEnd[0];
            }
        } else if (type.equals(Keyword.DEADLINE)) {
            String[] splitEnd = end.split(" ", 2);
            if (!isAllDayTask) {
                result = result + ":" + String.valueOf(isCompleted) + ":" + splitEnd[0] + " " + splitEnd[1];
            } else {
                result = result + ":" + String.valueOf(isCompleted) + ":" +splitEnd[0];
            }
        } else if (type.equals(Keyword.FLOATING)) {
            
            result = result + ":" + String.valueOf(isCompleted) + ":" ;
        }
        return result;
    }*/
    
    public String getDisplayedString() {
        String result = content;
        result = result + ":" + String.valueOf(isCompleted); 
        if (type.equals(Keyword.TIMED)) {
            String[] splitStart = start.split(" ", 2);
            String[] splitEnd = end.split(" ", 2);  
            if (splitEnd[1].equals("23:59") && splitStart[1].equals("00:00")) {
                result = result +":" + splitStart[0];
                result = result + " " + splitEnd[0]; 
            } else {
                result = result + ":" + splitStart[0] + " " + splitStart[1];
                result = result + " " + splitEnd[0] + " " + splitEnd[1];
            }
        } else if (type.equals(Keyword.DEADLINE)) {
            String[] splitEnd = end.split(" ", 2);
            if (!splitEnd[1].equals("23:59")) {
                result = result + ":" + splitEnd[0] + " " + splitEnd[1];
            } else {
                result = result + ":" +splitEnd[0];
            }
        } else if (type.equals(Keyword.FLOATING)) {
            
            result = result + ":" ;
        }
        return result;
    }
    
    /**
     * Get a particular value in a Task object
     * @param key
     * @return
     */
    public String getValue(Keyword key) {
        String value = "";
        if (key.equals(Keyword.CONTENT)) {
            value = content;
        } else if (key.equals(Keyword.START)) {
            value = start;
        } else if (key.equals(Keyword.END)) {
            value = end;
        } else if (key.equals(Keyword.TYPE)) {
            value = String.valueOf(type);
        } else if (key.equals(Keyword.COMPLETED)) {
            value = String.valueOf(isCompleted);
        } else if (key.equals(Keyword.ARCHIVED)) {
            value = String.valueOf(isArchived);
        } else if (key.equals(Keyword.ALLDAY)) {
            value = String.valueOf(isAllDayTask);
        }
        return value;
    }

    /**
     * Change a particular value in a Task object 
     * @param key
     * @param value
     */
    public void setValue(Keyword key, String value) {
        if (key.equals(Keyword.CONTENT)) {
            content = value;
        } else if (key.equals(Keyword.START)) {
            start = value;
        } else if (key.equals(Keyword.END)) {
            end = value;
        } else if (key.equals(Keyword.TYPE)) {
            type = Keyword.getMeaning(value);
        } else if (key.equals(Keyword.COMPLETED)) {
            isCompleted = Boolean.parseBoolean(value);
        } else if (key.equals(Keyword.ARCHIVED)) {
            isArchived = Boolean.parseBoolean(value);
        } else if (key.equals(Keyword.ALLDAY)) {
            isAllDayTask = Boolean.parseBoolean(value);
        }
    }
    
    /**
     * convert formatted string to a Task object
     * @param toConvert
     * @return Task
     */
    public static Task parseTask(String toConvert) {
        Task result = new Task();
        Scanner scanner = new Scanner(toConvert);
        while (scanner.hasNextLine()) {
            String thisLine = scanner.nextLine();
            int colonPosition = thisLine.indexOf(':');
            Keyword key = Keyword.getMeaning(thisLine.substring(0, colonPosition));
            String value = thisLine.substring(colonPosition + 1, thisLine.length()).trim();
            result.setValue(key, value);
        }
        scanner.close();
        return result;
    }
}

