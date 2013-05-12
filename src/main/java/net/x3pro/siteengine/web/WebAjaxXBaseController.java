package net.x3pro.siteengine.web;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.x3pro.siteengine.dao.XBaseDAO;
import net.x3pro.siteengine.domain.XBaseRecord;
import net.x3pro.siteengine.model.XBaseFormListByOwner;
import net.x3pro.siteengine.service.EngineController;
import net.x3pro.siteengine.service.SessionController;
import net.x3pro.siteengine.service.extensions.UserController;
import net.x3pro.xbase.XBaseController;
import net.x3pro.xbase.XBaseRenderObject;
import net.x3pro.xbase.XBaseSessionStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/ajax/xbase")
@Controller
public class WebAjaxXBaseController {
	
	@Autowired
	XBaseController xBaseController;
	
	@RequestMapping(value = "/getlistbyowner")
	@ResponseBody
	public XBaseFormListByOwner xBaseGetListByOwner(Model model, HttpServletRequest request,
			@RequestParam(value = "elementName", defaultValue="") String elementName,
			@RequestParam(value = "ownerValue", defaultValue="") String ownerValue,
			@RequestParam(value = "sessionId", defaultValue="") String sessionId) {
		XBaseFormListByOwner result = new XBaseFormListByOwner();
		if (sessionId.isEmpty()){
			result.setStatus("ERROR:bad session");
			return result;
		}			
		if (elementName.isEmpty() || ownerValue.isEmpty()){
			result.setStatus("ERROR:bad data");
			return result;
		}		
		HttpSession session = request.getSession(true);
		XBaseSessionStore sessionStore = (XBaseSessionStore)session.getAttribute("xbase_sessionstore_"+sessionId);
		if (sessionStore==null){
			result.setStatus("ERROR:not found session object");
			return result;
		}					
		XBaseRenderObject renderObject = sessionStore.getRenderObject();
		try{
			TreeMap<String, String> list = xBaseController.getListValueByOwner(elementName,ownerValue,renderObject);
			if (list==null){
				result.setStatus("ERROR:list not set");
				return result;
			}
			else{
				result.setList(list);
				result.setStatus("OK");
			}
		}
		catch (Exception e){
			result.setStatus("ERROR:"+e.fillInStackTrace());
			return result;
		}
				
		return result;
	}
	
	/*@RequestMapping(value = "/ext/{extName}")
	public String IndexPage(@PathVariable("extName") String extName, Model model, HttpServletRequest request) {
		SessionController currentSession = engineController.getCurrentSessionController();		
		if (currentSession.getCurrentUser().getAuthorized()){
			model.addAttribute("user_role_user", currentSession.getCurrentUser().hasRole(UserController.ROLE_USER));			
			List<Object> progKeyList = xBaseDAO.getList(ProgKey.class);
			model.addAttribute("progKeyList", progKeyList);
			return "ajax/index";
		}
		else
			return "ajax/notAuthorized";
	}*/
}
