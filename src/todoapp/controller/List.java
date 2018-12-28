/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import todoapp.model.AppState;
import todoapp.model.Task;
import todoapp.model.ToDoList;

/**
 * FXML Controller class
 *
 * @author BriMc
 */
public class List {

@FXML
    private AnchorPane listTopAnchorPane;

    @FXML
    private VBox listVBox;

    @FXML
    private Label listLabel;

    @FXML
    private JFXTreeTableView<Task> listTreeTableView;

    @FXML
    private JFXButton listAddButton;

    @FXML
    private JFXTextField listTextField;

    @FXML
    private JFXDatePicker listDatePicker;

    @FXML
    private JFXDialog listDialogBox;

    @FXML
    private JFXButton listDeleteButton;

    private AppState appState;
    private TreeItem<Task> taskRoot;
    private Task rootTask;
    private ToDoList selectedList;
    
    public void initialize() {
        appState = AppState.getInstance();
        selectedList = appState.getSelectedList();
        listTreeTableView = new JFXTreeTableView<>();
        //Set Columns for TreeTableView
        JFXTreeTableColumn<Task, String> descColumn = new JFXTreeTableColumn<>("Description");
        JFXTreeTableColumn<Task, String> completionDateColumn = new JFXTreeTableColumn<>("Completion Date");
        JFXTreeTableColumn<Task, Boolean> isCompleteColumn = new JFXTreeTableColumn<>("Complete");
        //Add columns to TreeTableView
        listTreeTableView.getColumns().addAll(descColumn, completionDateColumn, isCompleteColumn);

        
        rootTask = new Task(selectedList.getListName(), selectedList.getDeadlineDate()); 
         taskRoot = new TreeItem<Task>();
        
        descColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Task, String> param) -> {
            if(descColumn.validateValue(param)){
                return param.getValue().getValue().description;
            }else{
                return descColumn.getComputedValue(param);
            }
        });
        

        completionDateColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Task, String> param) ->{
            if(completionDateColumn.validateValue(param)) {
                return param.getValue().getValue().completionDate;
            }else{
                return completionDateColumn.getComputedValue(param);
            }
        });
    
        
        
    
        descColumn.setCellFactory((TreeTableColumn<Task, String> param) -> new GenericEditableTreeTableCell<>(
            new TextFieldEditorBuilder()));
        descColumn.setOnEditCommit((CellEditEvent<Task, String> t) -> t.getTreeTableView()
                                                                        .getTreeItem(t.getTreeTablePosition()
                                                                        .getRow())
                                                                        .getValue().description.set(t.getNewValue()));
        completionDateColumn.setCellFactory((TreeTableColumn<Task, String> param) -> new GenericEditableTreeTableCell<>(
            new TextFieldEditorBuilder()));
        completionDateColumn.setOnEditCommit((CellEditEvent<Task, String> t) -> t.getTreeTableView()
                                                                        .getTreeItem(t.getTreeTablePosition()
                                                                        .getRow())
                                                                        .getValue().completionDate.set(t.getNewValue()));

//        completeColumn.setCellFactory((TreeTableColumn<Task, Boolean> param) -> new GenericEditableTreeTableCell<>(
////            new )
////    }    
////    
    
    }
    
}
