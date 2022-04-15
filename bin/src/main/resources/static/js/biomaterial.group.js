$(document).ready(function() {
	$('.materialGroupList').on('change', function() {
		$('.materialGroupList').not(this).prop('checked', false);
	});
});

	function showMaterialSearchBar() {
		if($('.searchMaterialBar').is(":checked")){
			if($('#searchBarOfMaterial').css('display','none')){
				$('#searchBarOfMaterial').css('display','block')
			}
			if($('#searchBarOfGroup').css('display','block')){
				$('#searchBarOfGroup').css('display','none')
			}
			$("#displayGroupName").hide();
			$("#showExpandedMaterial").hide();	
			$("#showTr1").show();
			$("#showTr2").show();
			$("#noGroupError").empty();	
			$('#bioMaterialName').val("");
			$("option[class='val']").remove();
		} else {
			if($('#searchBarOfMaterial').css('display','block')){
				$('#searchBarOfMaterial').css('display','none')
			}
		}
	}

function showGroupSearchBar() {
		if($('.groupList').is(":checked")){
			if($('#searchBarOfGroup').css('display','none')){
				$('#searchBarOfGroup').css('display','block')
			}
			if($('#searchBarOfMaterial').css('display','block')){
				$('#searchBarOfMaterial').css('display','none')
			}
			$("#showDiv1").hide();
			$("#showExpandedMaterial").hide();
			$("#showTr2").show();
			$("#groupTr").show();
			$("#noMaterialError").empty();
			$('#bioMaterialGroupName').val("");
		} else {
			if($('#searchBarOfGroup').css('display','block')){
				$('#searchBarOfGroup').css('display','none')
			}
		}
	}

function changeColor() {
	var name = document.getElementById('groupName').value;
	if (name != null) {
		groupName.style.color = '#9ACD32';
	}
}
function typeName() {
	groupName.style.color = '#000000';
}

$(document).ready(function() {
	var coll = document.getElementsByClassName("collapsible");
	var i;
	for (i = 0; i < coll.length; i++) {
		coll[i].addEventListener("click", function() {
			var groupName = $("#groupName").val();
			$.ajax({
				type: 'GET',
				url: 'expandForExsistingBioMaterial/groupName/' + groupName,
				success: function(data) {
					data = JSON.parse(data);
					var html = '';
					data.forEach(function(materiallist) {
						html += '<li>' + materiallist.shortDesc + '</li><br>';
					});
					html = '<ul class= "removebullet">' + html + '</ul>';
					document.querySelector('#materialNameList').innerHTML = html;
				}
			});
		});
	}
});

var expandSelectedGroupName = "";
var removeSelectedGroupName = "";
var removeSelectedMaterialName = "";
var selectedGroupNameArray = [];
let selectedMaterialAndGroupMaterialsArray = [];
var selectedMaterialNameArray = [];
let selectedMaterialIdArray = [];
let selectedMaterialSize = "";
let exsistingGroupMaterialsArray = [];
let materialsNameList = "";
let exsistingMaterialsNameList = [];
let btnDeleteid = [];
function searchGroup(){
	jQuery('#displayGroup').empty();
	jQuery('#materialNameList').empty();
	var bioMaterialGroupName = $("#bioMaterialGroupName").val();
	$(".error").remove();
	if (bioMaterialGroupName.length < 1) {
		$('#bioMaterialGroupName').after('<span class="error">Please Enter Bio-Material Name</span>');
	} else {
		$.get("searchResultOfBioMaterialInGroupedMaterial?bioMaterialGroupName=" + bioMaterialGroupName, function(response) {
			console.log(response);
			var jsonValue = JSON.parse(response);
			if (jsonValue['data']) {
				var groupNameList = "";
				for (var i = 0; i < jsonValue.data.length; i++) {
					groupNameList += '<tr class="searchGroup"><td>' + jsonValue.data[i] + '<input type="hidden" name="groupMaterialId" id="groupMaterialId" value=' + jsonValue.materialId[i] + '></td><td style="width:50%"></td><td><input type="checkbox" class="groupNameCheckbox" id="groupNameCheckbox' + jsonValue.id[i] + '" name="groupNameCheckbox"/><input type="hidden" name="groupId" id="groupId" value="' + jsonValue.id[i] + '"></td></tr><br>';
					$('#displayGroup').html(groupNameList);
				}
				$("#noGroupError").empty();
				$('#groupTr').show();
			} 
			if (jsonValue['error']) {
				$("#noGroupError").html(jsonValue['error']);
				$('#noGroupError').css("color", "red");
				$('#groupTr').hide();
			}
		})
		$("#displayGroupName").show();
	}
}

$(document).ready(function() {
$('#displayGroup').on('change', '.searchGroup', function() {
	if ($(this).closest('tr').find('.groupNameCheckbox').is(':checked')) {
		var selectedGroupName = $(this).closest("tr").find('td').text();
		var groupMaterialId = $(this).closest("tr").find('#groupMaterialId').val();
		var groupId = $(this).closest("tr").find('#groupId').val();
		selectedGroupNameArray.push(selectedGroupName);
		var count = getOccurrence(selectedGroupNameArray, selectedGroupName);
		if (count == 1) {
			selectedMaterialAndGroupMaterialsArray.push(groupMaterialId);
			$('#showSelectedMaterial').append('<tr class="showGroupInColor materialList"><td><button type="button" style="font-size:x-large" class="expandButton buttonInactive unstyled-button" id= "expandButton" value="yes">&#65291;</button><input type="hidden" name="groupNameId" id="groupNameId" value=' + groupId + '></td><td style="width: 100%; margin-right:20px">' + selectedGroupName + '<input type="hidden" name="selectedBioMaterial" id="groupMaterialId" value="' + groupMaterialId + '"></td><div><td><button class="crossButtonGroup" id="btnDeleteid" name="btnDeletename" >&times;</button><input type="hidden" name="groupNameId" id="groupNameId" value=' + groupId + '></td></div></tr><tr class="bioMaterialNameListt bioMaterialNameList'+groupId+'"><td><div style="display:none;" class="content scroll" id="materialNameList'+groupId+'"></td></tr><br>');
			$("#showDiv2").show();
			$("#showDiv3").show();
		} else {
			$("#showDiv2").show();
			$("#showDiv3").show();
		}
	} else {
		$("#showDiv2").show();
		$("#showDiv3").show();
	}
});
});
function searchMaterial(){
	$("#showDiv1").hide();
	jQuery('#displayMaterial').empty();
	var firstIndex = $("#bioMaterialName").val();
	var process = $("#process option:selected").text();
	if(process == 'No Selection'){
		process = "";
	}
	var form = $("#form option:selected").text();
	if(form == 'No Selection'){
		form = "";
	}
	$(".error").remove();
	if (firstIndex.length < 1) {
		$('#bioMaterialName').after('<span class="error">This field is required</span>');
	}
	else {
		process = process.trim();
		form = form.trim();
		$.get("searchBioMaterialsForGrouping1?firstIndex=" + firstIndex + "&process=" + encodeURIComponent(process) + "&form=" + encodeURIComponent(form), function(response) {
			console.log(response);
			var jsonValue = JSON.parse(response);
			if (jsonValue['data']) {
				var materialList = "";
				for (var i = 0; i < jsonValue.data.length; i++) {
					materialList += '<tr class="getMaterial"><td>'
						+ jsonValue.data[i]
						+ '<input type="hidden" name="selectedBioMaterial" id="matId" value=' + jsonValue.msg[i] + '></td><td><input id="checkValue' + jsonValue.msg[i] + '" type="checkbox" class="checkboxbutton" name="selectedBioMaterial"/><br></td></tr>';
					$("#displayMaterial").html(materialList);
				}
				$("#noMaterialError").empty();
				$('#showTr1').show();
			}
			if (jsonValue['error']) {
				$("#noMaterialError").html(jsonValue['error']);
				$('#noMaterialError').css("color", "red");
				$('#showTr1').hide();
			}
		})
		$('#showDiv1').show();
	}

}

$(document).ready(function() {
$('#displayMaterial')
	.on('change', '.getMaterial', function() {
		if ($(this).closest('tr').find('.checkboxbutton').is(':checked')) {
			var id = $(this).closest("tr").find('td').text();
			var getId = $(this).closest("tr").find('#matId').val();
			selectedMaterialNameArray.push(id);
			var count = getOccurrence(selectedMaterialNameArray, id);
			if (count == 1) {
				selectedMaterialIdArray.push(getId);
				selectedMaterialSize = selectedMaterialIdArray.length;
				selectedMaterialAndGroupMaterialsArray.push(getId);
				var storeData = '<tr class="materialList"><td style="width: 100%; margin-right:20px">'
					+ id
					+ '<input type="hidden" name="selectedBioMaterial" id="matId" value="' + getId + '"></td><td></td><div><td><button class="crossButton" id="btnDeleteid" name="btnDeletename">&times;</button></div></td></tr>';

				$('#showSelectedMaterial').append(storeData);
				$("#showDiv2").show();
				$("#showDiv3").show();
			} else {
				$("#showDiv2").show();
				$("#showDiv3").show();
			}
		}
		else {
			$("#showDiv2").show();
			$("#showDiv3").show();
		}
	});
});

$(document).ready(function() {
$("#showSelectedMaterial").on('click', '.crossButtonGroup', function() {
	var groupNameId = $(this).closest("tr").find('#groupNameId').val();
	var groupMaterialId = $(this).closest("tr").find('#groupMaterialId').val();
	removeSelectedGroupName = $(this).closest("tr").find('td').text();
	removeSelectedGroupName = removeSelectedGroupName.substr(1);
	removeSelectedGroupName = removeSelectedGroupName.substr(0, removeSelectedGroupName.length - 1);
	$("#groupNameCheckbox" + groupNameId).removeAttr('checked');
	$(this).closest('tr').remove();
	$('.bioMaterialNameList'+groupNameId+'').remove();
	selectedGroupNameArray = arrayRemove(selectedGroupNameArray, removeSelectedGroupName);
	selectedMaterialAndGroupMaterialsArray = arrayRemove(selectedMaterialAndGroupMaterialsArray, groupMaterialId);
});
});

$(document).ready(function() {
$("#showSelectedMaterial").on('click', '.crossButton', function() {
	if(selectedMaterialAndGroupMaterialsArray != ""){
	var materialId = $(this).closest("tr").find('#matId').val();
	removeSelectedMaterialName = $(this).closest("tr").find('td').text();
	removeSelectedMaterialName = removeSelectedMaterialName.substr(0, removeSelectedMaterialName.length - 1);
	$("#checkValue" + materialId).removeAttr('checked');
	$(this).closest('tr').remove();
	selectedMaterialSize = selectedMaterialSize - 1;
	selectedMaterialNameArray = arrayRemove(selectedMaterialNameArray, removeSelectedMaterialName);
	selectedMaterialAndGroupMaterialsArray = arrayRemove(selectedMaterialAndGroupMaterialsArray, materialId);
	}else {
			$(this).closest('tr').remove();

	}
});
});

$(document).ready(function() {
	$("#showSelectedMaterial").on('click', '.expandButton', function() {
		var groupNameId = $(this).closest("tr").find('#groupNameId').val();
		if ($(this).val() == "yes") {
			$('.bioMaterialNameList'+groupNameId+'').show();
			var html = '';
			expandSelectedGroupName = $(this).closest("tr").find('td').text();
			expandSelectedGroupName = expandSelectedGroupName.substr(1);
			expandSelectedGroupName = expandSelectedGroupName.substr(0, expandSelectedGroupName.length - 1);
			$.get("expandForExsistingBioMaterial?groupName=" + expandSelectedGroupName, function(response) {
				var data = JSON.parse(response);
				console.log(data);
				data.forEach(function(materiallist) {
					html += '<li>' + materiallist.longDesc.toUpperCase() + '</li><br>';
				});
				html = '<ul class= "removebullet">' + html + '</ul>';
				$('#materialNameList'+groupNameId+'').html(html);
				$('#materialNameList'+groupNameId+'').show();
				$("#showSelectedMaterial").show();
				$("#showDiv2").show();
				$("#showDiv3").show();
			});
			$(this).val("no");
		} else {
			$('#materialNameList'+groupNameId+'').hide();
			$('.bioMaterialNameList'+groupNameId+'').hide();
			$(this).val("yes");
		}
	});
	});

$(document).ready(function() {
$("#saveGroup").on("click", function() {
	var status = 1;
	materialsNameList = $("#materialList").val();		
	if(materialsNameList == undefined){
	if (selectedMaterialAndGroupMaterialsArray != "") {
		exsistingGroupMaterialsArray.push(selectedMaterialAndGroupMaterialsArray);
		if(btnDeleteid != "") {
			status = 0;
		}
		var bioGroupName = $("#bioGroupName").val();
		$.get("saveMaterialInGroup?selectedMaterialAndGroupMaterialsArray=" + exsistingGroupMaterialsArray + "&selectedMaterialSize=" + selectedMaterialSize + "&groupName=" + bioGroupName + "&status=" + status, function(response) {
			var jsonData = JSON.parse(response);
			if (jsonData['data']) {
				$("#SuccessResponse").html(jsonData['data']);
				jQuery('#showSelectedMaterial').empty();
				$("#bioGroupName").val('');
				$("#displayGroupName").hide();
				$("#right").hide();
				$("#showDiv1").hide();
				$("#showDiv2").hide();
				$("#showDiv3").hide();
				$("#showExpandedMaterial").hide();
				$('#errorMsg').empty();
				$('#select50').empty();
				$("#material").removeAttr('checked');
				$("#group").removeAttr('checked');
				selectedMaterialIdArray = [];
				selectedMaterialAndGroupMaterialsArray = [];
				exsistingGroupMaterialsArray = [];
				selectedMaterialSize = 0;
				if($('#searchBarOfGroup').css('display','block')){
					$('#searchBarOfGroup').css('display','none')
				}
				if($('#searchBarOfMaterial').css('display','block')){
					$('#searchBarOfMaterial').css('display','none')
				}
			}

			if (jsonData['msg']) {
				$("#errorMsg").html(jsonData['msg']);
				$('#errorMsg').css("color", "red");
				$("#SuccessResponse").empty();
			}
			if (jsonData['already']) {
				$("#errorMsg").html(jsonData['already']);
				$('#errorMsg').css("color", "red");
				$("#SuccessResponse").empty();
			}
			if (jsonData['select50']) {
				$("#select50").html(jsonData['select50']);
				$('#select50').css("color", "red");
				$("#SuccessResponse").empty();
			}
		});
	} else if(btnDeleteid != "" && selectedMaterialAndGroupMaterialsArray == ""){
			$.alert({
					title: 'Alert!',
					content: 'Please Add Material OR Group.!',
				});
		} 
	} else if(materialsNameList != "" && btnDeleteid == "" && selectedMaterialAndGroupMaterialsArray == "") {
		$.alert({
				title: 'Alert!',
				content: 'Please Add Material OR Group.!',
			});
	} else {
		materialsNameList = materialsNameList.replace(/\s*,\s*/g, ",");
			materialsNameList = materialsNameList.replace(/[\[\]']+/g,'');
			exsistingGroupMaterialsArray.push(materialsNameList);
			$("#materialList").val("");
		if(btnDeleteid != ""){	
			exsistingGroupMaterialsArray.pop(materialsNameList);
			exsistingGroupMaterialsArray.push(exsistingMaterialsNameList[exsistingMaterialsNameList.length - 1]);
			$("#materialList").val("");			
		}	
		if(selectedMaterialAndGroupMaterialsArray != ""){
			exsistingGroupMaterialsArray.push(selectedMaterialAndGroupMaterialsArray);
		}
		console.log(exsistingGroupMaterialsArray);
		var bioGroupName = $("#bioGroupName").val();
		if(exsistingGroupMaterialsArray != "") {
			$.get("saveEditMaterialInGroup?exsistingGroupMaterialsArray=" + exsistingGroupMaterialsArray + "&selectedMaterialSize=" + selectedMaterialSize + "&groupName=" + bioGroupName, function(response, error) {			
				var jsonData = JSON.parse(response);
				if (jsonData['data']) {
					$("#SuccessResponse").html(jsonData['data']);
					jQuery('#showSelectedMaterial').empty();
					$("#bioGroupName").val('');
					$("#displayGroupName").hide();
					$("#right").hide();
					$("#showDiv1").hide();
					$("#showDiv2").hide();
					$("#showDiv3").hide();
					$("#showExpandedMaterial").hide();
					$('#errorMsg').empty();
					$('#select50').empty();
					$("#material").removeAttr('checked');
					$("#group").removeAttr('checked');
					selectedMaterialIdArray = [];
					selectedMaterialAndGroupMaterialsArray = [];
					exsistingGroupMaterialsArray = [];
					selectedMaterialSize = 0;
					if($('#searchBarOfGroup').css('display','block')){
						$('#searchBarOfGroup').css('display','none')
					}
					if($('#searchBarOfMaterial').css('display','block')){
						$('#searchBarOfMaterial').css('display','none')
					}
				}
	
				if (jsonData['msg']) {
					$("#errorMsg").html(jsonData['msg']);
					$('#errorMsg').css("color", "red");
					$("#SuccessResponse").empty();
				}
				if (jsonData['already']) {
					$("#errorMsg").html(jsonData['already']);
					$('#errorMsg').css("color", "red");
					$("#SuccessResponse").empty();
				}
				if (jsonData['select50']) {
					$("#select50").html(jsonData['select50']);
					$('#select50').css("color", "red");
					$("#SuccessResponse").empty();
				}
			});
		} 
	}
});
});

$(document).ready(function() {	
$(".clickedOpenPopup").on("click",function(){
	var groupId = $(this).closest("td").find("#groupId").val();
	if(groupId != null){
	$.get("getBioGroupingUsingId?groupId=" + groupId, function(response) {
        var groupDetails = "";	
        var groupMaterial = "";
        var errorMsg = "";
		var materialName = JSON.parse(response);
		console.log(JSON.stringify(materialName));
		for(var i = 0; i<materialName.msg.length; i++){
			groupDetails += "<TR><TD>ID</TD><TD>"+materialName.msg[i].id+"</TD></TR><TR><TD>GroupName</TD><TD>"+materialName.msg[i].groupName+"</TD></TR>"+
			                "<TR><TD>Added By</TD><TD>"+materialName.msg[i].addedBy+"</TD></TR><TR><TD>Updated By</TD><TD>"+materialName.msg[i].updatedBy+"</TD></TR>"+
			                "<TR><TD>Created At</TD><TD>"+materialName.msg[i].createdAt+"</TD></TR><TR><TD>Updated At</TD><TD>"+materialName.msg[i].updatedAt+"</TD></TR>";
		}
		if(materialName.data.length !=0){
		for(var j = 0; j<materialName.data.length; j++){
			groupMaterial +="<TR class='header'><TD>"+materialName.data[j].shortDesc+"</TD></TR>";
		}
		$("#dynamicMaterialName").html(groupDetails);
		$("#materialName").html(groupMaterial);
		$("#errorMsg").empty();
		$(".abc").show();
		$(".header").hide();
		$("#myModal").modal('show');
		}
		else{
			errorMsg +="No Material in Group Containing.";
			$("#errorMsg").html(errorMsg);
			$('#errorMsg').css("color", "red");
			$("#dynamicMaterialName").html(groupDetails);
		    $("#materialName").html(groupMaterial)

			$(".abc").show();
		$(".header").hide();
		$("#myModal").modal('show');
		}
		});
		}
		else{
			$("#clickedOpenPopup").focus();
			$.alert({
				title: 'Alert!',
				content: 'No Group .!',
			});
		}
});
});


$(function () {
        $(".header").hide();
$("#btn1").click(function () {
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

$(document).ready(function(){
    var size_item = $('.listing').length;
    var v=3;
    $('.listing').hide();
    $('.listing:lt('+v+')').show();
    $('#load_more').click(function () {
        v= (v+5 <= size_item) ? v+5 : size_item;
        $('.listing:lt('+v+')').show();
        if($(".listing:visible").length >= size_item ){ $("#load_more").hide(); }
    });
});

$(document).ready(function() {
	if($('.materialList').css('display','none')){
		$('.materialList').css('display','block')
	}
});

$(document).ready(function() {
	$("#showSelectedMaterial").on('click', '.crossButton', function() {
		if(materialsNameList == ""){
			materialsNameList = ($("#materialList").val());
		}	
	materialsNameList = materialsNameList.replace(/\s*,\s*/g, ",");
	materialsNameList = materialsNameList.replace(/[\[\]']+/g,'');
	var deleteExpandMaterial = $(this).closest("tr").find('#materialDeleteId').val();
	materialsNameList = materialsNameList.replace(deleteExpandMaterial, "");
	if(exsistingMaterialsNameList != "" && btnDeleteid == ""){
		exsistingMaterialsNameList.pop(materialsNameList);
	}
	exsistingMaterialsNameList.push(materialsNameList);
		btnDeleteid.push(deleteExpandMaterial);
		});
});
function getTextValue() {
	var firstIndex = $("#bioMaterialName").val();
	if (firstIndex != '') {
		console.log("Got First Index Of Material " + firstIndex + " Now fetching material");
		$.get("getSecondIndexBasedOnFirstIndex?firstIndex=" + firstIndex, function(response) {
			$('#selectedBioVariableId1').empty()
			var jsonValue = JSON.parse(response);
			console.log(jsonValue)
			var form = "";
			var process = "";
			if (jsonValue.msg.length == 0) {
				$("#process").find('option').remove();
				process += "<OPTION class='val' value = ''>No Selection</OPTION>";
				$("#process").html(process);
			}
			else {
				process += "<OPTION class='val' value = ''>No Selection</OPTION>";
				for (var i = 0; i < jsonValue.msg.length; i++) {
					process += "<OPTION class='val' value = " + jsonValue.msg[i] + ">" + jsonValue.msg[i] + "</OPTION>";
					$("#process").html(process);
				}
			}
			if (jsonValue.data.length == 0) {
				$("#form").find('option').remove();
				form += "<OPTION class='val' value = ''>No Selection</OPTION>";
				$("#form").html(form);
			}
			else {
				form += "<OPTION class='val' value = ''>No Selection</OPTION>";
				for (var i = 0; i < jsonValue.data.length; i++) {
					form += "<OPTION class='val' value = " + jsonValue.data[i] + ">" + jsonValue.data[i] + "</OPTION>";
					$("#form").html(form);
				}
			}
		})
	}
}

function arrayRemove(arr, value) {
	return arr.filter(function(ele) {
		return ele != value;
	});
}

function getOccurrence(array, value) {
	var count = 0;
	array.forEach((v) => (v === value && count++));
	return count;
}
let additionalComponentsName = [];
let additionalComponentsId = [];
let additionalCompositionId = [];
let componentsName = [];
let componentsValue = [];
let componentsValueCheck = [];
let selectedMaterial = "";
let shortDesc = "";
let keyword1 = ""
let keyword2 = "";
let keyword3 = "";
let keyword4 = "";
let bioStatus = 1;
let count = 0;
let x = 4;

function update() {
	if(document.getElementById("saveCustomMaterial").disabled){
		document.getElementById("saveCustomMaterial").disabled = false;
	}
	if(count == x){
		x = x + 4;
		var elm = document.createElement('div');
		elm.className = 'addComponents'+x+' form-group';
		elm.id = 'addComponents'+x;
		$('.addComponents:last').after(elm);
	}
	var select = document.getElementById('Additionalcomponents');
	var option = select.options[select.selectedIndex];	
	$.get("getBioCompositionByNutrientDescs?nutrientDesc=" + option.text, function(response) {
		var jsonValue = JSON.parse(response);
		if(count <= 4)
			$('.addComponents').append('<label class="text-info col-sm-1" >'+ option.text +' </label><div class="col-sm-1"><input class="form-control" id="'+ jsonValue.data.id +'" value="" autocomplete="off"/></div><label class="text-info col-sm-1" id="gram">'+ jsonValue.data.uom +'</label>');
		else 
			$('.addComponents'+x+'').append('<label class="text-info col-sm-1" >'+ option.text +' </label><div class="col-sm-1"><input class="form-control" id="'+ jsonValue.data.id +'" value="" autocomplete="off"/></div><label class="text-info col-sm-1" id="gram">'+ jsonValue.data.uom +'</label>');
		additionalComponentsId.push(jsonValue.data.id);
	});
	additionalComponentsName.push(option.text);
	$('#Additionalcomponents option:selected').remove();
	count++;
} 

$(document).ready(function() {   
	$("#saveCustomMaterial").on("click", function() { 
		var id = $('#customIdHidden').val();
		var shortDescName = $('#shortDesc').val();		
		var longDescName = document.getElementById("longDesc").innerHTML;
		var waterName = document.getElementById('waterLabel').innerText;
		var waterValue = $('#water').val();
		var fatName = document.getElementById('fatLabel').innerText;
		var fatValue = $('#fat').val();
		var proteinName = document.getElementById('proteinLabel').innerText;
		var proteinValue = $('#protein').val();
		var fiberName = document.getElementById('fiberLabel').innerText;
		var fiberValue = $('#fiber').val();
		var carbName = document.getElementById('carbLabel').innerText;
		var carbValue = $('#carb').val();
		var ashName = document.getElementById('ashLabel').innerText;
		var ashValue = $('#ash').val();
		var errorMsg = "";
		selectedMaterial = $("#selectedMaterialId").val();
		var re = /^.*[a-zA-Z].*$/;
		var pattern = /^[a-zA-Z\s]{1,20}$/;
		componentsName.push(waterName,fatName,proteinName,fiberName,carbName,ashName);
		componentsValueCheck.push(waterValue,fatValue,proteinValue,fiberValue,carbValue,ashValue);
		componentsValue.push(waterValue,fatValue,proteinValue,fiberValue,carbValue,ashValue);		
		for (i = 0; i < additionalComponentsName.length; i++) {
			window['value'+i] = + $('#'+additionalComponentsId[i]+'').val();
			componentsValue.push(window['value'+i]);
        }
        var numberArray = [];
        for (var i = 0; i < componentsValueCheck.length; i++)
        	numberArray.push(parseInt(componentsValueCheck[i]));
        var sumOfComponentsValueCheck = sum(numberArray);
        if(!shortDescName || !longDescName || !waterValue
        || !fatValue || !proteinValue || !fiberValue || !carbValue || !ashValue || componentsValue.some(isNaN)
        || sumOfComponentsValueCheck > 100){
			errorMsg +="Material can't be saved (some fields are Empty OR Compositions value are not Integer OR Additing of Composition out of 100gms)";
			$('#errorMsgMaterial').html(errorMsg);	
			componentsName = [];
			componentsValueCheck = [];
			componentsValue = [];
		} else if(!selectedMaterial){
			errorMsg +="Custom material can’t be saved- choose a keyword denoting the material first";
			$('#errorMsgMaterial').html(errorMsg);	
			componentsName = [];
			componentsValueCheck = [];
			componentsValue = [];
		}else if(!re.test(shortDescName)) {
			errorMsg +="Material can't be saved (Name contain at least one alphabet)";
			$('#errorMsgMaterial').html(errorMsg);	
			componentsName = [];
			componentsValueCheck = [];
			componentsValue = [];
		} else if(/\s/g.test(shortDescName)) {
			errorMsg +="Material can't be saved (whitespace not allow use underscore instead of this)";
			$('#errorMsgMaterial').html(errorMsg);	
			componentsName = [];
			componentsValueCheck = [];
			componentsValue = [];
		} else if((keyword1 != "" && !pattern.test(keyword1)) || (keyword2 != "" && !pattern.test(keyword2)) || (keyword3 != "" && !pattern.test(keyword3)) || 
		(keyword4 != "" && !pattern.test(keyword4)) ){
			errorMsg +="Material can't be saved (Keywords Must contain String)";
			$('#errorMsgMaterial').html(errorMsg);	
			componentsName = [];
			componentsValueCheck = [];
			componentsValue = [];
		} else {
			if(id == ""){
				$.get("saveCustomMaterial?shortDescName=" + shortDescName + "&longDescName=" + longDescName + "&componentsName=" + componentsName
				+ "&componentsValue=" + componentsValue + "&additionalComponentsId=" + additionalComponentsId, function(response) {
					var jsonData = JSON.parse(response);
					$('#errorMsgMaterial').html("");	
					if (jsonData['data']) {					
						$("#successResponseMaterial").html(jsonData['data']);
						componentsName = [];
						componentsValueCheck = [];
						componentsValue = [];
						$("#hideDiv").hide();
						$("#hideHeader").hide();
					}
					if (jsonData['msg']) {	
						$("#notUniqueName").html(jsonData['msg']);
						$('#notUniqueName').css("color", "red");
						componentsName = [];
						componentsValueCheck = [];
						componentsValue = [];
					}
				});
			} else {
				$.get("saveEditCustomMaterial?shortDescName=" + shortDescName + "&longDescName=" + longDescName + "&componentsName=" + componentsName
				+ "&componentsValue=" + componentsValue + "&materialId=" + id + "&status=" + bioStatus + "&additionalComponentsId=" + additionalComponentsId, function(response) {
					var jsonData = JSON.parse(response);
					$('#errorMsgMaterial').html("");	
					if (jsonData['data']) {					
						$("#successResponseMaterial").html(jsonData['data']);
						componentsName = [];
						componentsValueCheck = [];
						componentsValue = [];
						$("#editHideDiv").hide();
						$("#hideHeader").hide();
					}
					if (jsonData['msg']) {	
						$("#notUniqueName").html(jsonData['msg']);
						$('#notUniqueName').css("color", "red");
						componentsName = [];
						componentsValueCheck = [];
						componentsValue = [];
					}
				});
			}				
		}
	});
});

function sum(arr) { 
	let sum = 0; 
	for (let i = 0; i < arr.length; i++) 
    	sum += arr[i]; 
	return sum; 	
} 
    
$(document).ready(function () {
$("#selectedMaterialId").inputpicker({
		url: 'getSelectedUniqueMaterial',
		fields:['shortDesc'],
		fieldText : 'shortDesc',
		fieldValue : 'shortDesc',
		headShow: true,
		filterOpen: true,
		autoOpen: true,
		width: '100%',
		headShow: true,
	});
});

var key1 = ""
var key2 = "";
var key3 = "";
var key4 = "";
var selectShortDesc = "";
function autoFillLongDesc() {
	selectedMaterial = $("#selectedMaterialId").val();
	shortDesc = $('#shortDesc').val();
	if(shortDesc != "")
		selectShortDesc = "," + shortDesc.toLowerCase();
	if(document.getElementById("saveCustomMaterial").disabled){
		document.getElementById("saveCustomMaterial").disabled = false;
	}	
	$("#selectedMaterialId").on("change", function(){
        selectedMaterial = $(this).val();       
        document.getElementById("longDesc").innerHTML = selectedMaterial.toLowerCase() + selectShortDesc + key1 + key2 + key3 + key4;
    });
    $('#shortDesc').keyup(function(e) {	
		bioStatus = 0;
		shortDesc = $('#shortDesc').val();
		selectedMaterial = $("#selectedMaterialId").val();
		if(!selectedMaterial)
			document.getElementById("longDesc").innerHTML = selectedMaterial.toLowerCase() + shortDesc.toLowerCase() + key1 + key2 + key3 + key4;
		else 
			document.getElementById("longDesc").innerHTML = selectedMaterial.toLowerCase() + "," + shortDesc.toLowerCase() + key1 + key2 + key3 + key4;
	});
	if(document.getElementById("keyword1") != null){
		keyword1 = document.getElementById("keyword1").value;
		if(keyword1 != ""){			
			if(shortDesc == "" && selectedMaterial == "")
				key1 = keyword1.toLowerCase();
			else 
				key1 = "," + keyword1.toLowerCase();
		} else
			key1 = keyword1.toLowerCase();
		document.getElementById("longDesc").innerHTML = selectedMaterial.toLowerCase() + selectShortDesc + key1 + key2 + key3 + key4;
	}
	if(document.getElementById("keyword2") != null){
		if(keyword1 != ""){
			/*if(document.getElementById('errorKeyword').style.display = "block")
				document.getElementById('errorKeyword').style.display = "none";*/
			keyword2 = document.getElementById("keyword2").value;
			if(keyword2 != "")
				key2 = "," + keyword2.toLowerCase();
			else 
				key2 = keyword2.toLowerCase();
			document.getElementById("longDesc").innerHTML = selectedMaterial.toLowerCase() + selectShortDesc + key1 + key2 + key3 + key4;
		} else {
			//document.getElementById('errorKeyword').style.display = "block";
		}
	}
  	if(document.getElementById("keyword3") != null){
		if(keyword1 != "") {
			/*if(document.getElementById('errorKeyword').style.display = "block")
				document.getElementById('errorKeyword').style.display = "none";*/
			keyword3 = document.getElementById("keyword3").value;
			if(keyword3 != "")
				key3 = "," + keyword3.toLowerCase();
			else
				key3 = keyword3.toLowerCase();
			document.getElementById("longDesc").innerHTML = selectedMaterial.toLowerCase() + selectShortDesc + key1 + key2 + key3 + key4;
		} else {
			//document.getElementById('errorKeyword').style.display = "block";
		}
	}
	if(document.getElementById("keyword4") != null){
		if(keyword1 != "") {
			/*if(document.getElementById('errorKeyword').style.display = "block")
				document.getElementById('errorKeyword').style.display = "none";*/
			keyword4 = document.getElementById("keyword4").value;
			if(keyword4 != "")
				key4 = "," + keyword4.toLowerCase();
			else 
				key4 = keyword4.toLowerCase();
			document.getElementById("longDesc").innerHTML = selectedMaterial.toLowerCase() + selectShortDesc + key1 + key2 + key3 + key4;
		} else {
			//document.getElementById('errorKeyword').style.display = "block";
		}
	} 	
}

$(document).ready(function() {	
	$(".clickedBioMaterialPopup").on("click",function(){
		var biomaterialId = $(this).closest("td").find("#biomaterialId").val();
		if(biomaterialId.includes("9999")){
			if(biomaterialId != null){
				$.get("getCustomBioMaterialUsingId?biomaterialId=" + biomaterialId, function(response) {
		        var customMaterialDetails = "";	
		        var compositions = "";
		        var errorMsg = "";
				var listOfCustomMaterial = JSON.parse(response);
				console.log(JSON.stringify(listOfCustomMaterial));
				for(var i = 0; i<listOfCustomMaterial.customBioMaterialList.length; i++){
					customMaterialDetails += "<TR><TD>ID</TD><TD>"+listOfCustomMaterial.customBioMaterialList[i].id+"</TD></TR><TR><TD>Name</TD><TD>"+listOfCustomMaterial.customBioMaterialList[i].name+"</TD></TR><TR><TD>Short Desc</TD><TD>"+listOfCustomMaterial.customBioMaterialList[i].shortDesc+"</TD></TR>"+
					                "<TR><TD>Long Desc</TD><TD>"+listOfCustomMaterial.customBioMaterialList[i].longDesc+"</TD></TR><TR><TD>Added By</TD><TD>"+listOfCustomMaterial.customBioMaterialList[i].addedBy+"</TD></TR><TR><TD>Updated By</TD><TD>"+listOfCustomMaterial.customBioMaterialList[i].updatedBy+"</TD></TR>"+
					                "<TR><TD>Created At</TD><TD>"+listOfCustomMaterial.customBioMaterialList[i].createdAt+"</TD></TR><TR><TD>Updated At</TD><TD>"+listOfCustomMaterial.customBioMaterialList[i].updatedAt+"</TD></TR>";
				}
				compositions +="<TR class='header'><TD>compositionId</TD><TD class='header'>compositionName</TD><TD class='header'>nutrientValue</TD></TR>"
				if(listOfCustomMaterial.compositionsId.length !=0){
					for(var j = 0; j<listOfCustomMaterial.compositionsId.length; j++){
						compositions +="<TR class='header'><TD>"+listOfCustomMaterial.compositionsId[j]+"</TD><TD class='header'>"+listOfCustomMaterial.compositionsName[j]+"</TD><TD class='header'>"+listOfCustomMaterial.nutrientsValue[j]+"</TD></TR>";
					}
					$("#dynamicCustomMaterialName").html(customMaterialDetails);
					$("#compositionsName").html(compositions);
					$("#errorCustomMaterialMsg").empty();
					$(".abc").show();
					$(".header").hide();
					$("#customMaterial").modal('show');
				}
				else{
					errorMsg +="No Material in Group Containing.";
					$("#errorCustomMaterialMsg").html(errorMsg);
					$('#errorCustomMaterialMsg').css("color", "red");
					$("#dynamicCustomMaterialName").html(customMaterialDetails);
				    $("#compositionsName").html(compositions);
		
					$(".abc").show();
				$(".header").hide();
				$("#customMaterial").modal('show');
				}
				});
				}
			else{
				$("#clickedBioMaterialPopup").focus();
				$.alert({
					title: 'Alert!',
					content: 'No Material',
				});
			}
		} else {
			$("#biomaterialdetailpopup").modal('show');
		}
	});
});
let existingCompositionValue = [];
$(document).ready(function() {	
	$(".editBioMaterial").on("click",function(){
		var userName = $("#userName").val();		
		if(typeof userName != 'undefined'){
			var biomaterialId = $(this).closest("td").find("#editBiomaterialId").val();
			if(biomaterialId.includes("9999")){
				if(biomaterialId != null){
					var id = "";
					var name = "";
					var longDesc = "";
					var keywords = "";
					var waterName = document.getElementById('waterLabel').innerText;
					var waterValue = "";
					var fatName = document.getElementById('fatLabel').innerText;
					var fatValue = "";
					var proteinName = document.getElementById('proteinLabel').innerText;
					var proteinValue = "";
					var fiberName = document.getElementById('fiberLabel').innerText;
					var fiberValue = "";
					var carbName = document.getElementById('carbLabel').innerText;
					var carbValue = "";
					var ashName = document.getElementById('ashLabel').innerText;
					var ashValue = "";
					var errorMsg = "";				
					$.get("getCustomBioMaterialUsingId?biomaterialId=" + biomaterialId, function(response) {
						var listOfCustomMaterial = JSON.parse(response);								
						for(var i = 0; i<listOfCustomMaterial.customBioMaterialList.length; i++){	
							name = listOfCustomMaterial.customBioMaterialList[i].name;
							longDesc = listOfCustomMaterial.customBioMaterialList[i].longDesc;
							id = listOfCustomMaterial.customBioMaterialList[i].id;					
						}
						if(listOfCustomMaterial.compositionsId.length !=0){
							for(var j = 0; j<listOfCustomMaterial.compositionsName.length; j++){
								if(listOfCustomMaterial.compositionsName[j].includes(waterName.trim())){
									waterValue = listOfCustomMaterial.nutrientsValue[j];
								}
								if(listOfCustomMaterial.compositionsName[j].includes("Total lipid")){
									fatValue = listOfCustomMaterial.nutrientsValue[j];
								}
								if(listOfCustomMaterial.compositionsName[j].includes(proteinName.trim())){
									proteinValue = listOfCustomMaterial.nutrientsValue[j];
								}
								if(listOfCustomMaterial.compositionsName[j].includes(fiberName.trim())){
									fiberValue = listOfCustomMaterial.nutrientsValue[j];
								}
								if(listOfCustomMaterial.compositionsName[j].includes(carbName.trim())){
									carbValue = listOfCustomMaterial.nutrientsValue[j];
								}
								if(listOfCustomMaterial.compositionsName[j].includes(ashName.trim())){
									ashValue = listOfCustomMaterial.nutrientsValue[j];
								}
								var selectobject = document.getElementById("Additionalcomponents");								
								for (var i=0; i<selectobject.length; i++) {
	    							if (selectobject.options[i].value == listOfCustomMaterial.compositionsName[j]){					
										existingCompositionValue.push(listOfCustomMaterial.nutrientsValue[j]);																																									
										additionalComponentsName.push(selectobject.options[i].value);	
										additionalCompositionId.push(listOfCustomMaterial.compositionsId[j]);							
	        							selectobject.remove(i);
									}
								}									
							}
						}						
						$("#customIdHidden").val(id);
						$("#shortDesc").val(name);
						document.getElementById("longDesc").textContent = longDesc; 
						keywords = longDesc.split(",");
						$('#inputpicker-1').val(keywords[0].toUpperCase());
						$('#selectedMaterialId').val(keywords[0]); 
						$("#keyword1").val(keywords[2]);
						/*if(keywords[3] != null){							
							document.getElementById('keywordDiv2').style.display = "block";
							$("#keywordDiv2").html('<input type="text" class="form-control" id="keyword2" name="keyword2" maxlength="20" placeholder="Keyword2" oninput="autoFillLongDesc()" autocomplete="off"/>');
							$("#keyword2").val(keywords[3]);
							key = 2;
							$("#keywordNumber").val(2);
						}
						if(keywords[4] != null){
							document.getElementById('keywordDiv3').style.display = "block";
							$("#keywordDiv3").html('<input type="text" class="form-control" id="keyword3" name="keyword3" maxlength="20" placeholder="Keyword3" oninput="autoFillLongDesc()" autocomplete="off"/>');
							$("#keyword3").val(keywords[4]);	
							key = 3;	
							$("#keywordNumber").val(3);				
						}
						if(keywords[5] != null){							
							document.getElementById('keywordDiv4').style.display = "block";
							$("#keywordDiv4").html('<input type="text" class="form-control" id="keyword4" name="keyword4" maxlength="20" placeholder="Keyword4" oninput="autoFillLongDesc()" autocomplete="off"/>');
							$("#keyword4").val(keywords[5]);
							key = 4;
							$("#keywordNumber").val(4);
						} else {
							document.getElementById('addKey').style.display = "block";
						}*/
						$("#keyword2").val(keywords[3]);
						$("#keyword3").val(keywords[4]);
						$("#keyword4").val(keywords[5]);						
						$("#water").val(waterValue);
						$("#protein").val(proteinValue);
						$("#fiber").val(fiberValue);
						$("#carb").val(carbValue);
						$("#ash").val(ashValue);
						$("#fat").val(fatValue);
						$("#hideHeader").show();
						$("#divHeader").hide();
						$("#divTable").hide();
						$("#editHideDiv").show();	
						$.get("getBioCompositionByNutrientDesc?nutrientId=" + additionalCompositionId + "&existingCompositionValue=" + existingCompositionValue, function(response) {
							var jsonValue = JSON.parse(response);
							for (var k = 0; k <= 3; k++) {
								$('.addComponents').append('<label class="text-info col-sm-1" >'+ jsonValue.data[k].nutrientDesc +' </label><div class="col-sm-1"><input class="form-control" id='+ jsonValue.data[k].id +' value='+ jsonValue.value[k] +'></div><label class="text-info col-sm-1" id="gram">'+ jsonValue.data[k].uom +'</label>');									
								additionalComponentsId.push(jsonValue.data[k].id);
								count = 4;		
							}
							if(jsonValue.data.length > 3){
								for (var k = 4; k < jsonValue.data.length; k++) {
									if(count == x){
										x = x + 4;
										var elm = document.createElement('div');
										elm.className = 'addComponents'+x+' form-group';
										elm.id = 'addComponents'+x;
										$('.addComponents:last').after(elm);
									}
									$('.addComponents'+x+'').append('<label class="text-info col-sm-1" >'+ jsonValue.data[k].nutrientDesc +' </label><div class="col-sm-1"><input class="form-control" id='+ jsonValue.data[k].id +' value='+ jsonValue.value[k] +' autocomplete="off"></div><label class="text-info col-sm-1" id="gram">'+ jsonValue.data[k].uom +'</label>');									
									additionalComponentsId.push(jsonValue.data[k].id);		
								}
							}						
						});									
					});
				} else{
					$("#editBioMaterial").focus();
					$.alert({
						title: 'Alert!',
						content: 'No Material',
					});
				}
			} else {
				window.location.replace("updateBioMaterial?materialId=" + biomaterialId); 
			}
		} else {
			window.location.replace("login"); 
		}
	});
});

function composition(){
	if(document.getElementById("saveCustomMaterial").disabled){
		document.getElementById("saveCustomMaterial").disabled = false;
	}
}

$(document).ready(function() {
		$.get("getCompositions", function(response) {
			var jsonValue = JSON.parse(response);
			var process = "";
			process += "<OPTION class='getoption'>Select an option</OPTION>";					
				for (var i = 0; i < jsonValue.data.length; i++) {
					process += "<OPTION class='getoption' >" + jsonValue.data[i].nutrientDesc + "</OPTION>";
					$("#Additionalcomponents").html(process);
				}
		})
});

$(document).ready(function(){
	$(".editBioMaterial").on("click",function(){
		$.get("getCompositions", function(response) {
			var jsonValue = JSON.parse(response);
			var process = "";
			process += "<OPTION class='getoption'>Select an option</OPTION>";			
				for (var i = 0; i < jsonValue.data.length; i++) {
					process += "<OPTION class='getoption' >" + jsonValue.data[i].nutrientDesc + "</OPTION>";
					$("#Additionalcomponents").html(process);
				}
		})
	});
	$("#Additionalcomponents").select2({
	    placeholder: "Select an option",
	});
});

var key = 1;
$(document).ready(function(){
	$('.addKeyword').on('click', function() {
		var value = parseInt(document.getElementById('keywordNumber').value, 10);
	    value = isNaN(value) ? 1 : value;
	    value++;
	    document.getElementById('keywordNumber').value = value;
	    var i = $("#keywordNumber").val();
	    if(i<5){
	    	$('.add:last').before('<div class="col-sm-2"><input type="text" class="form-control" id="keyword'+i+'"name="keyword'+i+'" placeholder="Keyword'+i+'" maxlength="20" oninput="autoFillLongDesc()" autocomplete="off"/></div>');
	    	key++;
		}
		if(key == 4){
			document.getElementById('addKeyword').style.display = "none";
		}
	});
});