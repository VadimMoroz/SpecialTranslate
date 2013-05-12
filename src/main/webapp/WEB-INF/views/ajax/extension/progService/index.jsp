<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
	$(document).ready(function(){
		$("li.progServiceItem input:checkbox").bind("change",function(){
			if ($(this).is(':checked')){
				$(this).parent().parent().addClass("activeItem");	
			}
			else{				
				$(this).parent().parent().removeClass("activeItem");
			}
		}).trigger("change");
		function onError(data){$('#errorMessages').html("<b>ERROR:</b> "+data.statusText).show()};
		$("#formProgSettings").ajaxForm({target: $("#pageContent"),	error: onError});
		$("#formProgKey").ajaxForm({target: $("#pageContent"), error: onError});
	});	
</script>
<form id="formProgKey" method="POST" action="/ajax/ext/prog?command=savekeypassword">
	<table border="0">
		<tr>
			<td>Ключ для программы:</td><td><input type="text" readonly="readonly" value="${curUser.progKey.value}" size="50"></td>
		</tr><tr>		 
			<td>Пароль (новый):</td><td><input type="password" name="password" size=20/><input type="submit" class="button3d small" value="Сохранить пароль">
			<c:if test="${prog_field_password_not_set}">
				<div class="error_field">Пароль не указан</div>
			</c:if>
			<c:if test="${prog_password_ok}">
				<div class="marker_saved">Сохранено</div>
			</c:if></td>
		</tr>
	</table>
</form>
<br>
<h3>Настройка сценариев работы программы</h3>
<form id="formProgSettings" method="POST" action="/ajax/ext/prog?command=saveprogsettings">
	<ul>
		<c:forEach var="progServiceItem" items="${progServiceList}">
			<li class="progServiceItem">
				<div class="head">Сервис: <b>${progServiceItem.name}</b> - ${progServiceItem.description}</div>
				<ul>
					<c:forEach var="templateItem" items="${progServiceItem.templates}">
						<li>Автор шаблона: <b>${templateItem.author.user.name}</b> ${templateItem.author.description}
						<br>Автора заданий:
							<ul>
								<c:forEach var="taskStoresItem" items="${templateItem.taskStores}">									
									<li><label><input type="checkbox" name="${taskStoresItem.code}" <c:if test="${taskStoresItem.checked}">checked="checked"</c:if> /> <b>${taskStoresItem.author.user.name}</b> ${taskStoresItem.author.description}</label></li>
								</c:forEach>
							</ul>
						</li>
					</c:forEach>					
				</ul>
			</li>
		</c:forEach>
	</ul>
	<c:if test="${prog_keysettings_ok}">
				<div class="marker_saved">Сохранено</div>
			</c:if>
	<input type="submit" class="button3d" value="Сохранить настройки"/>
</form>
