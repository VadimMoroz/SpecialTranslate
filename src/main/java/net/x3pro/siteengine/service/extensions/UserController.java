package net.x3pro.siteengine.service.extensions;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import net.x3pro.siteengine.dao.UserDAO;
import net.x3pro.siteengine.domain.NewUser;
import net.x3pro.siteengine.domain.User;
import net.x3pro.siteengine.service.EngineController;
import net.x3pro.siteengine.service.EngineExtension;
import net.x3pro.siteengine.service.EngineSupport;
import net.x3pro.siteengine.support.EMail;
import net.x3pro.siteengine.support.ExtensionModelVars;
import net.x3pro.siteengine.support.RequestParametersController;
import net.x3pro.siteengine.support.ResponseJSONObject;
import net.x3pro.siteengine.support.ValueValidator;;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_SESSION)
public class UserController implements EngineExtension{
	public final String extensionName = "user";	
	public final static int ROLE_USER = 10;
	public final static int ROLE_SERVICE_ADMIN = 20;
	public final static int ROLE_SERVICE_MODERATOR = 30;
	public final static int ROLE_ADMIN = 1000;
	
	public final static int PAGE_URL_TYPE_LOGIN = 0;
	public final static int PAGE_URL_TYPE_PROFILE = 1;
	public final static int PAGE_URL_TYPE_LOGOUT = 3;
	
	private final static Map<Integer, String> roleMap = createRoleMap();
	private static Map<Integer, String> createRoleMap(){
		Map<Integer, String> result = new HashMap<Integer, String>();
		result.put(ROLE_USER, "USR");
		result.put(ROLE_SERVICE_ADMIN, "SRVC_ADM");
		result.put(ROLE_SERVICE_MODERATOR, "SRVC_MODER");
		result.put(ROLE_ADMIN, "ADM");
		return Collections.unmodifiableMap(result);
	}
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	EngineSupport engineSupport;
	
	@Autowired
	EngineController engineController;
	
	@Autowired
	EMail eMail;	
	
	private String name = "Not authorized";
	private Boolean authorized = false;
	private String email = "";
	private int state = 0;
	private String role = "";
	private User user;
	
	@Override
	public String getExtensionName() {
		return extensionName;
	}
	
	@Override
	public ResponseJSONObject requestJSON(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
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
	
	public Boolean getAuthorized(){
		return this.authorized;
	}

	public void clear(){
		this.user = null;
		this.authorized = false;
		this.name = "";
		this.email = "";
		this.role = "";
		this.state = 0;
	}
	
	public String getUserPageUrl(int pageUrlType){
		if (pageUrlType==PAGE_URL_TYPE_LOGIN)
			return "?command=user.login";
		else if (pageUrlType==PAGE_URL_TYPE_PROFILE)
			return "";
		else if (pageUrlType==PAGE_URL_TYPE_LOGOUT)
			return "?command=user.logout";
		else
			return "";		
	}
	
	public Boolean hasRole(Integer role){
		String roleStr = roleMap.get(role);
		if (roleStr==null)
			return false;		
		if (this.role.indexOf("\""+roleStr+"\"")<0)
			return false;
		else
			return true;
	}
	
	private Boolean LoadFromBase(String email, String password){
		this.clear();		
		User user = null;		
		user = userDAO.getUserFromEmail(email);
		if (user==null){
			return false;
		}
		else{
			if (user.CheckPassword(password)){
				this.user = user;				
				this.authorized = true;
				this.name = user.getName();
				this.email = user.getEmail();
				this.role = user.getRole();
				this.state = user.getState();				
				return true;
			}
			else
				return false;
		}
	}
		
	private String Activate(String regkey){
		return userDAO.ActivateUser(regkey);
	}
	
	public User getUser() {
		return user;
	}
	
	public String requestAPI(HttpServletRequest request) {
		return null;
	}
	
	public String requestExt(List<String> pagesURI, ExtensionModelVars extensionModelVars,	HttpServletRequest request) {
		String curPage = "";
		if (!pagesURI.isEmpty())
			curPage = pagesURI.get(0);
		if (curPage.equals("smallpage"))
			return "userpagesmall";
		return "index";
	}
	
	public String requestCommand(String[] commandArray, ExtensionModelVars extensionVars, HttpServletRequest request){
		String result = "";
		String command = "";
		String nowUrl = request.getRequestURI();	
		if (commandArray.length>=2)
			command = commandArray[1];
		if (command.equals("login")){
			if (requestLoginCommand(extensionVars, request))
				result = "redirect:"+nowUrl;
		}
		else if (command.equals("logout")) {
			if (requestLogoutCommand(extensionVars, request))
				result = "redirect:/";
		}
		else if (command.equals("register")) {
			if (requestRegisterCommand(extensionVars, request))
				result = "user.messages";
		}
		else if (command.equals("passreset")) {
			if (requestPassResetCommand(extensionVars, request))
				result = "user.messages";
		}
		else if (command.equals("resetpassword_s1")) {
			if (requestResetPasswordCommandStep1(extensionVars, request))
				result = "user.resetpassword_s2";
			else
				result = "user.messages";
		}
		else if (command.equals("resetpassword_s2")) {
			if (requestResetPasswordCommandStep2(extensionVars, request))
				result = "user.messages";
		}
		return result;
	}
	
	@Transactional
	private boolean requestLoginCommand(ExtensionModelVars extensionVars, HttpServletRequest request){	
		RequestParametersController requestParametersController = new RequestParametersController(request); 		
		String email = requestParametersController.getPreparedRequestParam("user.email");
		String password = requestParametersController.getRequestParam("user.password");
		ValueValidator valueValidator = new ValueValidator();		
		if (!email.isEmpty() & !password.isEmpty() & valueValidator.isEmailCorrect(email)){						
			if (this.LoadFromBase(email, password)){
				return true;
			}
			else{
				extensionVars.addError("form_user_not_found");				
				return false;
			}						
		}
		else {			
			if (email.isEmpty())
				extensionVars.addError("field_email_not_set");						
			else if (!valueValidator.isEmailCorrect(email))							
				extensionVars.addError("field_email_incorrect");
			if (password.isEmpty())
				extensionVars.addError("field_password_not_set");
			return false;
		}
	}
	
	@Transactional
	private boolean requestRegisterCommand(ExtensionModelVars extensionVars, HttpServletRequest request){	
		RequestParametersController requestParametersController = new RequestParametersController(request); 		
		String name = requestParametersController.getRequestParam("user.name").trim();
		String email = requestParametersController.getPreparedRequestParam("user.email");
		String password = requestParametersController.getRequestParam("user.password");
		String password2 = requestParametersController.getRequestParam("user.password2");
		ValueValidator valueValidator = new ValueValidator();
		if (!name.isEmpty() & valueValidator.isEmailCorrect(email) & !password.isEmpty() & !password2.isEmpty() & password.equals(password2)){						
			if (userDAO.UserExist(email)){
				extensionVars.addError("field_email_ready_use");
				return false;
			}
			else{
				NewUser newuser = new NewUser();				
				String registerkey = engineSupport.GenerateKey(50);				
				Date pdate = new java.util.Date();
				newuser.CreateNewNewUser(name, email, password, registerkey, pdate, "user");
				userDAO.SaveNewUser(newuser);								
				boolean mailSending;				
				String serviceName = engineController.getParameter("service","name");
				String urlActivation = engineController.getParameter("service","host")+"/activate?regkey="+registerkey;
				String mailSubject = "Активация аккаунта "+serviceName;
				String mailText = "Здравствуйте <b>"+name+"!</b><br>\n"+
						"Ваш email указан при регистрации на сервисе <b>"+serviceName+"</b><br>\n" +
						"Для активации учетной записи необходимо перейти по ссылке <a href=\""+urlActivation+"\">" +
								urlActivation+"</a><br>\n" +
								"<br>\n" +
								"С уважением, Администрация "+serviceName+".";
				mailSending = eMail.SendEmail("izzibboo@gmail.com", email, mailSubject, mailText);
				if (mailSending){
					extensionVars.addError("h2_register");
					extensionVars.addError("message_register_ok");								
				}
				else{								
					extensionVars.addError("message_register_error_send_email");								
				}							
				extensionVars.addVar("message_register_email", email);
				return true;
			}
		}
		else {			
			if (name.isEmpty())
				extensionVars.addError("field_name_not_set");
			if (email.isEmpty())
				extensionVars.addError("field_email_not_set");
			else if (!valueValidator.isEmailCorrect(email))							
				extensionVars.addError("field_email_incorrect");						
			if (password.isEmpty())
				extensionVars.addError("field_password_not_set");
			if (password2.isEmpty())
				extensionVars.addError("field_password2_not_set");
			else if (!password.equals(password2))
				extensionVars.addError("field_passwords_not_equal");
			return false;
		}
	
	}
	
	private boolean requestLogoutCommand(ExtensionModelVars extensionVars, HttpServletRequest request){	
		this.clear();
		return true;	
	}
	
	public boolean requestActivateCommand(ExtensionModelVars extensionVars, HttpServletRequest request, String regkey){
		extensionVars.addError("h2_activation");
		if (regkey.isEmpty()){
			extensionVars.addError("message_activation_error_bad_regkey");
			return true;
		}
		String username = this.Activate(regkey);
		if (username.isEmpty()){
			extensionVars.addError("message_activation_error");
			return true;
		}
		else{
			extensionVars.addVar("activation_name", username);
			extensionVars.addError("message_activation_complete");
			return true;
		}		
	}
	
	@Transactional
	private boolean requestPassResetCommand(ExtensionModelVars extensionVars, HttpServletRequest request){
		RequestParametersController requestParametersController = new RequestParametersController(request); 		
		String email = requestParametersController.getPreparedRequestParam("user.email");		
		ValueValidator valueValidator = new ValueValidator();		
		if (!email.isEmpty() & valueValidator.isEmailCorrect(email)){
			User user = userDAO.getUserFromEmail(email);			
			if (user!=null){
				String username = user.getName();
				String resetkey = engineSupport.GenerateKey(50);
				if (userDAO.SavePassReset(user, resetkey)){					
					boolean mailSending;					
					String serviceName = engineController.getParameter("service","name");
					String urlPassReset = engineController.getParameter("service","host")+"/resetpassword?key="+resetkey;
					String mailSubject = "Восстановления пароля "+serviceName;
					String mailText = "Здравствуйте <b>"+username+"!</b><br>\n"+
							"Вы отправили запрос на восстановление пароля от почтового ящика "+email+".\n" +
							"<br>Для того чтобы задать новый пароль, перейдите по ссылке <a href=\""+urlPassReset+"\">" +
							urlPassReset+"</a><br>\n" +
							"<br>\n" +
							"С уважением, Администрация "+serviceName+".";
					mailSending = eMail.SendEmail("izzibboo@gmail.com", email, mailSubject, mailText);
					if (mailSending){
						extensionVars.addError("h2_passreset");
						extensionVars.addError("message_passreset_complete");
														
					}
					else{								
						extensionVars.addError("message_passreset_error_send_email");
						extensionVars.addVar("message_register_email", email);
					}
					return true;					
				}
				else{
					extensionVars.addError("h2_passreset");
					extensionVars.addError("message_passreset_complete_error");
					return true;
				}
			}
			else{
				extensionVars.addError("form_user_not_found");
				return false;
			}
		}
		else {			
			if (email.isEmpty())
				extensionVars.addError("field_email_not_set");						
			else if (!valueValidator.isEmailCorrect(email))							
				extensionVars.addError("field_email_incorrect");
			return false;
		}		
	}
	
	@Transactional
	private boolean requestResetPasswordCommandStep1(ExtensionModelVars extensionVars, HttpServletRequest request){
		String key = request.getParameter("key");
		if (key==null)
			key = "";
		RequestParametersController requestParametersController = new RequestParametersController(request); 		
		if (userDAO.isPassResetRecordExist(key)){
			extensionVars.addVar("resetpassword_key", key);
			return true;
		}
		else{
			extensionVars.addError("message_resetpassword_error_not_find_key");
			return false;
		}		
	}
	
	@Transactional
	private boolean requestResetPasswordCommandStep2(ExtensionModelVars extensionVars, HttpServletRequest request){
		String key = request.getParameter("key");
		if (key==null)
			key = "";
		extensionVars.addVar("resetpassword_key", key);		
		RequestParametersController requestParametersController = new RequestParametersController(request);
		String password = requestParametersController.getRequestParam("user.password");
		String password2 = requestParametersController.getRequestParam("user.password2");
		if (!password.isEmpty() & password.equals(password2)){
			if (userDAO.isPassResetRecordExist(key)){
				if (userDAO.ResetPassword(key, password)){
					extensionVars.addError("message_resetpassword_complete");
				}
				else{
					extensionVars.addError("message_resetpassword_error_not_reset_password");
				}							
			}
			else{
				extensionVars.addError("message_resetpassword_error_not_find_key");				
			}
			return true;
		}
		else{
			if (password.isEmpty())
				extensionVars.addError("field_password_not_set");
			if (password2.isEmpty())
				extensionVars.addError("field_password2_not_set");
			else if (!password.equals(password2))
				extensionVars.addError("field_passwords_not_equal");
			return false; 
		}		
	}
}
