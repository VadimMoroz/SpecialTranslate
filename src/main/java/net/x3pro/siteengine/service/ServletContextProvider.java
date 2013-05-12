package net.x3pro.siteengine.service;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import com.sun.istack.internal.logging.Logger;

@Service
public class ServletContextProvider implements ServletContextAware{
	public static ServletContext context = null;
	public static String contextPath = "";
	public static String host = "";
	
	public void setServletContext(ServletContext servletContext) {
		this.context = servletContext;
		this.contextPath = servletContext.getContextPath();
		
		String phost = null;
		Object dirContext = servletContext.getAttribute("org.apache.catalina.resources");
		Class dirContextClass = dirContext.getClass();
		try{						
			Method hostMethod = dirContextClass.getMethod("getHostName", null);
			phost = (String)hostMethod.invoke(dirContext, null);			
			
		}
		catch (Exception e){
			
		}
		
		if (phost==null || phost.equals("localhost")){
			try{
				InetAddress local = InetAddress.getLocalHost();
				phost = local.getHostName();					
			}
			catch(Exception e){
				
			}
		}
						
		if (phost!=null){
			String specialPort = servletContext.getInitParameter("specialPort");			
			if (specialPort!=null)
				phost = phost + ":"+specialPort;
			this.host = phost;
		}						
	}
	
	public static ServletContext getServletContext() {
		return context;
	}
	
	public static String getContextPath(){
		return contextPath;
	}
	
	public static String getHost() {
		return host;
	}
}
