package com.chia.gae.locationhis;
import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.Key;
public class Chatroom {
	private Key key;
	private String name;
	private List<String> userNames = new ArrayList<String>();
	public Chatroom(String name){
		this.name = name;
	}
	public Chatroom(Key key, String name) {
		this(name);
		this.key = key;
	}
	public Key getKey() {
		return key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getUserNames() {
		return userNames;
	}
}