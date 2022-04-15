<!DOCTYPE HTML>
<html lang="en">

<%@ include file="./../header.jsp"%>
<link
	href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css"
	rel="stylesheet" />
<script
	src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
<body>

	<div class="container">
		<div id="topbar">
			<%@ include file="./../top_bar.jsp"%></div>
		<div id="menubar">
			<%@ include file="./../menu_bar.jsp"%></div>
		<div id="divHeader">
			<h2 class="text-info">Search Results for Bio-Materials</h2>

			<h5 class="text-primary">
				Bio-Materials containing text: <b>${bioMaterialSearchResultsForm.bioMaterialSearchForm.bioMaterialName }</b>
			</h5>

			<h5 class="text-primary">Database Search:</h5>

			<c:if test="${bioMaterialSearchResultsForm.lastPage>0}">
				<p class="text-primary">Showing Page
					${(bioMaterialSearchResultsForm.currentPage + 1)} of
					${bioMaterialSearchResultsForm.lastPage}</p>

				<!--  PAGER START -->
				<form:form action="searchBioMaterials" method="post"
					modelAttribute="bioMaterialSearchForm">
					<form:hidden
						value="${bioMaterialSearchResultsForm.bioMaterialSearchForm.bioMaterialName}"
						path="bioMaterialName" />


					<button type="submit" name="pageNumber" value="0"
						class="btn btn-default  btn-xs">Start</button>
					<c:if test="${(bioMaterialSearchResultsForm.currentPage) ne 0}">
						<button type="submit" name="pageNumber"
							value="${bioMaterialSearchResultsForm.currentPage-1}"
							class="btn btn-default btn-xs">Prev</button>
					</c:if>


					<c:forEach var="i"
						begin="${bioMaterialSearchResultsForm.pagerStart}"
						end="${bioMaterialSearchResultsForm.pagerEnd -1}">
						<c:choose>
							<c:when test="${i eq bioMaterialSearchResultsForm.currentPage}">
								<button type="button" name="pageNumber" value="${i}"
									class="btn btn-info btn-xs">${i+1 }</button>
							</c:when>
							<c:otherwise>
								<button type="submit" name="pageNumber" value="${i}"
									class="btn btn-default btn-xs">${i+1 }</button>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:if
						test="${(bioMaterialSearchResultsForm.currentPage) ne (bioMaterialSearchResultsForm.pagerEnd -1)}">
						<button type="submit" name="pageNumber"
							value="${bioMaterialSearchResultsForm.currentPage + 1}"
							class="btn btn-default  btn-xs">Next</button>
					</c:if>

					<button type="submit" name="pageNumber"
						value="${bioMaterialSearchResultsForm.lastPage-1}"
						class="btn btn-default  btn-xs">Last Page</button>

				</form:form>
				<!--  PAGER END -->

			</c:if>

		</div>


		<table class="table table-hover" id="divTable">
			<thead>
				<tr>
					<th>Short Desc</th>
					<th>Citation</th>
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="bioMaterial"
					items="${bioMaterialSearchResultsForm.bioMaterials}">
					<tr>
						<td>${bioMaterial.shortDesc}</td>
						<td>${fn:substring(bioMaterial.citation, 0, 20)}</td>
						<td>
							<button type="button"
								class="btn btn-default clickedBioMaterialPopup"
								id="clickedBioMaterialPopup">
								<span class="glyphicon glyphicon-search"></span> Details
							</button>&nbsp;&nbsp;&nbsp;<input type="hidden" value="${bioMaterial.id}"
							name="biomaterialId" id="biomaterialId" />
							<button type="button" class="btn btn-default editBioMaterial"
								id="editBioMaterial">
								<span class="glyphicon glyphicon-edit"></span> Edit
							</button>&nbsp;&nbsp;&nbsp;<input type="hidden" value="${bioMaterial.id}"
							name="editBiomaterialId" id="editBiomaterialId" /> <c:choose>
								<c:when test="${not empty pageContext.request.remoteUser}">
									<input type="hidden"
										value="${pageContext.request.userPrincipal.principal.bioUser.userRole}"
										id="userName" />
								</c:when>
							</c:choose> <a
							href="deleteMaterialUsingMaterialId?materialId=${bioMaterial.id}&shortDesc=${bioMaterialSearchResultsForm.bioMaterialSearchForm.bioMaterialName }"
							role="button" class="btn btn-info"> Delete </a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<!-- DETAILS POPUP START -->

		<div class="modal fade" id="customMaterial" tabindex="-1"
			role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-lg" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="exampleModalLabel">Bio-Material
							Details</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<table class="table table-striped" id="dynamicCustomMaterialName">
						</Table>
						<div>
							<p id="errorCustomMaterialMsg"></p>
							<table class="table table-striped">
								<thead>
									<tr class="abc">
										<th><button id="btn1" type="button"
												class="buttonInactive">
												&#65291;&nbsp;&nbsp;&nbsp;&nbsp;</button>Compositions</th>
									</tr>
								</thead>
								<tbody id="compositionsName">

								</tbody>
							</table>
						</div>

					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>

		<c:forEach var="bioMaterial"
			items="${bioMaterialSearchResultsForm.bioMaterials}">
			<div class="modal fade" id="biomaterialdetailpopup" tabindex="-1"
				role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-lg" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="exampleModalLabel">Bio-Material
								Details</h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<table class="table table-striped">
								<TR>
									<td>ID</td>
									<td>${bioMaterial.id}</td>
								</TR>


								<TR>
									<td>Short Desc</td>
									<td>${bioMaterial.shortDesc}</td>
								</TR>
								<TR>
									<td>Long Desc</td>
									<td>${bioMaterial.longDesc}</td>
								</TR>

								<TR>
									<td>Common Name</td>
									<td>${bioMaterial.commonName}</td>
								</TR>

								<TR>
									<td>Mfg Name</td>
									<td>${bioMaterial.mfgName}</td>
								</TR>
								<TR>
									<td>usdaSurvey</td>
									<td>${bioMaterial.usdaSurvey}</td>
								</TR>

								<TR>
									<td>Refuse Desc</td>
									<td>${bioMaterial.refuseDesc}</td>
								</TR>

								<TR>
									<td>Refuse Percentage</td>
									<td>${bioMaterial.refusePercentage}</td>
								</TR>
								<TR>
									<td>Scientific Name</td>
									<td>${bioMaterial.scientificName}</td>
								</TR>
								<TR>
									<td>Factors</td>
									<td>nFactor: ${bioMaterial.nFactor}&nbsp;&nbsp;pFactor:
										${bioMaterial.pFactor}&nbsp;&nbsp;fFactor:
										${bioMaterial.fFactor}&nbsp;&nbsp;choFactor:
										${bioMaterial.choFactor}</td>
								</TR>
								<TR>
									<td>Citation</td>
									<td><textArea readOnly rows="5" cols="100">${bioMaterial.citation}</textArea></td>
								</TR>
							</Table>


						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary"
								data-dismiss="modal">Close</button>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
		<!-- DETAILS POPUP END -->
		<h3 class="bg-info text-info" id="hideHeader" style="display: none">Contribute
			Your Bio-Material ...</h3>
		<h5 class="text-success" id="successResponseMaterial"></h5>
		<h5 id="errorMsgMaterial" class="text-danger"></h5>
		<!-- 		<h5 id="errorKeyword" class="text-danger" style="display:none">Please fill keyword1 before filling other keywords</h5>
 -->
		<p>&nbsp;</p>
		<div class="form-horizontal" id="editHideDiv" style="display: none">
			<div class="form-group">
				<input type="hidden" id="customIdHidden" /> <label
					class="text-info col-sm-1">Name</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="shortDesc"
						name="shortDesc" oninput="autoFillLongDesc()" autocomplete="off"
						required />
					<p id="notUniqueName"></p>
				</div>
			</div>

			<div class="form-group">
				<label class="text-info col-sm-1">Description</label>
				<div class="col-sm-10">
					<p class="form-control" id="longDesc" name="longDesc" value=""
						disabled="true"></p>
				</div>
			</div>
			<div class="form-group">
				<label class="text-info col-sm-1">Keywords:</label>
				<div class="col-sm-3 materialSearchBar">
					<input class="form-control" id="selectedMaterialId"
						name="selectedMaterialId" placeholder="Please enter material"
						oninput="autoFillLongDesc()" />
				</div>
				<div class="col-sm-2">
					<input type="text" class="form-control" id="keyword1"
						name="keyword1" maxlength="20" oninput="autoFillLongDesc()"
						placeholder="Keyword1" autocomplete="off" />
				</div>
				<div class="col-sm-2">
					<input type="text" class="form-control" id="keyword2"
						name="keyword2" placeholder="Keyword2" maxlength="20"
						oninput="autoFillLongDesc()" autocomplete="off"/>
				</div>

				<div class="col-sm-2">
					<input type="text" class="form-control" id="keyword3"
						name="keyword3" placeholder="Keyword3" maxlength="20"
						oninput="autoFillLongDesc()" autocomplete="off"/>
				</div>

				<div class="col-sm-2">
					<input type="text" class="form-control" id="keyword4"
						name="keyword4" placeholder="Keyword4" maxlength="20"
						oninput="autoFillLongDesc()" autocomplete="off"/>
				</div>
				<!-- <div class="col-sm-2" id="keywordDiv2" style="display: none">							
						</div>

						<div class="col-sm-2" id="keywordDiv3" style="display: none">						
						</div>

						<div class="col-sm-2" id="keywordDiv4" style="display: none">							
						</div>
						<div class="col-sm-1 add" id="addKey" style="display: none">
							<input type="hidden" id="keywordNumber" value="1"/>
							<button type="button" style="font-size:x-large" class="text-info unstyled-button addKeyword" id="addKeyword" value="yes" >&#65291;</button>
						</div> -->
			</div>
			<h4 class="bg-info text-info">Composition(in gms out of 100gms)</h4>
			<div class="form-group">
				<label class="text-info col-sm-1" id="waterLabel">Water </label>
				<div class="col-sm-1">
					<input class="form-control" id="water" value=""
						oninput="composition()" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="waterGram">g</label> <label
					class="text-info col-sm-1" id="fatLabel">Fat </label>
				<div class="col-sm-1">
					<input class="form-control" id="fat" value=""
						oninput="composition()" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="fatGram">g</label>
			</div>
			<div class="form-group">
				<label class="text-info col-sm-1" id="proteinLabel">Protein
				</label>
				<div class="col-sm-1">
					<input class="form-control" id="protein" value=""
						oninput="composition()" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="proteinGram">g</label> <label
					class="text-info col-sm-1" id="fiberLabel">Fiber </label>
				<div class="col-sm-1">
					<input class="form-control" id="fiber" value=""
						oninput="composition()" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="fiberGram">g</label> <label
					class="text-info col-sm-2">Additional Components:-</label>
				<div class="col-sm-4 abc">
					<select class="form-control" id="Additionalcomponents"
						name="Additionalcomponents" onChange="update()"></select>
				</div>
			</div>
			<div class="form-group">
				<label class="text-info col-sm-1" id="carbLabel">Carb </label>
				<div class="col-sm-1">
					<input class="form-control" id="carb" value=""
						oninput="composition()" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="carbGram">g</label> <label
					class="text-info col-sm-1" id="ashLabel">Ash </label>
				<div class="col-sm-1">
					<input class="form-control" id="ash" value=""
						oninput="composition()" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="ashGram">g</label>
			</div>
			<div class="form-group addComponents" id="addComponents"></div>
			<div class="form-group row">
				<div class="col-sm-offset-5 col-sm-2">
					<button type="submit" class="btn btn-success button1"
						id="saveCustomMaterial" disabled>Save</button>
				</div>
			</div>
		</div>






		<div id="footerbar">
			<p>&nbsp;</p>
			<%@ include file="./../footer_bar.jsp"%></div>
	</div>




</body>