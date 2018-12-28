/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.time.LocalDate;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author BriMc
 */
public class Task extends RecursiveTreeObject<Task> {
    private SimpleStringProperty description;
    private SimpleObjectProperty<LocalDate> completionDate;
    private SimpleBooleanProperty complete;
    
    
    public Task(String description, LocalDate completionDate){
        this.description = new SimpleStringProperty(description);
        this.completionDate = new SimpleObjectProperty(completionDate);
        this.complete = new SimpleBooleanProperty(false);
    }
    
    public String getDescription(){
        return description.get();
    }
    
    public void setDescription(String description){
        this.description = new SimpleStringProperty(description);
    }
    
    public LocalDate getCompletionDate(){
        return completionDate.get();
    }
    
    public void setCompletionDate(LocalDate completionDate){
        this.completionDate = new SimpleObjectProperty(completionDate);
    }
    
    public boolean getComplete(){
        return complete.get();
    }
    
    public void setComplete(boolean complete){
        this.complete = new SimpleBooleanProperty(complete);
    }
    
}
