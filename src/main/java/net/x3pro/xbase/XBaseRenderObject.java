package net.x3pro.xbase;

import java.util.ArrayList;

import javax.swing.Spring;

import org.hibernate.mapping.List;

import net.x3pro.xbase.annotation.XBaseField;

public class XBaseRenderObject{	
	private String title;
	private String buttonOkValue;
	private String buttonCancelValue;
	private String buttonOkTitle;
	private String buttonCancelTitle;
	private String sourceElementName;
	private String targetUrl;
	private String sessionObjectID;
	private ArrayList<XBaseRenderField> list;
	
	public XBaseRenderObject(String sourceElementName, String targetUrl) {		
		this.sourceElementName = sourceElementName;
		this.targetUrl = targetUrl;
		list = new ArrayList<XBaseRenderField>();
	}
	
	public void addField(XBaseRenderField field){
		list.add(field);
	}
	
	public ArrayList<XBaseRenderField> getFields(){
		return list;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getButtonOkValue() {
		return buttonOkValue;
	}

	public void setButtonOk(String buttonOkValue, String buttonOkTitle) {
		if (!buttonOkValue.isEmpty()){
			this.buttonOkValue = buttonOkValue;
			if (buttonOkTitle.isEmpty())
				this.buttonOkTitle = buttonOkValue;				
			else
				this.buttonOkTitle = buttonOkTitle;
		}
	}
	
	public void setButtonCancel(String buttonCancelValue, String buttonCancelTitle) {
		if (!buttonCancelValue.isEmpty()){
			this.buttonCancelValue = buttonCancelValue;
			if (buttonCancelTitle.isEmpty())
				this.buttonCancelTitle = buttonCancelValue;				
			else
				this.buttonCancelTitle = buttonCancelTitle;
		}
	}

	public String getButtonOkTitle() {
		return buttonOkTitle;
	}

	public String getButtonCancelTitle() {
		return buttonCancelTitle;
	}

	public String getButtonCancelValue() {
		return buttonCancelValue;
	}

	public String getSourceElementName() {
		return sourceElementName;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public String getSessionObjectID() {
		return sessionObjectID;
	}

	public void setSessionObjectID(String sessionObjectID) {
		this.sessionObjectID = sessionObjectID;
	}
}
