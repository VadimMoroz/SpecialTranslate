package net.x3pro.siteengine.domain.json;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import net.x3pro.siteengine.domain.User;

public class WordTranslateJSONModel {	
	private String word;
	private String translation;
	private String translationInfo;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	public String getTranslationInfo() {
		return translationInfo;
	}
	public void setTranslationInfo(String translationInfo) {
		this.translationInfo = translationInfo;
	}
}
