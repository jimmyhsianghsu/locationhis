package com.chia.gae.locationhis;
import java.util.Date;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
public abstract class ChatDao {
	protected static final String CHATROOM = "Chatroom";
	protected static final String CHATMESSAGE = "Chatmessage";
	public static final String NAME = "name";
	public static final String USERNAMES = "userNames";
	public static final String MESSAGE = "message";
	public static final String DATE = "date";
	protected static boolean entityComplete(Entity entity){
		return entity != null && entity.getKey() != null && entity.getKey().isComplete();
	}
	protected static boolean chatroomComplete(Chatroom chatroom){
		return chatroom != null && chatroom.getKey() != null && chatroom.getKey().isComplete();
	}
	protected static Chatroom newChatroom(Entity entity){
		if (entityComplete(entity)){
			String name = (String) entity.getProperty(NAME);
			List<String> userNames = (List<String>) entity.getProperty(USERNAMES);
			Chatroom chatroom = new Chatroom(entity.getKey(), name);
			if (userNames != null)
			chatroom.getUserNames().addAll(userNames);
			return chatroom;
		}
		return null;
	}
	protected static Chatmessage newChatmessage(Entity entity){
		if (entityComplete(entity)){
			String name = (String) entity.getProperty(NAME);
			String message = (String) entity.getProperty(MESSAGE);
			Date date = (Date) entity.getProperty(DATE);
			return new Chatmessage(name, message, date);
		}
		return null;
	}
	protected static Entity newChatroomEntity(Chatroom chatroom){
		if (chatroom != null && chatroom.getName() != null){
			Entity entity = new Entity(CHATROOM);
			entity.setProperty(NAME, chatroom.getName());
			entity.setProperty(USERNAMES, chatroom.getUserNames());
			return entity;
		}
		return null;
	}
	protected static Entity newChatmessageEntity(Chatroom chatroom, Chatmessage chatmessage){
		if (chatroomComplete(chatroom)){
			if (chatmessage.getName() != null && chatroom.getUserNames().contains(chatmessage.getName()))
			if (chatmessage != null && chatmessage.getName() != null && chatmessage.getDate() != null){
				Entity entity = new Entity(CHATMESSAGE, chatroom.getKey());
				entity.setProperty(NAME, chatmessage.getName());
				entity.setProperty(MESSAGE, chatmessage.getMessage());
				entity.setProperty(DATE, chatmessage.getDate());
				return entity;
			}
		}
		return null;
	}
	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	protected DatastoreService getDatastore(){return datastore;}
	protected Entity getChatroomEntitByName(String name){
		if (name != null){
			return datastore.prepare(
					new Query(CHATROOM).setFilter(
							new Query.FilterPredicate(NAME, FilterOperator.EQUAL, name))).asSingleEntity();
		}
		return null;
	}
	public abstract Chatroom findChatroomByName(String name);
	public abstract List<Chatroom> findChatroomByUser(String userName);
	public abstract List<Chatroom> findChatroomAll();
	public abstract Chatroom saveChatroom(Chatroom chatroom);
	public abstract Chatroom addUser(Chatroom chatroom, String userName);
	public abstract Chatmessage addMessage(Chatroom chatroom, Chatmessage chatmessage);
	public abstract List<Chatmessage> findMessage(String name, String userName);
	public abstract List<Chatmessage> findMessage(String name, String userName, Date date);
}