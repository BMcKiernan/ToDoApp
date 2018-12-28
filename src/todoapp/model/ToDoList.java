/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author BriMc
 */
public class ToDoList {
    private SimpleStringProperty listName;
    private SimpleStringProperty category;
    private ArrayList<Task> tasks;
    private SimpleObjectProperty<LocalDateTime> creationTime;
    private SimpleObjectProperty<LocalDate> deadlineDate;
    private SimpleObjectProperty<LocalTime> deadlineTime;
    
    public ToDoList(String listName,  String category, LocalDate deadlineDate, LocalTime deadlineTime){
        this.listName = new SimpleStringProperty(listName);
        this.deadlineDate = new SimpleObjectProperty(deadlineDate);
        this.deadlineTime = new SimpleObjectProperty(deadlineTime);
        this.category = new SimpleStringProperty(category);
        this.creationTime = new SimpleObjectProperty(LocalDateTime.now().withNano(0).withSecond(0));
    }
    
    public void setListName(String listName){
        this.listName = new SimpleStringProperty(listName);
    }
    
    public String getListName(){
        return listName.get();
    }
    
    public void setCategory(String category){
        this.category = new SimpleStringProperty(category);
    }
    
    public String getCategory(){
        return category.get();
    }
    
    public void setDeadlineDate(LocalDate deadlineDate){
        this.deadlineDate = new SimpleObjectProperty(deadlineDate);
    }
    
    public LocalDate getDeadlineDate(){
        return deadlineDate.get();
    }
    
       public void setDeadlineTime(LocalTime deadlineTime){
        this.deadlineTime = new SimpleObjectProperty(deadlineTime);
    }
    
    public LocalTime getDeadlineTime(){
        return deadlineTime.get();
    }
    
    public LocalDateTime getCreationTime(){
        return creationTime.get();
    }
    
    public void setTasks(ArrayList<Task> tasks){
        this.tasks = tasks;
    }
    
    public ArrayList<Task> getTasks(){
        return this.tasks;
    }
}
