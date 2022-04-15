$(document).ready(function() {
	$("#yVariableId").on("focus", function() {
		var materialId = $("#materialId").val();
		$("#yVariableId").empty();
		jQuery('#materialComposition').empty();
		jQuery('#execelContent').empty();
		jQuery('#storeIntoDB').empty();

		if (materialId != '') {
			console.log("Got MaterialID " + materialId + " Now fetching variables for the material");
			$.get("getVariablesForBiMaterial?materialId=" + materialId, function(response) {

				$('#yVariableId').empty()
				var jsonValue = JSON.parse(response);
				var variableOptionsString = "";
				for (var i = 0; i < jsonValue.data.length; i++) {
					variableOptionsString += "<OPTION value = " + jsonValue.data[i].id + ">" + jsonValue.data[i].name + "</OPTION>";
					var o = new Option(jsonValue.data[i].name, jsonValue.data[i].id);
					$(o).html(jsonValue.data[i].name);
				}
				console.log(variableOptionsString);
			})
		}
	});

	$("#delete-button").on("click", function() {
		$.alert({
			title: 'Success!',
			content: 'Successfully deleted the record!',
		});
		t
	});

	$("#file" ).on( "change", function() {
		var materialId = $("#materialId").val();
		var discreteGroupId = $("#discreteGroupId").val();
		var authorName = $("#authorName").val();
		var year = $("#year").val();
	    var variableId = $("#yVariableId").val();
	    var xVariableId1 = $("#xVariableId1").val();
	    var file = $("#file").val();
	    var datasetName = $("#datasetName").val();
	    var patternAuthor = /^[a-zA-Z\s]*$/;
	    var patternYear = /^[0-9]*$/;
	    var re = /^.*[a-zA-Z].*$/;
	    jQuery('#execelContent').empty();

        var formData = $("#form1")[0];
		var formdata = new FormData(formData);
		formdata.append('variableCount', variableCount);
		console.log( formData );
	
	    if(materialId == '' && discreteGroupId == ''){
			$("#file").val("");
			console.log( "Must select a Material OR Group before uploading the Excel File.");
			$("#materialId").focus();
			$.alert({
				title: 'Alert!',
				content: 'Must select a Material OR Group, before uploading the Excel File!',
			});
			return;
	    }
	    if(authorName == '' || authorName == null){
			$("#file").val("");
			console.log( "Must enter the author Name before uploading the Excel File.");
			$("#authorName").focus();
			$.alert({
				title: 'Alert!',
				content: 'Must enter the author Name before uploading the Excel File!',
			});
			return;
	    }

	    if(year.length !== 4 || !patternYear.test(year)){
			$("#file").val("");
	    	console.log( "Length of year is invalid. OR year must be Integer value");
	    	$("#year").focus();
	    	$.alert({
				title: 'Alert!',
				content: 'Length of Year must be 4 OR year must be Integer value!',
			});
	    	return;
	    }
	    if(year == '' || year == null){
			$("#file").val("");
			console.log( "Must enter the Experiment Year before uploading the Excel File.");
			$("#year").focus();
			$.alert({
				title: 'Alert!',
				content: 'Must enter the Experiment Year before uploading the Excel File!',
			});
			return;
	    }
	    if(variableId == '' || variableId == null){
			$("#file").val("");
			console.log( "Must select a Y-Variable before uploading the Excel File.");
			$("#yVariableId").focus();
			$.alert({
				title: 'Alert!',
				content: 'Must select a Y-Variable before uploading the Excel File!',
			});
			return;
	    }
	    if(xVariableId1 == '' || xVariableId1 == null){
			$("#file").val("");
			console.log( "Must select a X-Variable before uploading the Excel File.");
			$("#xVariableId1").focus();
			$.alert({
				title: 'Alert!',
				content: 'Must select a X-Variable before uploading the Excel File!',
			});
			return;
	    }
	    if(!patternAuthor.test(authorName)) {
			$("#file").val("");
			$("#errorMessage").html("Author Name must be character");
		}
		if(!re.test(datasetName)) {
			$("#file").val("");
			$("#errorMessage").html("DatasetName contain at least one alphabet");
		}
		else {
	    var fileLocation = file.replace(/^.*(\\|\/|\:)/, '');
	    
		if(fileLocation != '' ){
			jQuery('#execelContent').empty();
			$.ajax({
		    type: "POST",
			 url:"getExcelContent", 
			data: formdata,
		    enctype: 'multipart/form-data',
            processData: false,  
            contentType: false,
            cache: false,
			success: function( response ) {
				var jsonValue = JSON.parse(response);
				console.log(jsonValue);
			if(jsonValue['error']){
			   $("#execelContent").html(jsonValue['error']);
			   $('#execelContent').css("color", "red");
			   $("#errorMessage").html("");
			   $("#file").val("");
			 }
			 if(jsonValue['variableLength']){
			   $("#execelContent").html(jsonValue['variableLength']);
			   $('#execelContent').css("color", "red");
			   $("#errorMessage").html("");
			   $("#file").val("");
			 }
			 if(jsonValue['variableName']){
			   $("#execelContent").html(jsonValue['variableName']);
			   $('#execelContent').css("color", "red");
			   $("#errorMessage").html("");
			   $("#file").val("");
			 }
			 if(jsonValue['Yvariable']){
			   $("#execelContent").html(jsonValue['Yvariable']);
			   $('#execelContent').css("color", "red");
			   $("#errorMessage").html("");
			   $("#file").val("");
			 }
			 if(jsonValue['errorMsg']){
			   $("#errorMessage").html(jsonValue['errorMsg']);
			   $('#errorMessage').css("color", "red");
			   $("#file").val("");
			 }
			if(jsonValue['data']){
				$("#errorMessage").html("");
				var inptString = "<table class='table table-hover table-striped' style='width: "+tableWidth+"%'>\n"
				
				for( var i=0;i<Object.keys(jsonValue.data).length;i++){
					inptString+="<tr>\n" + 
					"";
					for(var j = 0; j < jsonValue.data[i].length; j++) {
						
						if(jsonValue.data[i][j].content != ""){	
								inptString += "<td style='border:1px solid black;color:black;height:2px;width:10px;'>\n"+
                  				jsonValue.data[i][j].content+
                				"</td>\n";
						}
					}
					
					inptString+="</tr>\n" + 
					"";
				}
				inptString += "\n" + "</table>";
				jQuery('#execelContent').append(inptString);
				$("#file").val(file);
		}
		}
		});
		
		} 
		}
	});
	
	$("#aVariableId").on("focus", function() {
		var materialId = $("#materialId").val();
		var variableId = $("#yVariableId").val();
		console.log("aVariableId got focus selected materialId:variabledId " + materialId + ":" + variableId);

		$("#aVariableId").empty();

		if (materialId == '') {
			console.log("Must select a material before selecting a variable.");
			$("#materialId").focus();
			$.alert({
				title: 'Alert!',
				content: 'Must select a material and variable before selecting a dependent variable.!',
			});
			return;
		}

		if (variableId == '' || variableId == null) {
			console.log("Must select a Variable efore selecting a Dependent Variable.");
			$("#yVariableId").focus();
			$.alert({
				title: 'Alert!',
				content: 'Must select a Variable before selecting a Dependent Variable.!',
			});
			return;
		}



		$.get("getVariablesInFormula?materialId=" + materialId + "&variableId=" + variableId, function(response) {
			$('#aVariableId').empty()

			var jsonValue = JSON.parse(response);
			console.log(jsonValue);
			var variableOptionsString = "";
			for (var i = 0; i < jsonValue.data.bioVariables.length; i++) {
				variableOptionsString += "<OPTION value = " + jsonValue.data.bioVariables[i].id + ">" + jsonValue.data.bioVariables[i].name + "</OPTION>";
				var o = new Option(jsonValue.data.bioVariables[i].name, jsonValue.data.bioVariables[i].id);
				$(o).html(jsonValue.data.bioVariables[i].name);
				$("#aVariableId").append(o);
			}
			for (var i = 0; i < jsonValue.data.bioComposition.length; i++) {
				variableOptionsString += "<OPTION value = " + jsonValue.data.bioComposition[i].id + ">" + jsonValue.data.bioComposition[i].tagName + "</OPTION>";
				var o = new Option(jsonValue.data.bioComposition[i].tagName, jsonValue.data.bioComposition[i].id);
				$(o).html(jsonValue.data.bioComposition[i].tagName);
				$("#aVariableId").append(o);
			}

			console.log(variableOptionsString);
		})
	});
	$("#cancelUploadFile").on("click", function() {
		jQuery('#execelContent').empty();
		$("#file").val("");
	});
});

function showDiscreteGroupSearchBar() {
	if ($('#discreteGroup').is(":checked")) {
		if ($('.groupSearchBar').css('display', 'none')) {
			$('.groupSearchBar').css('display', 'block')
			$('.materialSearchBar').css('display', 'none')
			$("#discreteMaterial").prop("checked", false);
			$("#materialId").val("");
		}
	} else {
		if ($('.groupSearchBar').css('display', 'block')) {
			$('.groupSearchBar').css('display', 'none')
			$('.materialSearchBar').css('display', 'block')
			$("#discreteMaterial").prop("checked", true);
		}
	}
}

function showDiscreteMaterialSearchBar() {
	if ($('#discreteMaterial').is(":checked")) {
		if ($('.materialSearchBar').css('display', 'none')) {
			$('.materialSearchBar').css('display', 'block')
			$('.groupSearchBar').css('display', 'none')
			$("#discreteGroup").prop("checked", false);
			$("#discreteGroupId").val("");
		}
	} else {
		if ($('.materialSearchBar').css('display', 'block')) {
			$('.materialSearchBar').css('display', 'none')
			$('.groupSearchBar').css('display', 'block')
			$("#discreteGroup").prop("checked", true);
		}
	}
}
function getAllYVariables() {
		$("#file").val("");
		$.get("getAllVariables", function(response) {
			var allVariables = JSON.parse(response);
			var variableOptionsString = "";
			for (var i = 0; i < allVariables.data.length; i++) {
				variableOptionsString += "<OPTION value = " + allVariables.data[i].id + ">" + allVariables.data[i].name + "</OPTION>";
			}
			$("#yVariableId").append(variableOptionsString);
		});
	}

/*function getAllXVariables(id){
		$("#file").val("");
		$.get("getAllVariables", function(response) {
			var allVariables = JSON.parse(response);
			var variableOptionsString = "";
			for (var i = 0; i < allVariables.data.length; i++) {
				variableOptionsString += "<OPTION value = " + allVariables.data[i].id + ">" + allVariables.data[i].name + "</OPTION>";				
			}
			if(id === "xVariableId2")
				$("#xVariableId2").append(variableOptionsString);
			if(id === "xVariableId3")
				$("#xVariableId3").append(variableOptionsString);
			if(id === "xVariableId4")
				$("#xVariableId4").append(variableOptionsString);
			if(id === "xVariableId5")
				$("#xVariableId5").append(variableOptionsString);
			if(id === "xVariableId6")
				$("#xVariableId6").append(variableOptionsString);
			if(id === "xVariableId7")
				$("#xVariableId7").append(variableOptionsString);
			if(id === "xVariableId8")
				$("#xVariableId8").append(variableOptionsString);
			if(id === "xVariableId9")
				$("#xVariableId9").append(variableOptionsString);
			if(id === "xVariableId10")
				$("#xVariableId10").append(variableOptionsString);
		});
}*/
var map = new Map();
function getAllVariables() {
	$("#file").val("");
	$.get("getAllVariables", function(response) {
		var allVariables = JSON.parse(response);
		var variableOptionsString = "";
		for (var i = 0; i < allVariables.data.length; i++) {
			variableOptionsString += "<OPTION value = " + allVariables.data[i].id + ">" + allVariables.data[i].name + "</OPTION>";
			map.set(allVariables.data[i].id, allVariables.data[i].name);
		}
		$("#xVariableId1").append(variableOptionsString);
	});
}

function getAllXVariables(ids) {
	if(ids == 2){
		var arrIndex1 = $("#xVariableId1 option:selected").val();
		map.delete(parseInt(arrIndex1));
		var itr = map.keys();  
		var itrs = map.values();
		var variableOptions = "";
        for(i = 0; i < map.size; i++)  {  
	        variableOptions += "<OPTION value = " + itr.next().value + ">" + itrs.next().value + "</OPTION>";
        }  
        $("#xVariableId2").append(variableOptions);
	}
	if(ids == 3){
		var arrIndex2 = $("#xVariableId2 option:selected").val();
		map.delete(parseInt(arrIndex2));
		var itr = map.keys();  
		var itrs = map.values();
		var variableOptions = "";
        for(i = 0; i < map.size; i++)  {  
	        variableOptions += "<OPTION value = " + itr.next().value + ">" + itrs.next().value + "</OPTION>";
        }  
        $("#xVariableId3").append(variableOptions);
	}
	if(ids == 4){
		var arrIndex3 = $("#xVariableId3 option:selected").val();
		map.delete(parseInt(arrIndex3));
		var itr = map.keys();  
		var itrs = map.values();
		var variableOptions = "";
        for(i = 0; i < map.size; i++)  {  
	        variableOptions += "<OPTION value = " + itr.next().value + ">" + itrs.next().value + "</OPTION>";
        }  
		$("#xVariableId4").append(variableOptions);
	}
	if(ids == 5){
		var arrIndex4 = $("#xVariableId4 option:selected").val();
		map.delete(parseInt(arrIndex4));
		var itr = map.keys();  
		var itrs = map.values();
		var variableOptions = "";
        for(i = 0; i < map.size; i++)  {  
	        variableOptions += "<OPTION value = " + itr.next().value + ">" + itrs.next().value + "</OPTION>";
        }  
		$("#xVariableId5").append(variableOptions);
	}
	if(ids == 6){
		var arrIndex5 = $("#xVariableId5 option:selected").val();
		map.delete(parseInt(arrIndex5));
		var itr = map.keys();  
		var itrs = map.values();
		var variableOptions = "";
        for(i = 0; i < map.size; i++)  {  
	        variableOptions += "<OPTION value = " + itr.next().value + ">" + itrs.next().value + "</OPTION>";
        }  
		$("#xVariableId6").append(variableOptions);
	}
	if(ids == 7){
		var arrIndex6 = $("#xVariableId6 option:selected").val();
		map.delete(parseInt(arrIndex6));
		var itr = map.keys();  
		var itrs = map.values();
		var variableOptions = "";
        for(i = 0; i < map.size; i++)  {  
	        variableOptions += "<OPTION value = " + itr.next().value + ">" + itrs.next().value + "</OPTION>";
        }  
		$("#xVariableId7").append(variableOptions);
	}
	if(ids == 8){
		var arrIndex7 = $("#xVariableId7 option:selected").val();
		map.delete(parseInt(arrIndex7));
		var itr = map.keys();  
		var itrs = map.values();
		var variableOptions = "";
        for(i = 0; i < map.size; i++)  {  
	        variableOptions += "<OPTION value = " + itr.next().value + ">" + itrs.next().value + "</OPTION>";
        }  
		$("#xVariableId8").append(variableOptions);
	}	
	if(ids == 9){
		var arrIndex8 = $("#xVariableId8 option:selected").val();
		map.delete(parseInt(arrIndex8));
		var itr = map.keys();  
		var itrs = map.values();
		var variableOptions = "";
        for(i = 0; i < map.size; i++)  {  
	        variableOptions += "<OPTION value = " + itr.next().value + ">" + itrs.next().value + "</OPTION>";
        }  
		$("#xVariableId9").append(variableOptions);
	}
	if(ids == 10){
		var arrIndex9 = $("#xVariableId9 option:selected").val();
		map.delete(parseInt(arrIndex9));
		var itr = map.keys();  
		var itrs = map.values();
		var variableOptions = "";
        for(i = 0; i < map.size; i++)  {  
	        variableOptions += "<OPTION value = " + itr.next().value + ">" + itrs.next().value + "</OPTION>";
        }  
		$("#xVariableId10").append(variableOptions);
	}
}

function setValueInHiddenField(id) {
	var id1 = jQuery("#xVariableId1 option:selected").text();
	$("#xVariableName1").val(id1);

	var id2 = jQuery("#xVariableId2 option:selected").text();
	$("#xVariableName2").val(id2);

	var id3 = jQuery("#xVariableId3 option:selected").text();
	$("#xVariableName3").val(id3);

	var id4 = jQuery("#xVariableId4 option:selected").text();
	$("#xVariableName4").val(id4);

	var id5 = jQuery("#xVariableId5 option:selected").text();
	$("#xVariableName5").val(id5);

	var id6 = jQuery("#xVariableId6 option:selected").text();
	$("#xVariableName6").val(id6);

	var id7 = jQuery("#xVariableId7 option:selected").text();
	$("#xVariableName7").val(id7);

	var id8 = jQuery("#xVariableId8 option:selected").text();
	$("#xVariableName8").val(id8);

	var id9 = jQuery("#xVariableId9 option:selected").text();
	$("#xVariableName9").val(id9);

	var id10 = jQuery("#xVariableId10 option:selected").text();
	$("#xVariableName10").val(id10);
	
}

jQuery(document).ready(function() {
	$('#yVariableId').change(function(){
	var yId = jQuery("#yVariableId option:selected").text();
	$("#yVariableName").val(yId);
  
  });
});

let variableCount = 1;
let tableWidth = 17; 
let xValue = 8.5;
function addXVariable() {
	$("#file").val("");
	jQuery('#execelContent').empty();
	var value = parseInt(document.getElementById('number').value, 10);
	value = isNaN(value) ? 1 : value;
	value++;
	document.getElementById('number').value = value;
	var i = $("#number").val();
	if (i < 11) {
		variableCount = i;
		tableWidth += xValue;
		$('.variableName:last').before('<div class="col-sm-1"><form:label  class="text-info text-infoo" path="xVariableId' + i + '" >X-axis variable(' + i + '):</form:label></div>');//onclick = "getAllXVariables(this.id)"
		$('.block:last').before('<div class="col-sm-1"><select class="form-control" id="xVariableId' + i + '" name = "xVariableId' + i + '" value="${bioDiscreetDataForm.xVariableId' + i + '}" onclick = "getAllXVariables('+i+')" onchange = "setValueInHiddenField('+i+')"></select></div>');
	} else {
		$.alert({
			title: 'Alert!',
			content: 'Only 10 X-Variables should be selected!',
		});
		return;
	}
}

// discreet data set details popup
$(document).ready(function() {
	$(".clickedDiscreetDataPopop").on("click", function() {
		var dataSet = $(this).closest("td").find("#detailDatasetName").val();
		if (dataSet != null) {
			$.get("getDiscreeDataSetUsingName?dataSet=" + dataSet, function(response) {
				var dataSetDetails = "";
				var xYVariablesHeading = "";
				var xYVariablesName = "";
				var detailsDiscreet = JSON.parse(response);				
				console.log(JSON.stringify(detailsDiscreet));
				for (var k = 0; k < detailsDiscreet.discreetDataVariableName.length; k++) {
					$('#xYVariablesName > tr > td').remove();
				}
				for (var i = 0; i < detailsDiscreet.data.length; i++) {
					dataSetDetails += "<TR><TD>DatasetName</TD><TD>" + detailsDiscreet.data[i].datasetName + "</TD></TR>" +
						"<TR><TD>Author Name</TD><TD>" + detailsDiscreet.data[i].author_name + "</TD></TR><TR><TD>Year</TD><TD>" + detailsDiscreet.data[i].year + "</TD></TR><TR id='discreetDataMaterialId'><TD>Material Id</TD><TD id='materialIdOfDiscreet'></TD></TR>" +
						"<TR><TD>Group Id</TD><TD>" + detailsDiscreet.data[i].groupId + "</TD></TR><TR><TD>Added By</TD><TD>" + detailsDiscreet.data[i].addedBy + "</TD></TR><TR><TD>Updated By</TD><TD>" + detailsDiscreet.data[i].updatedBy + "</TD></TR>" +
						"<TR><TD>Created At</TD><TD>" + detailsDiscreet.data[i].createdAt + "</TD></TR><TR><TD>Updated At</TD><TD>" + detailsDiscreet.data[i].updatedAt + "</TD></TR>";
					break;
				}
				$("#dynamicDataset").html(dataSetDetails);
				var number = 23
				var lastElement = detailsDiscreet.materialIdCommaSeparated[detailsDiscreet.materialIdCommaSeparated.length - 1];
				for (var i = 0; i < detailsDiscreet.materialIdCommaSeparated.length; i++) {
					if(i < number) {
						if(detailsDiscreet.materialIdCommaSeparated[i]==lastElement)
							$("#materialIdOfDiscreet").append(detailsDiscreet.materialIdCommaSeparated[i])
						else						
   							$("#materialIdOfDiscreet").append(detailsDiscreet.materialIdCommaSeparated[i] + ",")
   					} else if(i == number) {
						$("#materialIdOfDiscreet").append("<br>")
						$("#materialIdOfDiscreet").append(detailsDiscreet.materialIdCommaSeparated[i] + ",")
						number = number+number;
					}						
 				}				
				for (var i = 1; i < detailsDiscreet.discreetDataVariableName.length; i++) {
					xYVariablesHeading +="<TD class='header'>XVariable"+i+"</TD>"
					$("#xYVariablesHeading").html(xYVariablesHeading);
				}
				$("#xYVariablesHeading").append("<TD class='header'>YVariable</TD>");
				for (var j = 0; j < detailsDiscreet.discreetDataVariableName.length; j++) {
					xYVariablesName +="<TD class='header'>"+detailsDiscreet.discreetDataVariableName[j]+"</TD>"
					$("#xYVariableName").html(xYVariablesName);
				}	
				for (var k = 0; k < detailsDiscreet.data.length; k++) {
					$('.discreetDiv:last').before("<TR class='header' id='xYVariablesValue"+k+"'></TR>");
				}				
				var count = 0;
				var length = detailsDiscreet.discreetDataVariableName.length;
				for (var i = 0; i < detailsDiscreet.data.length; i++) {									
					for (var k = count; k < count + length; k++) {
						if(detailsDiscreet.discreetDataSetValue[k] != null)
							$("#xYVariablesValue"+i+"").append("<TD class='header' id='discreetDataSetValue'>"+detailsDiscreet.discreetDataSetValue[k]+"</TD>")					
					}
					count = count + detailsDiscreet.discreetDataVariableName.length;
				}				
				$(".abc").show();
				$(".header").hide();
				$("#myModal").modal('show');

			});
		}
	});
	$('#materialId').inputpicker({
		url: 'getMaterialsWithFormula',
		fields: ['id', 'shortDesc'],
		fieldText: 'shortDesc',
		fieldValue: 'id',
		headShow: true,
		filterOpen: true,
		autoOpen: true
	});

	$('#discreteGroupId').inputpicker({
		url: 'getBioGroups',
		fields: ['id', 'groupName'],
		fieldText: 'groupName',
		fieldValue: 'id',
		headShow: true,
		filterOpen: true,
		autoOpen: true
	});
	
	$("#addDiscreetData").on("click", function() {
		console.log("start")
		var uploadedFile = $("#file").val();
		var formData = $("#form1")[0];
		var formdata = new FormData(formData);
		if(uploadedFile != ""){
			$.ajax({
			    type: "POST",
				url:"addDiscreetData", 
				data: formdata,
			    enctype: 'multipart/form-data',
	            processData: false,  
	            contentType: false,
	            cache: false,
				success: function( response ) {
					var jsonValue = JSON.parse(response);
					if(jsonValue['error']){
					   	$("#errorMessage").html(jsonValue['error']);
				 	}
				 	if(jsonValue['msg']){
					   	$("#successMessage").html(jsonValue['msg']);
					   	$("#form1").hide();
					   	$("headingOfDiscreet").hide();
				 	}
				} 
			});
		} else {
			$("#errorMessage").html("Please upload file!");
		}
	});
	
	$(function () {
        $(".header").hide();
$("#discretePlusButton").click(function () {
            $('.buttonInactive').not(this).removeClass('buttonInactive');
            $(this).toggleClass('buttonActive');
            if ($(this).hasClass("buttonActive")) {
                $(".header").show();
            }
            else {
                $(".header").hide();
            }
        });
});
});

$(document).ready(function() {	
	$(".editDiscreetDataSet").on("click",function(){
		var userName = $("#userName").val();
		var dataSet = $(this).closest("td").find("#dataSetName").val();		
		if(typeof userName != 'undefined'){			
			if(dataSet != null){	
				$.get("getDiscreeDataSetUsingName?dataSet=" + dataSet , function(response) {
					var editDetailsDiscreet = JSON.parse(response);				
					console.log(JSON.stringify(editDetailsDiscreet));									
				});
			} else{
				$("#editDiscreetDataSet").focus();
				$.alert({
					title: 'Alert!',
					content: 'No Discreet Data',
				});
			}			
		} else {
			window.location.replace("login"); 
		}
	});
});
