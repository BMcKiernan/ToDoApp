/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Brian McKiernan
 */
public class ProcessState {
    private static final String storeDir = "dat";
    private static final String storeFile = "AppState.dat";
    private File file;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private FileOutputStream fos;
    private AppState appState;
    
    
    public ProcessState(){
        file = new File(storeFile);
        System.out.println(file.getAbsolutePath());
        this.appState = AppState.getInstance();
    }
    
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
