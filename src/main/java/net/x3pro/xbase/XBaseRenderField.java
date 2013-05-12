package net.x3pro.xbase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedMap;
import java.util.TreeMap;

import net.x3pro.siteengine.dao.XBaseDAO;

public class XBaseRenderField {
	final public static String type_STRING_MULTILINE = "STRING_MULTILINE";
	final public static String type_STRING = "STRING";
	final public static String type_INTEGER = "INTEGER";
	final public static String type_FLOAT = "FLOAT";
	final public static String type_BOOLEAN = "BOOLEAN";
	final public static String type_DATE = "DATE";
	final public static String type_OBJECT = "OBJECT";
	
	private String name;
	private int length = 0;
	private String title = "";
	private boolean hide = false;
	private boolean readOnly = false;
	private boolean isNotEmpty = false;
	private String objectValueName;
	private String valueType = "";
	private String value = "";
	private String owner;
	private Class sourceType;
	private Class ownerType;	
	private String subordinate;
	private TreeMap<String, String> list = null;
	
	private String errorMessage;
	private String errorMessageText;
	
	private String getValueString(Object obj, Class clazz, Field field){
		field.setAccessible(true);
		String result = null;
		try{
			result = field.get(obj).toString();
		}
		catch (Exception e){
			
		}
		if (result==null)
			result ="";
		field.setAccessible(false);
		return result;
	}
	
	public XBaseRenderField(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String tittle) {
		this.title = tittle;
	}
	
	public boolean isHide() {
		return hide;
	}
	
	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public TreeMap<String, String> getList() {
		return list;
	}
	
	public void setList(TreeMap<String, String> list) {
		this.list = list;		
	}


	public void addListItem(String value, String title) {		
		if (list==null){
			list = new TreeMap<String, String>();			
		}
		list.put(value, title);	
	}

	public String getValue() {
		return value;
	}

	public void setValue(Object obj, Class clazz, Field field) {
		this.objectValueName = field.getName();
		this.value = getValueString(obj, clazz, field);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessageText() {
		return errorMessageText;
	}

	public void setErrorMessageText(String errorMessageText) {
		this.errorMessageText = errorMessageText;
	}	

	public boolean isNotEmpty() {
		return isNotEmpty;
	}

	public void setNotEmpty(boolean isNotEmpty) {
		this.isNotEmpty = isNotEmpty;
	}
	
	public void setObjectValueName(String objectValueName) {
		this.objectValueName = objectValueName;
	}
	
	public boolean setObjectValue(Object obj, Class clazz, XBaseDAO xBaseDAO) throws Exception{
		if (readOnly)
			return true;
		if (value==null || value.isEmpty())
			return true;
		Field field = clazz.getDeclaredField(objectValueName);
		field.setAccessible(true);		
		if (valueType.equals(type_STRING) || valueType.equals(type_STRING_MULTILINE)){
			field.set(obj, value);				
		}
		else if (valueType.equals(type_INTEGER) ) {
			field.set(obj, Integer.parseInt(value));
		}
		else if (valueType.equals(type_OBJECT) ) {
			Object argsObj = xBaseDAO.getObjectFromId(value, field.getType());
			field.set(obj, argsObj);
		}
		field.setAccessible(false);
		return true;		
	}
	
	public boolean setValueFromForm(String value){
		if (readOnly)
			return true;
		
		boolean result = true;
		if (value==null){
			errorMessageText = "Значение не найдено";
			return false;
		}
		if (isNotEmpty & value.isEmpty()){
			errorMessageText = "Значение не заполнено";
			return false;
		}
		if (valueType.equals(type_STRING_MULTILINE) || valueType.equals(type_STRING)){
			this.value = value;
		}
		else if (valueType.equals(type_INTEGER)) {
			try{
				Integer pint = Integer.parseInt(value);
				if (isNotEmpty & pint.equals(0)){
					errorMessageText = "Значение не заполнено";
					return false;
				}
				this.value = pint.toString();
			}
			catch (Exception e){
				errorMessageText = "Ошибка преобразования значения в число";
				return false;
			}
		}
		else if (valueType.equals(type_OBJECT)) {
			String pstr = list.get(value);
			if (pstr==null){
				errorMessageText = "Выбранное значение не найдено";
				return false;
			}
			if (isNotEmpty & pstr.isEmpty()){
				errorMessageText = "Значение не заполнено";
				return false;
			}
			this.value = value;
		}
		
		
		return result;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Class getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Class ownerType) {
		this.ownerType = ownerType;
	}

	public String getSubordinate() {
		return subordinate;
	}

	public void setSubordinate(String subordinate) {
		this.subordinate = subordinate;
	}

	public Class getSourceType() {
		return sourceType;
	}

	public void setSourceType(Class sourceType) {
		this.sourceType = sourceType;
	}

}
