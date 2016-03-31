package com.chia.gae.locationhis;
import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Query.FilterOperator;
public class ChatDaoExt extends ChatDao{
	@Override
	public Chatroom findChatroomByName(String name){
		return newChatroom(getChatroomEntitByName(name));
	}
	@Override
	public List<Chatroom> findChatroomByUser(String userName){
		if (userName != null){
			QueryResultList<Entity> results = getDatastore().prepare(
					new Query(CHATROOM).setFilter(
							new Query.FilterPredicate(USERNAMES, FilterOperator.EQUAL, userName))).asQueryResultList(
									FetchOptions.Builder.withDefaults());
			if (results != null && results.size() > 0){
				List<Chatroom> chatrooms = new ArrayList<Chatroom>();
				for (Entity entity : results) {
					chatrooms.add(newChatroom(entity));
				}
				return chatrooms;
			}
		}
		return null;
	}
	@Override
	public List<Chatroom> findChatroomAll(){
		QueryResultList<Entity> results = getDatastore().prepare(
				new Query(CHATROOM)).asQueryResultList(FetchOptions.Builder.withDefaults());
		if (results != null && results.size() > 0){
			List<Chatroom> chatrooms = new ArrayList<Chatroom>();
			for (Entity entity : results) {
				chatrooms.add(newChatroom(entity));
			}
			return chatrooms;
		}
		return null;
	}
	@Override
	public Chatroom saveChatroom(Chatroom chatroom){
		Entity entity = newChatroomEntity(chatroom);
		if (entity != null){
			Key key = getDatastore().put(entity);
			if (key != null){
				return newChatroom(entity);
			}
		}
		return null;
	}
	@Override
	public Chatroom addUser(Chatroom chatroom, String userName){
		if (chatroomComplete(chatroom)){
			Entity entity = getChatroomEntitByName(chatroom.getName());
			if (entity != null && userName != null){
				List<String> userNames = (List<String>) entity.getProperty(USERNAMES);
				if (userNames == null){
					userNames = new java.util.ArrayList<String>();
				}
				if (!userNames.contains(userName))
				userNames.add(userName);
				entity.setProperty(USERNAMES, userNames);
				Key key = getDatastore().put(entity);
				if (key != null){
					return newChatroom(entity);
				}
			}
		}
		return null;
	}
	@Override
	public Chatmessage addMessage(Chatroom chatroom, Chatmessage chatmessage){
		if (chatroomComplete(chatroom) && chatmessage != null && chatmessage.getName() != null){
			Entity entity = null;
			try {
				entity = getDatastore().get(chatroom.getKey());
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				e.printStackTrace();
			}
			if (entity != null){
				List<String> userNames = (List<String>) entity.getProperty(USERNAMES);
				if (userNames != null && userNames.contains(chatmessage.getName())){
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
		Entity entity = newChatmessageEntity(chatroom, chatmessage);
		if (entity != null){
			Key key = getDatastore().put(entity);
			if (key != null){
				return newChatmessage(entity);
			}
		}
		return null;
	}
	@Override
	public List<Chatmessage> findMessage(String name, String userName){
		if (name != null && userName != null){
			Filter filter = CompositeFilterOperator.and(new Query.FilterPredicate(NAME, FilterOperator.EQUAL, name),
					new Query.FilterPredicate(USERNAMES, FilterOperator.EQUAL, userName));
			Entity chatroom = getDatastore().prepare(new Query(CHATROOM).setFilter(filter)).asSingleEntity();
			if (chatroom != null){
				QueryResultList<Entity> results = getDatastore().prepare(
						new Query(CHATMESSAGE).setAncestor(chatroom.getKey())).asQueryResultList(
								FetchOptions.Builder.withDefaults());
				if (results != null && results.size() > 0){
					List<Chatmessage> chatmessages = new ArrayList<Chatmessage>();
					for (Entity entity : results) {
						chatmessages.add(newChatmessage(entity));
					}
					return chatmessages;
				}
			}
		}
		return null;
	}
	@Override
	public List<Chatmessage> findMessage(String name, String userName, java.util.Date date){
		if (name != null && userName != null){
			if (date == null)
				return findMessage(name, userName);
			Filter filter = CompositeFilterOperator.and(new Query.FilterPredicate(NAME, FilterOperator.EQUAL, name),
					new Query.FilterPredicate(USERNAMES, FilterOperator.EQUAL, userName));
			Entity chatroom = getDatastore().prepare(new Query(CHATROOM).setFilter(filter)).asSingleEntity();
			if (chatroom != null){
				QueryResultList<Entity> results = getDatastore().prepare(
						new Query(CHATMESSAGE).setAncestor(chatroom.getKey()).setFilter(
								new Query.FilterPredicate(DATE, FilterOperator.GREATER_THAN, date))).asQueryResultList(
										FetchOptions.Builder.withDefaults());
				if (results != null && results.size() > 0){
					List<Chatmessage> chatmessages = new ArrayList<Chatmessage>();
					for (Entity entity : results) {
						chatmessages.add(newChatmessage(entity));
					}
					return chatmessages;
				}
			}
		}
		return null;
	}
}