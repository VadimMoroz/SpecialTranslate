package net.x3pro.siteengine.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.x3pro.siteengine.support.ExtensionModelVars;
import net.x3pro.siteengine.support.ResponseJSONObject;

import org.springframework.ui.Model;

public interface EngineExtension {
	public String requestCommand(String[] commandArray, ExtensionModelVars extensionVars, HttpServletRequest request);
	public String requestAPI(HttpServletRequest request);
	public ResponseJSONObject requestJSON(HttpServletRequest request);
	public String requestExt(List<String> pagesURI, ExtensionModelVars extensionModelVars, HttpServletRequest request);
	public String getExtensionName();
}
