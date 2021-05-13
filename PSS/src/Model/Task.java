package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {
	
	private static final String ANTI_TASK = "Cancellation";
	private static final String RECURRING_TASK = "Repeat";
	private static final String TRANSIENT_TASK = "Transient";	
	
    private String name;
    private LocalDate startDate, endDate;
    private LocalTime startTime, endTime;
    private int duration, id;
    private boolean repeat;
    private boolean suppressed;
    
    public Task(String name, LocalDate startDate, LocalDate endDate, int duration, boolean repeat, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.repeat = repeat;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public String getName() {
        return name;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public boolean getRepeat() {
        return repeat;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public String toString() {
    	return "Task Name: " + name;
    }
}
