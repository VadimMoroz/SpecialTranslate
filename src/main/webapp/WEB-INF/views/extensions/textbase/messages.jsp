<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page pageEncoding="UTF-8" %>
<div id="user_page_login">		
	<c:if test="${extension_textbase.flags.base_added}">
		База слов добавлена. Она будет обработана через некоторое время.
	</c:if>
	<c:if test="${extension_textbase.flags.not_found_valid_words}">
		<div class="error_message">Не найдено подходящих слов.
		<br>Слова должны быть длиннее 3 сиволов и содержать только символы латинницы или знак "-"</div>
	</c:if>
	<div class="link" style="margin-top:20px;"><a href="/">На главную</a></div>
</div>