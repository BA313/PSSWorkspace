package Controller;

import java.time.LocalDate;
import java.util.ArrayList;

import Model.JsonReader;
import Model.Recurring;
import Model.Task;
//TODO logic for different years
//TODO Handle overlapping

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
		
		public void unSuppress(Task task) {
			for(Task t: taskList) {
				if(task.equals(task)) {
					t.setSuppressed(false);
					break;
				}
			}
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
		
		//Verifies that new transient task do not overlap
		public void checkOverlap(Task newTask) {
			boolean overlapped = false; 

			for(Task tasks : taskList) {
				if(overlapped) //Base Case
					break;				
				else if(tasks.getType() == Task.RECURRING_TASK && newTask.getType() == Task.TRANSIENT_TASK) { //Check if new Transient Task overlaps Recurring Task
					Recurring RTask = (Recurring)tasks;
					if(checkRecurringOverlap(newTask, RTask)) { 
						overlapped = true;
						break;
					}
				}
				else if(newTask.getType() == Task.RECURRING_TASK && tasks.getType() == Task.RECURRING_TASK) { //Checks new Recurring Task to other Recurring tasks
					Recurring newRTask = (Recurring)newTask;
					Recurring RTask = (Recurring)tasks;
					if(checkNewRecurringOverlap(newRTask, RTask))
					{
						overlapped = true;
						break;
					}
				}
				else if(newTask.getType() == Task.RECURRING_TASK && tasks.getType() == Task.TRANSIENT_TASK) {	//Check new Recurring Task with other transient tasks
					Recurring newRTask = (Recurring)newTask;
					if(checkRecurringOverlap(tasks, newRTask))
					{
						overlapped = true;
						break;
					}
				}
				else {//Compare new Transient Task to other Transient Tasks
					if(newTask.getStartDate().equals(tasks.getStartDate()))
						if(checkTimeOverlap(newTask,tasks))
						{
							overlapped = true;
							break;
						}
				}
			}
			if(!overlapped) //If no overlap with other tasks
				taskList.add(newTask);
		}
		
		//Verifies that times do not collide
		private boolean checkTimeOverlap(Task newTask, Task taskInList) {
			int taskStartTime = taskInList.getStartTime().getHour()*100+taskInList.getStartTime().getMinute(); //Converts LocalTime to int of 2400 format
			int taskEndTime = taskStartTime + taskInList.getDuration()/60*100 + taskInList.getDuration()%60; //Get end time by adding duration and convert minutes to hours
			int newTaskStartTime = newTask.getStartTime().getHour()*100+newTask.getStartTime().getMinute(); //Convert LocalTime to int of 2400 format
			int newTaskEndTime = newTaskStartTime + newTask.getDuration()/60*100 + newTask.getDuration()%60; //Get end time by adding duration and convert minutes to hours
			
			if(newTask.getStartTime().equals(taskInList.getStartTime())) //Check if same startTime
			{
				System.out.println("Start Time overlaps with " + taskInList.getName());
				return true;
			}
			else if(newTaskStartTime > taskStartTime && newTaskStartTime <= taskEndTime) //Checks if StartTime is in between a task
			{
				System.out.println("Time interferes with " + taskInList.getName());
				return true;
			}
			else if(newTaskEndTime >= taskStartTime && newTaskEndTime <= taskEndTime) //Checks if endTime is in between a task
			{
				System.out.println("End Time for new task intereferes with " + taskInList.getName());
				return true;
			}
			else if(newTaskStartTime < taskStartTime && newTaskEndTime > taskEndTime) //Checks if time interval has a task
			{
				System.out.println("The task overlaps with " + taskInList.getName());
				return true;
			}
			return false;
		}
		
		//Check if new Transient Task overlaps with recurring tasks
		private boolean checkRecurringOverlap(Task newTask, Recurring RTask) {
			LocalDate tempDate = RTask.getStartDate();
			boolean case1 = false, returnCase = false;
			
			while(case1 == false) {
				if(tempDate.isBefore(newTask.getStartDate()))
				{
					tempDate = tempDate.plusDays(RTask.getFrequency());
				}
				else if(tempDate.equals(newTask.getStartDate()) && checkTimeOverlap(newTask,RTask))
				{
					returnCase = true;
					case1 = true;
				}
				else
					case1 = true;
			}
			return returnCase;
		}
		
		//Checks if new Recurring Task overlaps other recurring tasks
		private boolean checkNewRecurringOverlap(Recurring newRTask, Recurring taskInList)
		{
			LocalDate tempNewDate = newRTask.getStartDate(), tempTaskDate = taskInList.getStartDate(); //Declare LocalTime variables to increment dates
			boolean case1 = false, case2 = false, case3 = false, returnCase = false; //Cases for while loop
			
			if(newRTask.getStartDate().isAfter(taskInList.getEndDate())) //Checks if new Recurring Task occurs after period of Recurring Tasks
				return false;
			else if(newRTask.getEndDate().isBefore(taskInList.getStartDate())) //Checks if new Recurring Task occurs before start period of Recurring Tasks
				return false;
			while(case1 == false) {
				if(tempNewDate.isBefore(tempTaskDate) && tempNewDate.isBefore(taskInList.getEndDate())) { //Checks to make sure date is not over a task's endDate
					tempNewDate = tempNewDate.plusDays(newRTask.getFrequency()); //Increase Date by its frequency
					if(tempNewDate.isAfter(taskInList.getEndDate()))             //Check if passed task's endDate
						case2 = true;
				}
				else if(tempNewDate.isAfter(tempTaskDate) && tempTaskDate.isBefore(newRTask.getEndDate())) { //Checks if task is not over new task's endDate
					tempTaskDate = tempTaskDate.plusDays(taskInList.getFrequency());
					if(tempTaskDate.isAfter(newRTask.getEndDate()))
						case3 = true;
				}
				else if(tempNewDate.equals(tempTaskDate) && checkTimeOverlap(newRTask,taskInList)) { //Check if date and time overlap
					returnCase = true;
					case1 = true;
				}
				else if(case2 && case3) //If no overlap exists
					case1 = true;
				else if(tempNewDate.equals(tempTaskDate) && !checkTimeOverlap(newRTask,taskInList)) //Check if date and time overlap
					case1 = true;
			}
			return returnCase;
		}
		
}
