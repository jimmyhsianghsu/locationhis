package com.chia.gae.locationhis;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class ChatService {
	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss.SSS";
	private static final java.util.Comparator<Chatmessage> DATE_COMPARATOR = new java.util.Comparator<Chatmessage>(){
		@Override
		public int compare(Chatmessage cm1, Chatmessage cm2) {
			java.util.Date date1 = cm1.getDate();
			java.util.Date date2 = cm2.getDate();
			return date1.equals(date2) ? 0 : date1.after(date2) ? 1 : -1;
		}
	};
	private static final java.util.Comparator<Chatroom> NAME_COMPARATOR = new java.util.Comparator<Chatroom>(){
		@Override
		public int compare(Chatroom cm1, Chatroom cm2) {
			return cm1.getName().compareTo(cm2.getName());
		}
	};
	private ChatDao dao = new ChatDaoExt();
	public Chatroom getChatroomByName(String name){
		return dao.findChatroomByName(name);
	}
	public List<Chatroom> getChatroomByUser(String userName){
		return dao.findChatroomByUser(userName);
	}
	public List<Chatroom> getChatroomAll(){
		return dao.findChatroomAll();
	}
	public Chatroom saveChatroom(String name){
		if (getChatroomByName(name) == null){
			return dao.saveChatroom(new Chatroom(name));
		}
		return null;
	}
	public Chatroom addUser(String name, String userName){
		return dao.addUser(getChatroomByName(name), userName);
	}
	public Chatmessage addMessage(String name, String userName, String message){
		return dao.addMessage(getChatroomByName(name), new Chatmessage(userName, message, new java.util.Date()));
	}
	public List<Chatmessage> getChatMessages(String name, String userName){
		return dao.findMessage(name, userName);
	}
	public List<Chatmessage> getChatMessages(String name, String userName, String strDate){
		java.text.DateFormat df = new java.text.SimpleDateFormat(DATE_FORMAT);
		df.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Taipei"));
		java.util.Date date = null;
		if (strDate != null && !strDate.trim().isEmpty()){
			try {
				date = df.parse(strDate);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
		return dao.findMessage(name, userName, date);
	}
	public JSONObject jsonChatroom(Chatroom chatroom){
		JSONObject jObj = null;
		if (chatroom != null){
			jObj = new JSONObject();
			jObj.put(ChatDao.NAME, chatroom.getName());
			jObj.put(ChatDao.USERNAMES, JSONArray.fromObject(chatroom.getUserNames()));
		}
		return jObj;
	}
	public JSONObject jsonChatMessage(Chatmessage chatmessage){
		JSONObject jObj = null;
		if (chatmessage != null){
			jObj = new JSONObject();
			jObj.put(ChatDao.NAME, chatmessage.getName());
			jObj.put(ChatDao.MESSAGE, chatmessage.getMessage());
			java.text.DateFormat df = new java.text.SimpleDateFormat(DATE_FORMAT);
			df.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Taipei"));
			jObj.put(ChatDao.DATE, df.format(chatmessage.getDate()));
		}
		return jObj;
	}
	public JSONArray jsonChatrooms(List<Chatroom> chatrooms){
		JSONArray jAry = null;
		if (chatrooms != null && chatrooms.size() > 0){
			java.util.Collections.sort(chatrooms, NAME_COMPARATOR);
			jAry = new JSONArray();
			for (Chatroom chatroom : chatrooms){
				jAry.add(jsonChatroom(chatroom));
			}
		}
		return jAry;
	}
	public JSONArray jsonChatMessages(List<Chatmessage> chatmessages){
		JSONArray jAry = null;
		if (chatmessages != null && chatmessages.size() > 0){
			java.util.Collections.sort(chatmessages, DATE_COMPARATOR);
			jAry = new JSONArray();
			for (Chatmessage chatmessage : chatmessages){
				jAry.add(jsonChatMessage(chatmessage));
			}
		}
		return jAry;
	}
}