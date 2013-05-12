package net.x3pro.siteengine.web.administration;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.x3pro.siteengine.service.EngineController;
import net.x3pro.siteengine.service.SessionController;
import net.x3pro.siteengine.service.extensions.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/administration")
@Controller
public class WebAdministrationController {
	
	@Autowired
	EngineController engineController;
	
	@RequestMapping(value = "")
	public String indexPage(Model model, HttpServletRequest request) {
		SessionController currentSession = engineController.getCurrentSessionController();		
		if (currentSession.getCurrentUser().getAuthorized()){
			if (currentSession.getCurrentUser().hasRole(UserController.ROLE_ADMIN)){
				return "administration/index";
			}
			else{
				return "administration/accessdenied";
			}			
		}
		else
			return "ajax/notAuthorized";
	}
	
	@RequestMapping(value = "/templates")
	public String temaplatesPage(Model model, HttpServletRequest request) {
		SessionController currentSession = engineController.getCurrentSessionController();		
		if (currentSession.getCurrentUser().getAuthorized()){
			if (currentSession.getCurrentUser().hasRole(UserController.ROLE_ADMIN)){
				return "administration/templates";
			}
			else{
				return "administration/accessdenied";
			}			
		}
		else
			return "ajax/notAuthorized";
	}
	
	@RequestMapping(value = "/template/{template_name}")
	public String temaplatePage(Model model, HttpServletRequest request, @PathVariable("template_name") String template_name) {
		model.addAttribute("template_name", template_name);
		String filename = request.getSession().getServletContext().getRealPath("WEB-INF")+"\\views\\templates\\"+template_name+".jsp";
		File file = new File(filename);
		if (file.exists()){
			try{
				FileInputStream inFile = new FileInputStream(file.getPath());
				byte[] str = new byte[inFile.available()];
				inFile.read(str);
				String text = new String(str);				
				model.addAttribute("template_text", text);
			}
			catch(Exception e){
				model.addAttribute("error_message", "Ошибка чтения файла. <br>Error: "+e.fillInStackTrace());		
			}								
		}
		else{
			model.addAttribute("error_message", "Файл не найден");
		}
		return "administration/templateEdit";

		/*SessionController currentSession = engineController.getCurrentSessionController();		
		if (currentSession.getCurrentUser().isAutorized()){
			if (currentSession.getCurrentUser().hasRole(UserController.ROLE_ADMIN)){
								
				return "administration/templates";
			}
			else{
				return "administration/accessdenied";
			}			
		}
		else
			return "ajax/notAuthorized";*/
	}

}
