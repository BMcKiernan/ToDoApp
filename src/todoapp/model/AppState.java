package todoapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Brian McKiernan
 * The AppState class maintains the state of the ToDoLists,
 * and the Tasks of the selected ToDoList field selectedList.
 * This creates a separation between the management of the controllers,
 * the objects, and their respective state. AppState is a singleton
 * class and as such there is only one instance of the ToDoList app
 * state at any time.
 */
public class AppState {

    private List<ToDoList> toDoLists;
    private ToDoList selectedList;
    private boolean editPressed;
    
    /**
     * The AppState Constructor is private so instances of the
     * AppState Class cannot be created using the new keyword.
     * When the singleton AppState object is created the toDoLists field
     * is instantiated with an ArrayList of ToDoLists and selectedList is
     * set to null. EditPressed is a boolean value by default set to false.
     */
    private AppState() {
        toDoLists = new ArrayList<>();
        selectedList = null;
        editPressed = false;
    }
    
    /**
     * getInstance returns the appState from the private static appStateHodler
     * class.
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
        this.toDoLists.add(toDoList);
    }
    
    /**
     * removeList removes ToDoList object from the toDoLists field inside
     * of AppState.
     * @param list ToDoList instance
     */
    public void removeList(ToDoList list){
        this.toDoLists.remove(list);
    }
    
    /**
     * getLists returns the complete List of ToDoList objects
     * @return toDoLists List 
     */
    public List<ToDoList> getLists(){
        return this.toDoLists;
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
     * getTasks returns the selectedLists complete List of Task objects.
     * @return 
     */
    public List<Task> getTasks(){
        return selectedList.getTasks();
    }
    
    /**
     * setSelectedList sets the current ToDoList.
     * @param selectedList selected ToDoList
     */
    public void setSelectedList(ToDoList selectedList){
        this.selectedList = selectedList;
    }
    
    /**
     * getSelectedList returns the selected ToDoList.
     * @return  selectedToDoList
     */
    public ToDoList getSelectedList(){
        return selectedList;
    }
    
    /**
     * The setLists method is solely for setting up the singleton appState
     * when data has been deserialized from the filesystem.
     * @param list ToDoLists 
     */
    public void setLists(List<ToDoList> list){
        this.toDoLists = list;
    }
    
    /**
     * setEditPressed() sets the boolean editPressed to whatever boolean value is 
     * passed to it.
     * @param editPressed boolean
     */
    public void setEditPressed(boolean editPressed){
        this.editPressed = editPressed;
    }
    
    /**
     * editPressed() returns true or false to indicate whether the
     * CreateListController was created because the "Edit List" button was
     * clicked.
     * @return 
     */
    public boolean editPressed(){
        return this.editPressed;
    }
}
