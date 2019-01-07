package todoapp.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The Task class represents an objective to be completed as part of a ToDoList.
 * A Task contains a String description, LocalDate completionDate or a date to be completed by,
 * and a boolean field called complete which tells if this Task has been completed.
 * It also contains a list of subTasks which is made up of more tasks. This field
 * is used for maintaining nested subTasks added in the TreeTableView in the 
 * TaskListView.
 * @author Brian McKiernan
 */
public class Task extends RecursiveTreeObject<Task> implements Serializable {
    private String description;
    private String completionDate;
    private boolean complete;
    private final String pattern = "yyyy-MM-dd";
    private static DateTimeFormatter dateFormatter;
    private List<Task> subTasks;
    
    /**
     * Task() constructor only accepts description and completionDate.
     * complete is set to false by default.
     * @param description the task description
     * @param completionDate the date when the task must be completed by
     */
    public Task(String description, LocalDate completionDate){
        dateFormatter = DateTimeFormatter.ofPattern(pattern);
        this.description = description;
        this.complete = false;
        this.completionDate = completionDate.format(dateFormatter);
        this.subTasks = new ArrayList<Task>();
    }
    
    /**
     * getDescription returns the String description field
     * @return description
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * setDescription sets this Tasks description to the new value.
     * @param description new value to be set
     */
    public void setDescription(String description){
        this.description = description;
    }
    
    /**
     * getCompletionDate gets the date when the task should be completed by as a
     * String so it can be edited in a TreeTableView.
     * @return completionDate String
     */
    public String getCompletionDate(){
        return completionDate;
    }
    
    /**
     * setCompletionDate sets the date when the task should be completed by as a
     * String because it is edited in a TreeTableView.
     * @param completionDate 
     */
    public void setCompletionDate(String completionDate){
        this.completionDate = completionDate;
    }
    
    /**
     * addSubTask adds a new subTask to this Tasks list of subTasks.
     * @param subTask 
     */
    public void addSubTask(Task subTask){
        this.subTasks.add(subTask);
    }
    
    /**
     * removeSubTask removes a selected Task from the Tasks list of subTasks.
     * @param subTask 
     */
    public void removeSubTask(Task subTask){
        this.subTasks.remove(subTask);
    }
    
    /**
     * getSubTasks returns this Tasks list of Tasks.
     * @return subTasks list
     */
    public List<Task> getSubTasks(){
        return subTasks;
    }
    
    /**
     * isComplete returns the boolean complete which will be true or false depending
     * on whether it has been checked off in the TreeTableView.
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
        if(!subTasks.isEmpty() && complete == true){
            subTasks.forEach((task) -> {
                task.setComplete(complete);
            });
        }
        this.complete = complete;
    }
    
}
