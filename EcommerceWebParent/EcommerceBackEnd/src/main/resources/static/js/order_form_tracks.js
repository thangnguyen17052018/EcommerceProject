var trackRecordCount;

$(document).ready(function() {
	trackRecordCount = $(".hiddenTrackId").length;
	
	$("#trackList").on("click", ".linkRemoveTrack", function(e) {
		e.preventDefault();
		deleteTrack($(this));
		updateTrackCountNumbers();
	});
	
	$("#track").on("click", "#linkAddTrack", function(e) {
		e.preventDefault();
		addNewTrackRecord();
	});
	
	$("#trackList").on("change", ".dropDownStatus", function(e) {
		dropDownList = $(this);
		rowNumber = dropDownList.attr("rowNumber");
		selectedOption = $("option:selected", dropDownList);
		
		defaultNote = selectedOption.attr("defaultDescription");
		$("#trackNote" + rowNumber).text(defaultNote);
	});	
});

function deleteTrack(link) {
	rowNumber = link.attr('rowNumber');
	$("#rowTrack" + rowNumber).remove();
	$("#emptyLine" + rowNumber).remove();	
}

function updateTrackCountNumbers() {
	$(".divCountTrack").each(function (index, element) {
		element.innerHTML = "" + (index + 1);
	});
}

function addNewTrackRecord() {	
	htmlCode = generateTrackCode();	
	$("#trackList").append(htmlCode);
}

function generateTrackCode() {
	trackRecordCount = $(".hiddenTrackId").length;
	nextCount = trackRecordCount + 1;
	rowId = "rowTrack" + nextCount;
	emptyLineId = "emptyLine" + nextCount;
	trackNoteId = "trackNote" + nextCount;
	currentDateTime = formatCurrentDateTime();
	
	htmlCode = `
			<div class="row border p-3 mx-2" id="${rowId}">
				<input type="hidden" name="trackId" value="0" class="hiddenTrackId" />
				<div class="col-1 text-center">
					<div class="divCountTrack">${nextCount}</div>
					<div class="mt-1"><a class="fas fa-trash icon-purple fa-2x linkRemoveTrack" href="" rowNumber="${nextCount}"></a></div>					
				</div>				
				
				<div class="col-11">
				  <div class="form-group row">
				    <label class="col-form-label col-sm-3">Time:</label>
				    <div class="col-sm-9">
						<input type="datetime-local" name="trackDate" value="${currentDateTime}" class="form-control" required/>						
				    </div>
				  </div>					
				<div class="form-group row">  
				<label class="col-form-label col-sm-3">Status:</label>
				<div class="col-sm-9">
					<select name="trackStatus" class="form-control dropDownStatus" required style="max-width: 250px" rowNumber="${nextCount}">
			`;
	  
	htmlCode += $("#trackStatusOptions").clone().html();
	
	htmlCode += `
				      </select>						
				    </div>
				  </div>
				  <div class="form-group row">
				    <label class="col-form-label col-sm-3">Notes:</label>
				    <div class="col-sm-9">
						<textarea rows="2" cols="10" class="form-control" name="trackNotes" id="${trackNoteId}" style="max-width: 300px" required></textarea>
				    </div>
				  </div>
				  
				</div>				
			</div>	
			<div id="${emptyLineId}" class="row">&nbsp;</div>
	`;
	
	return htmlCode;
}

function formatCurrentDateTime() {
	date = new Date();
	year = date.getFullYear();
	month = date.getMonth() + 1;
	day = date.getDate();
	hour = date.getHours();
	minute = date.getMinutes();
	second = date.getSeconds();
	
	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;
	
	if (hour < 10) hour = "0" + hour;
	if (minute < 10) minute = "0" + minute;
	if (second < 10) second = "0" + second;
	
	return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;
	
}