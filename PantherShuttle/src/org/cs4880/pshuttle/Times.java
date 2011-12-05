package org.cs4880.pshuttle;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Times extends Activity {

	private int roth;
	private int ohio;
	private int sterling;
	private int hillcrest;
	private int campuscts;
	private int hudson;
	private int campus;
	private int seerley;
	
	public Times() {
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
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.times);
        
        Calendar now = Calendar.getInstance();
        int minute = now.get(Calendar.MINUTE);
        String stopOne = "";
        String stopTwo = "";
        String stopThree = "";
        int minOne = 0;
        int minTwo = 0;
        int minThree = 0;
        
        TextView nextOne = (TextView)findViewById(R.id.nextOne);
        TextView nextOneTime = (TextView)findViewById(R.id.nextOneTime);
        TextView nextTwo = (TextView)findViewById(R.id.nextTwo);
        TextView nextTwoTime = (TextView)findViewById(R.id.nextTwoTime);
        TextView nextThree = (TextView)findViewById(R.id.nextThree);
        TextView nextThreeTime = (TextView)findViewById(R.id.nextThreeTime);
        
        if ((minute >= 0 && minute < 4) || (minute >= 28 && minute < 34) || (minute >= 58)) {
        	//	ROTH Northeast Lot
        	stopOne = "ROTH Northeast Lot";
        	minOne = roth;
        	stopTwo = "27th and Ohio St.";
        	minTwo = ohio;
        	stopThree = "Sterling Apts";
        	minThree = sterling;
        } else if ((minute >= 4 && minute < 10) || (minute >= 34 && minute < 40)) {
        	//	27th and Ohio St
        	stopOne = "27th and Ohio St.";
        	minOne = ohio;
        	stopTwo = "Sterling Apts";
        	minTwo = sterling;
        	stopThree = "Hillcrest Apts";
        	minThree = hillcrest;
        } else if ((minute >= 10 && minute < 13) || (minute >= 40 && minute < 43)) {
        	//	Sterling Apts
        	stopOne = "Sterling Apts";
        	minOne = sterling;
        	stopTwo = "Hillcrest Apts";
        	minTwo = hillcrest;
        	stopThree = "Campus Court Apts";
        	minThree = campuscts;
        } else if ((minute >= 13 && minute < 19) || (minute >= 43 && minute < 49)) {
        	// 	Hillcrest Apts
        	stopOne = "Hillcrest Apts";
        	minOne = hillcrest;
        	stopTwo = "Campus Court Apts";
        	minTwo = campuscts;
        	stopThree = "27th St. and Hudson Rd";
        	minThree = hudson;
        } else if ((minute >= 19 && minute < 21) || (minute >= 49 && minute < 51)) {
        	//	Campus Court Apts
        	stopOne = "Campus Court Apts";
        	minOne = campuscts;
        	stopTwo = "27th St. and Hudson Rd";
        	minTwo = hudson;
        	stopThree = "23rd and Campus Streets";
        	minThree = campus;
        } else if ((minute >= 21 && minute < 23) || (minute >= 51 && minute < 53)) {
        	//	27th St. and Hudson Rd
        	stopOne = "27th St. and Hudson Rd";
        	minOne = hudson;
        	stopTwo = "23rd and Campus Streets";
        	minTwo = campus;
        	stopThree = "Bus Stop by Seerley";
        	minThree = seerley;
        } else if ((minute >= 23 && minute < 26) || (minute >= 53 && minute < 56)) {
        	//	23rd and Campus Streets
        	stopOne = "23rd and Campus Streets";
        	minOne = campus;
        	stopTwo = "Bus Stop by Seerley";
        	minTwo = seerley;
        	stopThree = "ROTH Northeast Lot";
        	minThree = roth;
        } else if ((minute >= 26 && minute < 28) || (minute >= 56 && minute < 58)) {
        	//	Bus Stop by Seerley
        	stopOne = "Bus Stop by Seerley";
        	minOne = seerley;
        	stopTwo = "ROTH Northeast Lot";
        	minTwo = roth;
        	stopThree = "27th and Ohio St.";
        	minThree = ohio;
        }
        
        nextOne.setText(stopOne);
        nextOneTime.setText(minOne + " minutes");
        nextTwo.setText(stopTwo);
        nextTwoTime.setText(minTwo + " minutes");
        nextThree.setText(stopThree);
        nextThreeTime.setText(minThree + " minutes");
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