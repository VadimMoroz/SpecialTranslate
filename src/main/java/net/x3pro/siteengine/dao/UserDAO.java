package net.x3pro.siteengine.dao;

import java.util.List;

import net.x3pro.siteengine.domain.NewUser;
import net.x3pro.siteengine.domain.User;
import net.x3pro.siteengine.domain.UserSupport;

//import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.context.CurrentSessionContext;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAO{
	
	@Autowired
	protected DAOUtil daoUtil;
		
	@Autowired
	protected SessionFactory sessionFactory;
	
	public User getUser(String email, String paswwrod){
		email = daoUtil.prepareSqlParameter(email);				
		List<User> list;		
		list = sessionFactory.getCurrentSession().createQuery("from User where email='"+email+"' and state>0").list();
		User result = null;
		if (list.size()>0){
			User user_ = list.get(0);
			if (user_.CheckPassword(paswwrod))
				result = user_;
		}
		return result;
	}
	
	public void SaveUser(User user){
		sessionFactory.getCurrentSession().save(user); 
	}
	
	@Transactional
	public void SaveNewUser(NewUser newuser){		
		sessionFactory.getCurrentSession().saveOrUpdate(newuser);		
	}
	
	@Transactional
	public Boolean UserExist(String email){
		List<User> list;
		email = daoUtil.prepareSqlParameter(email);
		list = sessionFactory.getCurrentSession().createQuery("from User where email='"+email+"'").list();
		return list.size()>0;
	}
	
	public String getUserName(String email){
		List<User> list;
		email = daoUtil.prepareSqlParameter(email);
		list = sessionFactory.getCurrentSession().createQuery("from User where email='"+email+"'").list();
		if (list.size()==0)
			return "";
		else{
			return list.get(0).getName();
		}		
	}
	
	@Transactional
	public String ActivateUser(String registerkey){
		String result = "";
		List<NewUser> newuserList;
		registerkey = daoUtil.prepareSqlParameter(registerkey);
		newuserList = sessionFactory.getCurrentSession().createQuery("from NewUser where registerkey='"+registerkey+"'").list();
		if (newuserList.size()>0){
			NewUser newuser = newuserList.get(0);
			User user = new User();			
			user.CreateNewUser(newuser.getName(), newuser.getEmail(), newuser.getPassword(), newuser.getRole(), 1, newuser.getdate());
			try{
				sessionFactory.getCurrentSession().save(user);
				sessionFactory.getCurrentSession().delete(newuser);
				result = user.getName();
			}
			catch (Exception e){
				
			}
		}
		return result;
	}
	
	@Transactional
	public User getUserFromEmail(String email){
		email = daoUtil.prepareSqlParameter(email);
		Session session = sessionFactory.getCurrentSession();
		List<User> list;
		list = session.createCriteria(User.class).
						add(Restrictions.eq("email", email)).
						setMaxResults(1).
						list();		
		if (list.size()==0)
			return null;
		else
			return list.get(0);		
	}
	
	@Transactional
	public boolean SavePassReset(User user, String key){
		key = daoUtil.prepareSqlParameter(key, false);
		UserSupport userSupport = new UserSupport();
		userSupport.setUser(user);
		userSupport.setCommand("passreset");
		userSupport.setValue(key);		
		try{
			Session session =  sessionFactory.getCurrentSession();
			List<UserSupport> list;
			list = session.createCriteria(UserSupport.class).
							add(Restrictions.eq("user", user)).list();			
			for (UserSupport rec: list)
				 sessionFactory.getCurrentSession().delete(rec);
			sessionFactory.getCurrentSession().save(userSupport);
			return true;
		}
		catch(Exception e){
			System.out.println(e.fillInStackTrace());
			return false;
		}			
	}
	
	public UserSupport getPassResetUser(String key){
		key = key.trim();
		if (key.isEmpty())
			return null;
		Session session =  sessionFactory.getCurrentSession();
		List<UserSupport> list;
		list = session.createCriteria(UserSupport.class).
				add(Restrictions.eq("command", "passreset")).
				add(Restrictions.eq("value", key)).list();
		if (list.size()==0)
			return null;
		else
			return list.get(0);
	}
	
	@Transactional
	public boolean isPassResetRecordExist(String key){
		UserSupport userSupport = getPassResetUser(key);		
		if (userSupport==null)
			return false;
		else{
			if (userSupport.getUser()==null)
				return false;
			else
				return true;
		}
	}
	
	@Transactional
	public boolean ResetPassword(String key, String password){
		key = key.trim();
		if (key.isEmpty())
			return false;
		Session session =  sessionFactory.getCurrentSession();
		try{
			User user;
			UserSupport userSupport = getPassResetUser(key);		
			if (userSupport==null){				
				return false;
			}
			else{
				user = userSupport.getUser();
			}
			if (user==null){				
				return false;
			}
			user.setPassword(password);
			session.save(user);
			session.delete(userSupport);				
			return true;
		}
		catch(Exception e){			
			return false;
		}
	}
}
