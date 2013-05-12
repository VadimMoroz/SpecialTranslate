package net.x3pro.siteengine.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="USERSUPPORT")
public class UserSupport {
	
	@Id
	@Column(name="ID")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "USERID")
	private User user;
	
	@Column(name="COMMAND")
	private String command;
	
	@Column(name="VALUE")
	private String value;
		
	public void setUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return this.user;
	}
	
	public void setCommand(String command){
		this.command = command;
	}
	
	public void setValue(String value){
		this.value = value;
	}

}
