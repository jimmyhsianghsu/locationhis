package com.chia.gae.locationhis;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@SuppressWarnings("serial")
public class ChatroomServlet extends HttpServlet {
	private ChatService chatService = new ChatService();
	private UserLocationService service = new UserLocationService();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AuthService authService = new AuthService(service);
		try {
			AuthEnum authEnum = authService.doAuth(req, resp);
			if (authEnum != null){
				String userName = authService.getName();
				String name = req.getParameter("name");
				if (name != null) name = java.net.URLDecoder.decode(name, "UTF-8");
				String message = req.getParameter("message");
				if (message != null) message = new String(message.getBytes("UTF-8"), "UTF-8"); // "ISO-8859-1"
				String strDate = req.getParameter("date");
				System.out.println(this.getClass().getSimpleName() + ':' + authEnum + ',' + name + ',' + strDate);
				resp.setContentType("text/html; charset=utf-8");
				switch (authEnum){
				case getChatroomByName:
					resp.getWriter().print(chatService.jsonChatroom(chatService.getChatroomByName(name)));
					break;
				case getChatroomAll:
					resp.getWriter().print(chatService.jsonChatrooms(chatService.getChatroomAll()));
					break;
				case saveChatroom:
					resp.getWriter().print(chatService.jsonChatroom(chatService.saveChatroom(name)));
					break;
				case addUser:
					resp.getWriter().print(chatService.jsonChatroom(chatService.addUser(name, message)));
					break;
				case getChatroomByUser:
					resp.getWriter().print(chatService.jsonChatrooms(getChatroomByUser(userName)));
					break;
				case addMessage:
					resp.getWriter().print(chatService.jsonChatMessage(chatService.addMessage(name, userName, message)));
					break;
				case getChatMessages:
					if (!rightRoom(name, userName)){
						resp.sendRedirect("page?room&wrongRoom");
						return;
					}
					resp.getWriter().print(chatService.jsonChatMessages(getChatMessages(name, userName, strDate)));
					break;
				case getName:
					resp.getWriter().print(service.jsonName(userName));
					break;
				case logout:
					// resp.sendRedirect("/?logout");
					resp.setHeader("Location", "/?logout");
					authService.errorResponse(resp);
					break;
				default:
				}
				return;
			}
		} catch (RuntimeException re){
			return;
		}
		boolean auth = authService.authRequest(req);
		boolean isAdmin = authService.isAdmin();
		if (!auth && !isAdmin){
			authService.errorResponse(resp);
			return;
		}
		resp.setContentType("text/html");
		String action = req.getParameter("action");
		String userName = authService.getName();
		String name = req.getParameter("name");
		String message = req.getParameter("message");
		Object obj = null;
		if ("getChatroomByName".equals(action)){
			if (isAdmin){
				obj = chatService.jsonChatroom(chatService.getChatroomByName(name));
			} else {
				authService.errorResponse(resp);
				return;
			}
		} else if ("getChatroomAll".equals(action)){
			if (isAdmin){
				obj = chatService.jsonChatrooms(chatService.getChatroomAll());
			} else {
				authService.errorResponse(resp);
				return;
			}
		} else if ("saveChatroom".equals(action)){
			if (isAdmin){
				obj = chatService.jsonChatroom(chatService.saveChatroom(name));
			} else {
				authService.errorResponse(resp);
				return;
			}
		} else if ("addUser".equals(action)){
			if (isAdmin){
				obj = chatService.jsonChatroom(chatService.addUser(name, message));
			} else {
				authService.errorResponse(resp);
				return;
			}
		} else if ("getChatroomByUser".equals(action)){
			obj = chatService.jsonChatrooms(chatService.getChatroomByUser(userName));
		} else if ("addMessage".equals(action)){
			obj = chatService.jsonChatMessage(chatService.addMessage(name, userName, message));
		} else if ("getChatMessages".equals(action)){
			obj = chatService.jsonChatMessages(chatService.getChatMessages(name, userName));
		}
		resp.getWriter().print(obj);
	}
	private boolean rightRoom(String name, String userName){
		java.util.List<Chatroom> rooms = chatService.getChatroomByUser(userName);
		if(rooms != null)
			for (Chatroom room :rooms)
				if(room.getName().equals(name))
					return true;
		return false;
	}
	private java.util.List<Chatmessage> getChatMessages(String name, String userName, String strDate){
		java.util.List<Chatmessage> chatMessages = chatService.getChatMessages(name, userName, strDate);
		if(chatMessages != null && chatMessages.size() > 0){
			java.util.Map<String, String> map = new java.util.HashMap<String, String>();
			for (Chatmessage message : chatMessages){
				String user = message.getName();
				if (!map.containsKey(user))
					map.put(user, service.getUserShortName(user));
				if (!userName.equals(user))
					message.setName(map.get(user));
			}
		}
		return chatMessages;
	}
	private java.util.List<Chatroom> getChatroomByUser(String userName){
		java.util.List<Chatroom> rooms = chatService.getChatroomByUser(userName);
		if (rooms != null && rooms.size() > 0){
			java.util.Map<String, String> map = new java.util.HashMap<String, String>();
			for (Chatroom room : rooms){
				java.util.List<String> users = room.getUserNames();
				if (users != null && users.size() > 0){
					for (int i = 0; i < users.size(); i++){
						String user = users.get(i);
						if (!map.containsKey(user))
							map.put(user, service.getUserShortName(user));
						users.set(i, user + " (" + map.get(user) + ')');
					}
				}
			}
		}
		return rooms;
	}
}