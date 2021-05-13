package Controller;

import java.time.LocalDate;
import java.util.ArrayList;

import Model.JsonReader;
import Model.Task;

//TODO add month, week, day sorting

public class Controller {
	
		//Working path to Test Data set
		private String testFilepath = "src/Test/";
		private String filepath = "src/data/";
		
		//Gobal variable that holds every task
		private ArrayList<Task> taskList;
		
		public Controller(){
			taskList = JsonReader.readTaskList(testFilepath+"testTasks.json");
			//System.out.print(taskList.get(0).toString());		
			//taskList = JsonReader.readStandardTaskList(filepath+"Set1.json");
			//System.out.println(taskList.get(0).toString());
			//taskList = new ArrayList<Task>();
		}
		
		public ArrayList<Task> getTasks() {
			return taskList;
		}
		
		public void addTask(Task task) {
			taskList.add(task);
		}
		
		//gets all tasks for a certain month 
		public ArrayList<Task> getMonthTasks(int m){
			ArrayList<Task> monthTasks = new ArrayList();
			for(Task task: taskList) {
				if(task.getStartDate().getMonthValue() == m) {
					monthTasks.add(task);
				}
			}
			return monthTasks;
		}
		
		//gets all tasks for a certain month and year
		public ArrayList<Task> getMonthYearTasks(int m, int y){
			ArrayList<Task> monthTasks = new ArrayList();
			for(Task task: taskList) {
				LocalDate temp = task.getStartDate();
				if(temp.getMonthValue() == m && temp.getYear() == y) {
					monthTasks.add(task);
				}
			}
			return monthTasks;
		}

}
