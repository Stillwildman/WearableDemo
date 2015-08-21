package com.vincent.wearabledemo.utils;

public class Poi {

	public String PATH_NAME;
	public double LAT;
	public double LNG;
	public String PATH_INFO;
	public double DISTANCE;
	
	public Poi(String pathName, double latitude, double longitude, String pathInfo)
	{
		PATH_NAME = pathName;
		LAT = latitude;
		LNG = longitude;
		PATH_INFO = pathInfo;
	}
	
	public void setDistance(double distance) {
		DISTANCE = distance;
	}
}
