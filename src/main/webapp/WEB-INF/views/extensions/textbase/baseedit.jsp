<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page pageEncoding="UTF-8" %>

<c:choose>
	<c:when test="${site.command=='textbase.add'}">	
		<%@ include file="add.jsp" %>
	</c:when>	
	<c:when test="${site.command=='textbase.messages'}">
		<%@ include file="messages.jsp" %>
	</c:when>
	<c:otherwise>
		<%@ include file="add.jsp" %>
	</c:otherwise>
</c:choose>
<ul>
<h2>Список сохраненных баз</h2>
<c:forEach var="baseitem" items="${site.ext.textbase.vars.textbases}">
	<li class="basetext_item" value="${baseitem.id}">${baseitem.name}<a class="button_del" href="javascript:void(0);"><img src="/resources/img/delete-icon.png"/></a></li>
</c:forEach>
</ul>