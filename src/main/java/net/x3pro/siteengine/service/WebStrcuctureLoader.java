package net.x3pro.siteengine.service;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Controller
public class WebStrcuctureLoader {
	
	@Autowired
	EngineController engineController;
	
	public void setFileStruct(Resource resource){
		try{			
			File file = resource.getFile();
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			document.getDocumentElement().normalize();			
			Node rootNode = document.getFirstChild();			
			NodeList rootNodeList = rootNode.getChildNodes();			
			NodeList parametersNodeList = null;
			NodeList menusNodeList = null;
			NodeList pagesNodeList = null;
			for (int i=0; i<rootNodeList.getLength(); i++){
				Node iNode = rootNodeList.item(i);	
				if (iNode.getNodeType()!=Node.ELEMENT_NODE)
					continue;
				if (iNode.getNodeName().equals("parameters"))
					parametersNodeList = iNode.getChildNodes();
				if (iNode.getNodeName().equals("menus"))
					menusNodeList = iNode.getChildNodes();
				if (iNode.getNodeName().equals("pages"))
					pagesNodeList = iNode.getChildNodes();
			}
			if (parametersNodeList!=null)				
				engineController.loadParameters(parametersNodeList);
			if (parametersNodeList!=null)				
				engineController.loadMenus(menusNodeList);
			if (pagesNodeList!=null)				
				engineController.loadPages(pagesNodeList);			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
