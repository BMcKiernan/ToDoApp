/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import todoapp.controller.ListScreenController;

/**
 *
 * @author BriMc
 */
public class ToDoApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/todoapp/view/ListScreen.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        ListScreenController listController = loader.getController();
        listController.start(stage);
        stage.setScene(new Scene(root));
        stage.setTitle("TO-DO");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
