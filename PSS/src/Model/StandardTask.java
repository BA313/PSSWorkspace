package Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StandardTask {
    private String Name;
    private String Type;
    private int StartDate;
    private int StartTime;
    private float Duration;
    private int EndDate;
    private int Frequency;

    public StandardTask(String name, String type, int startDate, int startTime, float duration, int endDate, int frequency) {
        Name = name;
        Type = type;
        StartDate = startDate;
        StartTime = startTime;
        Duration = duration;
        EndDate = endDate;
        Frequency = frequency;
    }

    public Task convertToTask() {
        LocalDate startDate = StartDate != 0
                ? LocalDate.parse(Integer.toString(StartDate), DateTimeFormatter.ofPattern("yyyyMMdd"))
                : LocalDate.now();      // when LocalDate is not found in this json object
        LocalDate endDate = EndDate != 0
                ? LocalDate.parse(Integer.toString(EndDate), DateTimeFormatter.ofPattern("yyyyMMdd"))
                : LocalDate.now();      // when LocalDate is not found in this json object
        LocalTime startTime = LocalTime.of(StartTime, 0);
        float newDuration = 60*Duration;
        boolean repeat = Frequency != 0;
        String type = Type;
        if(repeat) {
        	return new Recurring(Name, Type, startDate, endDate, (int)newDuration, repeat, startTime, Frequency);
        }else if(!type.equals(Task.ANTI_TASK)) {
        	return new Transient(Name, Type, startDate, endDate, (int)newDuration, repeat, startTime);
        }else {
        	return new Anti(Name, Type, startDate, endDate, (int)newDuration, repeat, startTime, null);
        }
        
    }
}


