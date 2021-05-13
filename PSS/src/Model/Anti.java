package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Anti extends Task {
	public Anti(String name, LocalDate startDate, LocalDate endDate, int duration, boolean repeat, LocalTime startTime,
			LocalTime endTime) {
		super(name, startDate, endDate, duration, repeat, startTime, endTime);
		// TODO Auto-generated constructor stub
	}

	private static boolean cancelTask() {
		return false;
	}
}
