package Controller;

import java.time.LocalDate;
import java.util.ArrayList;

import Model.JsonReader;
import Model.Task;

//TODO logic for different years

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
				LocalDate start = task.getStartDate();
				LocalDate end = task.getEndDate();
				if(start.getMonthValue() == m && start.getYear() == y) {
					monthTasks.add(task);
					continue;
				}
				if(task.getType().equals(Task.RECURRING_TASK)) {
					if(start.getMonthValue() <= m && end.getMonthValue() >= m && start.getYear() == y) {
						monthTasks.add(task);
					}
				}
			}
			return monthTasks;
		}
		
		//gets all tasks for a certain day in a month 
		public ArrayList<Task> getDayMonthTasks(LocalDate date){
			int d = date.getDayOfMonth(),  m = date.getMonthValue(),  y = date.getYear();
			ArrayList<Task> dayTasks = new ArrayList();
			for(Task task: taskList) {
				LocalDate start = task.getStartDate();
				LocalDate end = task.getEndDate();
				if(start.getMonthValue() == m && start.getDayOfMonth() == d && start.getYear() == y) {
					dayTasks.add(task);
					continue;
				}
				if(task.getType().equals(Task.RECURRING_TASK)) {
					if(start.getMonthValue() <= m && end.getMonthValue() >= m && start.getYear() == y) {
						if(!(end.getMonthValue() == m && d > end.getDayOfMonth()) 
								&& !(start.getMonthValue() == m && d < start.getDayOfMonth())) {
							if(date.getDayOfWeek() == start.getDayOfWeek())
								dayTasks.add(task);
						}
					}
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
				LocalDate start = task.getStartDate();
				LocalDate end = task.getEndDate();
				if(start.getMonthValue() == m && start.getYear() == y) {
					if ((weekStart <= start.getDayOfMonth()) && (start.getDayOfMonth() < weekEnd)) {
						weekTasks.add(task);
						continue;
					}
				}
				//tests for recurring tasks
				if(task.getType().equals(Task.RECURRING_TASK)) {
					//check if week is in the month range
					if(start.getMonthValue() <= m && end.getMonthValue() >= m && start.getYear() == y) {
						//check if the endDate is in this month
						if(end.getMonthValue() == m) {
							//if it is check to see if the endDate is in this week
							if ((weekStart <= end.getDayOfMonth()) 
									&& (end.getDayOfMonth() < weekEnd)) {
								weekTasks.add(task);
							}
						//already checked if the start date is in the week
						}else if(start.getMonthValue() == m) {
							continue;
						}else {
							weekTasks.add(task);
						}
					}
				}
			}
			return weekTasks;
		}

}
