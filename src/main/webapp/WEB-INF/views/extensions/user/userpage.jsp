<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div style="padding-left: 10%">
<c:choose>
	<c:when test="${site.command=='user.trylogin'}">	
		<%@ include file="login.jsp" %>
	</c:when>	
	<c:when test="${site.command=='user.trypassreset' or site.command=='user.passreset'}">
		<%@ include file="passreset.jsp" %>
	</c:when>
	<c:when test="${site.command=='user.tryregister' or site.command=='user.register'}">
		<%@ include file="register.jsp" %>
	</c:when>
	<c:when test="${site.command=='user.messages'}">	
		<%@ include file="messages.jsp" %>
	</c:when>
	<c:when test="${site.command=='user.resetpassword_s1' or site.command=='user.resetpassword_s2'}">	
		<%@ include file="resetpassword.jsp" %>
	</c:when>
	<c:otherwise>
		<%@ include file="login.jsp" %>
	</c:otherwise>
</c:choose>
</div>