<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>${site.getParam('tittle')}</title>
	<link rel="stylesheet" type="text/css" media="all" href="resources/css/simple.css" />	
	<c:forEach var="headerItem" items="${site.page.headers}">
		${headerItem}
	</c:forEach>
</head>
<body>
	<c:import url="../../pages/${site.page.pageName}.jsp"/>	
</body>
</html>
