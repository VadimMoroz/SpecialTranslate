package net.x3pro.siteengine.support;

import javax.servlet.http.HttpServletRequest;

public class RequestParametersController {
	
	private HttpServletRequest request;
	
	public RequestParametersController(HttpServletRequest request){
		this.request = request;
	}
	
	public String getRequestParam(String name){
		String result = this.request.getParameter(name);
		if (result==null)
			result = "";
		return result;
	}
	
	/**
	 * @return getRequestParam(name).trim().toLowerCase()
	 */
	public String getPreparedRequestParam(String name){
		return getRequestParam(name).trim().toLowerCase();
	}
	
	public String getPreparedRequestCommand(String command){
		return getRequestParam(command).trim().toUpperCase();
	}
}
