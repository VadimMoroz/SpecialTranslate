package net.x3pro.xbase;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.x3pro.siteengine.dao.XBaseDAO;
import net.x3pro.siteengine.domain.SiteMenu;
import net.x3pro.siteengine.domain.XBaseRecord;
import net.x3pro.siteengine.exception.XBaseException;
import net.x3pro.siteengine.service.EngineSupport;
import net.x3pro.xbase.annotation.XBaseField;
import net.x3pro.xbase.annotation.XBaseObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.sun.org.apache.bcel.internal.generic.IfInstruction;

@Service
public class XBaseController {
	
	@Autowired
	XBaseDAO xBaseDAO;
	
	@Autowired
	EngineSupport engineSupport;
	
	public void setFieldListByOwner(XBaseRenderField field, String ownerValue, boolean addEmptyItem){
		if (field.getList()!=null)
			field.setList(null);		
		if (field.getOwner().equals(""))
			return;
		if (!field.getValueType().equals(XBaseRenderField.type_OBJECT))
			return;
		if (ownerValue==null || ownerValue.trim().equals(""))
			return;
		ownerValue = ownerValue.trim();
		Object owner = xBaseDAO.getObjectFromId(ownerValue, field.getOwnerType());
		if (owner==null)
			return;
		List<Object> listByOwner = xBaseDAO.getListByOwner(field.getSourceType(), owner);		
		if (addEmptyItem)
			field.addListItem("", "");
		for (Object item:listByOwner){
			XBaseRecord xBaseRecord = (XBaseRecord)item;
			field.addListItem(xBaseRecord.getRecId(), xBaseRecord.getRecTitle());			
		}
	}
	
	public boolean renderObject(String sourceElement, String targetUrl, Object obj, Model model, HttpServletRequest request){
		HttpSession session = request.getSession();
		String errorMessage = "";
		String errorMessageFull = "";				
		Class clazz = obj.getClass();
		if (!clazz.isAnnotationPresent(XBaseObject.class))
			errorMessage =  "error:object is not annotated";
		else{
			XBaseRenderObject renderObject = new XBaseRenderObject(sourceElement, targetUrl);
			XBaseObject xBaseObject = (XBaseObject)clazz.getAnnotation(XBaseObject.class);
			renderObject.setTitle(xBaseObject.title());
			
			XBaseSessionStore sessionStore = new XBaseSessionStore();
			
			renderObject.setButtonOk(xBaseObject.buttonOkValue(), xBaseObject.buttonOkTitle());
			renderObject.setButtonCancel(xBaseObject.buttonOkValue(), xBaseObject.buttonOkTitle());
						
			Field[] field =  clazz.getDeclaredFields();		
			for (Field ifield: field){			
				if (ifield.isAnnotationPresent(XBaseField.class)){
					XBaseField xBaseField = ifield.getAnnotation(XBaseField.class);
					XBaseRenderField renderField = new XBaseRenderField(ifield.getName());
					
					renderField.setLength(xBaseField.length());
					renderField.setTitle(xBaseField.title());
					renderField.setReadOnly(xBaseField.readOnly());
					renderField.setNotEmpty(xBaseField.notEmpty());
					renderField.setOwner(xBaseField.owner());					
					renderField.setSourceType(ifield.getType());					
					try{
						
						if (ifield.getType().equals(String.class)){
							if (xBaseField.length()==0)
								renderField.setValueType(XBaseRenderField.type_STRING_MULTILINE);
							else
								renderField.setValueType(XBaseRenderField.type_STRING);							
						}
						else if (ifield.getType().equals(Integer.class)) {
							renderField.setValueType(XBaseRenderField.type_INTEGER);							
						}
						else if (ifield.getType().equals(Float.class)) {
							renderField.setValueType(XBaseRenderField.type_FLOAT);							
						}
						else if (ifield.getType().equals(Boolean.class)) {
							renderField.setValueType(XBaseRenderField.type_BOOLEAN);
						}
						else if (ifield.getType().equals(Date.class)) {
							renderField.setValueType(XBaseRenderField.type_DATE);
						}
						else{
							renderField.setValueType(XBaseRenderField.type_OBJECT);
							if (renderField.getOwner().equals("")){
								List<Object> plist = xBaseDAO.getList(ifield.getType());
								renderField.addListItem("","");
								for (Object item:plist){
									XBaseRecord xBaseRecord = (XBaseRecord)item;								
									renderField.addListItem(xBaseRecord.getRecId(), xBaseRecord.getRecTitle());
								}
							}
							
						}
						
						renderField.setValue(obj, clazz, ifield);						
						
					}
					catch(Exception e){
						errorMessage = errorMessage+"\n"+e.getLocalizedMessage();
						errorMessageFull = errorMessageFull+"\n"+e.fillInStackTrace();
					}
					renderObject.addField(renderField);
				}
			}
			if (errorMessage.isEmpty()){
				for (XBaseRenderField renderField:renderObject.getFields()){
					String ownerName = renderField.getOwner();
					if (!ownerName.isEmpty()){						
						for (XBaseRenderField prenderField:renderObject.getFields()){
							if (prenderField.getName().equals(ownerName)){
								prenderField.setSubordinate(renderField.getName());
								renderField.setOwnerType(prenderField.getSourceType());
								setFieldListByOwner(renderField, prenderField.getValue(),true);
							}
						}						
					}
				}
				String sessionId = engineSupport.GenerateKey(30);
				sessionStore.setObjSource(obj);
				sessionStore.setRenderObject(renderObject);
				session.setAttribute("xbase_sessionstore_"+sessionId, sessionStore);				
				renderObject.setSessionObjectID(sessionId);				
				model.addAttribute("xbase_renderobject", renderObject);
			}
			else{
				model.addAttribute("xbase_errormessage", errorMessage);
				model.addAttribute("xbase_errormessagefull", errorMessageFull);
			}
		}		
		return errorMessage.isEmpty();
	}	
	
	public Object getResultObject(Model model, HttpServletRequest request){
		Object result = null;
		String sessionId = request.getParameter("xbase_session_id");
		if ((sessionId==null) || (sessionId.isEmpty())){
			model.addAttribute("xbase_errormessage", "not found session id");
			return result;
		}
		HttpSession session = request.getSession(true);
		XBaseSessionStore sessionStore = (XBaseSessionStore)session.getAttribute("xbase_sessionstore_"+sessionId);
		if (sessionStore==null){
			model.addAttribute("xbase_errormessage", "not found session obj");
			return result;
		}		
		Object objSource = sessionStore.getObjSource();
		XBaseRenderObject renderObject = sessionStore.getRenderObject();		
		
		boolean isError = false;
		boolean isGlobalError = false;
		for (XBaseRenderField field:renderObject.getFields()){
			field.setErrorMessage(null);
			field.setErrorMessageText(null);
			boolean fieldSetValueResult;
			fieldSetValueResult = field.setValueFromForm(request.getParameter(field.getName()));
			if (!fieldSetValueResult){
				isError = true;
				if (!field.getOwner().isEmpty()){
					String ownerName = field.getOwner();
					for (XBaseRenderField prenderField:renderObject.getFields()){
						if (prenderField.getName().equals(ownerName)){
							setFieldListByOwner(field, prenderField.getValue(),true);
						}
					}
				}
				
			}
		}		
		if (isError){
			model.addAttribute("xbase_errormessage", "not get all form values");
		}
		else{
			Class clazz = objSource.getClass();
			for (XBaseRenderField field:renderObject.getFields()){
				boolean fieldSetObjectValue;
				try{
					fieldSetObjectValue = field.setObjectValue(objSource, clazz, xBaseDAO);
					if (!fieldSetObjectValue)
						isError = true;
				}
				catch(Exception e){
					model.addAttribute("xbase_errormessagefull", e.fillInStackTrace());
					isGlobalError = true;
					isError = true;
				}
				
			}
			if (isError){
				model.addAttribute("xbase_errormessage", "not set all object values");
			}
		}
		if (!isGlobalError)
			model.addAttribute("xbase_renderobject", renderObject);		
		if (!isError){			
			result = objSource;
			session.setAttribute("xbase_sessionstore_"+sessionId, null);
		}			
		return result;
	}	
	
	public TreeMap<String, String> getListValueByOwner(String elementName, String ownerValue, XBaseRenderObject renderObject) throws XBaseException{
		XBaseRenderField field = null;
		for (XBaseRenderField ifield:renderObject.getFields()){
			if (ifield.getName().equals(elementName)){
				field = ifield;
				break;
			}
		}
		if (field==null)
			throw new XBaseException("ERROR:field not found");			
		String ownerName = field.getOwner();
		if (ownerName==null || ownerName.isEmpty())
			throw new XBaseException("ERROR:owner not found. Field = "+field.getName());				
		setFieldListByOwner(field, ownerValue, false);
		return field.getList();
	}
}
