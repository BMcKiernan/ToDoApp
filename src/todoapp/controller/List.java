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
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import todoapp.model.Task;

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


    public void initialize() {
        listTreeTableView = new JFXTreeTableView<>();
        
    }    
    
}
