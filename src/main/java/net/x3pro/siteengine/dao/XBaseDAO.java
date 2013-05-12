package net.x3pro.siteengine.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.x3pro.siteengine.domain.User;

public interface XBaseDAO {
	public List<Object> getList(Class clazz);
	public void deleteObject(Object obj);
	public Object getObjectFromId(String id, Class clazz);
	public Object getObjectFromParam(String paramName, Object paramValue, Class clazz);
	public List<Object> getListByOwner(Class clazz, Object owner);
	public String prepareParameter(String value);
	public String prepareParameter(String value, boolean needConvertToLowerCase);
}
