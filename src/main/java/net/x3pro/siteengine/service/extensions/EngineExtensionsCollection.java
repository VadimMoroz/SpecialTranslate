package net.x3pro.siteengine.service.extensions;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import net.x3pro.siteengine.service.EngineExtension;
import net.x3pro.siteengine.support.ExtensionModelVars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class EngineExtensionsCollection{
	
	@Autowired
	UserController userController;
	
	@Autowired
	TextBaseController textBaseController;
	
	@Autowired
	ContentController contentController;
	
	public EngineExtension getExtension(String name){
		EngineExtension result = null;
		if (name.equals("user"))
			result = userController;
		else if (name.equals("textbase"))
			result = textBaseController;
		else if (name.equals("content"))
			result = contentController;
		return result;
	}		
}
