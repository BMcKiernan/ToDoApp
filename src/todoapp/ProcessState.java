package todoapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import todoapp.model.AppState;
import todoapp.model.ToDoList;

/**
 * The Process State class reads and writes the serializable ToDoLists from
 * the filesystem into AppState and vice versa when main is run setting up
 * the application.
 * @author Brian McKiernan
 */
public class ProcessState {
    private static final String storeFile = "AppState.dat";
    private File file;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private FileOutputStream fos;
    private AppState appState;
    
    /**
     * ProcessState() constructor sets up the file for reading/writing 
     * and gets the AppState instance.
     */
    public ProcessState(){
        file = new File(storeFile);
        System.out.println(file.getAbsolutePath());
        this.appState = AppState.getInstance();
    }
    
    /**
     * deserialize() reads the serialized ToDolist data from the filesystem and 
     * initializes AppState's data.
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void deserialize() throws IOException, ClassNotFoundException{
        ArrayList<ToDoList> toDoLists;
        if(file.exists() && !file.isDirectory()){
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            toDoLists = (ArrayList<ToDoList>) ois.readObject();
            ois.close();
        }else{
            toDoLists = new ArrayList<>();
        }
        appState.setLists(toDoLists);
    }
    
    /**
     * serialize() reads the ToDoList data from AppState and writes the serializable 
     * data to the filesystem
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void serialize() throws FileNotFoundException, IOException{
        if(!file.exists())
            file.createNewFile();
        fos = new FileOutputStream(file);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(appState.getLists());
        oos.flush();
        oos.close();
    }
}
