<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<%@ include file="head.jsp" %>
</head>
<body>
	<div id="header_title">
		<h1><a href="/">${site.getParam('name')}</a> - ${site.getParam('title')}</h1>		
	</div>
	<%@ include file="menu.jsp" %>
	<%@ include file="header.jsp" %>
	<div id="content">
		<c:import url="../../pages/${site.page.pageName}.jsp"/>									
	</div>
	
</body>
</html>
