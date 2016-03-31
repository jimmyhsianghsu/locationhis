package com.chia.gae.locationhis;
import java.io.IOException;
import javax.servlet.http.*;
@SuppressWarnings("serial")
public class LocationhisServlet extends HttpServlet {
	private UserLocationService service = new UserLocationService();
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		AuthService authService = new AuthService(service);
		try {
			AuthEnum authEnum = authService.doAuth(req, resp);
			System.out.println(this.getClass().getSimpleName() + ':' + authEnum);
			if (authEnum != null){
				String name = authService.getName();
				String password = authService.getPassword();
				resp.setContentType("text/html; charset=utf-8");
				switch (authEnum){
				case getUsers:
					resp.getWriter().print(service.jsonUsers(service.getUsers()));
					break;
				case saveUser:
					name = req.getParameter(UserLocationDao.NAME);
					password = req.getParameter(UserLocationDao.PASSWORD);
					resp.getWriter().print(service.jsonUser(service.saveUser(name, password)));
					break;
				case getUser:
					resp.getWriter().print(service.jsonUser(service.getUser(name, password)));
					break;
				case getLocations:
					resp.getWriter().print(service.jsonLocations(service.getLoactions(name, password)));
					break;
				case saveLocation:
					double lat = Double.parseDouble(req.getParameter(UserLocationDao.LAT));
					double lng = Double.parseDouble(req.getParameter(UserLocationDao.LNG));
					resp.getWriter().print(service.jsonLocation(service.saveLocation(name, password, lat, lng)));
					break;
				case saveUserShortName:
					String shortName = req.getParameter(UserLocationDao.SHORTNAME);
					resp.getWriter().print(service.jsonUser(service.saveUserShortName(name, password, shortName)));
					break;
				case getUserShortName:
					resp.getWriter().print(service.jsonName(service.getUserShortName(name, password)));
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
		String name = authService.getName();
		String password = authService.getPassword();
		if ("getUser".equals(action)){
			resp.getWriter().print(service.jsonUser(service.getUser(name, password)));
		} else if ("getUsers".equals(action)){
			if (isAdmin)
			resp.getWriter().print(service.jsonUsers(service.getUsers()));
			else {
				authService.errorResponse(resp);
				return;
			}
		} else if ("saveUser".equals(action)){
			if (isAdmin){
			name = req.getParameter(UserLocationDao.NAME);
			password = req.getParameter(UserLocationDao.PASSWORD);
			resp.getWriter().print(service.jsonUser(service.saveUser(name, password)));
			} else {
				authService.errorResponse(resp);
				return;
			}
		} else if ("getLocations".equals(action)){
			resp.getWriter().print(service.jsonLocations(service.getLoactions(name, password)));
		} else if ("saveLocation".equals(action)){
			double lat = Double.parseDouble(req.getParameter(UserLocationDao.LAT));
			double lng = Double.parseDouble(req.getParameter(UserLocationDao.LNG));
			resp.getWriter().print(service.jsonLocation(service.saveLocation(name, password, lat, lng)));
		}
	}
}