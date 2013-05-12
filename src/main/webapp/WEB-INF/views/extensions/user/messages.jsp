<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page pageEncoding="UTF-8" %>
<div id="user_page_login">
	<h2><c:if test="${user_h2_register}">Регистрация</c:if>
		<c:if test="${user_h2_activation}">Активация email</c:if>
		<c:if test="${user_h2_passreset}">Восстановление пароля</c:if>
		<c:if test="${user_h2_resetpassword}">Восстановление пароля</c:if></h2>		
	<c:if test="${user_message_register_ok}">
		Пользователь зарегистрирован.
		<br/>Для активации необходимо перейти по ссылке полученной на <b>${user_message_register_email}</b>
	</c:if>
	<c:if test="${user_message_register_error_send_email}">
		<div class="error_message">Ошибка отправки email по адресу <b>${user_message_register_email}</b>.
		<br>Попробуйте повторить попытку позже.</div>
	</c:if>
	<c:if test="${user_message_activation_error}">
		<div class="error_message">Ошибка активации email.
		<br>Возможно Ваш email уже активирован. Попробуйте зайти на сайт.</div>
	</c:if>
	<c:if test="${user_message_activation_error_bad_regkey}">
		<div class="error_message">Ошибка активации email.
		<br>Не обнаружен ключ активации.</div>
	</c:if>
	<c:if test="${user_message_activation_complete}">
		Здравствуйте <b>${user_activation_name}</b>
		<br>Ваш email активирован.
		<br>Можете заходить на сайт.
	</c:if>
	
	<c:if test="${user_message_passreset_complete_error}">
		<div class="error_message">Ошибка восстановления пароля.
		<br>Повторите попытку позже.</div>
	</c:if>
	<c:if test="${user_message_passreset_complete_error_bad_key}">
		<div class="error_message">Ошибка восстановления пароля.
		<br>Не обнаружен ключ для восстановления.</div>
	</c:if>
	<c:if test="${user_message_passreset_error_send_email}">
		<div class="error_message">Ошибка отправки email по адресу <b>${user_message_register_email}</b>.
		<br>Попробуйте повторить попытку позже.</div>
	</c:if>
	<c:if test="${user_message_passreset_complete}">		
		<br>На Ваш email отправлено письмо для восстановления пароля.		
	</c:if>
	
	<c:if test="${user_message_resetpassword_error_not_find_key}">
		<div class="error_message">Ключ для восстановления пароля не обнаружен.
		<br>Повторите процедуру восстановления пароля</div>
	</c:if>
	<c:if test="${user_message_resetpassword_error_not_reset_password}">
		<div class="error_message">Ошибка восстановления пароля.
		<br>Попробуйте повторить попытку позже.</div>
	</c:if>
	<c:if test="${user_message_resetpassword_complete}">
		Новый пароль сохранен.
	</c:if>	
	<div class="link" style="margin-top:20px;"><a href="/">На главную</a></div>
</div>