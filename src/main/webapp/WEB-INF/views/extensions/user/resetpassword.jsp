<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page pageEncoding="UTF-8" %>
<div id="user_page_login">
	<form method="post" name="user_page" action="?command=user.resetpassword_s2">	
		<h2>Восстановление пароля</h2>
		<c:if test="${user_resetpassword_key!=''}">
			<input type="hidden" name="key" value="${user_resetpassword_key}"/>
		</c:if>
		<table>
		<tr>			
			<tr>
				<td width="100">Пароль:</td>
				<td width="200">
					<input type="password" name="user.password" size="40">
					<c:if test="${user_field_password_not_set}">
						<div class="error_field">Пароль не указан</div>
					</c:if>
				</td>
			</tr>
			<tr>
				<td>Повтор пароля:</td>
				<td>
					<input type="password" name="user.password2" size="40">
					<c:if test="${user_field_password2_not_set}">
						<div class="error_field">Повтор пороля не указан</div>
					</c:if>
					<c:if test="${user_field_passwords_not_equal}">
						<div class="error_field">Пароли не совпадают</div>
					</c:if>
				</td>
			</tr>					
		</table>
		<c:if test="${user_form_user_not_found}">
			<div class="error_message">Пользователь с таким email и паролем не обнаружен</div>
		</c:if>			
		<div>${userpage_errormessage}</div>	
		<div class="link"><a href="/" onclick="user_page.submit();return false;">Восстановить</a></div>		
		<div class="link"><a href="/">На главную</a></div>
	</form>
</div>