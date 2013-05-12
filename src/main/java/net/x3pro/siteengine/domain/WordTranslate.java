package net.x3pro.siteengine.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonRawValue;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonView;
import org.json.JSONArray;
import org.json.JSONObject;

import sun.org.mozilla.javascript.internal.annotations.JSConstructor;
import sun.org.mozilla.javascript.internal.annotations.JSSetter;

import java.util.Date;

@Entity
@Table(name="WORDTRANSLATE")
public class WordTranslate {

	@Id
	@Column(name="WORD")
	private String word;
	
	@Column(name="ORIGINAL")
	private String original;
	
	@Column(name="TRANSLATION")
	private String translation;
	
	@Column(name="TRANSLATIONINFO")
	private String translationInfo;
		
	@Column(name="DATE")
	private Date date;
	
	@ManyToOne
	@JoinColumn(name="USER")
	private User user;

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
	
	@JsonIgnore
	public String getTranslationInfo() {
		return translationInfo;
	}
	
	@JsonRawValue
	public Object getTranslationInfoJson() {
		return translationInfo.trim();
	}

	public void setTranslationInfo(String translationInfo) {
		this.translationInfo = translationInfo;
	}

	@JsonIgnore
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}
}
	