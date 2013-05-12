package net.x3pro.siteengine.service.extensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.mapping.Collection;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import net.x3pro.siteengine.dao.TextBaseDAO;
import net.x3pro.siteengine.dao.UserDAO;
import net.x3pro.siteengine.domain.NewUser;
import net.x3pro.siteengine.domain.UserBase;
import net.x3pro.siteengine.domain.User;
import net.x3pro.siteengine.domain.WordTranslate;
import net.x3pro.siteengine.service.EngineExtension;
import net.x3pro.siteengine.support.EMail;
import net.x3pro.siteengine.support.ExtensionModelVars;
import net.x3pro.siteengine.support.RequestParametersController;
import net.x3pro.siteengine.support.ResponseJSONObject;
import net.x3pro.siteengine.support.ValueValidator;;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_SESSION)
public class ContentController implements EngineExtension{
	private String extensionName = "content";

	@Override
	public String getExtensionName() {
		return extensionName;
	}
	
	@Override
	public ResponseJSONObject requestJSON(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String requestExt(List<String> pagesURI, ExtensionModelVars extensionModelVars,	HttpServletRequest request) {		
		String result = "";
		for (String pageURI : pagesURI) {
			result = result+"/"+pageURI;
		}
		return result;
	}		
	
	public String requestAPI(HttpServletRequest request){
		return "ERROR:BAD_COMMAND";
	}
	
	public String requestCommand(String[] commandArray, ExtensionModelVars extensionVars, HttpServletRequest request){
		return "redirect:/";
	}
}
