package com.chia.gae.locationhis;
import java.util.Date;
public class Location {
	private double lat;
	private double lng;
	private Date date;
	public Location(double lat, double lng, Date date) {
		this.lat = lat;
		this.lng = lng;
		this.date = date;
	}
	public double getLat() {return lat;}
	public void setLat(double lat) {this.lat = lat;}
	public double getLng() {return lng;}
	public void setLng(double lng) {this.lng = lng;}
	public Date getDate() {return date;}
	public void setDate(Date date) {this.date = date;}
}