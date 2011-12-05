package org.cs4880.pshuttle;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	private Drawable drawable, drawable2;
	private MyItemizedOverlay itemizedoverlay, itemizedoverlay2;
	private Location location = null;
	private double lat, lng = 0;
	private LocationManager locationManager;
	private String bestProvider = "";
	private BusStopTimes time;
	private BusStop locRoth, locOhio, locSterling, locHillcrest, locCampuscts, locHudson, locCampus, locSeerley;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.maps); // bind the layout to the activity
		
		//setup the map
		setupMap();
		
		//Pull my location
		getMyLocation();
		
		//setup bustop locations
		setupBusStops();
		
		time = new BusStopTimes();
		
        //add bus stops to map
        getBusStops();
        
        //getDistances();
        
        getNextStops();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
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
	
	private void setupBusStops() {
		locRoth = new BusStop(this.getString(R.string.roth), 42.50868489190292, -92.45049700140953, bestProvider);
		locOhio = new BusStop(this.getString(R.string.ohio), 42.512937827497495, -92.46176898479462, bestProvider);
		locSterling = new BusStop(this.getString(R.string.sterling), 42.51246727052621, -92.47239053249359, bestProvider);
		locHillcrest = new BusStop(this.getString(R.string.hillcrest), 42.503411, -92.473598, bestProvider);
		locCampuscts = new BusStop(this.getString(R.string.campuscts), 42.50546385690268, -92.4680507183075, bestProvider);
		locHudson = new BusStop(this.getString(R.string.hudson), 42.51304953482958, -92.46543556451797, bestProvider);
		locCampus = new BusStop(this.getString(R.string.campus), 42.51677135081397, -92.45980560779572, bestProvider);
		locSeerley = new BusStop(this.getString(R.string.seerley), 42.51602008576133, -92.45587080717087, bestProvider);
	}
	
	private void getMyLocation() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		// Get the best provider
		Criteria criteria = new Criteria();
		bestProvider = locationManager.getProvider("gps").getName();
		
		location = locationManager.getLastKnownLocation(bestProvider);
		
		updateLocation(location);
	}
	
	private void getBusStops() {
		drawable2 = this.getResources().getDrawable(R.drawable.pin2);	//marker for bus stops
		itemizedoverlay2 = new MyItemizedOverlay(drawable2, this);
		
        //ROTH Northeast Lot
		GeoPoint gpRoth = new GeoPoint((int)(locRoth.getLatitude()*1E6), (int)(locRoth.getLongitude()*1E6));
		OverlayItem overlayRoth = new OverlayItem(gpRoth, locRoth.getName(), this.getString(R.string.arrives) + time.getRoth() + " minutes");
		overlayRoth.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayRoth);
		
        //27th and Ohio St. (Curris Business Building)
        GeoPoint gpOhio = new GeoPoint((int)(locOhio.getLatitude()*1E6), (int)(locOhio.getLongitude()*1E6));
		OverlayItem overlayOhio = new OverlayItem(gpOhio, locOhio.getName(), this.getString(R.string.arrives) + time.getOhio() + " minutes");
		overlayOhio.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayOhio);
        
        //Sterling Apts
        GeoPoint gpSterling = new GeoPoint((int)(locSterling.getLatitude()*1E6), (int)(locSterling.getLongitude()*1E6));
		OverlayItem overlayUVMSter = new OverlayItem(gpSterling, locSterling.getName(), this.getString(R.string.arrives) + time.getSterling() + " minutes");
        overlayUVMSter.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayUVMSter);
        
        //Hillcrest Apts
        GeoPoint gpHillcrest = new GeoPoint((int)(locHillcrest.getLatitude()*1E6), (int)(locHillcrest.getLongitude()*1E6));
		OverlayItem overlayHillcrest = new OverlayItem(gpHillcrest, locHillcrest.getName(), this.getString(R.string.arrives) + time.getHillcrest() + " minutes");
        overlayHillcrest.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayHillcrest);
        
        //Campus Court
        GeoPoint gpCampCts = new GeoPoint((int)(locCampuscts.getLatitude()*1E6), (int)(locCampuscts.getLongitude()*1E6));
		OverlayItem overlayCampCts = new OverlayItem(gpCampCts, locCampuscts.getName(), this.getString(R.string.arrives) + time.getCampuscts() + " minutes");
        overlayCampCts.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayCampCts);
        
        //27th St. and Hudson Rd
		GeoPoint gpHudson = new GeoPoint((int)(locHudson.getLatitude()*1E6), (int)(locHudson.getLongitude()*1E6));
		OverlayItem overlayHudson = new OverlayItem(gpHudson, locHudson.getName(), this.getString(R.string.arrives) + time.getHudson() + " minutes");
        overlayHudson.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayHudson);
        
        //23rd and Campus Streets
        GeoPoint gpW23C = new GeoPoint((int)(locCampus.getLatitude()*1E6), (int)(locCampus.getLongitude()*1E6));
		OverlayItem overlayW23C = new OverlayItem(gpW23C, locCampus.getName(), this.getString(R.string.arrives) + time.getCampus() + " minutes");
        overlayW23C.setMarker(drawable2);
        itemizedoverlay.addOverlay(overlayW23C);
        
        //Seerley
        GeoPoint gpSeerly = new GeoPoint((int)(locSeerley.getLatitude()*1E6), (int)(locSeerley.getLongitude()*1E6));
		OverlayItem overlaySeerly = new OverlayItem(gpSeerly, locSeerley.getName(), this.getString(R.string.arrives) + time.getSeerley() + " minutes");
        overlaySeerly.setMarker(drawable2);
        itemizedoverlay.addOverlay(overlaySeerly);
        
        //add overlays to map
        mapOverlays.add(itemizedoverlay2);
	}
	
	private void getDistances() {
		locRoth.setDistance(location.distanceTo(locRoth.getLocation()));
		locOhio.setDistance(location.distanceTo(locOhio.getLocation()));
		locSterling.setDistance(location.distanceTo(locSterling.getLocation()));
		locHillcrest.setDistance(location.distanceTo(locHillcrest.getLocation()));
		locCampuscts.setDistance(location.distanceTo(locCampuscts.getLocation()));
		locHudson.setDistance(location.distanceTo(locHudson.getLocation()));
		locCampus.setDistance(location.distanceTo(locCampus.getLocation()));
		locSeerley.setDistance(location.distanceTo(locSeerley.getLocation()));
		
		sortDistances();
	}
	
	private void sortDistances() {
		BusStopMap busStopMap = new BusStopMap();

		busStopMap.addStop(locRoth.getName(), locRoth.getDistance());
		busStopMap.addStop(locOhio.getName(), locOhio.getDistance());
		busStopMap.addStop(locSterling.getName(), locSterling.getDistance());
		busStopMap.addStop(locHillcrest.getName(), locHillcrest.getDistance());
		busStopMap.addStop(locCampuscts.getName(), locCampuscts.getDistance());
		busStopMap.addStop(locHudson.getName(), locHudson.getDistance());
		busStopMap.addStop(locCampus.getName(), locCampus.getDistance());
		busStopMap.addStop(locSeerley.getName(), locSeerley.getDistance());

        Set s = busStopMap.getMap().entrySet();

        // Using iterator in SortedMap 
        Iterator i = s.iterator();

        while(i.hasNext())
        {
            Map.Entry m =(Map.Entry)i.next();

            double key = (Double)m.getKey();
            String value = (String)m.getValue();

            System.out.println("Key :"+key+"  value :"+value);
        }
	}
	
	private void getNextStops() {
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
        	stopOne = locRoth.getName();
        	minOne = time.getRoth();
        	stopTwo = locOhio.getName();
        	minTwo = time.getOhio();
        	stopThree = locSterling.getName();
        	minThree = time.getSterling();
        } else if ((minute >= 4 && minute < 10) || (minute >= 34 && minute < 40)) {
        	//	27th and Ohio St
        	stopOne = locOhio.getName();
        	minOne = time.getOhio();
        	stopTwo = locSterling.getName();
        	minTwo = time.getSterling();
        	stopThree = locHillcrest.getName();
        	minThree = time.getHillcrest();
        } else if ((minute >= 10 && minute < 13) || (minute >= 40 && minute < 43)) {
        	//	Sterling Apts
        	stopOne = locSterling.getName();
        	minOne = time.getSterling();
        	stopTwo = locHillcrest.getName();
        	minTwo = time.getHillcrest();
        	stopThree = locCampuscts.getName();
        	minThree = time.getCampuscts();
        } else if ((minute >= 13 && minute < 19) || (minute >= 43 && minute < 49)) {
        	// 	Hillcrest Apts
        	stopOne = locHillcrest.getName();
        	minOne = time.getHillcrest();
        	stopTwo = locCampuscts.getName();
        	minTwo = time.getCampuscts();
        	stopThree = locHudson.getName();
        	minThree = time.getHudson();
        } else if ((minute >= 19 && minute < 21) || (minute >= 49 && minute < 51)) {
        	//	Campus Court Apts
        	stopOne = locCampuscts.getName();
        	minOne = time.getCampuscts();
        	stopTwo = locHudson.getName();
        	minTwo = time.getHudson();
        	stopThree = locCampus.getName();
        	minThree = time.getCampus();
        } else if ((minute >= 21 && minute < 23) || (minute >= 51 && minute < 53)) {
        	//	27th St. and Hudson Rd
        	stopOne = locHudson.getName();
        	minOne = time.getHudson();
        	stopTwo = locCampus.getName();
        	minTwo = time.getCampus();
        	stopThree = locSeerley.getName();
        	minThree = time.getSeerley();
        } else if ((minute >= 23 && minute < 26) || (minute >= 53 && minute < 56)) {
        	//	23rd and Campus Streets
        	stopOne = locCampus.getName();
        	minOne = time.getCampus();
        	stopTwo = locSeerley.getName();
        	minTwo = time.getSeerley();
        	stopThree = locRoth.getName();
        	minThree = time.getRoth();
        } else if ((minute >= 26 && minute < 28) || (minute >= 56 && minute < 58)) {
        	//	Bus Stop by Seerley
        	stopOne = locSeerley.getName();
        	minOne = time.getSeerley();
        	stopTwo = locRoth.getName();
        	minTwo = time.getRoth();
        	stopThree = locOhio.getName();
        	minThree = time.getOhio();
        }
        
        nextOne.setText(stopOne);
        nextOneTime.setText(minOne + " minutes");
        nextTwo.setText(stopTwo);
        nextTwoTime.setText(minTwo + " minutes");
        nextThree.setText(stopThree);
        nextThreeTime.setText(minThree + " minutes");
	}
		
	
	/** LocationListener functions */
		/** Register for the updates when Activity is in foreground */
		@Override
		protected void onResume() {
			super.onResume();
			locationManager.requestLocationUpdates(bestProvider, 0, 0, this);
		}

		/** Stop the updates when Activity is paused */
		@Override
		protected void onPause() {
			super.onPause();
			locationManager.removeUpdates(this);
		}
		
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
		
		private double getLatitude() {
			return lat;
		}
		
		private double getLongitude() {
			return lng;
		}
		
		private void updateLocation(Location loc){
			if (loc != null){
				lat = loc.getLatitude();
				lng = loc.getLongitude();
			}
			else
			{ 
				lat = 0;
				lng = 0;
				Toast.makeText(getApplicationContext(), "Error updating location", Toast.LENGTH_LONG).show();
			}
			
			lat = (getLatitude()*1E6);		//get my latitude
			lng = (getLongitude()*1E6);		//get my longitude
			GeoPoint point = new GeoPoint((int) lat, (int) lng);			//create a geo point at my location
			OverlayItem overlayitem = new OverlayItem(point, "You Are Here", "");	//display marker at my location
	        drawable.setBounds(-drawable.getIntrinsicWidth() / 2, -drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth() / 2, 0);
		    overlayitem.setMarker(drawable);			//set the marker at my location
	        itemizedoverlay.addOverlay(overlayitem);	//add the marker to the overlay
	        mapController.animateTo(point);				//display the map at my location
	        
	        //add the overlay to the map
	        mapOverlays.add(itemizedoverlay);
		}
	
		
	/** OptionsMenu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
    
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
