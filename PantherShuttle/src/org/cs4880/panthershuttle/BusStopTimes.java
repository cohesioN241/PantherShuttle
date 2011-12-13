package org.cs4880.panthershuttle;

import java.util.Calendar;

public class BusStopTimes {

	private int[] stop = { 4, 10, 13, 19, 21, 23, 26, 28 };
	
	public BusStopTimes() {
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);	// hour of day
        int minute = now.get(Calendar.MINUTE);		// minute of hour
        
		int offset = minute;
		if (minute > 28) {	// second half of the hour
			offset -= 30;
		}
		for (int i=0; i<stop.length; i++){
			stop[i] -= offset;		// time til next bus arrives
			
			if (stop[i] <= 0) {			// bus has already passed
				if ((hour == 16) && (minute >= 30)) {	// time is past 4:30 PM, don't want the user waiting for the next bus
					stop[i] = 0;
				} else {				// next time bus will arrive
					stop[i] += 30;
				}
			} else if ((hour == 6) && (minute >= 30)) {	// check the schedule after 6:30 AM
				stop[i] += 30;
			}
		}
    }
	
    public int getTime(String busstop){
    	int time = 0;
    	if (busstop.equals("roth")){
    		time = stop[0];
    	} else if (busstop.equals("ohio")){
    		time = stop[1];
    	} else if (busstop.equals("sterling")){
    		time = stop[2];
    	} else if (busstop.equals("hillcrest")){
    		time = stop[3];
    	} else if (busstop.equals("campuscts")){
    		time = stop[4];
    	} else if (busstop.equals("hudson")){
    		time = stop[5];
    	} else if (busstop.equals("campus")){
    		time = stop[6];
    	} else if (busstop.equals("seerley")){
    		time = stop[7];
    	}
    	return time;
    }
}