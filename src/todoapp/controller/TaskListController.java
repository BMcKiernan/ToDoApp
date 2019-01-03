package todoapp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeTableView.TreeTableViewSelectionModel;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import todoapp.model.AppState;
import todoapp.model.Task;

/**
 * 
 * @author Brian McKiernan
 */

enum err{
    VALIDDATE, NOTPAST, NOTAFTERROOT;
}


public class TaskListController {
    
    private Stage stage;
    private String date;
    private String description;
    private AppState appState;
    private Task rootTask;
    private TreeItem<Task> treeRootItem;
    private TreeItem<Task> selectedTask;
    private final String pattern = "yyyy-MM-dd";
    private DateTimeFormatter dateFormatter;


    
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
    
    /**
     * initialize() method is called during object creation. This method is where
     * the cellFactories, cellValueFactories, listeners, and  all general 
     * state is set up for the TaskList Screen.
     */
    public void initialize() {
        appState = AppState.getInstance();
        dateFormatter = DateTimeFormatter.ofPattern(pattern);
        description = "";
        date = "";
        rootTask = new Task(appState.getSelectedList().getListName(), appState.getSelectedList().getDeadlineDate());
        //Add columns to TreeTableView
        //create rootItem and set rootItem to rootTask
        treeRootItem = new TreeItem<Task>(rootTask);
        treeRootItem.setExpanded(true); 
        listTreeTableView.setRoot(treeRootItem);  
        listTreeTableView.setShowRoot(false);
        listTreeTableView.setEditable(true);
        listLabel.setText("Viewing: " + appState.getSelectedList().getListName());
        populateTasks(); //gets tasks - creates treeItems - adds to rootItem
        
        //CellValueFactories----------------------------------------------------
        descColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        completionDateColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("completionDate"));
        isCompleteColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Task, Boolean> param) -> {
            TreeItem<Task> treeItem = param.getValue();
            Task task = treeItem.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(task.isComplete());
            //singleCol.setOnEditCommit() doesn't work CheckBoxTreeTableCell
            //CheckBox is live, check boolean value if needed
            booleanProp.addListener((obs, oldVal, newVal) -> {
                task.setComplete(newVal);
            });
            return booleanProp;
        });
        //End CVFs--------------------------------------------------------------
  
        //CellFactories---------------------------------------------------------
        isCompleteColumn.setCellFactory((TreeTableColumn<Task,Boolean> p) -> {
            CheckBoxTreeTableCell<Task, Boolean> cell = new CheckBoxTreeTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        descColumn.setCellFactory((TreeTableColumn<Task, String> param)
                -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        descColumn.setOnEditCommit((CellEditEvent<Task, String> t) -> 
                t.getTreeTableView().getTreeItem(t.getTreeTablePosition()
                        .getRow()).getValue().setDescription(t.getNewValue()));
        completionDateColumn.setCellFactory((TreeTableColumn<Task, String> param)
                -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        completionDateColumn.setOnEditCommit((CellEditEvent<Task, String> t) -> {
            String oldValue = t.getOldValue(); String newValue = t.getNewValue();
            if(validateDateCell(t.getNewValue())){
                t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow())
                .getValue().setCompletionDate(newValue);
            }else{
                t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow())
                .getValue().setCompletionDate(oldValue);
            }
        });
        //End CFs---------------------------------------------------------------
        
        //listener detects when a Task has been selected - buttons appear
        listTreeTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                //System.out.println(oldVal +" -> " + newVal);
                if(newVal != null){
                    selectedTask = newVal;
                    if(selectedTask != treeRootItem){
                        listDeleteButton.setVisible(true);
                        //System.out.println("selectedTask: " + selectedTask + " treeRootItem: " + treeRootItem);
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
    
    /**
     * descriptionEntered is an FXML onAction method which gets the entered
     * description of the Task which is to be created.
     * @param event 
     */
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
    
    /**
     * datePicked is an FXML onAction method which gets the selected date
     * the user has chosen from the Jfoenix DatePicker.
     * @param event 
     */
    @FXML
    void datePicked(ActionEvent event) {
        event.consume();
        if(!listDatePicker.getEditor().getText().isEmpty())
            date = listDatePicker.getValue().format(dateFormatter);  
    }

    /**
     * returnPressed() simply closes the TaskList screen and reopens the main
     * ListScreen view.
     * @param event 
     */
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
    
    /**
     * deleteTask() is called when a user clicks on the delete button which 
     * becomes visible when the user has selected a Task they wants to preform
     * operations on. This method then removes the Task from the TreeTableView
     * and from AppState
     * @param event 
     */
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
                Task task = selectedItem.getValue();
                parent.getChildren().remove(selectedItem);
                appState.removeTask(task);
                //System.out.println("Removed: " + selectedItem);
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
    
    /**
     * createTask() gets the required data from the FXML elements and creates
     * a new Task which is added to the TreeTableView and to AppState in 
     * addNewSubTask().
     * @param event 
     */
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
        }else{
            errorLabel.setText("New description is required");
            errorLabel.setVisible(true);
        }
    }
    
    /**
     * addNewSubTask() adds newly created Tasks to the TreeTableView and also
     * adds them to the corresponding level of nested subTasks in AppState.
     * @param newTask to be added to appState, converted into TreeItem and added
     * to TreeTableView
     */
    private void addNewSubTask(Task newTask){
        errorLabel.setVisible(false);
        TreeItem<Task> taskItem = new TreeItem(newTask);
        TreeItem<Task> selectedItem;
        TreeTableViewSelectionModel<Task> taskSM = listTreeTableView.getSelectionModel();
        int row;
        if(taskSM.isEmpty() || treeRootItem.getChildren().isEmpty()){
            selectedItem = treeRootItem;
            //1. Add TopLevel Task to AppState's selectedList's list of Tasks
            appState.addTask(newTask);
        }else{
            row = taskSM.getSelectedIndex();
            selectedItem = taskSM.getModelItem(row);
            //2. Add nested Task to Task already added to AppState
            selectedItem.getValue().addSubTask(newTask);
        }
        selectedItem.getChildren().add(taskItem);
        selectedItem.setExpanded(true);
    }
    
    /**
     * getRecursiveChildren converts every Task passed to it into a 
     * TreeItem. The base case is tasks.getSubTasks().isEmpty() in which 
     * case the sole Task is turned into a TreeItem and returned from the method.
     * Otherwise each Tasks ArrayList of "subTasks" is iterated
     * over and getRecursiveChildren is called on them so that each
     * nested subTask and its subsequent nested subTasks will be added to its 
     * parent TreeItem from the call prior to it.
     * @param task the Task to be turned into a TreeItem with its children added
     * @return TreeItem
     */
    private static TreeItem<Task> getRecursiveChildren(Task task){
        TreeItem<Task> thisItem = new TreeItem<>(task);
        if(!task.getSubTasks().isEmpty()){
            task.getSubTasks().forEach((subTask) -> {
                thisItem.getChildren().add(getRecursiveChildren(subTask));
            });
        }
        return thisItem;
    }
    
    /**
     * populateTasks() adds all of the appState's selectedList's ArrayList of 
     * Tasks to the TreeTableView. For each task in appState's ArrayList
     * the Task is turned into a TreeItem inside of getRecursiveChildren() and
     * is added to the treeRootItem when it returns.
     */
    private void populateTasks(){
        if(!appState.getTasks().isEmpty()){
            appState.getTasks().forEach((task) -> {
                treeRootItem.getChildren().add(getRecursiveChildren(task));
            });
        }
    }
    
    
    boolean validateDateCell(String subString){
        errorLabel.setVisible(false);
        LocalDate parentDate;
        LocalDate present = LocalDate.now();
        parentDate = LocalDate.parse(selectedTask.getParent().getValue().getCompletionDate());
        if(!validDate(subString) || subString.isEmpty()){
            errorLabel.setText("Date must be in proper yyyy-MM-dd format");
            errorLabel.setVisible(true);
            return false;
        }
        if(!parentDate.isAfter(LocalDate.parse(subString)) || (
                !parentDate.isEqual(LocalDate.parse(subString)))){
            errorLabel.setText("Sub task completion date must be before"
                    + " or equal to parent task's completion date");
            errorLabel.setVisible(true);
            return false;
        }
        if(!present.isBefore(LocalDate.parse(subString)) || (
                !present.isEqual(LocalDate.parse(subString)))){
            errorLabel.setText("The completion date cannot be in the past");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }
    
    boolean validDate(String dateStr){   
        if(dateStr.length() != 10){
            return false;
        }else{
            for(int i = 0; i < dateStr.length(); i++){
                if(i != 4 && i != 7){
                    if(!Character.isDigit(dateStr.charAt(i)))
                        return false;
                }
                if(i == 4 || i == 7){
                    if(dateStr.charAt(i) != '-')
                        return false;
                }
            }
        }
        return true;
    }
} 

