package org.cs4880.pshuttle;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PantherShuttleActivity extends Activity implements OnClickListener {
	
	private ImageView imgView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        imgView = (ImageView)findViewById(R.id.logo);
	    imgView.setImageResource(R.drawable.logo2);
	    
	    Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
	    
	    View btnMap = findViewById(R.id.btnMap);
	    btnMap.setOnClickListener(this);
	    
	    View btnSchedule = findViewById(R.id.btnSchedule);
	    btnSchedule.setOnClickListener(this);
        
        View btnTimes = findViewById(R.id.btnTimes);
        btnTimes.setOnClickListener(this);
    }
    
    public void onClick(View v){
    	switch(v.getId()){
    	case R.id.btnMap:
    		Intent i = new Intent(this, Map.class);
    		startActivity(i);
    		break;
    	case R.id.btnSchedule:
    		Intent j = new Intent(this, Schedule.class);
    		startActivity(j);
    		break;
    	case R.id.btnTimes:
    		Intent k = new Intent(this, Times.class);
    		startActivity(k);
    		break;
    	}
    }
}