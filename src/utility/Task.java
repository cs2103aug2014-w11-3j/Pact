package utility;

import java.util.Scanner;

public class Task {
    
    public String content;
    public String start;
    public String end;
    public Keyword type;
    public boolean isAllDayTask;
    public boolean isCompleted;
    public boolean isArchived;
    
    public enum TaskType {
        FLOATING, DEADLINE, TIMED
    }
    
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
        this.content = copy.content;
        this.start = copy.start;
        this.end = copy.end;
        this.type = copy.type;
        this.isCompleted = copy.isCompleted;
        this.isArchived = copy.isArchived;
        this.isAllDayTask = copy.isAllDayTask;
    }
    
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
    
    public String getDisplayedString() {
        String result = content;
        if (type.equals(Keyword.TIMED)) {
            String[] splitStart = start.split(" ", 2);
            String[] splitEnd = end.split(" ", 2);
            if (!isAllDayTask) {
                result = result + " from " + splitStart[0] + " at " + splitStart[1];
                result = result + " to " + splitEnd[0] + " at " + splitEnd[1];
            } else {
                result = result + " from " + splitStart[0];
                result = result + " to " + splitEnd[0];
            }
        } else if (type.equals(Keyword.DEADLINE)) {
            String[] splitEnd = end.split(" ", 2);
            if (!isAllDayTask) {
                result = result + " on " + splitEnd[0] + " at " + splitEnd[1];
            } else {
                result = result + " on " + splitEnd[0];
            }
        } else if (type.equals(Keyword.FLOATING)) {
        }
        return result;
    }
    
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

