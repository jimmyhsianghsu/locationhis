package com.chia.gae.locationhis;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@SuppressWarnings("serial")
public class PageServlet extends HttpServlet {
	private UserLocationService service = new UserLocationService();
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		AuthService authService = new AuthService(service);
		try {
			AuthEnum authEnum = authService.doAuth(req, resp);
			System.out.println(this.getClass().getSimpleName() + ':' + authEnum + ',' + req.getParameter("room"));
			if (authEnum != null){
				switch (authEnum){
				case pageIndex:
					req.getRequestDispatcher("/WEB-INF/page/index.html").forward(req, resp);
					break;
				case pageChatroom:
					req.getRequestDispatcher("/WEB-INF/page/chatroom.html").forward(req, resp);
					break;
				case pageRoom:
					String room = req.getParameter("room");
					resp.setHeader("url", req.getRequestURI().replace("/", "") + '?' + req.getQueryString());
					if (room == null || room.trim().isEmpty()){
						req.getRequestDispatcher("/WEB-INF/page/room.html").forward(req, resp);
					} else {
						req.getRequestDispatcher("/WEB-INF/page/chat.html").forward(req, resp);
					}
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
		if (isAdmin){
			if (req.getParameter("index") != null){
				req.getRequestDispatcher("/WEB-INF/page/index.html").forward(req, resp);
				return;
			} else if (req.getParameter("chatroom") != null){
				req.getRequestDispatcher("/WEB-INF/page/chatroom.html").forward(req, resp);
				return;
			} else {
				authService.errorResponse(resp);
				return;
			}
		}
		if (auth){
			if (req.getParameter("chat") != null){
				req.getRequestDispatcher("/WEB-INF/page/chat.html").forward(req, resp);
				return;
			}
		}
	}
}