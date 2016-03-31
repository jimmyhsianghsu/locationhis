package com.chia.gae.locationhis;
import java.util.Date;
public class Chatmessage {
	private String name;
	private String message;
	private Date date;
	public Chatmessage(String name, String message, Date date) {
		super();
		this.name = name;
		this.message = message;
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}