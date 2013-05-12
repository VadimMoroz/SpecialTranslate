package net.x3pro.siteengine.dao;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
class DAOUtil {
	
	public String prepareSqlParameter(String value){
		return prepareSqlParameter(value, true);
	}
	
	/**
	 * @param needConvertToLowerCase
	 */
	public String prepareSqlParameter(String value, boolean needConvertToLowerCase){		
		String result = value.trim();
		if (needConvertToLowerCase)
			result = result.toLowerCase();
		value = value.replaceAll("\'", "");
		value = value.replaceAll("\"", "");
		value = value.replaceAll("\n", "");		
		return value;
	}	
}
