<!DOCTYPE HTML>
<html lang="en">

<%@ include file = "./../header.jsp" %>

<body>

<div class="container">
	<div id="topbar"> <%@ include file = "./../top_bar.jsp" %></div>
	<div id="menubar"> <%@ include file = "./../menu_bar.jsp" %></div>
	
			
	<h2 class="text-info">Find Bio Property</h2>
	<h5 class="text-danger">${searchErrorMessage}</h5>
	<p>&nbsp;</p>
<h4 class="text-info" id="response"></h4>


	<form:form  action="searchBioProperty"  method="post"  modelAttribute="showBioDiscreetDataForm" >
	<form:label class="text-info" path = "dataSetName">Bio Property Name</form:label>
	    <div class="form-group  row">
	    	<div class= "col-sm-10">	    	
		      <form:input class="form-control"  path="dataSetName"   placeholder="Enter dataset name or Y-Variable name " autocomplete="off"/>
		      <form:errors  class="text-danger"  path="dataSetName"/>
		    </div>
		    <div class ="col-sm-2">
		      <button type="submit" class="btn btn-info">Search</button>
		    </div>
		 </div> 		
	 </form:form>	
	
	
<c:choose>
  <c:when test="${!empty bioDiscreetData}">
  <hr/>
	<h4 class="text-info">DataSet's containing text : ${showBioDiscreetDataForm.dataSetName} </h4>
	<table class="table table-hover table-striped">
	    <thead>
	      <tr>
	        <th>DataSet Name</th>
	        <th>&nbsp;</th>
	        <th>&nbsp;</th>
	        <th>&nbsp;</th>
	        <th>&nbsp;</th>
	      </tr>
	    </thead>
	    <tbody>
			<c:forEach var="bioDiscreet" items="${bioDiscreetData}">
				<tr class="listing" id = "deleteGrp${bioDiscreet.datasetName}">
   					<%-- <td>${bioDiscreetData.yBioVariable.name}</td> --%>
   					<td>${bioDiscreet.datasetName}</td>
   					 <td>&nbsp;</td>
	                 <td>&nbsp;</td>
	                 <td>&nbsp;</td>
	                 <td>&nbsp;</td>
   					<td>
			     		
			     		<button type="button" class="btn btn-default clickedDiscreetDataPopop" id="clickedDiscreetDataPopop">
		      				<span class="glyphicon glyphicon-search"></span> Details
		    			</button>&nbsp;&nbsp;&nbsp;<input type="hidden" value="${bioDiscreet.datasetName}" name="detailDatasetName" id="detailDatasetName"/>
		    			<%-- <a href="javascript:void(0);" onclick="getReport(&quot;${bioGroup.id}&quot;); return false;" role="button" class="btn btn-default "> --%>
			     		<%-- <a href="editDiscreetDataSet?dataSetId=${bioDiscreet.id}" role="button" class="btn btn-default ">
			    	  		<span class="glyphicon glyphicon-edit"></span> Edit
			     		</a>&nbsp;&nbsp;&nbsp; --%>
			     		<button type="button" class="btn btn-default editDiscreetDataSet" id="editDiscreetDataSet">
		      				<span class="glyphicon glyphicon-edit"></span> Edit
		    			</button>&nbsp;&nbsp;&nbsp;<input type="hidden" value="${bioDiscreet.datasetName}" name="dataSetName" id="dataSetName"/>
		    			<c:choose>
	 <c:when test ="${not empty pageContext.request.remoteUser}">
	 	<input type="hidden" value="${pageContext.request.userPrincipal.principal.bioUser.userRole}" id="userName"/>
	 </c:when>
 </c:choose>
			     		<a href="deleteDiscreetDataSetUsingName?dataSet=${bioDiscreet.datasetName}&discreteDataSetName=${showBioDiscreetDataForm.dataSetName}" role="button" class="btn btn-info">
			    	  		Delete
			     		</a>
		     		</td>
   					
  				</tr>	
			</c:forEach>
		</tbody>
	</table>
	<div id="load_more">Load More Group</div>
	</c:when>	
	</c:choose>
	  	<!-- DETAILS POPUP START -->
	
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-lg" role="document" >
		    <div class="modal-content" style="width:960px">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLabel">Discreet Data Set Details</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <table class="table table-striped" id="dynamicDataset">
				</Table>
				<div >
				<p id="errorMsgDiscrete"></p>
				<table class="table table-striped">
				<thead>
				<tr class="abc">
				<th><button id="discretePlusButton" type="button" class="buttonInactive">
                &#65291;&nbsp;&nbsp;&nbsp;</button>Variables</th>
				</tr>
				</thead>
				<tbody id="xYVariablesName">
					<TR class='header' id='xYVariablesHeading'></TR>
					<TR class='header' id='xYVariableName'></TR>
					<TR class='header discreetDiv'></TR>
				</tbody>
				</table>
				</div>
		        
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
		      </div>
		    </div>
		  </div>
	    </div>
	<!-- DETAILS POPUP END -->
	
	
	
	
	
	
	
	
	
	<div id="footerbar"> <p>&nbsp;</p> <%@ include file = "./../footer_bar.jsp" %></div>	
</div>  
</body> 
