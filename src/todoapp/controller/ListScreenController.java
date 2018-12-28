/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp.controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.application.Application;
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

public class ListScreenController extends Application {

    private FXMLLoader listLoader;
    private Stage stage;
    private AppState appState;
    private ObservableList<ToDoList> toDoLists;
    private ToDoList selectedList;

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

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        initialize();

    }
    
    
    void initialize(){
        appState = AppState.getInstance(); //add read from filesystem or db
        toDoLists = FXCollections.observableArrayList();
        populateLists();
        //set nested deadline columns date and time and add to Deadline col
        listColumnDeadline = new TableColumn<>("Deadline");
        listColumnCategory = new TableColumn<>("Category");
        listColumnListName = new TableColumn<>("List Name");
        listColumnCreated = new TableColumn<>("Created");
        listColumnDeadlineDate = new TableColumn <ToDoList, LocalDate>("Date");
        listColumnDeadlineTime = new TableColumn <ToDoList, LocalTime>("Time");
        listColumnDeadline.getColumns().addAll(listColumnDeadlineDate, listColumnDeadlineTime);
        
        listColumnDeadlineDate.setCellValueFactory(new PropertyValueFactory<>("deadlineDate"));
        listColumnDeadlineTime.setCellValueFactory(new PropertyValueFactory<>("deadlineTime"));
        listColumnListName.setCellValueFactory(new PropertyValueFactory<>("listName"));
        listColumnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        listColumnCreated.setCellValueFactory(new PropertyValueFactory<>("creationTime"));
        listTableView.setItems(toDoLists);
        listTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if(newVal != null){
                    selectedList = newVal;
                    listDeleteButton.setVisible(true);
                    listOpenButton.setVisible(true);
                }else{
                    selectedList = null;
                    listDeleteButton.setVisible(false);
                    listOpenButton.setVisible(false);
                }
            });

       
    }
    
    @FXML
    void createNewList(ActionEvent event) {
        event.consume();
        listLoader = new FXMLLoader();
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
    
    @FXML
    void deleteList(ActionEvent event) {
        event.consume();
        int n = JOptionPane.showConfirmDialog(null,"Permanently delete list?", "Confirmation needed", JOptionPane.YES_NO_OPTION);
        if(n == JOptionPane.YES_OPTION){
            toDoLists.remove(selectedList);
            appState.removeList(selectedList);
        }
    }

    @FXML
    void openList(ActionEvent event) {
        
    }
    
    private void populateLists(){
         if(!appState.getList().isEmpty()){
             ArrayList<ToDoList> list = appState.getList();
                 toDoLists.addAll(list);
             }
    }
}
