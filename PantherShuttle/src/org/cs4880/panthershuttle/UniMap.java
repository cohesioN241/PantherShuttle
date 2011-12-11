package org.cs4880.panthershuttle;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class UniMap extends MapActivity implements LocationListener {

	private MapController mapController;
	private MapView mapView;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private MyItemizedOverlay itemizedoverlay;
	private Location location = null;
	private GeoPoint myLocation = null;
	private OverlayItem overlayitem = null;
	private double lat, lng = 0;
	private LocationManager locationManager;
	private String gpsProvider = "";
	private BusStopTimes time;
	private BusStop locRoth, locOhio, locSterling, locHillcrest, locCampuscts, locHudson, locCampus, locSeerley;
	private BusStop[] busstops;
	private boolean isRunning = false;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.maps); // bind the layout to the activity
		
		//setup the map
		setupMap();
		
		//Pull my location
		getMyLocation();
		
		//setup bustop locations
		setupBusStops();
		
		//check if the bus is currently running
		Calendar now = Calendar.getInstance();
        if ((now.get(Calendar.HOUR_OF_DAY) >= 7) && (now.get(Calendar.HOUR_OF_DAY) < 17) && (now.get(Calendar.DAY_OF_WEEK) > 0) && (now.get(Calendar.DAY_OF_WEEK) < 6)) {
        	isRunning = true;
        }
        
        if (isRunning) {	//is the bus running?
	        //get the nearest bus stops
	        getNearestStops();
        } else {	//notify user that bus is not currently running
        	getOfflineStops();
        	notifyOffline();
        }
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/** This method will setup our Google Map */
	private void setupMap() {
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.pin);	//marker for my location
        itemizedoverlay = new MyItemizedOverlay(drawable, this);
		
		mapView.setStreetView(true);
		mapController = mapView.getController();
		mapController.setZoom(16); // Zoom 1 is world view
	}
	
	/** This method sets up the bus stops */
	private void setupBusStops() {
		time = new BusStopTimes();
		
		busstops = new BusStop[8];
		locRoth = new BusStop(this.getString(R.string.roth), time.getTime("roth"), 42.50868489190292, -92.45049700140953, gpsProvider);
		busstops[0] = locRoth;
		locOhio = new BusStop(this.getString(R.string.ohio), time.getTime("ohio"), 42.512937827497495, -92.46176898479462, gpsProvider);
		busstops[1] = locOhio;
		locSterling = new BusStop(this.getString(R.string.sterling), time.getTime("sterling"), 42.51246727052621, -92.47239053249359, gpsProvider);
		busstops[2] = locSterling;
		locHillcrest = new BusStop(this.getString(R.string.hillcrest), time.getTime("hillcrest"), 42.503411, -92.473598, gpsProvider);
		busstops[3] = locHillcrest;
		locCampuscts = new BusStop(this.getString(R.string.campuscts), time.getTime("campuscts"), 42.50546385690268, -92.4680507183075, gpsProvider);
		busstops[4] = locCampuscts;
		locHudson = new BusStop(this.getString(R.string.hudson), time.getTime("hudson"), 42.51304953482958, -92.46543556451797, gpsProvider);
		busstops[5] = locHudson;
		locCampus = new BusStop(this.getString(R.string.campus), time.getTime("campus"), 42.51677135081397, -92.45980560779572, gpsProvider);
		busstops[6] = locCampus;
		locSeerley = new BusStop(this.getString(R.string.seerley), time.getTime("seerley"), 42.51602008576133, -92.45587080717087, gpsProvider);
		busstops[7] = locSeerley;
	}
	
	/** This method will get my current location */
	private void getMyLocation() {
		//get the location based service
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		//use gps as our primary location provider
		gpsProvider = locationManager.getProvider("gps").getName();
		
		//request location updates every 15 seconds and distance changed by 20 meters
		locationManager.requestLocationUpdates(gpsProvider, 0, 0, this);
		
		//get our last known location
		location = locationManager.getLastKnownLocation(gpsProvider);
		
		//update our location
		updateLocation(location);
	}
	
	/** This method displays the bus stops on the map */
	private void getOfflineStops() {
		for (int i=0; i<busstops.length; i++) {
			Drawable locStop = null;
			MyItemizedOverlay overlayStop = null;
			String bustime = "";
			if (isRunning) {
				bustime = "Arrives in " + busstops[i].getTime() + " minutes";
			}
			GeoPoint gp = new GeoPoint((int)(busstops[i].getLatitude()*1E6), (int)(busstops[i].getLongitude()*1E6));
			OverlayItem overlayitem = new OverlayItem(gp, busstops[i].getName(), bustime);
			locStop = this.getResources().getDrawable(R.drawable.pin2);	//red marker
			overlayStop = new MyItemizedOverlay(locStop, this);
			
			//set the overlayitem marker
			overlayitem.setMarker(locStop);
			
			//add bus stop to overlay
			overlayStop.addOverlay(overlayitem);

	        //add overlays to map
	        mapOverlays.add(overlayStop);
		}
	}
	
	/** This method displays the bus stops on the map */
	private void getBusStops(BusStop busstop, int stop) {
		Drawable locStop = null;
		MyItemizedOverlay overlayStop = null;
		String bustime = "";
		if (isRunning) {
			bustime = "Arrives in " + busstop.getTime() + " minutes";
		}
		GeoPoint gp = new GeoPoint((int)(busstop.getLatitude()*1E6), (int)(busstop.getLongitude()*1E6));
		OverlayItem overlayitem = new OverlayItem(gp, busstop.getName(), bustime);
		switch (stop){
			case 1:
				locStop = this.getResources().getDrawable(R.drawable.pin2);	//red marker
				overlayStop = new MyItemizedOverlay(locStop, this);
				break;
			case 2:
				locStop = this.getResources().getDrawable(R.drawable.pin3);	//orange marker
				overlayStop = new MyItemizedOverlay(locStop, this);
				break;
			case 3:
				locStop = this.getResources().getDrawable(R.drawable.pin4);	//yellow marker
				overlayStop = new MyItemizedOverlay(locStop, this);
				break;
			case 4:
				locStop = this.getResources().getDrawable(R.drawable.pin5);	//teal marker
				overlayStop = new MyItemizedOverlay(locStop, this);
				break;
			case 5:
				locStop = this.getResources().getDrawable(R.drawable.pin6);	//light-blue marker
				overlayStop = new MyItemizedOverlay(locStop, this);
				break;
			case 6:
				locStop = this.getResources().getDrawable(R.drawable.pin7);	//blue marker
				overlayStop = new MyItemizedOverlay(locStop, this);
				break;
			case 7:
				locStop = this.getResources().getDrawable(R.drawable.pin8);	//purple marker
				overlayStop = new MyItemizedOverlay(locStop, this);
				break;
			case 8:
				locStop = this.getResources().getDrawable(R.drawable.pin9);	//magenta marker
				overlayStop = new MyItemizedOverlay(locStop, this);
				break;
		}
		
		//set the overlayitem marker
		overlayitem.setMarker(locStop);
		
		//add bus stop to overlay
		overlayStop.addOverlay(overlayitem);

        //add overlays to map
        mapOverlays.add(overlayStop);
	}
	
	
	/** This method sets the distance to each bus stop */
	private void getNearestStops() {
		locRoth.setDistance(location.distanceTo(locRoth.getLocation()));
		locOhio.setDistance(location.distanceTo(locOhio.getLocation()));
		locSterling.setDistance(location.distanceTo(locSterling.getLocation()));
		locHillcrest.setDistance(location.distanceTo(locHillcrest.getLocation()));
		locCampuscts.setDistance(location.distanceTo(locCampuscts.getLocation()));
		locHudson.setDistance(location.distanceTo(locHudson.getLocation()));
		locCampus.setDistance(location.distanceTo(locCampus.getLocation()));
		locSeerley.setDistance(location.distanceTo(locSeerley.getLocation()));
		
		//sort our bus stops according to distance
		sortDistances();
	}
	
	/** This method sorts the bus stops according to shortest distance first */
	private void sortDistances() {
		BusStopMap busStopMap = new BusStopMap();

		busStopMap.addStop(locRoth, locRoth.getDistance());
		busStopMap.addStop(locOhio, locOhio.getDistance());
		busStopMap.addStop(locSterling, locSterling.getDistance());
		busStopMap.addStop(locHillcrest, locHillcrest.getDistance());
		busStopMap.addStop(locCampuscts, locCampuscts.getDistance());
		busStopMap.addStop(locHudson, locHudson.getDistance());
		busStopMap.addStop(locCampus, locCampus.getDistance());
		busStopMap.addStop(locSeerley, locSeerley.getDistance());

        Set s = busStopMap.getMap().entrySet();

        // Using iterator in SortedMap 
        Iterator i = s.iterator();

        int rank = 1;
        int marker = 1;
        while(i.hasNext())
        {
            Map.Entry m =(Map.Entry)i.next();

            BusStop busstop = (BusStop)m.getValue();

            TableLayout tl = (TableLayout)findViewById(R.id.tableMain);
            TableRow row = new TableRow(this);
            
            TextView name = new TextView(this);
            name.setText(rank + ". " + busstop.getName());
            
            TextView time = new TextView(this);
            time.setText(busstop.getTime() + " minutes");
            time.setGravity(Gravity.RIGHT);
            
            // add the bus stops to the map
            switch (marker){
            	case 1:
            		getBusStops(busstop, marker);
            		name.setTextColor(Color.argb(255,255,52,51));
                	time.setTextColor(Color.argb(255,255,52,51));
            		break;
            	case 2:
            		getBusStops(busstop, marker);
            		name.setTextColor(Color.argb(255,255,158,21));
                	time.setTextColor(Color.argb(255,255,158,21));
            		break;
            	case 3:
            		getBusStops(busstop, marker);
            		name.setTextColor(Color.argb(255,255,255,21));
                	time.setTextColor(Color.argb(255,255,255,21));
            		break;
            	case 4:
            		getBusStops(busstop, marker);
            		name.setTextColor(Color.argb(255,51,224,225));
                	time.setTextColor(Color.argb(255,51,224,225));
            		break;
            	case 5:
            		getBusStops(busstop, marker);
            		name.setTextColor(Color.argb(255,51,160,225));
                	time.setTextColor(Color.argb(255,51,160,225));
            		break;
            	case 6:
            		getBusStops(busstop, marker);
            		name.setTextColor(Color.argb(255,51,58,225));
                	time.setTextColor(Color.argb(255,51,58,225));
            		break;
            	case 7:
            		getBusStops(busstop, marker);
            		name.setTextColor(Color.argb(255,165,51,225));
                	time.setTextColor(Color.argb(255,165,51,225));
            		break;
            	case 8:
            		getBusStops(busstop, marker);
            		name.setTextColor(Color.argb(255,225,51,171));
                	time.setTextColor(Color.argb(255,225,51,171));
            		break;
            }
            
            // add the bus stops to the view
            tl.addView(row);
            row.addView(name);
            row.addView(time);
            
            rank++;
            marker++;
        }
	}
	
	private void notifyOffline(){
		// prepare the alert box
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        // set the title
        alertbox.setTitle("Notice");
        
        // set the message to display
        alertbox.setMessage("The Panther Shuttle runs between the hours of 7 AM and 5 PM, Monday through Friday. Bus arrival times will not be displayed, but you may still view the bus stop locations and the Panther Shuttle schedule.");

        // add a neutral button to the alert box and assign a click listener
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

            // click listener on the alert box
            public void onClick(DialogInterface arg0, int arg1) {
                // do nothing
            }
        });

        // show it
        alertbox.show();
	}
		
	
	/** LocationListener functions */
		/** Register for the updates when Activity is in foreground */
		@Override
		protected void onResume() {
			super.onResume();
			locationManager.requestLocationUpdates(gpsProvider, 0, 0, this);
		}

		/** Stop the updates when Activity is paused */
		@Override
		protected void onPause() {
			super.onPause();
			locationManager.removeUpdates(this);
		}
		
		/** My location has changed */
		@Override
		public void onLocationChanged(Location location) {
			updateLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		/** This method is called when our location has changed */
		private void updateLocation(Location loc){
			if (loc != null){
				lat = loc.getLatitude();
				lng = loc.getLongitude();
			}
			else
			{ 
				//default location to center of campus
				lat = 42.515085;
				lng = -92.461388;
				location = new Location(gpsProvider);
				location.setLatitude(lat);
				location.setLongitude(lng);
				Toast.makeText(getApplicationContext(), "Error updating location", Toast.LENGTH_LONG).show();
			}
			
			if (myLocation != null) {
				myLocation = null;
				itemizedoverlay.removeOverlay(overlayitem);
				overlayitem = null;
			}
			myLocation = new GeoPoint((int) (lat*1E6), (int) (lng*1E6));			//create a geo point at my location
			overlayitem = new OverlayItem(myLocation, "You Are Here", "");	//display marker at my location
	        drawable.setBounds(-drawable.getIntrinsicWidth() / 2, -drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth() / 2, 0);
		    overlayitem.setMarker(drawable);			//set the marker at my location
	        itemizedoverlay.addOverlay(overlayitem);	//add the marker to the overlay
	        mapController.animateTo(myLocation);				//display the map at my location
	        
	        //add the overlay to the map
	        mapOverlays.add(itemizedoverlay);
		}
	
		
	/** OptionsMenu */
		/** This method creates the options menu */
		@Override
		public boolean onCreateOptionsMenu(Menu menu){
			MenuInflater inflater=getMenuInflater();
			inflater.inflate(R.menu.menu, menu);
			return true;
		}
	    
		/** This method determines which options menu item was selected */
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()){
			case R.id.menuRefresh:
				Intent i = getIntent();
			    overridePendingTransition(0, 0);
			    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			    finish();
	
			    overridePendingTransition(0, 0);
			    startActivity(i);
	    		return true;
			case R.id.menuSchedule:
	    		Intent j = new Intent(this, Schedule.class);
	    		startActivity(j);
	    		return true;
			}
			return false;
		}
}
