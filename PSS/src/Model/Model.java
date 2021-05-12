package Model;

import java.util.ArrayList;


public class Model {
	
	//both work the same one is direct path, the other is simple path
	private String filepath = "src/Test/";
	
	private ArrayList<Task> taskList;
	
	public Model(){
		taskList = JsonReader.readTaskList(filepath+"testTasks.json");
		System.out.print(taskList.get(0).toString());		
	}
}
