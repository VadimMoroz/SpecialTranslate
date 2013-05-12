package net.x3pro.siteengine.support;

import java.util.HashMap;

public class ExtensionModelVars{
	private HashMap<String, Boolean> errors;
	private HashMap<String, Boolean> flags;
	private HashMap<String, String> messages;
	private HashMap<String, Object> vars;
	
	public ExtensionModelVars() {
		errors = new HashMap<String, Boolean>();
		flags = new HashMap<String, Boolean>();
		messages = new HashMap<String, String>();
		vars = new HashMap<String, Object>();
	}
	
	public void addError(String name){
		errors.put(name, true);
	}
	
	public void addFlag(String name){
		flags.put(name, true);
	}
	
	public void addMessage(String name, String value){
		messages.put(name, value);
	}
	
	public void addVar(String name, Object value){
		vars.put(name, value);
	}
	
	public HashMap<String, Boolean> getErrors() {
		return errors;
	}
	
	public HashMap<String, Boolean> getFlags() {
		return flags;
	}
	
	public HashMap<String, Object> getVars() {
		return vars;
	}
	
	public HashMap<String, String> getMessages() {
		return messages;
	}
}
