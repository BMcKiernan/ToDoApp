/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Brian McKiernan
 * The AppState class maintains the state of the ToDoLists,
 * and the Tasks of the selected ToDoList field selectedList.
 * This creates a separation between the management of the controllers and
 * the objects and their respective state. AppState is a singleton
 * class and as such there is only one instance of the ToDoList app
 * state at any time.
 */
public class AppState {

    private List<ToDoList> toDoLists;
    private ToDoList selectedList;
    
    /**
     * The AppState Constructor is private so instances of the
     * AppState Class cannot be created using the new keyword.
     * When the singleton AppState object is created the toDoLists field
     * is instantiated with an ArrayList of ToDoLists and selectedList is
     * set to null.
     */
    private AppState() {
        toDoLists = new ArrayList<>();
        selectedList = null;
    }
    
    /**
     * getInstance returns the appState instance maintained by
     * the static method appStateHolder.
     * @return appStateHolder.INSTANCE either a new AppState if this is the
     * first time getInstance() is called or the instance instantiated at some
     * point earlier during run time.
     */
    public static AppState getInstance() {
        return appStateHolder.INSTANCE;
    }
    
    /**
     * appStateHolder is an innerclass which instantiates a final AppState 
     * instance.
     */
    private static class appStateHolder {
        private static final AppState INSTANCE = new AppState();
    }
    
    /**
     * addList adds a new ToDoList object to the toDoLists field 
     * inside of AppState.
     * @param toDoList new instance of ToDoList
     */
    public void addList(ToDoList toDoList){
        toDoLists.add(toDoList);
    }
    
    /**
     * removeList removes ToDoList object from the toDoLists field inside
     * of AppState.
     * @param list ToDoList instance
     */
    public void removeList(ToDoList list){
        toDoLists.remove(list);
    }
    
    /**
     * getLists returns the complete List of ToDoList objects
     * @return toDoLists List 
     */
    public List<ToDoList> getLists(){
        return toDoLists;
    }
    
    /**
     * addTask adds a new Task object to the selectedList field inside of AppState.
     * @param task new instance of Task
     */
    public void addTask(Task task){
        selectedList.addTask(task);
    }
    
    /**
     * removeTask removes Task object from the tasks field inside of AppState.
     * @param task Task instance
     */
    public void removeTask(Task task){
        selectedList.removeTask(task);
    }
    
    /**
     * getTasks returns complete List of Task objects
     * @return 
     */
    public List<Task> getTasks(){
        return selectedList.getTasks();
    }
    
    /**
     * setSelectedList sets the current ToDoList which will be operated on 
     * in TaskList
     * @param selectedList selected ToDoList
     */
    public void setSelectedList(ToDoList selectedList){
        this.selectedList = selectedList;
    }
    
    /**
     * getSelectedList returns the selected ToDoList for TaskList
     * @return  selectedToDoList
     */
    public ToDoList getSelectedList(){
        return selectedList;
    }
}
