<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page pageEncoding="UTF-8" %>
<div id="baseitems">
	<c:forEach var="baseitem" items="${site.ext.textbase.vars.textbases}">
		<a class="baseitem" href="${baseitem.id}">${baseitem.name}</a>		
	</c:forEach>
</div>
<div id="wordblock">
	<div id="wordblockarea">
		<div id="word">
			<div id="wordvalue"></div>			
		</div>
		<div id="translation" style="display:none;"></div>
		<div id="buttonY" onclick="SetupNextWord();">Y</div>
		<div id="buttonSave" onclick="SaveWords();">SAVE</div>
		<div id="buttonSaveNA" style="display:none;">SAVE</div>
		<div id="buttonN" onclick="ShowTranslate();">N</div>
	</div>
</div>
<div id="workarea">

</div>