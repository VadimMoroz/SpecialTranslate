<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page pageEncoding="UTF-8" %>
<div id="user_page_login">
	<form method="post" name="user_page" action="?command=user.register">
		<h2>Форма регистрации</h2>
		<table>
		<tr>
			<td width="100">Имя:</td>
			<td width="200">
				<input type="text" name="user.name" size="40">
				<c:if test="${extension_user.errors.field_name_not_set}">
					<div class="error_field">Имя не указано</div>
				</c:if>
			</td>
		</tr>
		<tr>
			<td>Email:</td>
			<td width="200">
				<input type="text" name="user.email" size="40">
				<c:if test="${extension_user.errors.field_email_not_set}">
					<div class="error_field">Email не указан</div>
				</c:if>
				<c:if test="${extension_user.errors.field_email_ready_use}">
					<div class="error_field">Email уже используется другим пользователем</div>
				</c:if>
				<c:if test="${extension_user.errors.field_email_incorrect}">
					<div class="error_field">Некорректный email</div>
				</c:if>
			</td>
		</tr>
		<tr>
			<td>Пароль:</td>
			<td>
				<input type="password" name="user.password" size="40">
				<c:if test="${extension_user.errors.field_password_not_set}">
					<div class="error_field">Пароль не указан</div>
				</c:if>
			</td>
		</tr>
		<tr>
			<td>Повтор пароля:</td>
			<td>
				<input type="password" name="user.password2" size="40">
				<c:if test="${extension_user.errors.field_password2_not_set}">
					<div class="error_field">Повтор пороля не указан</div>
				</c:if>
				<c:if test="${extension_user.errors.field_passwords_not_equal}">
					<div class="error_field">Пароли не совпадают</div>
				</c:if>
			</td>
		</tr>			
		</table>			
		<div>${userpage_errormessage}</div>
		<div class="link"><a href="/" onclick="user_page.submit();return false;">Регистрация</a></div>		
		<div class="link"><a href="?command=user.trylogin">Вход</a></div>
	</form>	
</div>