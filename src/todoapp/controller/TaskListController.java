package todoapp.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.util.StringConverter;
import todoapp.model.AppState;
import todoapp.model.Task;

/**
 * TaskListController controls and renders the main screen for a ToDoList.
 * ToDoLists are made up of Tasks and the TaskListController provides the 
 * interactive editing features which allows users to create and edit Tasks which
 * are part of their selected ToDoList.
 * @author Brian McKiernan
 */

public class TaskListController {
    
    private Stage stage;
    private String date;
    private String description;
    private AppState appState;
    private Task rootTask;
    private TreeItem<Task> treeRootItem;
    private TreeItem<Task> selectedItem;
    private final String pattern = "yyyy-MM-dd";
    private DateTimeFormatter dateFormatter;
    private TreeTableViewSelectionModel<Task> taskSM;
    private int flag;

    
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
        listDatePicker.setConverter(new StringConverter<LocalDate>() {
        	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        	@Override
        	public String toString(LocalDate localDate) {
        		if(localDate == null)
        			return "";
        		return dateTimeFormatter.format(localDate);
        	}
        	
        	@Override
        	public LocalDate fromString(String dateString) {
        		if(dateString == null || dateString.trim().isEmpty())
        			return null;
        		return LocalDate.parse(dateString,  dateTimeFormatter);
        	}
        });
    }
    
    /**
     * initialize() method is called during object creation. This method is where
     * the cellFactories, cellValueFactories, listeners, and  all general 
     * state is set up for the TaskList Screen.
     */
    public void initialize() {
        taskSM = listTreeTableView.getSelectionModel();
        appState = AppState.getInstance();
          System.out.println("appState " + appState.getTasks());
        dateFormatter = DateTimeFormatter.ofPattern(pattern);
        description = "";
        date = "";
        rootTask = new Task("rootTask", appState.getSelectedList().getDeadlineDate());
        selectedItem = treeRootItem;
        flag = 0;
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
                listTreeTableView.refresh();
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
        descColumn.setCellFactory((TreeTableColumn<Task, String> param)-> 
                new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        descColumn.setOnEditCommit((CellEditEvent<Task, String> t) -> 
                t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue().setDescription(t.getNewValue()));
        
        completionDateColumn.setCellFactory((TreeTableColumn<Task, String> param) -> 
              new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        completionDateColumn.setOnEditCommit(new EventHandler<CellEditEvent<Task, String>>() {
            @Override
            public void handle(CellEditEvent<Task, String> t) {
                if(validateDateCell(t.getNewValue(), 2)){
                    t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow())
                            .getValue().setCompletionDate(t.getNewValue());
                }else
                    listTreeTableView.refresh();
            }
        });
        //End CFs---------------------------------------------------------------
        
        //listener detects when a Task has been selected - buttons appear
        taskSM.selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                flag = 99;
                System.out.println(oldVal +" -> " + newVal);
                if(newVal != null){
                    System.out.println("Task name: " + newVal.getValue().getDescription() + " " + taskSM.getSelectedIndex());
                    if(!treeRootItem.getChildren().isEmpty()){
                        List<TreeItem<Task>> rootChildren = treeRootItem.getChildren();
                        for(TreeItem<Task> rootChild : rootChildren){
                            if(newVal.equals(rootChild)){
                                flag = 1;
                                break;
                            }
                        }
                    }
                    if(flag == 99){
                        if(newVal.equals(treeRootItem)){
                            flag = 0;
                        }else{
                            flag = 2;
                        }
                    }
                    selectedItem = newVal;
                }else{
                    flag = 0;
                    selectedItem = treeRootItem;
                }
                if(flag == 0)
                    listDeleteButton.setVisible(false);
                else if(flag == 99)
                    System.out.println("\nWhat the hell\n");
                else
                    listDeleteButton.setVisible(true);
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
        if(!listDatePicker.getEditor().getText().isEmpty()){
            date = listDatePicker.getValue().format(dateFormatter);
        }
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
            stage.setScene(new Scene(root, 950, 600));
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
        taskSM = listTreeTableView.getSelectionModel();
        if(!taskSM.isEmpty()){
            int row = taskSM.getSelectedIndex();
            TreeItem<Task> selectedItem = taskSM.getModelItem(row);
            TreeItem<Task> foundItem = null; //item to be searched for in root
            List<TreeItem<Task>> rootChilds = treeRootItem.getChildren();

            for(TreeItem<Task> childItem: rootChilds){
                if(selectedItem.equals(childItem)){
                    foundItem = childItem;
                }
            }
            //if its not in rootItems children its a nested subTask and 
            //its safe to call getParent()
            if(foundItem == null){
                //get the parent task and remove the selectedItems task
                selectedItem.getParent().getValue().removeSubTask(selectedItem.getValue());
                //remove the actual selectedItem from the treetableview
                selectedItem.getParent().getChildren().remove(selectedItem);
            }else{ //else remove directly from treeRootItem
                TreeItem<Task> savedItem = foundItem;
                appState.removeTask(foundItem.getValue());
                treeRootItem.getChildren().remove(savedItem);
            } 
            
            if(treeRootItem.getChildren().isEmpty()){
                listDeleteButton.setVisible(false);
            }
            System.out.println("\ndeleteTask \n" + treeRootItem.getChildren());
            System.out.println("appState " + appState.getTasks());
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
        if(!description.isEmpty()){
            if(!date.isEmpty()){
                //TreeItem<Task> taskItem = new TreeItem(newTask);
                taskSM = listTreeTableView.getSelectionModel();
                if(validateDateCell(date, 1)){
                    addNewSubTask(new Task(description, LocalDate.parse(date)));
                    listTextField.clear();
                }
            }else{
                errorLabel.setText("New date is required");
                errorLabel.setVisible(true);
            }
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
        TreeItem<Task> taskItem = new TreeItem<>(newTask);
        TreeItem<Task> selectedItem;
        taskSM = listTreeTableView.getSelectionModel();
        selectedItem = taskSM.getModelItem(taskSM.getSelectedIndex());
        if(taskSM.isEmpty() || treeRootItem.getChildren().isEmpty() || selectedItem == null){
            selectedItem = treeRootItem;
            //1. Add TopLevel Task to AppState's selectedList's list of Tasks
            appState.addTask(newTask);
        }else{
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
    
    /**
     * validateDateCell is used to verfiy that the subString set in the cellFactory
     * for the completion date column is in the proper yyyy-MM-dd format,
     * that the subString date of nested subtasks is not set to a date later 
     * then their parent Task, and that the date does not occur in the past.
     * @param subString String representation of a LocalDate
     * @return boolean
     */
    private boolean validateDateCell(String subString, int callingLocation){
        /*
         *flag == 0 is rootItem, flag == 1 is rootItemChild, flag == 2 is nested item
         *callingLocatgion == 1 is createTask, callingLocation == 2 is setCellFactory completionDateColumn
         */
    	errorLabel.setVisible(false);
        LocalDate parentDate = null;
        LocalDate present = LocalDate.now();
        taskSM = listTreeTableView.getSelectionModel();
        if(subString.isEmpty() || !validDate(subString)){
            errorLabel.setText("Date must be in proper yyyy-MM-dd format");
            errorLabel.setVisible(true);
            return false;
        }
        /* (callingLocation == 1 && flag == 1) <--> parse selectedItem rootChildItem which is parent of new subTask being created
         * (callingLocation == 1 && flag == 2) <--> parse selectedItem nestedItem which is parent of new subTask being created
         * (callingLocation == 2  && flag == 2) <--> parse selectedItem.getParent() which is parent of treeItem being edited in cFactory
         */
        if((callingLocation == 1 && flag == 1) || flag == 2){
        	if((flag == 2 && callingLocation == 1) || (flag == 1 && callingLocation == 1)) {
        		parentDate = LocalDate.parse(selectedItem.getValue().getCompletionDate());
        	}
        	if(flag == 2 && callingLocation == 2) {
        		parentDate = LocalDate.parse(selectedItem.getParent().getValue().getCompletionDate());
        	}
            if(LocalDate.parse(subString).isAfter(parentDate)){
                errorLabel.setText("Sub task completion date must not occur"
                        + " after it's selected containing task's completion date");
                errorLabel.setVisible(true);
                return false;
            }
        }
        /* (callingLocation == 2 && flag == 1) <--> parse rootItem which is parent of treeItem being edited in cFactory
         * (flag = 0) <--> parse rootItem which is parent of new Task being created
         * !! only callingLocatgion == 0 will get flag == 0 in taskSM.selectedItemProperty().addListener
         * because user can never select rootItem, its only selected by default when no Task in TreeTableView is selected
         * and -> a new Task is being added to the rootItem of the TreeTableView.!!
         */
        if((callingLocation == 2 && flag == 1) || flag == 0){
            parentDate = LocalDate.parse(rootTask.getCompletionDate());
            if(LocalDate.parse(subString).isAfter(parentDate)){
                errorLabel.setText("This Task's completion date cannot occur after \""
                        + appState.getSelectedList().getListName() +"s\" deadline");
                errorLabel.setVisible(true);
                return false;
            }
        }
        if(LocalDate.parse(subString).isBefore(present)){ 
            errorLabel.setText("Completion date cannot be in the past");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }
    
    /**
     * validDate verifies that the String dateStr parameter is in the proper
     * yyyy-MM-dd format. This method is called by validDateCell.
     * @param dateStr string in date format
     * @return boolean
     */
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
