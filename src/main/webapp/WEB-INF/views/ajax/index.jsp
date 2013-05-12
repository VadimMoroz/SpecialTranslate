<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="workblock">
	<c:if test="${user_role_user}">	
		<div class="workitem"><b>Баланс:</b> 100 izm (izzi money)</div>
		<div class="workitem"><b>Ключи для программы izzi.exe:</b> <a class="button3d_small" href="javascript:loadHTTPSourceGet('/ajax/progkey/edit?command=add','ajaxmainblock')">+</a>
			<table>
			<c:forEach var="item" items="${progKeyList}">
				<tr>
					<td><div class="workitemrec">${item.recTitle}</div></td>
					<td><a class="button3d_small" href="javascript:loadHTTPSourceGet('/ajax/progkey/edit?command=setbalance&key=${item.recTitle}','ajaxmainblock')">$</a></td>
					<td><a class="button3d_small" href="javascript:loadHTTPSourceGet('/ajax/progkey/del?key=${item.recTitle}','ajaxmainblock','Вы действительно хотите удалить этот ключ?')">x</a></td>									
				</tr>	
			</c:forEach>
			</table>
		</div>
	</c:if>
	<c:if test="${!user_role_user}">
		<b>Не настроены роли пользователя.</b>
	</c:if>
</div>
