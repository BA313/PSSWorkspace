package Controller;

import java.time.LocalDate;
import java.util.ArrayList;

import Model.JsonReader;
import Model.Task;

public class Controller {
	
		//Working path to Test Data set
		private String testFilepath = "src/Test/";
		private String filepath = "src/data/";
		
		//Gobal variable that holds every task
		private ArrayList<Task> taskList;
		
		public Controller(){
			//taskList = JsonReader.readTaskList(testFilepath+"testTasks.json");
			//System.out.print(taskList.get(0).toString());		
			taskList = JsonReader.readStandardTaskList(filepath+"Set1.json");
			//System.out.println(taskList.get(0).toString());
			//taskList = new ArrayList<Task>();
		}
		
		public ArrayList<Task> getTasks() {
			return taskList;
		}
		
		public void addTask(Task task) {
			taskList.add(task);
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
		
		//gets all tasks for a certain day in a month 
		public ArrayList<Task> getDayMonthTasks(int d, int m, int y){
			ArrayList<Task> dayTasks = new ArrayList();
			for(Task task: taskList) {
				LocalDate temp = task.getStartDate();
				if(temp.getMonthValue() == m && temp.getDayOfMonth() == d && temp.getYear() == y) {
					dayTasks.add(task);
				}
			}
			return dayTasks;
		}
		
		//gets all tasks for a certain week in a month
		public ArrayList<Task> getWeekTasks(int d, int w, int m, int y){
			ArrayList<Task> weekTasks = new ArrayList();
			int weekStart = d - w;
			int weekEnd = weekStart + 7;
			for(Task task: taskList) {
				LocalDate temp  = task.getStartDate();
				if(temp.getMonthValue() == m && temp.getYear() == y)
					if ((weekStart <= temp.getDayOfMonth()) && (temp.getDayOfMonth() < weekEnd)) {
						weekTasks.add(task);
				}
			}
			return weekTasks;
		}

}
