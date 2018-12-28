/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author BriMc
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
    
    //Don't need this methods because access modifier must be public for 
    //JFoenix JFXTreeTableColumn cell factories
    public String getDescription(){
        return description.get();
    }
    
    public void setDescription(String description){
        this.description = new SimpleStringProperty(description);
    }
    
    public String getCompletionDate(){
        return completionDate.get();
    }
    
    public void setCompletionDate(LocalDate completionDate){
        this.completionDate = new SimpleStringProperty(completionDate.format(dateFormatter));
    }
    
    public boolean isComplete(){
        return complete;
    }
    
    public void setComplete(boolean complete){
        this.complete = complete;
    }
    
}
