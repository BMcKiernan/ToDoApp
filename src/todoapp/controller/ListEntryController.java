package todoapp.controller;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ListEntryController extends HBox {

    private FXMLLoader entryLoader;
    private boolean selected;
    
    @FXML
    private Label entryTitle;

    @FXML
    private Label entryDescription;

    @FXML
    private CheckBox entryCheckBox;

    public ListEntryController() {
        selected = false;
        entryLoader = new FXMLLoader();
        entryLoader.setLocation(getClass().getResource("/todoapp/view/ListEntry.fxml"));
        entryLoader.setRoot(this);
        entryLoader.setController(this);
        try{
            entryLoader.load();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        entryCheckBox.setOnAction(event -> {
            event.consume();
            if(entryCheckBox.isSelected())
                selected = true;
            if(!entryCheckBox.isSelected())
                selected = false;
        });
    }
    
    public void setTitle(String title){
       
        entryTitle.setText(title);
    }
    
    public String getTitle(){
        return entryTitle.getText();
    }
    
    public void setDescription(String description){
        entryDescription.setText(description);
    }
    
    public String getDescription(){
        return entryDescription.getText();
    }
    
    public boolean isSelected(){
        return selected;
    }
}