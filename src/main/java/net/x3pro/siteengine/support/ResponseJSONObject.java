package net.x3pro.siteengine.support;

import java.util.HashMap;
import java.util.Map;

public class ResponseJSONObject {
	private String result = "";
	private String code = "";
	private String comment = "";
	private Map<String, Object> data;
	
	public ResponseJSONObject() {
		data = new HashMap<String, Object>();
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Map<String, Object> getData() {
		return data;
	}
	
	public void addData(String name, Object value){
		data.put(name, value);
	}
	
	public void setResultError(String code, String comment){
		this.result = "ERROR";
		this.code = code;
		this.comment = comment;
	}
	
	public void setResultError(String code){
		setResultError(code, "");
	}
	
	public void setResultOK(String code, String comment){
		this.result = "OK";
		this.code = code;
		this.comment = comment;
	}
	
	public void setResultOK(String code){
		setResultOK(code, "");
	}
	
	public void setResultOK(){
		setResultOK("");
	}
	
}
