<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<div style="">
	<div id="info">
		Для запуска перевода необходимо разрешить использование аплета.
		<br>Это необходимо для получения разрешения HTTP запроса на адрес http://translate.yandex.net/</div>
		<div id="applet">
		<applet style="border:px solid #000;" codebase="${site.getParam('host')}/resources/applet/" code="BaseTranslating.class" archive="TranslateAppletStarter.jar" width="230" height="30" title="Помощь в переводе базы слов"
			alt="Помощь в переводе базы слов">
			Помощь в переводе базы слов
			<param name="url"   value="${site.getParam('host')}/api/textbase">
			<param name="sessionid"   value="${site.sessionId}">
			<param name="timeout"   value="2">
			<param name="translator_url" value="http://translate.yandex.net/dicservice.json/lookup?callback=ya_.json.c(12)&ui=ru&lang=en-ru&flags=1&text=%WORD%">
		</applet>
	</div>
</div>