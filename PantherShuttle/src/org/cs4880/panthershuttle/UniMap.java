package org.cs4880.panthershuttle;

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
	private Drawable drawable, drawable2;
	private MyItemizedOverlay itemizedoverlay, itemizedoverlay2;
	private Location location = null;
	private GeoPoint myLocation = null;
	private OverlayItem overlayitem = null;
	private double lat, lng = 0;
	private LocationManager locationManager;
	private String gpsProvider = "";
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
		
        //add bus stops to map
        getBusStops();
        
        //get the nearest bus stops
        getNearestStops();
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
		time = new BusStopTimes();
		
		locRoth = new BusStop(this.getString(R.string.roth), time.getTime("roth"), 42.50868489190292, -92.45049700140953, gpsProvider);
		locOhio = new BusStop(this.getString(R.string.ohio), time.getTime("ohio"), 42.512937827497495, -92.46176898479462, gpsProvider);
		locSterling = new BusStop(this.getString(R.string.sterling), time.getTime("sterling"), 42.51246727052621, -92.47239053249359, gpsProvider);
		locHillcrest = new BusStop(this.getString(R.string.hillcrest), time.getTime("hillcrest"), 42.503411, -92.473598, gpsProvider);
		locCampuscts = new BusStop(this.getString(R.string.campuscts), time.getTime("campuscts"), 42.50546385690268, -92.4680507183075, gpsProvider);
		locHudson = new BusStop(this.getString(R.string.hudson), time.getTime("hudson"), 42.51304953482958, -92.46543556451797, gpsProvider);
		locCampus = new BusStop(this.getString(R.string.campus), time.getTime("campus"), 42.51677135081397, -92.45980560779572, gpsProvider);
		locSeerley = new BusStop(this.getString(R.string.seerley), time.getTime("seerley"), 42.51602008576133, -92.45587080717087, gpsProvider);
	}
	
	private void getMyLocation() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		gpsProvider = locationManager.getProvider("gps").getName();
		
		locationManager.requestLocationUpdates(gpsProvider, 15000, 20, this);
		
		location = locationManager.getLastKnownLocation(gpsProvider);
		
		updateLocation(location);
	}
	
	private void getBusStops() {
		drawable2 = this.getResources().getDrawable(R.drawable.pin2);	//marker for bus stops
		itemizedoverlay2 = new MyItemizedOverlay(drawable2, this);
		
        //ROTH Northeast Lot
		GeoPoint gpRoth = new GeoPoint((int)(locRoth.getLatitude()*1E6), (int)(locRoth.getLongitude()*1E6));
		OverlayItem overlayRoth = new OverlayItem(gpRoth, locRoth.getName(), "Arrives in " + locRoth.getTime() + " minutes");
		overlayRoth.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayRoth);
		
        //27th and Ohio St. (Curris Business Building)
        GeoPoint gpOhio = new GeoPoint((int)(locOhio.getLatitude()*1E6), (int)(locOhio.getLongitude()*1E6));
		OverlayItem overlayOhio = new OverlayItem(gpOhio, locOhio.getName(), "Arrives in " + locOhio.getTime() + " minutes");
		overlayOhio.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayOhio);
        
        //Sterling Apts
        GeoPoint gpSterling = new GeoPoint((int)(locSterling.getLatitude()*1E6), (int)(locSterling.getLongitude()*1E6));
		OverlayItem overlayUVMSter = new OverlayItem(gpSterling, locSterling.getName(), "Arrives in " + locSterling.getTime() + " minutes");
        overlayUVMSter.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayUVMSter);
        
        //Hillcrest Apts
        GeoPoint gpHillcrest = new GeoPoint((int)(locHillcrest.getLatitude()*1E6), (int)(locHillcrest.getLongitude()*1E6));
		OverlayItem overlayHillcrest = new OverlayItem(gpHillcrest, locHillcrest.getName(), "Arrives in " + locHillcrest.getTime() + " minutes");
        overlayHillcrest.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayHillcrest);
        
        //Campus Court
        GeoPoint gpCampCts = new GeoPoint((int)(locCampuscts.getLatitude()*1E6), (int)(locCampuscts.getLongitude()*1E6));
		OverlayItem overlayCampCts = new OverlayItem(gpCampCts, locCampuscts.getName(), "Arrives in " + locCampuscts.getTime() + " minutes");
        overlayCampCts.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayCampCts);
        
        //27th St. and Hudson Rd
		GeoPoint gpHudson = new GeoPoint((int)(locHudson.getLatitude()*1E6), (int)(locHudson.getLongitude()*1E6));
		OverlayItem overlayHudson = new OverlayItem(gpHudson, locHudson.getName(), "Arrives in " + locHudson.getTime() + " minutes");
        overlayHudson.setMarker(drawable2);
        itemizedoverlay2.addOverlay(overlayHudson);
        
        //23rd and Campus Streets
        GeoPoint gpW23C = new GeoPoint((int)(locCampus.getLatitude()*1E6), (int)(locCampus.getLongitude()*1E6));
		OverlayItem overlayW23C = new OverlayItem(gpW23C, locCampus.getName(), "Arrives in " + locCampus.getTime() + " minutes");
        overlayW23C.setMarker(drawable2);
        itemizedoverlay.addOverlay(overlayW23C);
        
        //Seerley
        GeoPoint gpSeerly = new GeoPoint((int)(locSeerley.getLatitude()*1E6), (int)(locSeerley.getLongitude()*1E6));
		OverlayItem overlaySeerly = new OverlayItem(gpSeerly, locSeerley.getName(), "Arrives in " + locSeerley.getTime() + " minutes");
        overlaySeerly.setMarker(drawable2);
        itemizedoverlay.addOverlay(overlaySeerly);
        
        //add overlays to map
        mapOverlays.add(itemizedoverlay2);
	}
	
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
            
            tl.addView(row);
            row.addView(name);
            row.addView(time);
            
            rank++;
        }
	}
	
	private void getNearestStops() {
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
		
	
	/** LocationListener functions */
		/** Register for the updates when Activity is in foreground */
		@Override
		protected void onResume() {
			super.onResume();
			locationManager.requestLocationUpdates(gpsProvider, 15000, 20, this);
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
