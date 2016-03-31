package com.chia.gae.locationhis;
import com.google.appengine.api.datastore.Key;
public class User {
	private Key key;
	private String name;
	private String password;
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}
	public User(Key key, String name, String password) {
		this(name, password);
		this.key = key;
	}
	public Key getKey() {return key;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
	private String shortName;
	public String getShortName() {return shortName;}
	public void setShortName(String shortName) {this.shortName = shortName;}
}