package erb;

import java.util.ArrayList;

public class GoalTracker {

	ArrayList<ERBGoal> listOfAllGoals = new ArrayList<ERBGoal>();
	
	public GoalTracker() {
		
	}
	
	public void addGoal(ERBGoal erbGoal) {
		listOfAllGoals.add(erbGoal);
	}
	
	public void removeGoal(ERBGoal erbGoal) {
		listOfAllGoals.remove(erbGoal);
	}
	
	public int getNumberOfGoals() {
		return listOfAllGoals.size();
	}
	
	public ERBGoal getGoal(int id) {
		for(ERBGoal erbGoal: listOfAllGoals) {
			if(erbGoal.getId() == id) {
				return erbGoal;
			}
		}
		return null;
	}
	
	public ERBGoal getGoal(String title) {
		for(ERBGoal erbGoal: listOfAllGoals) {
			if(erbGoal.getTitle().contentEquals(title)) {
				return erbGoal;
			}
		}
		return null;
	}
	
	public ArrayList<ERBGoal> getStaticGoals(){
		ArrayList<ERBGoal> staticGoals = new ArrayList<ERBGoal>();
		for(ERBGoal erbGoal: listOfAllGoals) {
			if(erbGoal.isStatic) {
				staticGoals.add(erbGoal);
			}
		}
		return staticGoals;
	}
	
	public ArrayList<ERBGoal> getNonStaticGoals(){
		ArrayList<ERBGoal> nonStaticGoals = new ArrayList<ERBGoal>();
		for(ERBGoal erbGoal: listOfAllGoals) {
			if(!erbGoal.isStatic) {
				nonStaticGoals.add(erbGoal);
			}
		}
		return nonStaticGoals;
	}
	
	public ArrayList<ERBGoal> getAllGoals(){
		return listOfAllGoals;
	}
	
}
