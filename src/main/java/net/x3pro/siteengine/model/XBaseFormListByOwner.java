package net.x3pro.siteengine.model;

import java.util.HashMap;
import java.util.TreeMap;

public class XBaseFormListByOwner {
	
	private TreeMap<String, String> list;
	private String status = "";

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TreeMap<String, String> getList() {
		return list;
	}

	public void setList(TreeMap<String, String> list) {
		this.list = list;
	}	
}
