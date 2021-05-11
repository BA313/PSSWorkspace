package Model;

import java.util.ArrayList;


public class Model {
	
	//both work the same one is direct path, the other is simple path
	private String filepath = "C:/Users/Ben/Documents/College/Semester 2 yr 3/CS-3560/PSSWorkspace/PSSWorkspace/PSS/src/Test/";
	private String filepath2 = "src/Test/";
	
	private ArrayList<Task> taskList;
	
	public Model(){
		taskList = JsonReader.readTransientTaskList(filepath2+"testTransientTasks.json");
		System.out.print(taskList.get(0).toString());		
	}
}
