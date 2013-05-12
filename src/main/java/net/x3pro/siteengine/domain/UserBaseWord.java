package net.x3pro.siteengine.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name="USERBASEWORD")
public class UserBaseWord {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private int id;
	
	@Column(name="WORD")
	private String word;
	
	@ManyToOne
	@JoinColumn(name="USERBASE")
	private UserBase userBase;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public UserBase getUserBase() {
		return userBase;
	}

	public void setUserBase(UserBase userBase) {
		this.userBase = userBase;
	}
}
	