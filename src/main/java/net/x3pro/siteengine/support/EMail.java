package net.x3pro.siteengine.support;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.InternetAddress;

import javax.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_REQUEST)
public class EMail {
	private JavaMailSenderImpl mailSender;
	
	public void setMailSender(JavaMailSenderImpl pmailSender){
		this.mailSender = pmailSender;
	}
	
	public boolean SendEmail(String from, String to, String subject, String msg){
		MimeMessage message = mailSender.createMimeMessage();		
		MimeMessageHelper helper = new MimeMessageHelper(message,"UTF-8");		
		Boolean result;
		try{
			subject = new String(subject.getBytes("Cp1251"),"UTF-8");
			msg = new String(msg.getBytes("Cp1251"),"UTF-8");
			helper.setFrom(from);
			helper.setText(to);
			helper.setSubject(subject);
			helper.setText(msg, true);			
			InternetAddress internetAdress = new InternetAddress();
			internetAdress.setAddress(to);
			message.setRecipient(RecipientType.TO, internetAdress);
			mailSender.send(message);			
			result = true;
		}
		catch (Exception e){		
			result = false;
			System.out.println(e.fillInStackTrace());
		}
		return result;
	}
}
