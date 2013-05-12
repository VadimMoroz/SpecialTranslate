package net.x3pro.siteengine.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.x3pro.siteengine.service.extensions.EngineExtensionsCollection;
import net.x3pro.siteengine.support.ExtensionModelVars;
import net.x3pro.siteengine.support.ResponseJSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class ExtensionController {
	
	@Autowired
	SiteDecorator siteDecorator;
	
	@Autowired
	EngineExtensionsCollection engineExtensionsCollection;

	public void requestCommand(String command, Model model, HttpServletRequest request){
		String[] commandArray = command.split("\\.");
		String extName = commandArray[0];
		String newCommand = "";
		String regirectAdr = "";	
		EngineExtension engineExtension = engineExtensionsCollection.getExtension(extName);		
		if (engineExtension!=null){
			ExtensionModelVars extensionModelVars;
			extensionModelVars = siteDecorator.getExt().get(extName);
			if (extensionModelVars==null)
				extensionModelVars = new ExtensionModelVars();			
			newCommand = engineExtension.requestCommand(commandArray, extensionModelVars, request);
			siteDecorator.addExtensionVars(extName, extensionModelVars);			
		}
		if (!newCommand.isEmpty() & !newCommand.equals(command)){
			if (newCommand.substring(0, 9).equals("redirect:"))
				regirectAdr = newCommand.substring(9);
			else
				command =  newCommand;
		}		
		siteDecorator.setCommand(command);
		siteDecorator.setRedirectAdr(regirectAdr);
	}	
	
	public String requestExt(String extName, List<String> pagesURI, Model model){
		if (extName.isEmpty())
			return "ERROR:Ext not found";
		EngineExtension engineExtension = engineExtensionsCollection.getExtension(extName);
		if (engineExtension==null)
			return "ERROR:Ext not found";
		else{						
			ExtensionModelVars extensionModelVars;
			extensionModelVars = siteDecorator.getExt().get(extName);
			if (extensionModelVars==null){
				extensionModelVars = new ExtensionModelVars();
			}				
			String result = engineExtension.requestExt(pagesURI, extensionModelVars, siteDecorator.getHttpRequest());
			siteDecorator.addExtensionVars(extName, extensionModelVars);						
			return result;
		}			
	}
	
	public String requestApi(String extName, HttpServletRequest request){
		if (extName.isEmpty())
			return "ERROR:API not found";
		EngineExtension engineExtension = engineExtensionsCollection.getExtension(extName);
		if (engineExtension==null)
			return "ERROR:API not found";
		else
			return engineExtension.requestAPI(request);
	}
	
	public ResponseJSONObject requestJSON(String extName, HttpServletRequest request){		
		ResponseJSONObject result;
		if (extName.isEmpty()){
			result = new ResponseJSONObject();
			result.setResultError("EXT_NOT_FOUND","Extension not found");			
			return result;
		}			
		EngineExtension engineExtension = engineExtensionsCollection.getExtension(extName);
		if (engineExtension==null){
			result = new ResponseJSONObject();
			result.setResultError("JSON_RESPONSE_NOT_FOUND","JSON response not found");			
			return result;
		}
		else
			return engineExtension.requestJSON(request);
	}
}
