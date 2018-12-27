/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author BriMc
 */
public class ToDoList {
    private SimpleStringProperty title;
    private SimpleStringProperty category;
    private ArrayList<String> tasks;
    private SimpleObjectProperty<LocalDateTime> deadline;
    private SimpleObjectProperty<LocalDateTime> creationTime;

    
    public ToDoList(String title,  String category, LocalDateTime deadline){
        this.title = new SimpleStringProperty(title);
        this.deadline = new SimpleObjectProperty(deadline);
        this.category = new SimpleStringProperty(category);
        this.creationTime = new SimpleObjectProperty(LocalDateTime.now().withNano(0).withSecond(0));
    }
    
    public void setTitle(String title){
        this.title = new SimpleStringProperty(title);
    }
    
    public String getTitle(){
        return title.get();
    }
    
    public void setCategory(String category){
        this.category = new SimpleStringProperty(category);
    }
    
    public String getCategory(){
        return category.get();
    }
    
    public void setDeadline(LocalDateTime deadline){
        this.deadline = new SimpleObjectProperty(deadline);
    }
    
    public LocalDateTime getDeadline(){
        return deadline.get();
    }
    
    public LocalDateTime getCreationTime(){
        return creationTime.get();
    }
    
    public void setTasks(ArrayList<String> tasks){
        this.tasks = tasks;
    }
    
    public ArrayList<String> getTasks(){
        return this.tasks;
    }
}
