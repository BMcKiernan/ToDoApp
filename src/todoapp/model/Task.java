package todoapp.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

/**
 * The Task class represents an objective to be completed as part of a ToDoList.
 * A Task contains a String description, LocalDate completionDate or a date to be completed by,
 * and a boolean field called complete which tells if this Task has been completed.
 * It also contains a list of subTasks which is made up of more tasks. This field
 * is used for maintaining nested subTasks added in the TreeTableView in the 
 * TaskListView
 * @author Brian McKiernan
 */
public class Task extends RecursiveTreeObject<Task> {
    private SimpleStringProperty description;
    private SimpleStringProperty completionDate;
    private boolean complete;
    private List<Task> subTasks;
    private final String pattern = "yyyy-MM-dd";
    private static DateTimeFormatter dateFormatter;
    
    public Task(String description, LocalDate completionDate){
        this.description = new SimpleStringProperty(description);
        this.complete = false;
        dateFormatter = DateTimeFormatter.ofPattern(pattern);
        this.completionDate = new SimpleStringProperty(completionDate.format(dateFormatter));
        subTasks = new ArrayList<Task>();
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
    public void setCompletionDate(String completionDate){
        this.completionDate = new SimpleStringProperty(completionDate);
    }
    
    /**
     * addSubTask adds a new subTask to this Tasks list of subTasks
     * @param task 
     */
    public void addSubTask(Task subTask){
        this.subTasks.add(subTask);
    }
    
    /**
     * getSubTasks returns this Tasks list of Tasks which store nested ToDoList
     * items
     * @return subTasks list
     */
    public List<Task> getSubTasks(){
        return subTasks;
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
