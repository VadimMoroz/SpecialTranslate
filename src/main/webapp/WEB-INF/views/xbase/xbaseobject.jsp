<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<div id="xbase_datablock">
	<h2>${xbase_renderobject.title}</h2>
	<c:if test="${xbase_errormessage!=null}">
		<h2>ОШИБКА: ${xbase_errormessage}</h2>
		<br>${xbase_errormessagefull}
	</c:if>
	${requestUrl}
	<c:if test="${xbase_renderobject!=null}">
		<form name="xbase_obj" onsubmit="loadHTTPSourceGet('${xbase_renderobject.targetUrl}?command=ok&'+getRequestBody(this),'${xbase_renderobject.sourceElementName}'); return false;">
			<input type="hidden" name="xbase_session_id" value="${xbase_renderobject.sessionObjectID}"/>
			<table class="xbase_fields_table">
				<c:forEach items="${xbase_renderobject.fields}" var="field">
					<c:set var="field_readonly" value=""/>
					<c:if test="${field.readOnly}">
						<c:set var="field_readonly" value='readonly="readonly"'/>	
					</c:if>
					<tr>
						<td><label>${field.title}:</label>
						<c:if test="${field.notEmpty}"><span class="red_star">*</span></c:if>
						</td>
						<td>						
							<div class="xbase_field_value">
								<c:choose>
									<c:when test="${field.valueType=='STRING'}">
										<input type="text" name="${field.name}" value="${field.value}" style="width:${field.length}px;" ${field_readonly} />
									</c:when>
									<c:when test="${field.valueType=='STRING_MULTILINE'}">
										<textarea name="${field.name}" style="width:${field.length}px;""/>
									</c:when>
									<c:when test="${field.valueType=='INTEGER'}">
										<input type="text" name="${field.name}" style="width:${field.length}px;" value="${field.value}" ${field_readonly}/>
									</c:when> 
									<c:when test="${field.valueType=='FLOAT'}">
										<input type="text" name="${field.name}" style="width:${field.length}px;" size="${field.length}"/>
									</c:when>
									<c:when test="${field.valueType=='DATE'}">
										<input type="text" name="${field.name}" style="width:${field.length}px;"/>
									</c:when>
									<c:when test="${field.valueType=='BOOLEAN'}">
										<input type="checkbox" name="${field.name}" style="width:${field.length}px;" checked="${field.value}"/>							
									</c:when>
									<c:when test="${field.valueType=='OBJECT'}">
										<c:set var="field_subordinate" value=""/>
										<c:if test="${field.subordinate!=''}">
											<c:set var="field_loadbyowner" value='onchange="loadListByOwner(form, \'${field.subordinate}\', this.value, \'${xbase_renderobject.sessionObjectID}\');"'/>	
										</c:if>
										<select name="${field.name}" style="width:${field.length}px;" ${field_loadbyowner}>
											<c:forEach items="${field.list}" var="item">
												<c:set var="xbase_item_selected" value=""/>
												<c:if test="${field.value==item.key}">
													<c:set var="xbase_item_selected" value='selected="selected"'/>													
												</c:if>											
												<option value="${item.key}"  ${xbase_item_selected}>${item.value}</option>
											</c:forEach>
										</select>
									</c:when>
									<c:otherwise>
										<b>Неизвестный тип данных: ${field.valueType}</b> 
									</c:otherwise>
								</c:choose>
							</div>
							<div class="xbase_field_error">${field.errorMessageText}</div>
						</td>
					</tr>							
				</c:forEach>
			</table>
			<input type="hidden" name="xbase_command" value=""/>			
			<c:if test="${xbase_renderobject.buttonOkValue!=''}">
				<input type="submit" class="submit_ok" onclick="form.xbase_command.value='${xbase_renderobject.buttonOkValue}'" value="${xbase_renderobject.buttonOkTitle}"/>
			</c:if>	
		</form>
	</c:if>
</div>