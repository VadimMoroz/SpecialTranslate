<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!site.curUser.authorized}">
	<%@ include file="../extensions/user/userpage.jsp" %>
</c:if>
<c:if test="${site.curUser.authorized}">
	<script type="text/javascript">
		prepareMobileVersion();
	</script>
	<div id="screen">
		<c:import url="/ext/textbase">
			<c:param name="page">teach</c:param>
		</c:import>
	</div>
</c:if>
