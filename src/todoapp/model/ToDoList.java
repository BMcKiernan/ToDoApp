package todoapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * A ToDoList has several properties such as a listName, category, creationTime,
 * a deadline which is made up of a LocalDate and LocalTime, and an associated 
 * list of tasks which represent the ToDoList items to be completed.
 * @author Brian McKiernan
 */
public class ToDoList {
    private String listName;
    private String category;
    private List<Task> tasks;
    private SimpleObjectProperty<LocalDateTime> creationTime;
    private SimpleObjectProperty<LocalDate> deadlineDate;
    private SimpleObjectProperty<LocalTime> deadlineTime;
    
    /**
     * The ToDoList constructor initializes a new instance of a ToDolist. The 
     * listName, category, deadlineDate, and deadlineTime are passed to the 
     * ToDoList constructor at creation time while the actual creationTime field
     * and the ArrayList of Tasks are automatically initialized to the current
     * LocalDateTime and an empty ArrayList<Task> respectively.
     * @param listName
     * @param category
     * @param deadlineDate
     * @param deadlineTime 
     */
    public ToDoList(String listName, String category, LocalDate deadlineDate, LocalTime deadlineTime){
        this.listName = listName;
        this.deadlineDate = new SimpleObjectProperty(deadlineDate);
        this.deadlineTime = new SimpleObjectProperty(deadlineTime);
        this.category = category;
        this.creationTime = new SimpleObjectProperty(LocalDateTime.now().withNano(0).withSecond(0));
        this.tasks = new ArrayList<>();
    }
    
    /**
     * setListName provides a way for a ToDoLists name to be changed after it
     * has already been created.
     * @param listName the value which this.listName is to be set to.
     */
    public void setListName(String listName){
        this.listName = listName;
    }
    
    /**
     * getListName returns the ToDoList objects listName
     * @return 
     */
    public String getListName(){
        return listName;
    }
    
    /**
     * setCategory method provides a way for a ToDoLists category to be changed
     * after it has already been created.
     * @param category type to be set
     */
    public void setCategory(String category){
        this.category = category;
    }
    
    /**
     * getCategory returns the ToDoLists category type
     * @return category type
     */
    public String getCategory(){
        return category;
    }
    
    /**
     * setDeadLineDate method provides a way for the LocalDate part of a ToDoLists
     * deadline to be changed after creation time.
     * @param deadlineDate new LocalDate value
     */
    public void setDeadlineDate(LocalDate deadlineDate){
        this.deadlineDate = new SimpleObjectProperty(deadlineDate);
    }
    
    /**
     * getDeadLineDate returns a ToDoLists deadline LocalDate
     * @return deadlineDate 
     */
    public LocalDate getDeadlineDate(){
        return deadlineDate.get();
    }
    
    /**
     * setDeadLineTime method provides a way for the LocalTime part of a ToDoLists
     * deadline to be changed after creation time.
     * @param deadlineDate new LocalTime value
     */
    public void setDeadlineTime(LocalTime deadlineTime){
        this.deadlineTime = new SimpleObjectProperty(deadlineTime);
    }
    
    /**
     * getDeadLineTime returns a ToDoLists deadline LocalTime
     * @return deadlineTime
     */
    public LocalTime getDeadlineTime(){
        return deadlineTime.get();
    }
    
    /**
     * getCreationTime method returns the LocalDateTime at which the the ToDoList
     * was created
     * @return creationTime
     */
    public LocalDateTime getCreationTime(){
        return creationTime.get();
    }
    
    /**
     * getTasks returns the ToDoList objects associate Task List.
     * @return tasks
     */
    public List<Task> getTasks(){
        return this.tasks;
    }
    
    /**
     * addTask adds a new Task object to the ToDoLists associated Task List
     * @param task to be added
     */
    public void addTask(Task task){
        tasks.add(task);
    }
    
    /**
     * removeTask removes a specific Task object from the ToDoLists associated
     * Task list
     * @param task to be removed
     */
    public void removeTask(Task task){
        tasks.remove(task);
    }
}
