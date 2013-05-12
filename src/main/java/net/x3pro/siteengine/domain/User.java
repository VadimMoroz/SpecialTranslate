package net.x3pro.siteengine.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name="USERS")
public class User {
	
	@Id
	@Column(name="ID")
	private int id;

	@Column(name="NAME")
	private String name;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="STATE")
	private int state;
	
	@Column(name="ROLE")
	private String role;
	
	@Column(name="DATE")
	private Date date;
	
	public int getId() {
		return id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String getRole(){
		return this.role;
	}
	
	public int getState(){
		return this.state;
	}
	
	public Boolean CheckPassword(String ppassword){
		return this.password.equals(ppassword);
	}
	
	public void CreateNewUser(String name, String email, String password, String role, int state, Date date){
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;		
		this.state = state;
		this.date = date;
	}
	
	public void setPassword(String password){
		this.password = password;
	}

}
	