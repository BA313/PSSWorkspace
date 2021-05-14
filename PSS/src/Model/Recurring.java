package Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Recurring extends Task {
	
	private int frequency;
	
	public Recurring(String name, LocalDate startDate, LocalDate endDate, int duration, boolean repeat,
			LocalTime startTime, int frequency) {
		super(name, RECURRING_TASK, startDate,endDate, duration, repeat, startTime);
		this.frequency = frequency;
	}
	

}