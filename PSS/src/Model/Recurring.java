package Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Recurring extends Task {
	
	public Recurring(String name, LocalDate startDate, LocalDate endDate, int duration, boolean repeat,
			LocalTime startTime, LocalTime endTime) {
		super(name, startDate, endDate, duration, repeat, startTime, endTime);
	}
	private int repeatBasis;
	private String task, category;
	private Date endDate;


}
