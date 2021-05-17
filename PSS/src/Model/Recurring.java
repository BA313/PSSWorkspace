package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Recurring extends Task {
	
	//TODO for future implementation of varied repeats
	private int frequency;
	
	public Recurring(String name, LocalDate startDate, LocalDate endDate, int duration, boolean repeat,
			LocalTime startTime, int frequency) {
		super(name, RECURRING_TASK, startDate,endDate, duration, repeat, startTime);
		this.frequency = frequency;
	}
	
	public int getFrequency() {
		return frequency;
	}

}