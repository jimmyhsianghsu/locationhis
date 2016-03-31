package com.chia.gae.locationhis;
import java.io.IOException;
public class AuthService {
	private static final String ADMIN_NAME = "carbo";
	private static final String ADMIN_PASSWORD = "boop";
	private UserLocationService service;
	private String name;
	private String password;
	public AuthService(UserLocationService service) {
		this.service = service;
	}
	public boolean authRequest(javax.servlet.http.HttpServletRequest req) throws IOException {
		return basicAuth(req.getHeader("Authorization"));
	}
	public void errorResponse(javax.servlet.http.HttpServletResponse resp) throws IOException {
		resp.setHeader("WWW-Authenticate", "BASIC realm=\"users\"");
		resp.sendError(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
	}
	private boolean basicAuth(String auth) throws IOException {
		if (auth != null && auth.toUpperCase().startsWith("BASIC ")){
			String userpassDecoded = new String(org.datanucleus.util.Base64.decode(auth.substring(6)));
			int index = userpassDecoded.indexOf(':');
			name = userpassDecoded.substring(0, index);
			password = userpassDecoded.substring(index + 1);
		}
		System.out.println(this.getClass().getSimpleName() + ':' + name);
		return service.getUser(name, password) != null;
	}
	public boolean isAdmin(){
		return ADMIN_NAME.equals(name) && ADMIN_PASSWORD.equals(password);
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public AuthEnum doAuth(javax.servlet.http.HttpServletRequest req,
			javax.servlet.http.HttpServletResponse resp) throws IOException{
		boolean auth = authRequest(req);
		String action = req.getParameter("action");
		if ((action == null || action.trim().isEmpty()) && !req.getRequestURI().endsWith("page")) return null;
		for (AuthEnum authEnum : AuthEnum.values()){
			if (authEnum.name().equals(action)){
				if ((authEnum.isAdmin() && !isAdmin()) || (authEnum.auth() && !auth)){
					errorResponse(resp);
					throw new RuntimeException();
				}
				return authEnum;
			}
			if (authEnum.name().startsWith("page")){
				if (req.getParameter(authEnum.name().substring(4).toLowerCase()) != null){
					if ((authEnum.isAdmin() && !isAdmin()) || (authEnum.auth() && !auth)){
						errorResponse(resp);
						throw new RuntimeException();
					}
					return authEnum;
				}
			}
		}
		return null;
	}
}