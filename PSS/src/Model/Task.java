package Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Task {
	
	public static final String ANTI_TASK = "Cancellation";
	public static final String RECURRING_TASK = "Repeat";
	public static final String TRANSIENT_TASK = "Transient";	
	
    private String name;
    private String type;
    private String category;
    private LocalDate startDate, endDate;
    private LocalTime startTime;
    private int duration, id;
    private boolean repeat;
    private boolean suppressed;
    
    public Task(String name,String type, String category, LocalDate startDate, LocalDate endDate, int duration, boolean repeat, LocalTime startTime) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.repeat = repeat;
        this.startTime = startTime;
    }
    
    public StandardTask convertToStandardTask() {
    			int formattedStartDate = Integer.parseInt(getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    			int formattedEndDate = Integer.parseInt(getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    			int formattedStartTime = getStartTime().getHour()*100;
    			float formattedDuration = (float)getDuration() / 60;
    			
    			if(getStartTime().getMinute() != 0) {
    				formattedStartTime += getStartTime().getMinute();
    			}
    			else {
    				formattedStartTime /= 100;
    			}
    			
    			if(!getRepeat()) {
    				return new StandardTask(getName(),getCategory(), formattedStartDate,formattedStartTime,formattedDuration,formattedEndDate,0);
    			}
    			else {
    				Recurring RTask = (Recurring)this;
    				return new StandardTask(getName(),getCategory(), formattedStartDate,formattedStartTime,formattedDuration,formattedEndDate,RTask.getFrequency());
    			}
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public String getCategory() {
    	return category;
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
    
    public String toString() {
    	return "Task Name: " + name;
    }
    
    public boolean getSuppressed() {
    	return suppressed;
    }
    
    public boolean setSuppressed(boolean s) {
    	return suppressed = s;
    }
    
}
