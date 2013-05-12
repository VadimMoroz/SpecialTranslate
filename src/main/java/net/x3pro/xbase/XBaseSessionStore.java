package net.x3pro.xbase;

import java.util.HashMap;
import java.util.List;

public class XBaseSessionStore {
	
	private Object objSource;
	private String sourceElement;
	private String targetUrl;	
	//private HashMap<String, HashMap<String, String>> listMap;
	private XBaseRenderObject renderObject;
	
	/*public void addList(String name, HashMap<String, String> list){
		if (listMap==null)
			listMap = new HashMap<String, HashMap<String,String>>();
		listMap.put(name, list);
	}*/
	
	/*public Boolean isValueExist(String listName, String valueName){
		Boolean result = false;
		HashMap<String,String> list = listMap.get(listName);
		if (list!=null){
			String value = list.get(valueName);
			if (value!=null)
				if (!value.isEmpty())
					result = true;
		}			
			
		return result;
	}*/

	public Object getObjSource() {
		return objSource;
	}

	public void setObjSource(Object objSource) {
		this.objSource = objSource;
	}

	public String getSourceElement() {
		return sourceElement;
	}

	public void setSourceElement(String sourceElement) {
		this.sourceElement = sourceElement;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public XBaseRenderObject getRenderObject() {
		return renderObject;
	}

	public void setRenderObject(XBaseRenderObject renderObject) {
		this.renderObject = renderObject;
	}

}
