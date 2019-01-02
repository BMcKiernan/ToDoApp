package todoapp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeTableView.TreeTableViewSelectionModel;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import todoapp.model.AppState;
import todoapp.model.Task;

/**
 * 
 * @author Brian McKiernan
 */
public class TaskListController {

    private AppState appState;
    private TreeItem<Task> treeRootItem;
    private Task rootTask;
    private TreeItem<Task> selectedTask;
    private Stage stage;
    private String date;
    private final String pattern = "yyyy-MM-dd";
    private DateTimeFormatter dateFormatter;
    private String description;

    
    @FXML
    private AnchorPane listTopAnchorPane;

    @FXML
    private VBox listVBox;

    @FXML
    private Label listLabel;

    @FXML
    private TreeTableView<Task> listTreeTableView;

    @FXML
    private JFXButton listAddButton;

    @FXML
    private JFXTextField listTextField;

    @FXML
    private JFXDatePicker listDatePicker;

    @FXML
    private JFXButton listDeleteButton;

    @FXML
    private JFXButton listReturnButton;

    @FXML
    private Label errorLabel;
    
    
    @FXML
    private TreeTableColumn<Task, Boolean> isCompleteColumn;

    @FXML
    private TreeTableColumn<Task, String> descColumn;

    @FXML
    private TreeTableColumn<Task, String> completionDateColumn;
    
    public void start(Stage stage) {
        listDeleteButton.setVisible(false);
        this.stage = stage;
    }
    
    
    public void initialize() {
        appState = AppState.getInstance();
        dateFormatter = DateTimeFormatter.ofPattern(pattern);
        description = "";
        date = "";
//        isCompleteColumn = new TreeTableColumn<Task,Boolean>();
//        descColumn = new TreeTableColumn<Task,String>();
//        completionDateColumn = new TreeTableColumn<Task,String>();
        rootTask = new Task(appState.getSelectedList().getListName(), appState.getSelectedList().getDeadlineDate());
        //Add columns to TreeTableView
        //listTreeTableView.getColumns().addAll(descColumn, completionDateColumn, isCompleteColumn);
        //create rootItem and set rootItem to rootTask
        treeRootItem = new TreeItem<Task>(rootTask);
        treeRootItem.setExpanded(true); 
        listTreeTableView.setRoot(treeRootItem);  
        listTreeTableView.setShowRoot(false);
        listTreeTableView.setEditable(true);
        listLabel.setText(appState.getSelectedList().getListName());
        populateTasks(); //gets tasks - creates treeItems - adds to rootItem
        isCompleteColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, Boolean>,
            ObservableValue<Boolean>>(){

            @Override
            public ObservableValue<Boolean> call(TreeTableColumn.CellDataFeatures<Task, Boolean> param){
                TreeItem<Task> treeItem = param.getValue();
                Task task = treeItem.getValue();
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(task.isComplete());

                //singleCol.setOnEditCommit() doesn't work CheckBoxTreeTableCell
                //if isComplete triggered column change
                booleanProp.addListener(new ChangeListener<Boolean>(){

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue){
                                task.setComplete(newValue);
                    }
                });
                return booleanProp;
            }
        });
        
          descColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Task, String>("description"));
          completionDateColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Task, String>("completionDate"));

        
        isCompleteColumn.setCellFactory(new Callback<TreeTableColumn<Task, Boolean>, TreeTableCell<Task, Boolean>>(){
            @Override
            public TreeTableCell<Task, Boolean> call(TreeTableColumn<Task,Boolean> p){
                CheckBoxTreeTableCell<Task, Boolean> cell = new CheckBoxTreeTableCell<Task, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
    
        descColumn.setCellFactory((TreeTableColumn<Task, String> param) -> new GenericEditableTreeTableCell<>(
            new TextFieldEditorBuilder()));
        descColumn.setOnEditCommit((CellEditEvent<Task, String> t) -> t.getTreeTableView()
                                                                        .getTreeItem(t.getTreeTablePosition()
                                                                        .getRow())
                                                                        .getValue().setDescription(t.getNewValue()));
        completionDateColumn.setCellFactory((TreeTableColumn<Task, String> param) -> new GenericEditableTreeTableCell<>(
            new TextFieldEditorBuilder()));
        completionDateColumn.setOnEditCommit((CellEditEvent<Task, String> t) -> t.getTreeTableView()
                                                                        .getTreeItem(t.getTreeTablePosition()
                                                                        .getRow())
                                                                        .getValue().setCompletionDate(t.getNewValue()));
        
        
        
        listTreeTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                System.out.println(oldVal +" -> " + newVal);
                if(newVal != null){
                    selectedTask = newVal;
                    if(selectedTask != treeRootItem){
                        listDeleteButton.setVisible(true);
                        System.out.println("selectedTask: " + selectedTask + " treeRootItem: " + treeRootItem);
                    }
                }else{
                    selectedTask = null;
                    listDeleteButton.setVisible(false);
                }
            });
        
        
        listTextField.textProperty().addListener((observ, oldVal, newVal) -> {
            errorLabel.setVisible(false);
            description = newVal;
        });
    }
    
    @FXML
    void createTask(ActionEvent event) {
        event.consume();
        errorLabel.setVisible(false);
        if(!listTextField.getText().isEmpty()){
            description = listTextField.getText();
            Task newTask;
            LocalDate dueDate;
            if(listDatePicker.getEditor().getText().isEmpty()){
                dueDate = LocalDate.now().plusMonths(6);
            }
            else{
                dueDate = LocalDate.parse(date);
            }
            newTask = new Task(description, dueDate);
            //appState.addTask(newTask);
            addNewSubTask(newTask);//add child item
            listTextField.clear();
            listDatePicker.getEditor().clear();
        }else{
            errorLabel.setText("New description is required");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    void descriptionEntered(ActionEvent event) {
        event.consume();
        errorLabel.setVisible(false);
        if(!listTextField.getText().isEmpty()){
            description = listTextField.getText();
        }else{
            errorLabel.setText("New task description is required");
            errorLabel.setVisible(true);
        }
    }
    
    @FXML
    void datePicked(ActionEvent event) {
        event.consume();
        if(!listDatePicker.getEditor().getText().isEmpty())
            date = listDatePicker.getValue().format(dateFormatter);  
    }

    @FXML
    void returnPressed(ActionEvent event) {
        event.consume();
        FXMLLoader listLoader = new FXMLLoader();
        listLoader.setLocation(getClass().getResource("/todoapp/view/ListScreen.fxml"));
        try {
            AnchorPane root = (AnchorPane) listLoader.load();
            ListScreenController listScreenController = listLoader.getController();
            stage.close();
            listScreenController.start(stage);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    void deleteTask(ActionEvent event) {
        event.consume();
        errorLabel.setVisible(false);
        TreeTableViewSelectionModel<Task> taskSM = listTreeTableView.getSelectionModel();
        
        if(!taskSM.isEmpty()){
            int row = taskSM.getSelectedIndex();
            TreeItem<Task> selectedItem = taskSM.getModelItem(row);
            TreeItem<Task> parent = selectedItem.getParent();
            if(parent != null){
                parent.getChildren().remove(selectedItem);
                appState.removeTask(selectedItem.getValue());
                System.out.println("Removed: " + selectedItem);
            }
            else{
                errorLabel.setText("Cannot remove root task");
                errorLabel.setVisible(true);
            }
            
            if(treeRootItem.getChildren().isEmpty()){
                listDeleteButton.setVisible(false);
            }
        }

    }

    private void addNewSubTask(Task newTask){
        errorLabel.setVisible(false);
        TreeItem<Task> taskItem = new TreeItem(newTask);
        TreeItem<Task> selectedItem;
        TreeTableViewSelectionModel<Task> taskSM = listTreeTableView.getSelectionModel();
        int row;
        if(taskSM.isEmpty() || treeRootItem.getChildren().isEmpty()){
            selectedItem = treeRootItem;
            appState.addTask(newTask);
        }else{
            row = taskSM.getSelectedIndex();
            selectedItem = taskSM.getModelItem(row);
            selectedItem.getValue().addSubTask(newTask);
        }
        selectedItem.getChildren().add(taskItem);
        selectedItem.setExpanded(true);
    }
    
    
    private static TreeItem<Task> getRecursiveChildren(Task task){
        TreeItem<Task> thisItem = new TreeItem<>(task);
        if(!task.getSubTasks().isEmpty()){
            task.getSubTasks().forEach((subTask) -> {
                thisItem.getChildren().add(getRecursiveChildren(subTask));
            });
        }
        return thisItem;
    }
    
    private void populateTasks(){
        if(!appState.getTasks().isEmpty()){
            appState.getTasks().forEach((task) -> {
                treeRootItem.getChildren().add(getRecursiveChildren(task));
            });
        }
    }
}
