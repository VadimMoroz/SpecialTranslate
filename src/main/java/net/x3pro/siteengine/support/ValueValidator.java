package net.x3pro.siteengine.support;

public class ValueValidator {
	
	public boolean isEmailCorrect(String email){
		int pos = email.indexOf('@');
		if (pos<0)
			return false;
		if (email.length()>pos+1){
			pos = email.indexOf('@', pos+1);		
			if (pos>=0)
				return false;
		}
		pos = email.indexOf(' ');		
		if (pos>=0)
			return false;
		return true;
	}
}
