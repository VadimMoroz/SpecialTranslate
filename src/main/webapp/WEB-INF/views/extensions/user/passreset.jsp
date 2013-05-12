<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page pageEncoding="UTF-8" %>
<div id="user_page_login">
	<form method="post" name="user_page" action="?command=user.passreset">		
		<h2>Восстановление пароля</h2>
		<table>
		<tr>
			<td width="100">e-mail:</td>
			<td width="200">
				<input type="text" name="user.email" size="40">
				<c:if test="${user_field_email_not_set}">
					<div class="error_field">Не указан email</div>
				</c:if>
				<c:if test="${user_field_email_incorrect}">
					<div class="error_field">Некорректный email</div>
				</c:if>
			</td>
		</tr>			
		</table>
		<c:if test="${user_form_user_not_found}">
			<div class="error_message">Пользователь с таким email не обнаружен</div>
		</c:if>			
		<div>${userpage_errormessage}</div>	
		<div class="link"><a href="/" onclick="user_page.submit();return false;">Восстановить</a></div>
		<div class="link"><a href="?command=user.tryregister">Регистрация</a></div>
		<div class="link"><a href="?command=user.trylogin">Вход</a></div>
	</form>
</div>