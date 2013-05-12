<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="header">
	<h3 class="title title-single">${site.page.title}</h3>
	<div id="header_right_block">
		<c:import url="/ext/user">
			<c:param name="page">smallpage</c:param>
		</c:import>
	</div>	
	<div class="clear"></div>
</div>
