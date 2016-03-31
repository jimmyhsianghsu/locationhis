package com.chia.gae.locationhis;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.QueryResultList;
public class UserLocationDao {
	private static final String USER = "User";
	public static final String NAME = "name";
	public static final String PASSWORD = "password";
	private static final String LOCATION = "Location";
	public static final String LAT = "lat";
	public static final String LNG = "lng";
	public static final String DATE = "date";
	public static final String SHORTNAME = "shortName";
	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	public User findUser(String name){
		if (name != null){
			Query q = new Query(USER).setFilter(new Query.FilterPredicate(NAME, FilterOperator.EQUAL, name));
			PreparedQuery pq = datastore.prepare(q);
			Entity entity = pq.asSingleEntity();
			return newUser(entity);
		}
		return null;
	}
	public List<User> findUsers(){
		List<User> users = null;
		Query q = new Query(USER);
		PreparedQuery pq = datastore.prepare(q);
		QueryResultList<Entity> results = pq.asQueryResultList(FetchOptions.Builder.withDefaults());
		if (results != null && results.size() > 0){
			users = new ArrayList<User>();
			for (Entity entity : results) {
				// users.add(new User((String) entity.getProperty(NAME), null));
				users.add(newUser(entity));
			}
		}
		return users;
	}
	public List<Location> findLocations(User user){
		List<Location> locations = null;
		if (user != null && user.getKey() != null && user.getKey().isComplete()){
			Query q = new Query(LOCATION).setAncestor(user.getKey());
			PreparedQuery pq = datastore.prepare(q);
			QueryResultList<Entity> results = pq.asQueryResultList(FetchOptions.Builder.withDefaults());
			if (results != null && results.size() > 0){
				locations = new ArrayList<Location>();
				for (Entity entity : results) {
					locations.add(newLocation(entity));
				}
			}
		}
		return locations;
	}
	public User saveUser(User user){
		Entity entity = newUserEntity(user);
		if (entity != null){
			Key key = datastore.put(entity);
			if (key != null){
				return newUser(entity);
			}
		}
		return null;
	}
	public Location saveUserLocation(User user, Location location){
		Entity entity = newLocationEntity(user, location);
		if (entity != null){
			Key key = datastore.put(entity);
			if (key != null){
				return newLocation(entity);
			}
		}
		return null;
	}
	private User newUser(Entity entity){
		if (entity != null && entity.getKey() != null && entity.getKey().isComplete()){
			String name = (String) entity.getProperty(NAME);
			String password = (String) entity.getProperty(PASSWORD);
			User user = new User(entity.getKey(), name, password);
			user.setShortName((String) entity.getProperty(SHORTNAME));
			return user;
		}
		return null;
	}
	private Location newLocation(Entity entity){
		if (entity != null && entity.getKey() != null && entity.getKey().isComplete()){
			double lat = (Double) entity.getProperty(LAT);
			double lng = (Double) entity.getProperty(LNG);
			Date date = (Date) entity.getProperty(DATE);
			return new Location(lat, lng, date);
		}
		return null;
	}
	private Entity newUserEntity(User user){
		if (user != null && user.getName() != null && user.getPassword() != null){
			Entity entity = new Entity(USER);
			entity.setProperty(NAME, user.getName());
			entity.setProperty(PASSWORD, user.getPassword());
			return entity;
		}
		return null;
	}
	private Entity newLocationEntity(User user, Location location){
		if (user != null && user.getKey() != null && user.getKey().isComplete()){
			if (location != null && location.getLat() > 0 && location.getLng() > 0){
				Entity entity = new Entity(LOCATION, user.getKey());
				entity.setProperty(LAT, location.getLat());
				entity.setProperty(LNG, location.getLng());
				entity.setProperty(DATE, location.getDate());
				return entity;
			}
		}
		return null;
	}
	public User saveUserShortName(User user, String shortName){
		if (user != null && user.getKey() != null && user.getKey().isComplete()){
			Entity entity = new Entity(user.getKey());
			entity.setProperty(NAME, user.getName());
			entity.setProperty(PASSWORD, user.getPassword());
			entity.setProperty(SHORTNAME, shortName);
			Key key = datastore.put(entity);
			if (key != null){
				return newUser(entity);
			}
		}
		return null;
	}
}