package todoapp.controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import todoapp.model.AppState;
import todoapp.model.ToDoList;

public class ListScreenController {

    private Stage stage;
    private ToDoList selectedList;
    private AppState appState;
    private ObservableList<ToDoList> toDoLists;

    @FXML
    private AnchorPane listTopAnchorPane;

    @FXML
    private JFXButton listNewButton;

    @FXML
    private JFXButton listDeleteButton;

    @FXML
    private JFXButton listOpenButton;

    @FXML
    private AnchorPane listInnerAnchorpane;

    @FXML
    private TableView<ToDoList> listTableView;
       
    @FXML
    private TableColumn<ToDoList, String> listColumnListName;

    @FXML
    private TableColumn<ToDoList, String> listColumnCategory;

    @FXML
    private TableColumn<ToDoList, LocalDateTime> listColumnCreated;

    @FXML
    private TableColumn<ToDoList, LocalDateTime> listColumnDeadline;

    private TableColumn<ToDoList, LocalDate> listColumnDeadlineDate;
    private TableColumn<ToDoList, LocalTime> listColumnDeadlineTime;

    
    public void start(Stage stage) {
        this.stage = stage;
        //no class constructor so init doesn't automatically get called
        initialize(); 
    }
    
    /**
     * initialize() sets up the TreeTableView and includes cellValueFactories,
     * listeners, and other necessary state setup for the application screen.
     */
    void initialize(){
        appState = AppState.getInstance(); //add read from filesystem or db
        toDoLists = FXCollections.observableArrayList();
        System.out.println("lstscreen initialize called\n");
        populateLists();
        //set nested deadline columns date and time and add to Deadline col
        listColumnDeadlineDate = new TableColumn <ToDoList, LocalDate>("Date");
        listColumnDeadlineTime = new TableColumn <ToDoList, LocalTime>("Time");
        listColumnDeadline.getColumns().addAll(listColumnDeadlineDate, listColumnDeadlineTime);
        
        listColumnDeadlineDate.setCellValueFactory(new PropertyValueFactory<>("deadlineDate"));
        listColumnDeadlineTime.setCellValueFactory(new PropertyValueFactory<>("deadlineTime"));
        listColumnListName.setCellValueFactory(new PropertyValueFactory<>("listName"));
        listColumnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        listColumnCreated.setCellValueFactory(new PropertyValueFactory<>("creationTime"));
        listTableView.setItems(toDoLists);
        //Make buttons visible when a list has been selected.
        listTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if(newVal != null){
                    selectedList = newVal;
                    appState.setSelectedList(selectedList);
                    listDeleteButton.setVisible(true);
                    listOpenButton.setVisible(true);
                }else{
                    selectedList = null;
                    appState.setSelectedList(null);
                    listDeleteButton.setVisible(false);
                    listOpenButton.setVisible(false);
                }
            });
    }
    
    /**
     * createNewList() is an FXML onAction method which is associated with the
     * NewList button and becomes visible when the user selects a list. 
     * createNewList() opens a screen dedicated to creating ToDoLists.
     * @param event 
     */
    @FXML
    void createNewList(ActionEvent event) {
        event.consume();
        FXMLLoader listLoader = new FXMLLoader();
        listLoader.setLocation(getClass().getResource("/todoapp/view/CreateList.fxml"));
        try {
            AnchorPane root = (AnchorPane) listLoader.load();
            CreateListController createListScreen = listLoader.getController();
            stage.close();
            createListScreen.start(stage);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * deleteList() is an FXML onAction method associated with the delete button
     * which becomes visible when the user selects a list. deleteList() shows 
     * a confirmation dialog before the user can delete the list and if the 
     * user clicks yes the list is deleted from the observableArrayList used 
     * for the TableView and from AppState.
     * @param event 
     */
    @FXML
    void deleteList(ActionEvent event) {
        event.consume();
        int n = JOptionPane.showConfirmDialog(null,"Permanently delete list?",
                "Confirmation needed", JOptionPane.YES_NO_OPTION);
        if(n == JOptionPane.YES_OPTION){
            //calling toDoLists.remove first before appState is a bug
            //because listener - selectedList change
            appState.removeList(selectedList);
            toDoLists.remove(selectedList);
            //System.out.println("AppState size after removal: " +appState.getLists().size());
        }
    }

    /**
     * openList() is an FXML onAction method associated with the Open button 
     * which becomes visible when the user selects a list. openList "Opens" the 
     * selected toDoList by creating the TaskList screen which is populated 
     * with the selectedLists Tasks.
     * @param event 
     */
    @FXML
    void openList(ActionEvent event) {
        event.consume();
        FXMLLoader taskLoader = new FXMLLoader();
        taskLoader.setLocation(getClass().getResource("/todoapp/view/TaskList.fxml"));
        try{
            AnchorPane root = (AnchorPane) taskLoader.load();
            TaskListController taskListScreen = taskLoader.getController();
            stage.close();
            taskListScreen.start(stage);
            stage.setScene(new Scene(root));
            stage.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * populateLists() adds all of AppStates ToDolists to the ListScreens 
     * toDoLists observableArrayList which stores the data for the TableView.
     */
    private void populateLists(){
        //System.out.println("populateLists called: appState list size  \n" + appState.getLists().size());
        if(!appState.getLists().isEmpty()){
            toDoLists.addAll(appState.getLists());
        }
    }
}
