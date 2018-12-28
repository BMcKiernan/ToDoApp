/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapp.model;

import java.util.ArrayList;

/**
 *
 * @author BriMc
 */
public class AppState {
    
    private ArrayList<ToDoList> toDoLists;
    private ToDoList selectedList;
    private AppState() {
        toDoLists = new ArrayList<>();
    }
    
    public static AppState getInstance() {
        return appStateHolder.INSTANCE;
    }
    
    private static class appStateHolder {

        private static final AppState INSTANCE = new AppState();
    }
    
    public void addList(ToDoList toDoList){
        toDoLists.add(toDoList);
    }
    
    public ArrayList<ToDoList> getList(){
        return toDoLists;
    }
    
    public void removeList(ToDoList list){
        toDoLists.remove(list);
    }
    
    public void setSelectedList(ToDoList selectedList){
        this.selectedList = selectedList;
    }
    
    public ToDoList getSelectedList(){
        return selectedList;
    }
}
