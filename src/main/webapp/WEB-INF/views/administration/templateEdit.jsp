<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2>Редактирование шаблона: ${template_name}</h2>
<c:if test="${error_message!=null}"><div class="error_msg">${error_message}</div></c:if>
<c:if test="${template_text!=null}">
	<form>
		<input type="hidden" name="template_name" value="${template_name}"/>
		<textarea rows="20" cols="40" name="template_text">
			${template_text}
		</textarea>
	</form>
</c:if>