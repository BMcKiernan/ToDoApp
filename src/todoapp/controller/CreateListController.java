/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import todoapp.model.AppState;
import todoapp.model.Task;
import todoapp.model.ToDoList;

public class CreateListController extends Application {

    private String title;
    private Stage stage;
    private String category;
    private ArrayList<Task> tasks;
    private FXMLLoader listLoader;
    private AppState appState;
    private String date;
    private String time;

    protected ToDoList toDoList;

    @FXML
    private AnchorPane createTopAnchorPane;

    @FXML
    private AnchorPane createNewInnerAnchorPane;

    @FXML
    private JFXTextField createNewListTitle;

    @FXML
    private JFXButton createNewListButton;

    @FXML
    private JFXComboBox<String> createNewListCategory;

    @FXML
    private JFXDatePicker createNewListDate;

    @FXML
    private JFXTimePicker createNewListTime;

    @FXML
    private Label createNewListError;

    private static final String datePattern = "yyyy-MM-dd";
    private static DateTimeFormatter dateFormatter;
    private static final String timePattern = "hh:mm";
    private static DateTimeFormatter timeFormatter;
    private LocalDate datePart;
    private LocalTime timePart;
    
    public CreateListController() {
        dateFormatter = DateTimeFormatter.ofPattern(datePattern);
        timeFormatter = DateTimeFormatter.ofPattern(timePattern);
        title = "";
        date = "";
        time = "";
        category = "";
        createNewListCategory = new JFXComboBox<String>();
    }

    @Override
    public void start(Stage stage) {
        createNewListTitle.textProperty().addListener((observ, oldVal, newVal) -> {
            title = newVal;
        });
        createNewListCategory.setItems(FXCollections.observableArrayList(
                new String("Work"),
                new String("School"),
                new String("Shopping"),
                new String("General")));
        this.stage = stage;
        appState = AppState.getInstance(); //maintain lists 
    }

    @FXML
    void createList(ActionEvent event) {
        event.consume();
        createNewListError.setVisible(false);
        if (title.isEmpty()) {
            createNewListError.setText("New list name is required.");
            createNewListError.setVisible(true);
        }
        if (!title.isEmpty()) {
            ToDoList newList;
            if(!date.isEmpty() && !time.isEmpty()){
                datePart = LocalDate.parse(date);
                timePart = LocalTime.parse(time);
            }else if(!date.isEmpty() && time.isEmpty()){
                datePart = LocalDate.parse(date);
                timePart = LocalTime.MIDNIGHT;
            }else if(date.isEmpty() && !time.isEmpty()){
                datePart = LocalDate.now().plusMonths(6);
                timePart = LocalTime.parse(time);
            }else{
                datePart = LocalDate.now().plusMonths(6);
                timePart = LocalTime.MIDNIGHT;
            }
            
            if(category.isEmpty())
                category = "General";
            newList = new ToDoList(title, category, datePart, timePart);
            newList.setTasks(new ArrayList<Task>());
            appState.addList(newList);
            
            listLoader = new FXMLLoader();
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
    }

    //@FXML
    //void initialize();
    @FXML //createNewListCategory
    void categoryEntered(ActionEvent event) {
        event.consume();
        category = createNewListCategory.getValue();
    }

    @FXML
    void deadlineEntered(ActionEvent event) {
        event.consume();
        if (!createNewListDate.getValue().toString().isEmpty()) {
            date = createNewListDate.getValue().format(dateFormatter);
        }
    }

    @FXML
    void timeEntered(ActionEvent event) {
        event.consume();
        if (!createNewListTime.getValue().toString().isEmpty()) {
            time = createNewListTime.getValue().format(timeFormatter);
        }
    }

    @FXML //JFoenix apparently only counts ENTER as actionevent
    void titleEntered(ActionEvent event) {
        event.consume();
        createNewListError.setVisible(false);
        if (!createNewListTitle.getText().isEmpty()) {
            title = createNewListTitle.getText();
        } else {
            createNewListError.setText("New list name is required.");
            createNewListError.setVisible(true);
        }

    }
    


}
