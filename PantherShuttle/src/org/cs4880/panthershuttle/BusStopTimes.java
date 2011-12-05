package org.cs4880.panthershuttle;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BusStopTimes extends Activity {

	private int roth, ohio, sterling, hillcrest, campuscts, hudson, campus, seerley;
	
	public BusStopTimes() {
    	Calendar now = Calendar.getInstance();
        int minute = now.get(Calendar.MINUTE);
        
    	if (minute <= 28) {
        	roth = 4 - minute;
        	ohio = 10 - minute;
        	sterling = 13 - minute;
        	hillcrest = 19 - minute;
        	campuscts = 21 - minute;
        	hudson = 23 - minute;
        	campus = 26 - minute;
        	seerley = 28 - minute;
        } else {
        	roth = 34 - minute;
        	ohio = 40 - minute;
        	sterling = 43 - minute;
        	hillcrest = 49 - minute;
        	campuscts = 51 - minute;
        	hudson = 53 - minute;
        	campus = 56 - minute;
        	seerley = 58 - minute;
        }
        
        if (roth < 0) {
        	roth += 30;
        }
        if (ohio < 0) {
        	ohio += 30;
        }
        if (sterling < 0) {
        	sterling += 30;
        }
        if (hillcrest < 0) {
        	hillcrest += 30;
        }
        if (campuscts < 0) {
        	campuscts += 30;
        }
        if (hudson < 0) {
        	hudson += 30;
        }
        if (campus < 0) {
        	campus += 30;
        }
        if (seerley < 0) {
        	seerley += 30;
        }
    }
    
    public int getRoth() {
    	return roth;
    }
    
    public int getOhio() {
    	return ohio;
    }
    
    public int getSterling() {
    	return sterling;
    }
    
    public int getHillcrest() {
    	return hillcrest;
    }
    
    public int getCampuscts() {
    	return campuscts;
    }
    
    public int getHudson() {
    	return hudson;
    }
    
    public int getCampus() {
    	return campus;
    }
    
    public int getSeerley() {
    	return seerley;
    }
}