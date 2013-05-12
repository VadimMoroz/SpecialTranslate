package net.x3pro.siteengine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class SiteMenu {
	
	private String url = "";	
	private String title = "";
	private boolean active = false;
	private boolean isAutorized = false;
	private List<String> roles = null;
	
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isAutorized() {
		return isAutorized;
	}
	public void setAutorized(boolean isAutorized) {
		this.isAutorized = isAutorized;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}	
}
