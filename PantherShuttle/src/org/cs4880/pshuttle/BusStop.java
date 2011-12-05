package org.cs4880.pshuttle;

import android.location.Location;

public class BusStop {
	
	private String name = "";
	private double latitude;
	private double longitude;
	private double distance;
	private Location location;
	
	public BusStop(String name, double latitude, double longitude, String provider)
	{
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.location = new Location(provider);
		this.location.setLatitude(latitude);
		this.location.setLongitude(longitude);
	}
	
	public void setName(String inputName)
	{
		name = inputName;
	}
	
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public String getName()
	{
		return name;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
	public Location getLocation()
	{
		return location;
	}
}
