package com.vincent.wearabledemo.Utils;

public class Poi {

	private String pathName;
	private double lat;
	private double lng;
	private String pathInfo;
	private double distance;
	
	public Poi(String pathName, double latitude, double longitude, String pathInfo)
	{
		this.pathName = pathName;
		this.lat = latitude;
		this.lng = longitude;
		this.pathInfo = pathInfo;
	}
	
	public String getPathName() {
		return pathName;
	}
	
	public double getLatitude() {
		return lat;
	}
	
	public double getLongitude() {
		return lng;
	}

	public String getPathInfo() {
		return pathInfo;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getDistance() {
		return distance;
	}
	
}
