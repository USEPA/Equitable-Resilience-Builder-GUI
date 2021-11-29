package erb;

import java.util.ArrayList;

public class TaskTracker {

	ArrayList<ERBTask> listOfAllTasks = new ArrayList<ERBTask>();
	
	public TaskTracker() {
		
	}
	
	public void addTask(ERBTask erbTask) {
		listOfAllTasks.add(erbTask);
	}
	
	public void removeTask(ERBTask erbTask) {
		listOfAllTasks.remove(erbTask);
	}
	
	public int getNumberOfTasks() {
		return listOfAllTasks.size();
	}
	
	public ERBTask getTask(int id) {
		for(ERBTask erbTask: listOfAllTasks) {
			if(erbTask.getId() == id) {
				return erbTask;
			}
		}
		return null;
	}
	
	public ERBTask getTask(String title) {
		for(ERBTask erbTask: listOfAllTasks) {
			if(erbTask.getTitle().contentEquals(title)) {
				return erbTask;
			}
		}
		return null;
	}
	
	public ArrayList<ERBTask> getStaticTasks(){
		ArrayList<ERBTask> staticTasks = new ArrayList<ERBTask>();
		for(ERBTask erbTask: listOfAllTasks) {
			if(erbTask.isStatic) {
				staticTasks.add(erbTask);
			}
		}
		return staticTasks;
	}
	
	public ArrayList<ERBTask> getNonStaticTasks(){
		ArrayList<ERBTask> nonStaticTasks = new ArrayList<ERBTask>();
		for(ERBTask erbTask: listOfAllTasks) {
			if(!erbTask.isStatic) {
				nonStaticTasks.add(erbTask);
			}
		}
		return nonStaticTasks;
	}
	
	public ArrayList<ERBTask> getAllTasks(){
		return listOfAllTasks;
	}
}
