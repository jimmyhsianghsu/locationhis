package com.chia.gae.locationhis;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class UserLocationService {
	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss.SSS";
	private static final java.util.Comparator<Location> DATE_COMPARATOR = new java.util.Comparator<Location>(){
		@Override
		public int compare(Location loc1, Location loc2) {
			java.util.Date date1 = loc1.getDate();
			java.util.Date date2 = loc2.getDate();
			return date1.equals(date2) ? 0 : date1.after(date2) ? 1 : -1;
		}
	};
	private UserLocationDao dao = new UserLocationDao();
	private User getUser(String name){
		if (name != null && !name.trim().isEmpty()){
			return dao.findUser(name.trim());
		}
		return null;
	}
	public User getUser(String name, String password){
		User user = getUser(name);
		if (user != null && user.getPassword().equals(password)){
			return user;
		}
		return null;
	}
	public List<User> getUsers(){return dao.findUsers();}
	public User saveUser(String name, String password){
		if (getUser(name) == null && password != null && !password.trim().isEmpty()){
			password = password.trim();
			return dao.saveUser(new User(name, password));
		}
		return null;
	}
	public List<Location> getLoactions(String name, String password){
		return dao.findLocations(getUser(name, password));
	}
	public Location saveLocation(String name, String password, double lat, double lng){
		return dao.saveUserLocation(getUser(name, password), new Location(lat, lng, new Date()));
	}
	public JSONObject jsonUser(User user){
		return user != null ? JSONObject.fromObject(user) : null;
	}
	public JSONArray jsonUsers(List<User> users){
		return users != null ? JSONArray.fromObject(users) : null;
	}
	public JSONObject jsonLocation(Location location){
		JSONObject jObj = null;
		if (location != null){
			jObj = new JSONObject();
			jObj.put(UserLocationDao.LAT, location.getLat());
			jObj.put(UserLocationDao.LNG, location.getLng());
			java.text.DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			df.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Taipei"));
			jObj.put(UserLocationDao.DATE, df.format(location.getDate()));
		}
		return jObj;
	}
	public JSONArray jsonLocations(List<Location> locations){
		JSONArray jAry = null;
		if (locations != null && locations.size() > 0){
			java.util.Collections.sort(locations, DATE_COMPARATOR);
			jAry = new JSONArray();
			for (Location location : locations){
				jAry.add(jsonLocation(location));
			}
		}
		return jAry;
	}
	public JSONObject jsonName(String name){
		if (name != null && !name.trim().isEmpty()){
			JSONObject jObj = new JSONObject();
			jObj.put(UserLocationDao.NAME, name.trim());
			return jObj;
		}
		return null;
	}
	public User saveUserShortName(String name, String password, String shortName){
		User user = getUser(name, password);
		if (user != null && shortName != null && !shortName.trim().isEmpty()){
			return dao.saveUserShortName(user, shortName.trim());
		}
		return null;
	}
	public String getUserShortName(String name, String password){
		User user = getUser(name, password);
		return user != null ? user.getShortName() : null;
	}
	public String getUserShortName(String name){
		User user = getUser(name);
		return user != null ? user.getShortName() : null;
	}
}