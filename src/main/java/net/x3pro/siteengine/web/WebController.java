package net.x3pro.siteengine.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import net.x3pro.siteengine.dao.UserDAO;
import net.x3pro.siteengine.service.EngineController;
import net.x3pro.siteengine.service.SiteDecorator;
import net.x3pro.siteengine.service.SessionController;
import net.x3pro.siteengine.service.EngineSupport;
import net.x3pro.siteengine.service.extensions.UserController;
import net.x3pro.siteengine.support.ResponseJSONObject;

import org.hibernate.annotations.Parameter;
import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import java.util.HashMap;

@Controller
public class WebController {
	
	@Autowired
	EngineController engineController;
	
	@RequestMapping(value = "/resetpassword")
	public String resetpasswordPage(Model model, HttpServletRequest request,
									@RequestParam(value = "key", defaultValue="") String key) {
		return "redirect:/?command=user.resetpassword_s1&key="+key;
	}
	
	@RequestMapping(value = "/api/{extName}")
	@ResponseBody
	public String requestAPI(@PathVariable("extName") String extName, HttpServletRequest request){		
		return engineController.requestApi(extName, request);		
	}	
	
	@RequestMapping(value = "/json/{extName}")
	@ResponseBody
	public ResponseJSONObject requestJSON(@PathVariable("extName") String extName, HttpServletRequest request){
		return engineController.requestJSON(extName, request);		
	}
	
	@RequestMapping("/ext/{extName}")	
	public String requestExt(@PathVariable("extName") String extName,  @RequestParam("page") String page, Model model){		
		return engineController.requestExt(extName,  page, model);		
	}
	
	@RequestMapping("/**")		
	public String allPages(Model model, HttpServletRequest request){				
		return engineController.request(request.getRequestURI(), model, request);
	}
	
	@RequestMapping(value = "/requestheaders")
	@ResponseBody
	public String requestHeaders(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaderNames();
		String result = "";
		while (headers.hasMoreElements()){
			String name = (String)headers.nextElement();
			result = result + name + " = "+request.getHeader(name)+"\n<br>";
		}
		return result;
	}
}
