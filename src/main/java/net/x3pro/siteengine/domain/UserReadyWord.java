package net.x3pro.siteengine.domain;

import org.codehaus.jackson.annotate.JsonRawValue;

public class UserReadyWord {
	private String word = "";
	private String translation = "";
	private String translationInfo = "";
	private Integer complete = 0;
	private Integer attempt = 0;
	
	public String getWord() {
		return word;
	}
	public void setWORD(String word) {
		this.word = word;
	}
	public String getTranslation() {
		return translation;
	}
	public void setTRANSLATION(String translation) {
		this.translation = translation;
	}
	@JsonRawValue
	public String getTranslationInfo() {
		return translationInfo;
	}	
	public void setTRANSLATIONINFO(String translationInfo) {
		this.translationInfo = translationInfo;
	}
	public Integer getComplete() {
		if (complete==null)
			return 0;
		else
			return complete;
	}
	public void setCOMPLETE(Integer complete) {
		this.complete = complete;
	}
	public Integer getAttempt() {
		if (attempt==null)
			return 0;
		else
			return attempt;
	}
	public void setATTEMPT(Integer attempt) {
		this.attempt = attempt;
	}
}
