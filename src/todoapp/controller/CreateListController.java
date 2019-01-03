package todoapp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import todoapp.model.AppState;
import todoapp.model.ToDoList;

public class CreateListController {

    private String category;
    private String title;
    private String date;
    private String time;
    private Stage stage;
    private AppState appState;


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
    
    @FXML
    private JFXButton cancelButton;

    private static final String datePattern = "yyyy-MM-dd";
    private static DateTimeFormatter dateFormatter;
    private static final String timePattern = "hh:mm";
    private static DateTimeFormatter timeFormatter;
    private LocalDate datePart;
    private LocalTime timePart;
    
    /**
     * CreateListController() constructor initializes the CreaListController. 
     */
    public CreateListController() {
        dateFormatter = DateTimeFormatter.ofPattern(datePattern);
        timeFormatter = DateTimeFormatter.ofPattern(timePattern);
        title = "";
        date = "";
        time = "";
        category = "";
        createNewListCategory = new JFXComboBox<String>();
    }

    public void start(Stage stage) {
        this.stage = stage;
        createNewListTitle.textProperty().addListener((observ, oldVal, newVal) -> {
            title = newVal;
        });
        createNewListCategory.setItems(FXCollections.observableArrayList(
                "Work",
                "School",
                "Shopping",
                "General"
        ));
        appState = AppState.getInstance(); //maintain lists
        if(appState.editPressed()){
            createNewListTitle.setText(appState.getSelectedList().getListName());
            createNewListDate.setValue(appState.getSelectedList().getDeadlineDate());
            createNewListTime.setValue(appState.getSelectedList().getDeadlineTime());
            createNewListCategory.setValue(appState.getSelectedList().getCategory());
            createNewListButton.setText("Update");
        }
    }

   
    /**
     * createList() is an FXML onAction method associated with the create button.
     * createList() gets the data stored in the DatePicker, TimePicker, ComboBox,
     * and TextBox FXML elements.
     * @param event 
     */
    @FXML
    void createList(ActionEvent event) {
        event.consume();
        if(appState.editPressed())
            updateTaskFields();
        createNewListError.setVisible(false);
        if (title.isEmpty()) {
            createNewListError.setText("New list name is required.");
            createNewListError.setVisible(true);
        }
        if (!title.isEmpty()) {
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
            
            if(appState.editPressed()){
                appState.getSelectedList().setCategory(category);
                appState.getSelectedList().setDeadlineDate(datePart);
                appState.getSelectedList().setDeadlineTime(timePart);
                appState.getSelectedList().setListName(title);
                appState.setEditPressed(false);
            }else
                appState.addList(new ToDoList(title, category, datePart, timePart));
            
            FXMLLoader listLoader = new FXMLLoader();
            listLoader.setLocation(getClass().getResource("/todoapp/view/ListScreen.fxml"));
            try {
                AnchorPane root = (AnchorPane) listLoader.load();
                ListScreenController listScreenController = listLoader.getController();
                stage.close();
                listScreenController.start(stage);
                stage.setScene(new Scene(root));
                stage.setResizable(true);
                stage.show();

            } catch (IOException ex) {
                //ex.printStackTrace();
            }
        }
    }

    /**
     * updateTaskFields manually calls the onAction methods to get the values
     * that makeup a Task. This is necessary for repurposing the CreateListController
     * to also act as an EditListController with minimal changes.
     */
    void updateTaskFields(){
        categoryEntered(new ActionEvent());
        deadlineEntered(new ActionEvent());
        timeEntered(new ActionEvent());
        titleEntered(new ActionEvent());
    }
    
    /**
     * categoryEntered() is an FXML onAction method associated with the 
     * ComboBox FXML element in the CreateList view. It gets the category picked
     * for new list creation.
     * @param event 
     */
    @FXML //createNewListCategory
    void categoryEntered(ActionEvent event) {
        event.consume();
        category = createNewListCategory.getValue();
    }

    /**
     * deadlineEntered() is an FXML onAction method associated with the DatePicker
     * FXML element. deadlineEntered() gets the date date value from the picker.
     * @param event 
     */
    @FXML
    void deadlineEntered(ActionEvent event) {
        event.consume();
        if (!createNewListDate.getValue().toString().isEmpty()) {
            date = createNewListDate.getValue().format(dateFormatter);
        }
    }

    /**
     * timeEntered() is an FXML onAction method associated with the FXML
     * TimePicker. timeEntered() gets the time value from the picker.
     * @param event 
     */
    @FXML
    void timeEntered(ActionEvent event) {
        event.consume();
        if (!createNewListTime.getValue().toString().isEmpty()) {
            time = createNewListTime.getValue().format(timeFormatter);
        }
    }
    
    /**
     * titleEntered() is an FMXL onAction method associated with the TextBox
     * FXML element. This method gets the title entered from the the TextBox 
     * element. This method only works if enter is pressed after a title has 
     * been entered into the TextBox so a listener in the start method also 
     * detects the title TextBox input changes.
     * @param event 
     */
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
    
    /**
     * cancelPushed() is an FXML onAction method associated with the cancel
     * button in the CreateList screen. This method allows the user to return to
     * the ListScreen in the event that they no longer wish to create a new list.
     * @param event 
     */
    @FXML
    void cancelPushed(ActionEvent event) {
        FXMLLoader listLoader = new FXMLLoader();
        listLoader.setLocation(getClass().getResource("/todoapp/view/ListScreen.fxml"));
        try {
            AnchorPane root = (AnchorPane) listLoader.load();
            ListScreenController listScreenController = listLoader.getController();
            stage.close();
            listScreenController.start(stage);
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
