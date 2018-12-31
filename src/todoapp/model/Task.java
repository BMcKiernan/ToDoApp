package todoapp.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;

/**
 * The Task class represents an objective to be completed as part of a ToDoList.
 * A Task contains a String description, LocalDate completionDate or a date to be completed by,
 * and a boolean field called complete which tells if this Task has been completed.
 * @author Brian McKiernan
 */
public class Task extends RecursiveTreeObject<Task> {
    public SimpleStringProperty description;
    public SimpleStringProperty completionDate;
    public boolean complete;
    private final String pattern = "yyyy-MM-dd";
    private static DateTimeFormatter dateFormatter;
    
    public Task(String description, LocalDate completionDate){
        this.description = new SimpleStringProperty(description);
        this.complete = false;
        dateFormatter = DateTimeFormatter.ofPattern(pattern);
        this.completionDate = new SimpleStringProperty(completionDate.format(dateFormatter));
    }
    
    /**
     * getDescription returns the String description field
     * @return description
     */
    public String getDescription(){
        return description.get();
    }
    
    /**
     * setDescription sets this Tasks description to the new value.
     * @param description new value to be set
     */
    public void setDescription(String description){
        this.description = new SimpleStringProperty(description);
    }
    
    /**
     * getCompletionDate gets the date when the task should be completed by as a
     * String so it can be edited in a TreeTableView
     * @return completionDate String
     */
    public String getCompletionDate(){
        return completionDate.get();
    }
    
    /**
     * setCompletionDate sets the date when the task should be completed by as a
     * String because it is edited in a TreeTableView
     * @param completionDate 
     */
    public void setCompletionDate(LocalDate completionDate){
        this.completionDate = new SimpleStringProperty(completionDate.format(dateFormatter));
    }
    
    /**
     * isComplete returns the boolean complete which will be true or false depending
     * on whether it has been checked off in the TreeTableView
     * @return complete
     */
    public boolean isComplete(){
        return complete;
    }
    
    /**
     * setComplete sets the Tasks complete field to a new boolean value.
     * @param complete boolean to be set
     */
    public void setComplete(boolean complete){
        this.complete = complete;
    }
    
}
