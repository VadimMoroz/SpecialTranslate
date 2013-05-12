package net.x3pro.siteengine.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.persistence.Table;

@Repository
public class DAOService {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public String specialSave(Object object, String[] fieldsSet, String[] fieldsSearch){		
		Class clazz = object.getClass();
		if (!clazz.isAnnotationPresent(Table.class))
			return "IS_NOT_ANNOTATION_PRESENT";
		Table table = (Table)clazz.getAnnotation(Table.class);
		String tableName = table.name();
		if (tableName==null || tableName.isEmpty())
			return "IS_NOT_TABLE";		
		String fieldsSQL = "";
		String fieldsValueSQL = "";
		String fieldsWhileSQL = "";
		HashMap<String,Object> fieldsValue = new HashMap<String, Object>();
		for (int i=0;i<fieldsSet.length;i++) {
			String fieldSetName = fieldsSet[i];
			Field field;
			try{
				field = clazz.getDeclaredField(fieldSetName);
			}
			catch(Exception e){
				return "ERROR_FIND_FIELD";
			}			
			if (!field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(JoinColumn.class))				
				return "NOT_FIND_COLUMN: "+fieldSetName;
			String fieldName;
			Object fieldValue = null;
			if (field.isAnnotationPresent(Column.class)){
				Column column = (Column)field.getAnnotation(Column.class);
				fieldName = column.name();
				try{			
					boolean accessible = field.isAccessible();
					if (!accessible)
						field.setAccessible(true);
					fieldValue = field.get(object);
					if (!accessible)
						field.setAccessible(false);
				}
				catch(Exception e){			
					e.printStackTrace();
					return "ERROR_GET_FIELD";
				}
			}
			else{
				JoinColumn column = (JoinColumn)field.getAnnotation(JoinColumn.class);
				fieldName = column.name();
				Class typeClass = field.getType();				
				Field[] joinFields = typeClass.getDeclaredFields();
				boolean fieldFound = false;
				for (int j=0;j<joinFields.length;j++){
					Field joinField = joinFields[j];					
					if (joinField.isAnnotationPresent(Id.class)){
						fieldFound = true;
						Object fieldParentValue = null;
						try{
							boolean accessible = field.isAccessible();
							if (!accessible)
								field.setAccessible(true);
							fieldParentValue = field.get(object);
							if (!accessible)
								field.setAccessible(false);							
						}
						catch(Exception e){			
							e.printStackTrace();
							return "ERROR_GET_FIELD_ID_OBJECT:"+fieldSetName;
						}
						if (fieldParentValue!=null){
							try{
								boolean accessible = joinField.isAccessible();
								if (!accessible)
									joinField.setAccessible(true);
								fieldValue = joinField.get(fieldParentValue);
								if (!accessible)
									joinField.setAccessible(false);							
							}
							catch(Exception e){			
								e.printStackTrace();
								return "ERROR_GET_FIELD_ID:"+fieldSetName;
							}
						}
					}
				}
				if (!fieldFound)
					return "NOT_GET_ID:"+fieldSetName;
			}			
			if (fieldName==null || fieldName.isEmpty())
				return "NOT_GET_COLUMN";
			fieldsSQL = fieldsSQL+(fieldsSQL.isEmpty()?"":", ")+fieldName;
			fieldsValueSQL = fieldsValueSQL+(fieldsValueSQL.isEmpty()?"":", ")+":"+fieldSetName;
			for (int j=0;j<fieldsSearch.length;j++){
				if (fieldsSearch[j].equals(fieldSetName)){
					fieldsWhileSQL = fieldsWhileSQL+(fieldsWhileSQL.isEmpty()?"":" AND ")+fieldName+"=:"+fieldSetName;					
					break;
				}
			}
			fieldsValue.put(fieldName, fieldValue);
			System.out.println("fieldName = "+fieldName+"     fieldValue = "+fieldValue.toString());
		}
		
		String SQL = "INSERT INTO "+tableName+" ("+fieldsSQL+")"+
				" SELECT * FROM (SELECT "+fieldsValueSQL+") AS tmp"+
				" WHERE NOT EXISTS("+
				" SELECT "+fieldsSQL+" FROM userbaseword WHERE "+fieldsWhileSQL+
				" ) LIMIT 1";
		System.out.println("SQL = "+SQL);
		
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(SQL);		
		for (String fieldSet : fieldsSet) {
			sqlQuery.setParameter(fieldSet, fieldsValue.get(fieldSet));
		}
		sqlQuery.executeUpdate();
		return null;
		
	}

}
