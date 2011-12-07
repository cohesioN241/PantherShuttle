package org.cs4880.panthershuttle;

import java.util.SortedMap;
import java.util.TreeMap;

public class BusStopMap {
	private SortedMap<Double,BusStop> busStopList;
	
	public BusStopMap() {
		busStopList = new TreeMap<Double,BusStop>();
	}
	
	public void addStop(BusStop busstop, Double distance) {
		busStopList.put(distance, busstop);
	}
	
	public void removeStop(Double distance) {
		busStopList.remove(distance);
	}
	
	public void clearStops(){
		busStopList.clear();
	}
	
	public SortedMap<Double,BusStop> getMap(){
		return busStopList;
	}
	
}
