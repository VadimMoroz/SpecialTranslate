<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul id="menu-main" class="menu">
	<c:set var="page_request_uri" value="${site.pageVar.requestUri}"/>		
	<c:forEach var="menuItem" items="${site.getMenu('header')}">
		<c:set var="active_menu_css" value=""/>							
		<c:if test="${page_request_uri==menuItem.url}">
			<c:set var="active_menu_css" value="current-menu-item"/>
		</c:if>				
		<li class="main-menu-item ${active_menu_css}"><a href="${menuItem.url}">${menuItem.title}</a></li>
	</c:forEach>				
</ul>