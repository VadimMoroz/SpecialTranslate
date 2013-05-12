package net.x3pro.siteengine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.AttributeOverride;
import javax.persistence.Entity;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;



import net.x3pro.siteengine.domain.SiteMenu;
import net.x3pro.siteengine.domain.SitePage;
import net.x3pro.siteengine.service.extensions.UserController;
import net.x3pro.siteengine.support.ExtensionModelVars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_REQUEST)
public class SiteDecorator {
	
	private String command;
	private String redirectAdr;
	private HashMap<String, String> pageVar = new HashMap<String, String>();
	private HttpServletRequest request;
	private HashMap<String, String> parameters;
	private SitePage sitePage = null;
	private List<String> pagesURI;
	private HashMap<String, ExtensionModelVars> extensionsVars = new HashMap<String, ExtensionModelVars>();
	
	@Autowired
	EngineController engineController;
	
	@Autowired
	SessionController sessionController;
	
	@Autowired
	UserController userController;	
	
	public void setParameters(HashMap<String, String> params) {
		this.parameters = params;
	}
	
	public String getParam(String name){		
		String result;
		result = parameters.get(name);
		if (result==null)
			result = "";
		return result;
	}
	
	public List<SiteMenu> getMenu(String prefix){		
		boolean roleAdmin = false;
		UserController currentUser = sessionController.getCurrentUser();
		boolean isAuthorized = currentUser.getAuthorized();
		if (isAuthorized){
			roleAdmin = currentUser.hasRole(UserController.ROLE_ADMIN);
		}
		return engineController.getMenus(prefix);
	}

	public UserController getCurUser() {
		return userController;
	}
	
	public String getSessionId(){
		String result = "";
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().toUpperCase().equals("JSESSIONID")){
				result = cookie.getValue();
				break;
			}
		}
		return result;
	}
	
	public void loadExtension(String extName, HashMap<String, String> parameters){
		
	}

	///////////////////////// Getters & Setters
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getRedirectAdr() {
		return redirectAdr;
	}

	public void setRedirectAdr(String redirectAdr) {
		this.redirectAdr = redirectAdr;
	}

	public void addPageVar(String key, String value) {
		pageVar.put(key, value);
	}
	
	public HashMap<String, String> getPageVar() {
		return pageVar;
	}
	
	public void setHttpRequest(HttpServletRequest request){
		this.request = request;
	}
	
	public HttpServletRequest getHttpRequest() {
		return request;
	}
	
	public void setSitePage(SitePage sitePage) {
		this.sitePage = sitePage;
	}
	
	public SitePage getPage() {
		return sitePage;
	}
	
	public void setPagesURI(List<String> pagesURI) {
		this.pagesURI = pagesURI;
	}
	
	public List<String> getPagesURI() {
		List<String> result = new ArrayList<String>();
		result.addAll(pagesURI);
		return result;
	}

	public void addExtensionVars(String name, ExtensionModelVars extensionModelVars){
		extensionsVars.put(name, extensionModelVars);
	}
	
	public HashMap<String, ExtensionModelVars> getExt() {
		return extensionsVars;
	}
	
	public String getContextPath(){
		return engineController.getContextPath();
	}
}
