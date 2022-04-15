<!DOCTYPE HTML>
<html lang="en">

<%@ include file="./../header.jsp"%>
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
<body>

	<div class="container">
		<div id="topbar">
			<%@ include file="./../top_bar.jsp"%></div>
		<div id="menubar">
			<%@ include file="./../menu_bar.jsp"%></div>


		<p>&nbsp;</p>
		<h3 class="bg-info text-info" id="hideHeader">Contribute Your
			Bio-Material ...</h3>
		<h5 class="text-success" id="successResponseMaterial"></h5>
		<h5 id="errorMsgMaterial" class="text-danger"></h5>
<!-- 		<h5 id="errorKeyword" class="text-danger" style="display:none">Please fill keyword1 before filling other keywords</h5>
 -->		<div class="form-horizontal" id="hideDiv">
			<div class="form-group">
				<input type="hidden" id="customIdHidden" /> <label
					class="text-info col-sm-1">Name</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="shortDesc"
						name="shortDesc" oninput="autoFillLongDesc()" autocomplete="off" required />
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
				<div class="col-sm-3">
					<input class="form-control" id="selectedMaterialId"
						name="selectedMaterialId" placeholder="Please enter material"
						oninput="autoFillLongDesc()" />
				</div>
				<div class="col-sm-2">
					<input type="text" class="form-control" id="keyword1"
						name="keyword1" placeholder="Keyword1" maxlength="20"
						oninput="autoFillLongDesc()" autocomplete="off"/>
				</div>
				<!-- <div class="col-sm-1 add">
						<input type="hidden" id="keywordNumber" value="1"/>
						<button type="button" style="font-size:x-large" class="text-info unstyled-button addKeyword" id="addKeyword" value="yes" >&#65291;</button>
				</div> -->
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
			</div>
			<h4 class="bg-info text-info">Characteristic parameters values (In a 100 gms of the custom materials)</h4>
			<div class="form-group">
				<label class="text-info col-sm-1" id="waterLabel">Water </label>
				<div class="col-sm-1">
					<input class="form-control" id="water" value="" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="waterGram">g</label>
				<label class="text-info col-sm-1" id="fatLabel">Fat </label>
				<div class="col-sm-1">
					<input class="form-control" id="fat" value="" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="fatGram">g</label>
			</div>
			<div class="form-group">
				<label class="text-info col-sm-1" id="proteinLabel">Protein
				</label>
				<div class="col-sm-1">
					<input class="form-control" id="protein" value="" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="proteinGram">g</label>
				<label class="text-info col-sm-1" id="fiberLabel">Fiber </label>
				<div class="col-sm-1">
					<input class="form-control" id="fiber" value="" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="fiberGram">g</label>
				<label
					class="text-info col-sm-2">Additional Components:-</label>
				<div class="col-sm-4">
					<select class="form-control" id="Additionalcomponents" name="Additionalcomponents" onChange="update()"></select>
				</div>
			</div>
			<div class="form-group">
				<label class="text-info col-sm-1" id="carbLabel">Carb </label>
				<div class="col-sm-1">
					<input class="form-control" id="carb" value="" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="carbGram">g</label>
				<label class="text-info col-sm-1" id="ashLabel">Ash </label>
				<div class="col-sm-1">
					<input class="form-control" id="ash" value="" autocomplete="off" required />
				</div>
				<label class="text-info col-sm-1" id="ashGram">g</label>
			</div>
			<div class="form-group addComponents" id="addComponents"></div>
			<div class="form-group row">
				<div class="col-sm-offset-5 col-sm-2">
					<button type="submit" class="btn btn-success button1"
						id="saveCustomMaterial">Save</button>
				</div>
			</div>
		</div>
		<div id="footerbar">
			<p>&nbsp;</p>
			<%@ include file="./../footer_bar.jsp"%></div>
	</div>
</body>
<script>
        $(document).ready(function() { $("#Additionalcomponents").select2(); });
</script>