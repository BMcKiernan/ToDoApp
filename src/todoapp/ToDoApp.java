package todoapp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import todoapp.controller.ListScreenController;

/**
 * 
 * @author Brian McKiernan
 */
public class ToDoApp extends Application {
    

    @Override
    public void start(Stage stage) throws Exception {
        ProcessState ps = new ProcessState();
        ps.deserialize();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/todoapp/view/ListScreen.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        ListScreenController listController = loader.getController();
        listController.start(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setTitle("TO-DO");
        stage.show();
        stage.setOnCloseRequest(event -> {
            try {
                ps.serialize();
            } catch (IOException ex) {
                Logger.getLogger(ToDoApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
