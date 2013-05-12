<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!site.curUser.authorized}">
	<%@ include file="../extensions/user/userpage.jsp" %>
</c:if>
<c:if test="${site.curUser.authorized}">	
	<c:import url="/ext/textbase">
		<c:param name="page">baseedit</c:param>
	</c:import>
</c:if>
