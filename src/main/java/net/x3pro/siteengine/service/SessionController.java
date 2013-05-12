package net.x3pro.siteengine.service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import net.x3pro.siteengine.service.extensions.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_SESSION)
public class SessionController {
	
	@Autowired
	UserController userController;
	
	private Model model;
	private WebApplicationContext context;
	
	public UserController getCurrentUser(){
		return this.userController;
	}
	
	public void setModel(Model model){
		this.model = model;
	}
	
	public WebApplicationContext getContext(){
		return this.context;
	}
	
	
}
