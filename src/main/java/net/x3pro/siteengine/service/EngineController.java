package net.x3pro.siteengine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import net.x3pro.siteengine.domain.SiteMenu;
import net.x3pro.siteengine.domain.SitePage;
import net.x3pro.siteengine.support.ResponseJSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory. BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Controller
public class EngineController {
	private static final Logger logger = Logger.getLogger(EngineController.class);
	private HashMap<String, HashMap<String, String>> parametersGroup;	
	private HashMap<String, List<SiteMenu>> menusGroup;
	private HashMap<String, SitePage> pagesGroup;
	private HashMap<String, String> siteParameters;
	
	@Autowired
	SessionController sessionController;
	
	@Autowired
	SiteDecorator siteDecorator;
	
	@Autowired
	ExtensionController extensionController;
	
	@Autowired
	ServletContextProvider servletContextProvider;
	
	public void loadParameters(NodeList nodeList){		
		parametersGroup = new HashMap<String, HashMap<String, String>>();
		for (int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if (node.getNodeType()!=Node.ELEMENT_NODE)
				continue;			
			HashMap<String, String> parameters = new HashMap<String, String>();
			parametersGroup.put(node.getNodeName(), parameters);
			NodeList parametersNodeList = node.getChildNodes();
			for (int j=0; j<parametersNodeList.getLength(); j++){
				Node pNode = parametersNodeList.item(j);
				if (pNode.getNodeType()!=Node.ELEMENT_NODE)
					continue;				
				parameters.put(pNode.getNodeName(), pNode.getChildNodes().item(0).getNodeValue());
			}			
		}
		siteParameters = parametersGroup.get("site");
		if (siteParameters==null)
			siteParameters = new HashMap<String, String>();
		if (!siteParameters.containsKey("host"))
			siteParameters.put("host", "http://"+getHost()+"/");
		
		HashMap<String, String> serviceParameters = null;
		if (parametersGroup.containsKey("service"))
			 serviceParameters = parametersGroup.get("service");
		else{
			serviceParameters = new HashMap<String, String>();
			parametersGroup.put("service", serviceParameters);
		}
		if (!serviceParameters.containsKey("host"))
			serviceParameters.put("host", "http://"+getHost()+"/");
		if (!serviceParameters.containsKey("name"))
			serviceParameters.put("name", getHost());
		
		
	}
	
	public void loadMenus(NodeList nodeList){
		String contextPath = servletContextProvider.getContextPath();
		menusGroup = new HashMap<String, List<SiteMenu>>();
		for (int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if (node.getNodeType()!=Node.ELEMENT_NODE)
				continue;						
			List<SiteMenu> menus = new ArrayList<SiteMenu>();			
			menusGroup.put(node.getNodeName().toLowerCase(), menus);
			NodeList parametersNodeList = node.getChildNodes();
			for (int j=0; j<parametersNodeList.getLength(); j++){				
				Node pNode = parametersNodeList.item(j);
				if (pNode.getNodeType()!=Node.ELEMENT_NODE)
					continue;				
				SiteMenu siteMenu = new SiteMenu();
				NamedNodeMap nodeMap = pNode.getAttributes();
				for (int k=0; k<nodeMap.getLength(); k++){
					Node nodeAttr = nodeMap.item(k);
					if (nodeAttr.getNodeType()!=Node.ATTRIBUTE_NODE)
						continue;
					String attrName = nodeAttr.getNodeName();
					if (attrName.equals("url"))
						siteMenu.setUrl(contextPath+nodeAttr.getNodeValue());
					else if (attrName.equals("title"))
						siteMenu.setTitle(nodeAttr.getNodeValue());
					else if (attrName.equals("active"))
						siteMenu.setActive(nodeAttr.getNodeValue().equals("1"));
				}
				menus.add(siteMenu);
			}			
		}		
	}
	
	public void loadPages(NodeList nodeList){
		String contextPath = servletContextProvider.getContextPath();
		pagesGroup = new HashMap<String, SitePage>();
		for (int i=0; i<nodeList.getLength(); i++){				
			Node pNode = nodeList.item(i);			
			if (pNode.getNodeType()!=Node.ELEMENT_NODE)
				continue;						
			SitePage sitePage = new SitePage();				
			NamedNodeMap nodeMap = pNode.getAttributes();
			for (int j=0; j<nodeMap.getLength(); j++){
				Node nodeAttr = nodeMap.item(j);				
				if (nodeAttr.getNodeType()!=Node.ATTRIBUTE_NODE)
					continue;
				String attrName = nodeAttr.getNodeName();
				if (attrName.equals("url"))
					sitePage.setUrl(nodeAttr.getNodeValue());
				else if (attrName.equals("title"))
					sitePage.setTitle(nodeAttr.getNodeValue());
				else if (attrName.equals("name"))
					sitePage.setPageName(nodeAttr.getNodeValue());
				else if (attrName.equals("template"))
					sitePage.setTemplateName(nodeAttr.getNodeValue());
				else if (attrName.equals("active"))
					sitePage.setActive(nodeAttr.getNodeValue().equals("1"));
			}
			NodeList nodeParamsList = pNode.getChildNodes();
			for (int j=0; j<nodeParamsList.getLength(); j++){
				Node nodeParam = nodeParamsList.item(j);
				if (nodeParam.getNodeType()!=Node.ELEMENT_NODE)
					continue;				
				if (nodeParam.getNodeName().toLowerCase().equals("header")){					
					String type = "";
					String value = "";
					NamedNodeMap nodeParamMap = nodeParam.getAttributes();					
					for (int k=0; k<nodeParamMap.getLength(); k++){
						Node nodeAttr = nodeParamMap.item(k);						
						if (nodeAttr.getNodeType()!=Node.ATTRIBUTE_NODE)
							continue;
						String attrName = nodeAttr.getNodeName();
						if (attrName.equals("type"))
							type = nodeAttr.getNodeValue();
						else if (attrName.equals("value"))
							value = nodeAttr.getNodeValue();
					}
					String headerValue = "";
					if (type.equals("js"))
						headerValue = "<script type='text/javascript' src='"+contextPath+"/resources/"+type+"/"+value+"'></script>";
					else if (type.equals("css"))
						headerValue = "<link rel='stylesheet' type='text/css' href='"+contextPath+"/resources/"+type+"/"+value+"' />";
					if (!headerValue.isEmpty())
						sitePage.addHeader(headerValue);
				}				
			}
			if (sitePage.getUrl()!=null){
				pagesGroup.put(sitePage.getUrl().toLowerCase(), sitePage);				
			}
		}		
	}
	
	public String getParameter(String prefix, String name){
		String result;
		HashMap<String, String> parameters = parametersGroup.get(prefix);
		if (parameters==null)
			return "";
		result = parameters.get(name);
		if (result==null)
			result = "";		
		return result;
	}
	
	public List<SiteMenu> getMenus(String prefix){		
		List<SiteMenu> result = menusGroup.get(prefix);
		if (result==null)
			result = new ArrayList<SiteMenu>();
		return result;
	}
	
	@Deprecated
	public SessionController getCurrentSessionController(){						
		return sessionController;
	}
	
	public SiteDecorator getCurrentSiteController(){		
		return siteDecorator;		
	}
	
	public String request(String requestURI, Model model, HttpServletRequest request){		
		requestURI = requestURI.substring(request.getContextPath().length());
		requestURI = requestURI.toLowerCase();
		if (requestURI.startsWith("/"))
			requestURI = requestURI.substring(1);
		String command = request.getParameter("command");
		if (command==null)
			command = "";
		else
			command = command.trim().toLowerCase();
		if (!command.isEmpty())
			extensionController.requestCommand(command, model, request);			
						
		String redirectAdr = siteDecorator.getRedirectAdr();
		if (redirectAdr==null)
			redirectAdr="";
		if (!redirectAdr.isEmpty()){			
			return "redirect:"+redirectAdr;
		}
		else{			
			SitePage sitePage = pagesGroup.get(requestURI);			
			if (sitePage==null)
				return "error404";
			else{
				siteDecorator.addPageVar("requestUri", request.getRequestURI());				
				siteDecorator.setSitePage(sitePage);
				siteDecorator.setHttpRequest(request);
				siteDecorator.setParameters(siteParameters);
				model.addAttribute("site", siteDecorator);
				return "templates/"+sitePage.getTemplateName()+"/index";
			}
		}			
	}
	
	public String requestExt(String extName, String page, Model model){		
		extName = extName.toLowerCase();
		List<String> pagesURI = getPagesURI(page);		
		String result = "/extensions/"+extName+"/"+extensionController.requestExt(extName, pagesURI, model);
		if (!model.asMap().containsKey("site"))
			model.addAttribute("site", siteDecorator);
		return result;
	}
	
	public String requestApi(String extName, HttpServletRequest request){
		return extensionController.requestApi(extName, request);
	}
	
	public ResponseJSONObject requestJSON(String extName, HttpServletRequest request){
		return extensionController.requestJSON(extName, request);
	}
	
	private List<String> getPagesURI(String requestURI){
		requestURI = requestURI.toLowerCase();
		if (requestURI.startsWith("/"))
			requestURI = requestURI.substring(1);
		String[] arrayPagesURI = requestURI.split("/");
		List<String> pagesURI = new ArrayList<String>();
		if (arrayPagesURI.length==0){
			pagesURI.add("");			
		}
		else{
			for (String pageURI : arrayPagesURI) {
				pagesURI.add(pageURI.toLowerCase());
			}
		}
		return pagesURI;
	}
	
	private List<String> getPagesURI(String[] pagePagesURI){
		List<String> pagesURI = new ArrayList<String>();
		if (pagePagesURI.length==0){
			pagesURI.add("");			
		}
		else{
			for (String pageURI : pagePagesURI) {
				pagesURI.add(pageURI.toLowerCase());
			}
		}
		return pagesURI;
	}
	
	public void logDebug(Object message){
		logger.debug(message);
	}
	
	public void logInfo(Object message){
		logger.info(message);
	}
	
	public void logWarn(Object message){
		logger.warn(message);
	}
	
	public String getContextPath(){
		return servletContextProvider.getContextPath();
	}
	
	public String getHost(){
		return servletContextProvider.getHost();
	}
}
