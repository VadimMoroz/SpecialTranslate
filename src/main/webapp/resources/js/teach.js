var wordsArray = [];
var wordsBeforeArray = [];
var currentWord = null;
var currentPos = 0;
var showOriginal = true;

function loadData(words){
	var length = words.length;	
	for (var i=0;i<length;i++ ){
		element = words[i];
		word = {word: element.word,
				translation: element.translation,
				complete: element.complete,
				attempt: element.attempt,
				complete_: element.complete,
				attempt_: element.attempt,
				show: true};			
		var translationInfo = element.translationInfo;
		var length2 = translationInfo.length;
		var translationTypes = [];			
		for (var j=0;j<length2;j++){
			var element2 = translationInfo[j];
			var translationType = element2[0];
			var length3 = element2.length;
			var translationParts = [];
			for (var l=1;l<length3;l++){
				var element3 = element2[l];
				translationParts[l-1] = {translation: element3[0],
										rating: element3[1]};												
			}			
			translationTypes[j] = {type:translationType, parts:translationParts};
		}
		word.translationInfo = translationTypes;
		wordsArray[i] = word;						
	}
}
 
function LoadWordsBefore(){
	wordsBeforeArray = [];
	for (var i=0;i<wordsArray.length;i++){
		var pos = Math.floor(Math.random()*wordsBeforeArray.length);
		wordsBeforeArray.splice(pos,0,wordsArray[i]);
	}
}

function SetEmptyWord(){
	$("#wordvalue").html("Все слова данной базы пройдены.");
}

function SetupNextWord(){
	if (wordsBeforeArray.length==0){
		SetEmptyWord();
		return;
	}
	currentWord = null;
	var currentWord_ = null;
	var nowPos = currentPos;
	while (currentWord==null) {		
		currentWord_ = wordsBeforeArray[currentPos];
		currentPos++;
		if (currentPos>=wordsBeforeArray.length){
			currentPos = 0;
		}
		if (currentWord_.complete<3){
			currentWord = currentWord_;
		}
		else{			
			if (nowPos==currentPos){
				break;
			}
		}
	}
	if (currentWord==null){
		SetEmptyWord();
		return;
	}
	currentWord.complete++;				
	
	$("#wordvalue").html("<div class='wordinfo'>просмотров: <b>"+currentWord.complete+"</b> попыток: <b>"+currentWord.attempt+"</b></div>"+currentWord.word);
	var translationHtml = "<div class='smallword'>"+currentWord.word+"</div><div class='translationword'>"
						+currentWord.translation+"</div><ul class='wordtypes'>";
	var translationInfo = currentWord.translationInfo;
	for (var i=0;i<translationInfo.length;i++) {
		var translationType = translationInfo[i];
		translationHtml+="<li class='wordtype'>"+translationType.type+"<ul>";
		var translationParts = translationType.parts;
		for (var j=0;j<translationParts.length;j++) {
			var translationPart = "<li><div class='ratingblock'><img class='rating' src='/resources/img/progress.jpg' width='"+Math.floor(translationParts[j].rating*60)+"%'/></div>"+translationParts[j].translation+"</li>";			
			translationHtml+=translationPart;
		}
		translationHtml+="</ul></li>";
	}		
	$("#translation").html(translationHtml);
	$("#word").show();
	$("#translation").hide();	
}	

function ShowTranslate(){
	if (currentWord==null){
		return;
	}
	currentWord.attempt++;
	currentWord.complete = 0;
	$("#word").hide();
	$("#translation").show();	
}

function SaveWords(){
	if (wordsBeforeArray.length==0){
		return;
	}
	var wordsReadyArray = [];
	for (var i=0;i<wordsBeforeArray.length;i++){
		currentWord_ = wordsBeforeArray[i];
		if (currentWord_.complete!=currentWord_.complete_||currentWord_.attempt!=currentWord_.attempt_){
			wordsReadyArray.push({word:currentWord_.word,
									complete:currentWord_.complete,
									attempt:currentWord_.attempt});
		}
	}
	$('#buttonSaveNA').show();
	$('#buttonSave').hide();
	$.getJSON('json/textbase',
			{command:"savewords",words:JSON.stringify(wordsReadyArray)})
			.done(function(json){
					if (json.result=="OK"){
						alert("SAVED");
						$('#buttonSaveNA').hide();
						$('#buttonSave').show();
					}
					else{
						var code = json.code;							
						if (code=="BAD_COMMAND"){
							alert("Ошибка. неправильная команда");
						}
						else if (code=="NOT_AUTHORIZED"){
							alert("Ошибка. необходимо авторизироваться");
						}
						else
							alert("Ошибка. "+json.code);
					};
				});
}


$(document).ready(function(){
	$("a.baseitem").click(function(){
		wordsArray = [];
		wordsBeforeArray = [];
		wordsReadyArray = [];
		currentPos = 0;
		var id = $(this).attr("href");		
		if (id=="")
			return;
		$.getJSON('json/textbase',
				{command:"getbase",id:id})
				.done(function(json){
					if (json.result=="OK"){
						loadData(json.data.words);
						LoadWordsBefore();
						SetupNextWord();
					}
					else{
						var code = json.code;							
						if (code=="BAD_COMMAND"){
							alert("Ошибка. неправильная команда");
						}
						else if (code=="NOT_AUTHORIZED"){
							alert("Ошибка. необходимо авторизироваться");
						}
						else if (code=="BAD_ID"){
							alert("Ошибка. не получен идентификатор записи базы слов");
						}
						else if (code=="CANNOT_DELETE"){
							alert("Ошибка удаления базы слов");
						}
						else
							alert("Ошибка. "+json.code);
					};
				});
		return false;
	});		
	
	$("li.basetext_item a.button_del").click(function(){		
		var curparent = $(this).parent();
		$.getJSON('json/textbase',
				{command:"delete",id:$(this).parent().attr("value")})
				.done(function(json){				
						if (json.result=="OK"){
							curparent.remove();
						}
						else{
							var code = json.code;							
							if (code=="BAD_COMMAND"){
								alert("Ошибка. неправильная команда");
							}
							else if (code=="NOT_AUTHORIZED"){
								alert("Ошибка. необходимо авторизироваться");
							}
							else if (code=="BAD_ID"){
								alert("Ошибка. не получен идентификатор записи базы слов");
							}
							else if (code=="CANNOT_DELETE"){
								alert("Ошибка удаления базы слов");
							}
							else
								alert("Ошибка. "+json.code);
						};											
		});			
	});
	
	$("li.basetext_item").click(function(){
		$("#basename").val($(this).text());		
	});
});

function prepareMobileVersion(){
	$(document).ready(function(){
		var h = $("#buttonY").width();
		$("#buttonY").height(h);
		$("#buttonSave").height(h);
		$("#buttonN").height(h);
	});
}