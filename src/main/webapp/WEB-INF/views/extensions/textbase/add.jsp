<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page pageEncoding="UTF-8" %>
<form name="wordbase_new" method="post" action="?command=textbase.add">
	<div>Имя базы:<input type="text" name="basename" id="basename" size="50"/></div>
	<c:if test="${site.ext.textbase.errors.field_basename_isempty}">
		<div class="error_field">Имя не указано</div>
	</c:if>
	<div>Текст для перевода:<br>
	<textarea rows="10" cols="48" name="basetext"></textarea></div>
	<c:if test="${site.ext.textbase.errors.field_basetext_isempty}">
		<div class="error_field">Текст не указан</div>
	</c:if>
	<div><input type="submit" value="Сохранить" /></div>
</form>