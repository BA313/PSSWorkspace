package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transient extends Task {
	
	
	
	public Transient(String name, LocalDate startDate, LocalDate endDate, int duration, boolean repeat,
			LocalTime startTime) {
		super(name,TRANSIENT_TASK, startDate, endDate, duration, repeat, startTime);
	}

}
