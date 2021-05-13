package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transient extends Task {
	
	
	
	public Transient(String name, LocalDate startDate, LocalDate endDate, int duration, boolean repeat,
			LocalTime startTime, LocalTime endTime) {
		super(name, startDate, endDate, duration, repeat, startTime, endTime);
	}

	
	private static String formatTime(int time) {
		String tempTime = "", formattedTime = "";
		
		if(time >= 1200) //Lets us know that the time entered is in PM
		{
			tempTime = String.valueOf(time-1200); //Get hours by subtracting 12 hours
			if(tempTime.length() == 3) //Takes cares of single hours like 1,2,..9
			{
				formattedTime += String.valueOf(tempTime.charAt(0));
				formattedTime += ":" + String.valueOf(tempTime.charAt(1)) + String.valueOf(tempTime.charAt(2)) + " PM";
			}
			else
			{
				if(tempTime.length() == 2) //If time entered is at 12 PM with minutes included
					formattedTime = "12:" + String.valueOf(tempTime.charAt(0)) + String.valueOf(tempTime.charAt(1)) + " PM";
				else if(tempTime.length() == 1) //Exactly 12:00 PM
					formattedTime = "12:00 PM";
				else //Takes care of 10 and 11 PM
				{
					formattedTime +=String.valueOf(tempTime.charAt(0)) + String.valueOf(tempTime.charAt(1));
					formattedTime += ":" + String.valueOf(tempTime.charAt(2)) + String.valueOf(tempTime.charAt(3)) + " PM";				
				}			
			}
		}
		else
		{
			tempTime = String.valueOf(time);
			if(tempTime.length() == 3)
			{
				formattedTime += String.valueOf(tempTime.charAt(0));
				formattedTime += ":" + String.valueOf(tempTime.charAt(1)) + String.valueOf(tempTime.charAt(2)) +" AM";
			}
			else
			{
				if(time > 59) //Anything but 
				{
					formattedTime = String.valueOf(tempTime.charAt(0)) + String.valueOf(tempTime.charAt(1));
					formattedTime += ":" + String.valueOf(tempTime.charAt(2)) + String.valueOf(tempTime.charAt(3)) + " AM";				
				}
				else if(time == 0) //Exactly 12 AM 
					formattedTime = "12:00 AM";
				else
					formattedTime = "12:" + String.valueOf(tempTime.charAt(0)) + String.valueOf(tempTime.charAt(1)) + " AM";			
			}
		}
		return formattedTime;
	}
}
