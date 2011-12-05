package org.cs4880.panthershuttle;

import java.util.SortedMap;
import java.util.TreeMap;

public class BusStopMap {
	private SortedMap<Double,String> busStopList;
	
	public BusStopMap() {
		busStopList = new TreeMap<Double,String>();
	}
	
	public void addStop(String stopName, Double distance) {
		busStopList.put(distance, stopName);
	}
	
	public void removeStop(Double distance) {
		busStopList.remove(distance);
	}
	
	public String getClosestStop(){
		return busStopList.get(busStopList.firstKey());
	}
	
	public void clearStops(){
		busStopList.clear();
	}
	
	public SortedMap<Double,String> getMap(){
		return busStopList;
	}
	
}
