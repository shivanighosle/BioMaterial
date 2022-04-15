<!DOCTYPE HTML>
<html lang="en">

<%@ include file = "./../header.jsp" %>

 <script src="js/bioDiscreetData.dynamic.select.list.js"></script>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.bundle.min.js"></script>
 <script src="js/biomaterial.dynamic.measurement.input.js"></script>
<body>


<div class="container">
	<div id="topbar"> <%@ include file = "./../top_bar.jsp" %></div>
	<div id="menubar"> <%@ include file = "./../menu_bar.jsp" %></div>
	
			
			<h2 class="text-info" id="headingOfDiscreet">Add Discreet Dataset</h2>
			<h5 class="text-success" id="successMessage"></h5>
			<h5 class="text-danger" id="errorMessage"></h5>			
			
			<p>&nbsp;</p>
			
			<form:form  action=""  method="post" enctype="multipart/form-data" modelAttribute="bioDiscreetDataForm" id="form1">
				<div class="form-group row">
					<div class="col-sm-2">
						<form:label  class="text-info" path="materialId" >Select Material Or Group: </form:label>
					</div>
					<div class="col-sm-2">
						<input type="checkbox" id="discreteMaterial" onchange="showDiscreteMaterialSearchBar()" checked><span>Material</span><br><br> 
						<input type="checkbox" id="discreteGroup" onchange="showDiscreteGroupSearchBar()"><span>Group</span>
					</div>
					<div class="col-sm-8">
						<div class="materialSearchBar">
							<form:input class="form-control" id="materialId" path="materialId" value ="${bioDiscreetDataForm.materialName}"   placeholder="Please enter bio-material name" autocomplete="off"/>
							<form:errors  class="text-danger"  path="materialId" />
						</div>
						<div class="groupSearchBar" style="display: none">
							<form:input class="form-control" id="discreteGroupId" path="groupId" value ="${bioDiscreetDataForm.groupName}"   placeholder="Please enter bio-group name" autocomplete="off"/>
							<form:errors  class="text-danger"  path="groupId" />
						</div>
					</div>
				</div>	
				<div class="form-group row">
					<div class="col-sm-2">
						<form:label  class="text-info" path="authorName">Author Name: </form:label>
					</div>
					<div class="col-sm-10">
						<form:input class="form-control" id="authorName" path="authorName" value="${bioDiscreetDataForm.authorName}"  placeholder="Author Name" autocomplete="off"/>
						<form:errors  class="text-danger"  path="authorName" />
					</div>
				</div>	
				
				<div class="form-group row">
					<div class="col-sm-2">
						<form:label  class="text-info" path="year" >Publish Year: </form:label>
					</div>
					<div class="col-sm-10">
						<form:input class="form-control" id="year" path="year" value="${bioDiscreetDataForm.year}"  placeholder="Publish Year" autocomplete="off"/>
						<form:errors  class="text-danger"  path="year" />
					</div>
				</div>	
				<div class="form-group row">
					<div class="col-sm-2">
						<form:label  class="text-info" path="datasetName">Name of the Dataset: </form:label>
					</div>
					<div class="col-sm-10">
						<form:input class="form-control" id="datasetName" path="datasetName" value="${bioDiscreetDataForm.datasetName}"  placeholder="Enter the name of Dataset" autocomplete="off"/>
						<form:errors  class="text-danger"  path="datasetName" />
					</div>
				</div>	
				<div class="form-group row">
					<div class="col-sm-2">
						<form:label  class="text-info" path="yVariableId" >Upload Dataset: </form:label>
					</div>
					<%-- <div class="col-sm-10">
						<form:select class="form-control" id="yVariableId" value="${bioDiscreetDataForm.variableId}" path="yVariableId" onchange = "setValueInHiddenField1()"/><input type = "hidden" name = "yVariableName" id="yVariableName">
						<form:errors  class="text-danger"  path="yVariableId" />
					</div> --%>
				</div>	
				<div class="form-group row">
					<div class="col-sm-1">
						<form:label  class="text-info" path="xVariableId1" >X-axis variable(1):</form:label>	
						<input type = "hidden" name = "xVariableName1" id = "xVariableName1"><input type = "hidden" name = "xVariableName2" id = "xVariableName2"><input type = "hidden" name = "xVariableName3" id = "xVariableName3"><input type = "hidden" name = "xVariableName4" id="xVariableName4"><input type = "hidden" name = "xVariableName5" id="xVariableName5">
						<input type = "hidden" name = "xVariableName6" id="xVariableName6"><input type = "hidden" name = "xVariableName7" id="xVariableName7"><input type = "hidden" name = "xVariableName8" id="xVariableName8"><input type = "hidden" name = "xVariableName9" id="xVariableName9"><input type = "hidden" name = "xVariableName10" id="xVariableName10">
					</div>
					<div class="col-sm-1 variableName">
						<form:label  class="text-info" path="yVariableId" >Y-axis variable: </form:label>
					</div>
				</div>
				<div class="form-group row">
					<div class="col-sm-1">
						<form:select class="form-control" id="xVariableId1" path="xVariableId1" onclick = "getAllVariables()" onchange=" setValueInHiddenField()"/>
						<form:errors  class="text-danger"  path="xVariableId1" />					
					</div>
					<div class="col-sm-1 block">
						<form:select class="form-control" id="yVariableId" value="${bioDiscreetDataForm.yVariableId}" path="yVariableId" onclick = "getAllYVariables()" onchange = "setValueInHiddenField1()"/><input type = "hidden" name = "yVariableName" id="yVariableName">
						<form:errors  class="text-danger"  path="yVariableId" />
					</div>
					<div class="col-sm-1">
						<input type="hidden" id="number" value="1"/>
						<button type="button" style="font-size:x-large" class="text-info unstyled-button addVariable" id="addVariable" value="yes" title="click to add another column for x-axis" onclick="addXVariable()">&#65291;</button>
					</div>
				</div>
				<div class="form-group" id="execelContent">
					<!-- Values are added by JQuerry -->
				</div>
				<div class="form-group row">
					<div class="col-sm-3">
						<form:label for="file" class="btn btn-info btn-default" path="yVariableId" type="button" value="Upload">Upload</form:label>
						<form:input type="file" path="file" id = "file" accept=".xls,.xlsx" value = "${bioDiscreetDataForm.file}" style="display:none; visibility:none;"></form:input>
					</div>
				</div>               
				<div>
				</div>

					<div class="form-group row">
		  				<div class="col-sm-offset-5 col-sm-1">
		  				<input class="btn btn-success btn-lg" type="button" id="addDiscreetData" name="addDiscreetData" value="Save" />
		   				</div>
		   				<div>
		     				<input class="btn btn-danger btn-lg" type="button" id="cancelUploadFile" name="cancelUploadFile" value="Cancel" />
		   				</div>
		    		</div>
			</form:form>
			
		
	<p>&nbsp;</p>
	
	<div id="footerbar"> <p>&nbsp;</p> <%@ include file = "./../footer_bar.jsp" %></div>	
</div>  
</body> 