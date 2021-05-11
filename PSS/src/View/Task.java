package View;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {
    private String name;
    private LocalDate startDate, endDate;
    private LocalTime startTime, endTime;
    private int duration, id;
    private boolean repeat;
    
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
}
