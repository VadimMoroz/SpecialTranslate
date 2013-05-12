package net.x3pro.siteengine.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name="WORDFORTRANSLATE")
public class WordForTranslate {

	@Id
	@Column(name="WORD")
	private String word;
	
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
	