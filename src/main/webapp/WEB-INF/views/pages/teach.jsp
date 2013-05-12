<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!site.curUser.authorized}">
	<%@ include file="../extensions/user/userpage.jsp" %>
</c:if>
<c:if test="${site.curUser.authorized}">
	<a href="/teachmobile">Мобильная версия</a>
	<h2>Выберите базу слов</h2>
	<div style="width: 500;height: 500;font-size: 0.4em;">
	<c:import url="/ext/textbase">
		<c:param name="page">teach</c:param>
	</c:import>
	</div>
</c:if>
