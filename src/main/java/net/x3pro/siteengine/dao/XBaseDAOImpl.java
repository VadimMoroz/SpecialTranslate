package net.x3pro.siteengine.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Id;

import org.hibernate.SessionFactory;
//import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.org.mozilla.javascript.internal.EcmaError;

import net.x3pro.siteengine.domain.User;
import net.x3pro.xbase.XBaseRenderObject;
import net.x3pro.xbase.annotation.XBaseObject;

@Repository
public class XBaseDAOImpl implements XBaseDAO{
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	@Autowired
	DAOUtil daoUtil;

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Object> getList(Class clazz){					
		List<Object> result;
		result = sessionFactory.getCurrentSession().createCriteria(clazz).list();			
		return result;
	}
	
	@Transactional
	public void deleteObject(Object obj){
		sessionFactory.getCurrentSession().delete(obj);
	}
	
	@Transactional
	public Object getObjectFromParam(String paramName, Object paramValue, Class clazz){
		Object result = null;
		List<Object> list =  sessionFactory.getCurrentSession().createCriteria(clazz).
											add(Restrictions.eq(paramName, paramValue)).
											list();
		if (list.size()>0){
			result = list.get(0);
		}
		return result;
	}
	
	@Transactional
	public Object getObjectFromId(String objId, Class clazz){
		if (!clazz.isAnnotationPresent(Table.class))
			return null;
		Class objTypeClass = null;
		for (Field field:clazz.getDeclaredFields()){
			if (field.isAnnotationPresent(Id.class)){
				objTypeClass = field.getType();
				break;
			}
		}
		if (objTypeClass==null)
			return null;
		
		if (objTypeClass.equals(String.class)){
			return sessionFactory.getCurrentSession().get(clazz, objId);		
		}
		else if (objTypeClass.equals(Integer.class)||objTypeClass.equals(int.class)){
			Object result = null;
			result = sessionFactory.getCurrentSession().get(clazz, Integer.parseInt(objId));
			return result;
		}
		else
			return null;
	}
	
	@Transactional
	public List<Object> getListByOwner(Class clazz, Object owner){
		return sessionFactory.getCurrentSession().createCriteria(clazz).
											add(Restrictions.eq("owner", owner)).
											list();
	}
	
	public String prepareParameter(String value){
		return daoUtil.prepareSqlParameter(value);
	}
	
	public String prepareParameter(String value, boolean needConvertToLowerCase){
		return daoUtil.prepareSqlParameter(value, needConvertToLowerCase);
	}
	
}
