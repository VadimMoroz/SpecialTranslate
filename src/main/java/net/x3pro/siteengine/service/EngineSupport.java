package net.x3pro.siteengine.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Service
public class EngineSupport{	
	
	public String GenerateKey(int length){
		String characters = "abcdefghijklmnopqrstuvwxyz1234567890";
		Random random = new Random();
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(random.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
}
