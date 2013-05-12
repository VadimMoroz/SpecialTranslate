package net.x3pro.siteengine.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NEWUSERS")
public class NewUser {
	
	@Id
	@Column(name="EMAIL")
	private String email;

	@Column(name="NAME")
	private String name;	
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="REGISTERKEY")
	private String registerkey;
	
	@Column(name="DATE")
	private Date date;
	
	@Column(name="ROLE")
	private String role;
	
	public String getEmail(){
		return this.email;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Date getdate(){
		return this.date;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public String getRole(){
		return this.role;
	}
	
	public void CreateNewNewUser(String pname, String pemail, String ppassword, String pregisterkey, Date pdate, String prole){
		this.name = pname;
		this.email = pemail;
		this.password = ppassword;
		this.registerkey = pregisterkey;
		this.date = pdate;
		this.role = prole;		
	}
}
