package net.x3pro.siteengine.service.extensions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.hibernate.mapping.Collection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import net.x3pro.siteengine.dao.TextBaseDAO;
import net.x3pro.siteengine.dao.UserDAO;
import net.x3pro.siteengine.domain.NewUser;
import net.x3pro.siteengine.domain.UserBase;
import net.x3pro.siteengine.domain.User;
import net.x3pro.siteengine.domain.UserReadyWord;
import net.x3pro.siteengine.domain.UserWord;
import net.x3pro.siteengine.domain.WordNotTranslate;
import net.x3pro.siteengine.domain.WordTranslate;
import net.x3pro.siteengine.domain.json.WordTranslateJSONModel;
import net.x3pro.siteengine.service.EngineExtension;
import net.x3pro.siteengine.support.EMail;
import net.x3pro.siteengine.support.ExtensionModelVars;
import net.x3pro.siteengine.support.RequestParametersController;
import net.x3pro.siteengine.support.ResponseJSONObject;
import net.x3pro.siteengine.support.ValueValidator;;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_SESSION)
public class TextBaseController implements EngineExtension{
	private String extensionName = "textbase";
	public final static int ROLE_USER = 10;
	public final static int ROLE_SERVICE_ADMIN = 20;
	public final static int ROLE_SERVICE_MODERATOR = 30;
	public final static int ROLE_ADMIN = 1000;
	
	private static final Logger logger = Logger.getLogger(TextBaseController.class);	
	
	@Autowired
	UserController curUser;
	
	@Autowired
	TextBaseDAO textBaseDAO;
	
	@Override
	public String getExtensionName() {
		return extensionName;
	}
	
	public String requestExt(List<String> pagesURI, ExtensionModelVars extensionModelVars,	HttpServletRequest request) {		
		if (!curUser.getAuthorized())
			return "redirect:/"+curUser.getUserPageUrl(UserController.PAGE_URL_TYPE_LOGIN);
		String curPage = "";
		if (!pagesURI.isEmpty())
			curPage = pagesURI.get(0);
		if (curPage.equals("baseedit")){
			extensionModelVars.addVar("textbases", textBaseDAO.getTextBaseRecords(curUser.getUser()));
			return curPage;
		}
		else if (curPage.equals("teach")||curPage.equals("teachmobile")){
			extensionModelVars.addVar("textbases", textBaseDAO.getTextBaseRecords(curUser.getUser()));
			return curPage;
		}
		return "index";
	}	
		
	public ResponseJSONObject requestJSON(HttpServletRequest request) {
		ResponseJSONObject result = new ResponseJSONObject();			
		if (!curUser.getAuthorized()){
			result.setResultError("NOT_AUTHORIZED");
			return result;
		};
		result.setResultError("BAD_COMMAND");		
		RequestParametersController requestParametersController = new RequestParametersController(request); 		
		String command = requestParametersController.getPreparedRequestCommand("command");		
		if (command.equals("DELETE")){
			String idValue = requestParametersController.getRequestParam("id");			
			int id = 0;
			try{
				id = Integer.parseInt(idValue);
			}
			catch(Exception e){
				result.setResultError("BAD_ID");
				return result;
			}
			if (textBaseDAO.deleteTextBaseRecords(curUser.getUser(), id))
				result.setResultOK();				
			else
				result.setResultError("CANNOT_DELETE");				
		}
		else if (command.equals("GETBASE")){
			String idValue = requestParametersController.getRequestParam("id");			
			int id = 0;
			try{
				id = Integer.parseInt(idValue);
			}
			catch(Exception e){
				result.setResultError("BAD_ID");
				return result;
			}
			UserBase textBaseRecord = textBaseDAO.getTextBaseRecord(id);
			if (textBaseRecord==null){
				result.setResultError("RECORD_NOT_FOUND");
				return result;
			}
			if (textBaseRecord.getUser().getId()!=curUser.getUser().getId()){
				result.setResultError("RECORD_NOT_FOUND");
				return result;
			}
			result.setResultOK();		
			result.addData("words", textBaseDAO.getUserReadyWords(idValue,curUser.getUser()));			
		}
		else if (command.equals("SAVEWORDS")){
			try{
				JSONArray wordsArray = new JSONArray(request.getParameter("words"));
				if (wordsArray==null){
					result.setResultError("EMPTY_WORDS");
					return result;
				}
				for (int i=0; i<wordsArray.length(); i++) {
					JSONObject jsonObject = wordsArray.getJSONObject(i);
					UserWord userWord = new UserWord();
					userWord.setWord(jsonObject.getString("word"));
					userWord.setUser(curUser.getUser());
					userWord.setAttempt(jsonObject.getInt("attempt"));
					userWord.setComplete(jsonObject.getInt("complete"));
					textBaseDAO.saveUserWord(userWord);					
					result.setResultOK();
				}
			}
			catch(Exception e){
				e.printStackTrace();
				result.setResultError("NOT_CAN_GET_WORDS");				
				return result;
			}
		}
		return result;
	}
	
	public String requestAPI(HttpServletRequest request){
		if (!curUser.getAuthorized())
			return "ERROR:USER_IS_NOT_AUTHORIZED";
		RequestParametersController requestParametersController = new RequestParametersController(request); 		
		String command = requestParametersController.getPreparedRequestCommand("command");		
		if (command.equals("GET_WORD_FOR_TRANSLATE")){
			String word = getWordForTranslate();
			if (word==null)
				return "OK:NEED_WAIT";
			else
				return "OK:WORD="+word;
		}		
		else if (command.equals("SET_WORD_TRANSLATING")){			
			String word = requestParametersController.getPreparedRequestParam("word");			
			String wordTranslateInfo = requestParametersController.getRequestParam("wordtranslateinfo");			
			
			logger.debug("wordTranslateInfo = "+wordTranslateInfo);
			
			if (!word.isEmpty() & !wordTranslateInfo.isEmpty()){
				HashMap<String, String> translationResult = getTranslateDetailsFromYandex(word,wordTranslateInfo);
				String Res = translationResult.get("RES");
				if (Res!=null){
					if (Res.equals("OK")){
						String translation = translationResult.get("TRANSLATION");
						String translationInfo = translationResult.get("TRANSLATIONINFO");
						if (word.equals(translation.toLowerCase())){
							WordNotTranslate notTranslate = new WordNotTranslate();
							notTranslate.setWord(word);
							notTranslate.setUser(curUser.getUser());
							notTranslate.setDate(new Date());
							textBaseDAO.setWordNotTranslate(notTranslate);
						}
						else{
							WordTranslate wordTranslate = new WordTranslate();
							wordTranslate.setWord(word);
							wordTranslate.setDate(new Date());				
							wordTranslate.setUser(curUser.getUser());
							wordTranslate.setTranslation(translation);
							wordTranslate.setTranslationInfo(translationInfo);
							textBaseDAO.setWordTranslating(wordTranslate, curUser.getUser());
						}
						return "OK";
					}
					else if (Res.equals("WORD_IS_NOT_SIMPLE")){
						return "OK";
					}
					else{
						return "ERROR:UNKNOWN_RESULT";
					}
				}
				else{
					return "ERROR:WRONG_WORD";
				}							
			}
			else
			return "ERROR:EMPTY_DATA";
		}
		else
			return "ERROR:BAD_COMMAND";
	}

	private String getWordForTranslate(){
		String word = textBaseDAO.getWordForTranslating(curUser.getUser());
		return word;
	}		
	
	private HashMap<String, String> getTranslateDetailsFromYandex(String word, String wordTranslateInfo){		
		HashMap<String, String> result = new HashMap<String, String>();		
		try{			
			int pos = wordTranslateInfo.indexOf('{');
			wordTranslateInfo = wordTranslateInfo.substring(pos);
			wordTranslateInfo = wordTranslateInfo.substring(0,wordTranslateInfo.length()-1);
			JSONObject jsonObject = new JSONObject(wordTranslateInfo);
			
			String translation = null;
			String wordOrigin = null;
			String transcription = null;
			JSONObject jsonObjectOut = new JSONObject();
			JSONArray jsonArrayOut = new JSONArray();
			
			JSONArray jsonArray = jsonObject.getJSONArray("def");
			if (jsonArray.length()==0){
				result.put("RES", "WORD_IS_NOT_SIMPLE");
				return result;
			}
			for (int i=0; i<jsonArray.length(); i++){
				JSONObject jsonObjectTranslating = jsonArray.getJSONObject(i);
				String translationType = jsonObjectTranslating.getString("pos");
				if (i==0){
					wordOrigin = jsonObjectTranslating.getString("text");
					transcription = jsonObjectTranslating.getString("ts");
				}
				JSONArray jsonArrayOutTranslations = new JSONArray();
				JSONArray jsonArrayTranslating = jsonObjectTranslating.getJSONArray("tr");
				for (int j=0; j<jsonArrayTranslating.length();j++){
					JSONObject jsonObjectTranslatingWord = jsonArrayTranslating.getJSONObject(j);					
					String translationPart = jsonObjectTranslatingWord.getString("text");					
					if (i==0 && j==0){
						translation = translationPart;
					}
					jsonArrayOutTranslations.put(translationPart);
				}
				JSONObject jsonObjectTranslatingPart = new JSONObject();
				jsonObjectTranslatingPart.put("pos", translationType);
				jsonObjectTranslatingPart.put("tr", jsonArrayOutTranslations);
				jsonArrayOut.put(jsonObjectTranslatingPart);
			}
			jsonObjectOut.put("ts", transcription);
			jsonObjectOut.put("trs", jsonArrayOut);
			
			result.put("WORDORIGIN", wordOrigin);
			result.put("TRANSLATION", translation);						
			result.put("TRANSLATIONINFO", jsonObjectOut.toString());
			result.put("RES", "OK");
		}
		catch(Exception e){
			e.printStackTrace();
			result.put("ERROR", "DATA_ERROR");
			return result;
		}
		
		return result;
	}
	
	public String requestCommand(String[] commandArray, ExtensionModelVars extensionVars, HttpServletRequest request){
		if (!curUser.getAuthorized())
			return "redirect:/";
		
		String result = "";
		String command = "";
		String nowUrl = request.getRequestURI();
				
		if (commandArray.length>=2)
			command = commandArray[1];
		if (command.equals("add")){
			if (requestAddCommand(extensionVars, request))
				result = "textbase.messages";
			else
				result = "textbase.add";
		}	
		return result;
	}
		
	@Transactional
	private boolean requestAddCommand(ExtensionModelVars extensionVars, HttpServletRequest request){
		RequestParametersController requestParametersController = new RequestParametersController(request); 		
		String basename = requestParametersController.getPreparedRequestParam("basename");
		String basetext = requestParametersController.getRequestParam("basetext");
		if (!basename.isEmpty() & !basetext.isEmpty()){
			HashSet<String> wordCollection = getWordCollection(basetext);
			if (wordCollection.size()==0){
				extensionVars.addFlag("not_found_valid_words");
			}
			else{
				UserBase textBaseRecord = new UserBase();
				textBaseRecord.setName(basename);				
				textBaseRecord.setUser(curUser.getUser());				
				textBaseDAO.saveBaseText(textBaseRecord, wordCollection);
				textBaseDAO.saveWords(wordCollection);
				extensionVars.addFlag("base_added");
			}			
			return true;
		}
		else{
			if (basename.isEmpty())
				extensionVars.addError("field_basename_isempty");
			if (basetext.isEmpty())
				extensionVars.addError("field_basetext_isempty");			
			return false;
		}
	}
	
	private HashSet<String> getWordCollection(String basetext){
		HashSet<String> result = new HashSet<String>();
		String tmp = basetext;		
		tmp = tmp.replaceAll("\\.", "\\s").replaceAll("\\,", "\\s");				
		String[] words = tmp.split("\\s+");		
		for (String word : words) {
			if (word.length()<4)
				continue;
			if (word.toUpperCase().equals(word))
				continue;			
			word = word.toLowerCase();
			if (!hasOnlyEnglishSimbols(word))
				continue;
			result.add(word);			
		}		
		return result;
	}
	
	private boolean hasOnlyEnglishSimbols(String word){
		for (int i=0; i<word.length(); i++){
			int ord = (int)word.charAt(i);
			if (!((ord>=97 & ord<=122) || ord==45))
				return false;
		}
		return true;
	}

}
