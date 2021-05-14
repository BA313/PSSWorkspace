package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Anti extends Task {
	
	private Task canceledTask;
	
	public Anti(String name, LocalDate startDate, LocalDate endDate, int duration, 
			boolean repeat, LocalTime startTime, Task task) {
		super(name, ANTI_TASK, startDate, endDate, duration, repeat, startTime);
		canceledTask = task;
		try {
		cancelTask(task);
		}catch(NullPointerException e) {
			System.out.println(e);
		}
	}
	
	public Task getCancelled() {
		return canceledTask;
	}
	
	public boolean cancelTask(Task task) {
		task.setSuppressed(true);
		return true;
	}
}
