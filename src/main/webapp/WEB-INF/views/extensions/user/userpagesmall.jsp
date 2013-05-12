<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page pageEncoding="UTF-8" %>
<div id="user_page_login_small">
<c:if test="${site.curUser.authorized}">
	<div style="padding: 5px;">
		Здравствуйте, <b>${site.curUser.name}</b>		
		<div class="link"><a id="user_page_login_small_button" href="/?command=user.logout">Выход</a></div>
	</div>
	<a id="button_help" href="/autotranslate" target="blank" 
	onclick="window.open('/autotranslate','pmw','scrollbars=1,top=0,left=0,resizable=1,width=480,height=130'); return false;" >Помощь в переводе
		<span>(Запуск аплета)</span></a>	 
	 
</c:if>
<c:if test="${!site.curUser.authorized}">	
	<form method="post" name="user_page_small" action="/?command=user.login">
		<table>
		<tr>
			<td width="70">e-mail:</td>
			<td width="200"><input type="text" name="user.email" size="20"></td>
		</tr>
		<tr>
			<td>Пароль:</td>
			<td><input type="password" name="user.password" size="20"></td>
		</tr>			
		</table>		
		<div class="link"><a href="/" onclick="user_page_small.submit();return false;">Вход</a></div>
		<div class="link"><a href="/?command=user.tryregister">Регистрация</a></div>
		<div class="link"><a href="/?command=user.trypassreset">Забыли пароль?</a></div>
	</form>
</c:if>
</div>