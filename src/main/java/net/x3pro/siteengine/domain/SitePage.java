package net.x3pro.siteengine.domain;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class SitePage {

	private String url = "";	
	private String title = "";
	private String pageName = "";
	private String templateName = "";
	private ArrayList<String> headers = new ArrayList<String>();
	private boolean active = false;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public ArrayList<String> getHeaders() {
		return headers;
	}
	public void addHeader(String value){
		headers.add(value);		
	}
}
