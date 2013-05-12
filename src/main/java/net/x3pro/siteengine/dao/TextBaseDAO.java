package net.x3pro.siteengine.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.x3pro.siteengine.domain.SiteMenu;
import net.x3pro.siteengine.domain.SitePage;
import net.x3pro.siteengine.domain.UserBase;
import net.x3pro.siteengine.domain.User;
import net.x3pro.siteengine.domain.UserReadyWord;
import net.x3pro.siteengine.domain.UserWord;
import net.x3pro.siteengine.domain.WordForTranslate;
import net.x3pro.siteengine.domain.WordNotTranslate;
import net.x3pro.siteengine.domain.WordTranslate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.CurrentSessionContext;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TextBaseDAO {
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	@Autowired
	protected DAOService daoService;
	
	@Transactional
	public void saveBaseText(UserBase userBase, HashSet<String> words){
		Session session = sessionFactory.getCurrentSession();
		List<UserBase> resBaseList = session.createCriteria(UserBase.class).
									add(Restrictions.eq("user", userBase.getUser())).
									add(Restrictions.eq("name", userBase.getName())).
									list();
		UserBase resBase;
		if (resBaseList.size()>0){
			resBase = resBaseList.get(0);
		}
		else{
			session.save(userBase);
			resBase = userBase;
		}		
		if (words.size()==0)
			return;		
		for (String word : words) {
			String SQL = "INSERT INTO userbaseword (userbase,word)"+
						" SELECT * FROM (SELECT "+resBase.getId()+", '"+word+"') AS tmp"+
						" WHERE NOT EXISTS("+
						" SELECT userbase, word FROM userbaseword WHERE userbase="+resBase.getId()+" AND word = '"+word+"'"+
						" ) LIMIT 1";			
			session.createSQLQuery(SQL).executeUpdate();
		}
		
		
	}
	
	@Transactional
	public void saveWords(HashSet<String> words){
		Session session = sessionFactory.getCurrentSession();
		List<WordTranslate> list = session.createCriteria(WordTranslate.class).
									add(Restrictions.in("word", words)).
									list();
		for (WordTranslate item : list) {
			words.remove(item.getWord());
		}
		if (words.size()==0)
			return;
		List<WordForTranslate> list2 = session.createCriteria(WordForTranslate.class).
										add(Restrictions.in("word", words)).
										list();
		for (WordForTranslate item : list2) {
			words.remove(item.getWord());
		}
		if (words.size()==0)
			return;
		String wordstext = "";
		int i = 0;
		int c = words.size();
		for (String word : words) {
			i++;
			wordstext = wordstext.concat("(\""+word+"\")"+((i==c)?"":","));
		}
		String SQL = "INSERT INTO wordfortranslate (word) VALUES "+wordstext;		
		session.createSQLQuery(SQL).executeUpdate();								
	}
	
	@Transactional
	public void deleteWordForTranslating(String word){
		Session session = sessionFactory.getCurrentSession();
		WordForTranslate wordForTranslate = (WordForTranslate)session.get(WordForTranslate.class, word);
		if (wordForTranslate!=null)
			sessionFactory.getCurrentSession().delete(wordForTranslate);
	}
	
	@Transactional
	public String getWordForTranslating(User user){		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);		
		Session session = sessionFactory.getCurrentSession();
		List<WordForTranslate> list = session.createCriteria(WordForTranslate.class).
										add(Restrictions.or(Restrictions.le("date", calendar.getTime()), Restrictions.isNull("date"))).
										setMaxResults(1).
										list();
		if (list.size()==0)
			return null;
		else{
			WordForTranslate wordForTranslate = list.get(0);
			String result = wordForTranslate.getWord();
			wordForTranslate.setDate(new Date());
			wordForTranslate.setUser(user);
			session.save(wordForTranslate);
			return result;
		}
	}
	
	@Transactional
	public WordTranslate getWordWithNullOriginal(){		
		/*Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);		
		Session session = sessionFactory.getCurrentSession();
		List<WordForTranslate> list = session.createCriteria(WordForTranslate.class).
										add(Restrictions.or(Restrictions.le("date", calendar.getTime()), Restrictions.isNull("date"))).
										setMaxResults(1).
										list();
		if (list.size()==0)
			return null;
		else{
			WordForTranslate wordForTranslate = list.get(0);
			String result = wordForTranslate.getWord();
			wordForTranslate.setDate(new Date());
			wordForTranslate.setUser(user);
			session.save(wordForTranslate);
			return result;
		}*/
		return null;
	}
	
	@Transactional
	public void setWordTranslating(WordTranslate wordTranslate, User user){		
		Session session = sessionFactory.getCurrentSession();
		List<WordForTranslate> list = session.createCriteria(WordForTranslate.class).
										add(Restrictions.eq("word", wordTranslate.getWord())).
										add(Restrictions.eq("user", user)).
										setMaxResults(1).
										list();
		if (list.size()>0){			
			WordForTranslate wordForTranslate = list.get(0);
			session.delete(wordForTranslate);
			session.save(wordTranslate);
		}
	}
	
	@Transactional
	public List<UserBase> getTextBaseRecords(User user){		
		Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(UserBase.class).										
										add(Restrictions.eq("user", user)).										
										list();
	}
	
	@Transactional
	public boolean deleteTextBaseRecords(User user, int id){		
		Session session = sessionFactory.getCurrentSession();		
		UserBase record = (UserBase)session.get(UserBase.class, id);		
		if (record==null)
			return true;
		if (record.getUser().getId()==user.getId()){
			session.createQuery("DELETE UserBaseWord WHERE userBase = :userBase").
								setParameter("userBase", record).
								executeUpdate();
			session.delete(record);
			return true;
		}
		else
			return false;		
	}
	
	@Transactional
	public UserBase getTextBaseRecord(int id){		
		return (UserBase)sessionFactory.getCurrentSession().get(UserBase.class, id);	
	}
	
	@Transactional
	public WordTranslate getWordTranslate(String word){		
		return (WordTranslate)sessionFactory.getCurrentSession().get(WordTranslate.class, word);	
	}
	
	@Transactional
	public void setWordNotTranslate(WordNotTranslate notTranslate){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(notTranslate);
		WordForTranslate forTranslate = (WordForTranslate)session.get(WordForTranslate.class, notTranslate.getWord());
		if (forTranslate!=null)
			session.delete(forTranslate);		
	}
	
	@Transactional
	public List<UserReadyWord> getUserReadyWords(String userBaseID, User user){
		try{
			int pInt = Integer.valueOf(userBaseID);
		}
		catch(Exception e){
			return null;
		}
		List<UserReadyWord> result = new ArrayList<UserReadyWord>();
		Session session = sessionFactory.getCurrentSession();
		String SQL = "SELECT baseword.word, wtranslate.TRANSLATION, wtranslate.TRANSLATIONINFO, uword.COMPLETE, uword.ATTEMPT"+
						" FROM userbaseword baseword" +
						" JOIN userbase ubase ON ubase.ID = baseword.USERBASE" +
						" JOIN wordtranslate wtranslate ON wtranslate.WORD = baseword.WORD" +
						" LEFT JOIN userword uword ON uword.WORD = baseword.WORD AND uword.USER = :user_id"+
						" WHERE baseword.USERBASE = :base_id" +
						" AND (uword.COMPLETE IS NULL OR uword.COMPLETE < 3)";	
		result = session.createSQLQuery(SQL).
				setParameter("user_id", user.getId()).
				setParameter("base_id", userBaseID).
				setResultTransformer(Transformers.aliasToBean(UserReadyWord.class)).list();
		return result;
	}
	
	@Transactional
	public void saveUserWord(UserWord userWord){
		Session session = sessionFactory.getCurrentSession();
		List<UserWord> resUserWords = session.createCriteria(UserWord.class).
									add(Restrictions.eq("user", userWord.getUser())).
									add(Restrictions.eq("word", userWord.getWord())).
									list();
		UserWord resUserWord;
		if (resUserWords.size()>0){
			resUserWord = resUserWords.get(0);
			resUserWord.setAttempt(userWord.getAttempt());
			resUserWord.setComplete(userWord.getComplete());
		}
		else{
			resUserWord = userWord;
		}
		session.saveOrUpdate(resUserWord);	
		/*String[] filedsSet = {"word","user","complete","attempt"};
		String[] filedsSearch = {"word","user"};
		String result = daoService.specialSave(userWord, filedsSet, filedsSearch);
		if (result!=null)
			System.out.println("RESULT = "+result);*/
		
	}	
}
