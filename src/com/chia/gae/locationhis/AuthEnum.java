package com.chia.gae.locationhis;
public enum AuthEnum {
	pageIndex(true, false), pageChatroom(true, false), pageRoom(false, true),
	saveUserShortName(false, true), getUserShortName(false, true),
	logout(false, false),
	getName(false, true),
	getUsers(true, false),
	saveUser(true, false),
	getUser(false, true),
	getLocations(false, true),
	saveLocation(false, true),
	getChatroomByName(true, false),
	getChatroomAll(true, false),
	saveChatroom(true, false),
	addUser(true, false),
	getChatroomByUser(false, true),
	addMessage(false, true),
	getChatMessages(false, true);
	private boolean isAdmin;
	private boolean auth;
	private AuthEnum(boolean isAdmin, boolean auth){
		this.isAdmin = isAdmin;
		this.auth = auth;
	}
	public boolean isAdmin(){return isAdmin;}
	public boolean auth(){return auth;}
}