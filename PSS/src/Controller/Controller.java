package Controller;

import java.time.LocalDate;
import java.util.ArrayList;

import Model.JsonReader;
import Model.Task;

//TODO logic for different years

public class Controller {
	
		//Working path to Test Data set
		private static final String testFilepath = "src/Test/";
		private static final String filepath = "src/data/";
		
		//Global variable that holds every task
		private ArrayList<Task> taskList;
		
		public Controller(){
			//taskList = JsonReader.readTaskList(testFilepath+"testTasks.json");
			taskList = JsonReader.readStandardTaskList(filepath+"Set1.json");
		}
		
		public ArrayList<Task> getTasks() {
			return taskList;
		}
		
		public void addTask(Task task) {
			taskList.add(task);
		}
		
		public void removeTask(Task task) {
			taskList.remove(task);
		}
		
		//gets all tasks for a certain month and year
		public ArrayList<Task> getMonthYearTasks(LocalDate date){
			int m = date.getMonthValue(),  y = date.getYear();
			ArrayList<Task> monthTasks = new ArrayList();
			for(Task task: taskList) {
				LocalDate start = task.getStartDate();
				LocalDate end = task.getEndDate();
				if(start.getMonthValue() == m && start.getYear() == y) {
					monthTasks.add(task);
					continue;
				}
				if(task.getType().equals(Task.RECURRING_TASK)) {
					if(start.isBefore(date.plusMonths(1)) && end.isAfter(date.minusMonths(1))) {
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
					if(start.isBefore(date.plusMonths(1)) && end.isAfter(date.minusMonths(1))) {
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
		public ArrayList<Task> getWeekTasks(LocalDate date){
			int d = date.getDayOfMonth(), w = date.getDayOfWeek().getValue(),
					m = date.getMonthValue(), y = date.getYear();
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
					if(start.isBefore(date.plusMonths(1)) && end.isAfter(date.minusMonths(1))) {
						//check if the endDate is in this month
						if(end.getMonthValue() == m) {
							//if it is check to see if the endDate is in this week
							while(end.getMonthValue() == m) {
								if ((weekStart <= end.getDayOfMonth()) 
										&& (end.getDayOfMonth() < weekEnd)) {
									weekTasks.add(task);
									break;
								}
								end = end.minusWeeks(1);
							}
						//already checked if the start date is in the week
						}else if(start.getMonthValue() == m) {
							while(start.getDayOfMonth() <= date.lengthOfMonth() && start.getMonthValue() == m) {
								if ((weekStart <= start.getDayOfMonth()) 
										&& (start.getDayOfMonth() < weekEnd)) {
									weekTasks.add(task);
									break;
								}
								start = start.plusWeeks(1);
							}
						}else {
							weekTasks.add(task);
						}
					}
				}
			}
			return weekTasks;
		}

}
