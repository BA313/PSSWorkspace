package pss;

import java.util.Date;

public class Task {
    private String name;
    private Date startDate, endDate;
    private int duration, id;
    private boolean repeat;
    
    public Task(String name, Date startDate, Date endDate, int duration, boolean repeat) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.repeat = repeat;
    }
    
    public String getName() {
        return name;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public boolean getRepeat() {
        return repeat;
    }
}
