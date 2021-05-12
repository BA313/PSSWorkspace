package Controller;

import java.util.ArrayList;

import Model.JsonReader;
import Model.Task;

public class Controller {
	
		//Working path to Test Data set
		private String testFilepath = "src/Test/";
		private String filepath = "src/data/";
		
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

}
