package edu.cornell.cals.biomat.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.cornell.cals.biomat.dao.BioComposition;
import edu.cornell.cals.biomat.dao.BioGrouping;
import edu.cornell.cals.biomat.dao.BioMaterial;
import edu.cornell.cals.biomat.dao.BioMaterialComposition;
import edu.cornell.cals.biomat.model.material.BioMaterialCompositionForm;
import edu.cornell.cals.biomat.model.material.BioMaterialSearchForm;
import edu.cornell.cals.biomat.model.material.BioMaterialSearchResultsForm;
import edu.cornell.cals.biomat.model.material.BioObservedPointsForm;
import edu.cornell.cals.biomat.model.material.EditBioFormulaForm;
import edu.cornell.cals.biomat.model.measurement.BioMeasurementForm;
import edu.cornell.cals.biomat.service.BioCompositionService;
import edu.cornell.cals.biomat.service.BioFormulaMaterialService;
import edu.cornell.cals.biomat.service.BioMatEmailService;
import edu.cornell.cals.biomat.service.BioMaterialCompositionService;
import edu.cornell.cals.biomat.service.BioMaterialGroupingService;
import edu.cornell.cals.biomat.service.BioMaterialService;

@Controller
public class BioMaterialsController {
	private static final int PAGE_SIZE 		= 10;
	private static final int PAGER_ELEMENTS = 15;
	Logger logger = LoggerFactory.getLogger(BioMaterialsController.class);
	
	
	@Autowired
	protected BioMaterialService bioMaterialService;
	@Autowired
	BioMatEmailService bioMatEmailService;
	@Autowired
	protected BioMaterialCompositionService bioMaterialCompositionService;
	//@Autowired
	//protected BioMeasurementRepository bioMeasurementRepository;
	
	@Autowired
	private BioMaterialGroupingService bioMaterialGroupingService;

	@Autowired
	protected BioFormulaMaterialService bioFormulaMaterialService;
	
	@Autowired
	protected BioCompositionService bioCompositionService;
	
	@GetMapping("bioObservedPoints")
	public ModelAndView displayBioObservedPoints() {
		logger.info("displayBioObservedPoints");
		BioObservedPointsForm bioObservedPointsForm = new BioObservedPointsForm();
		
		ModelAndView  mv = new ModelAndView("materials/bioObservedPoints","bioObservedPointsForm",bioObservedPointsForm);
		return mv;
	}	

	@PostMapping("updateFormulaMaterialAssociation")
	public ModelAndView formulaMaterialAssociation(@RequestParam(value="selectedFormulaId") Long selectedFormulaId,
			@RequestParam(value="selectedBioMaterialId", required=false) Long selectedBioMaterialId,
			@RequestParam(value="selectedBioGroupId", required=false) Long selectedBioGroupId,
			@RequestParam(value="userAction", required=false) String userAction	,
			@AuthenticationPrincipal Principal principal) {
		
		logger.info("userAction {}",userAction);
		EditBioFormulaForm editBioFormulaForm= new EditBioFormulaForm();
		BioMaterial bm  = null;
		BioGrouping bg = null;
		if(userAction.equals("addMaterialToFormula")){
			try {
				boolean b = bioFormulaMaterialService.addMaterialToBioFormula(selectedFormulaId, selectedBioMaterialId,principal.getName());
				if(b == true) {
					bm  = bioMaterialService.getBioMaterial(selectedBioMaterialId);
				}
				else {
				editBioFormulaForm.setErrorMessage("This Material is Already Associate to that Formula");
				}				
			}
			catch(Exception ex) {
				logger.error(ex.getMessage(),ex);
				editBioFormulaForm.setErrorMessage("Unable to add Material to the Formula.  Has the material already been added?");
			}
		}else if(userAction.equals("deleteMaterialFromFormula")){
			bioFormulaMaterialService.delete(""+selectedBioMaterialId, ""+selectedFormulaId);
		}
		if(userAction.equals("addGoupToFormula")) {
			try {
				boolean bgs = bioFormulaMaterialService.addGroupToBioFormula(selectedFormulaId, selectedBioGroupId, principal.getName());
				if(bgs == true) {
					bg = bioMaterialGroupingService.getGroupNameAndIdBasedOnId(selectedBioGroupId);
				}
				else {
					editBioFormulaForm.setErrorMessage("This Group is Already Associate to that Formula");
				}				
			}
			catch(Exception ex) {
				logger.error(ex.getMessage(),ex);
				editBioFormulaForm.setErrorMessage("Can not Associate Selected Group, Because data problem.");
			}
		}
		else if(userAction.equals("deleteGroupFromFormula")){
			bioFormulaMaterialService.deleteGroupBasedOnId(""+selectedBioGroupId, ""+selectedFormulaId);
		}
		editBioFormulaForm.setSelectedFormulaId(selectedFormulaId);
		List<BioMaterial> bmList = bioFormulaMaterialService.getBioMaterialByFormulaId(selectedFormulaId);
		List<BioGrouping> bgList = bioFormulaMaterialService.getBioGroupByFormulaId(selectedFormulaId);
		bmList = bmList.stream().distinct().collect(Collectors.toList());
		bgList = bgList.stream().distinct().collect(Collectors.toList());
		if(bm!=null) {
			//Remove null from the list
			bmList  =bmList.stream().filter(bioMaterial -> bioMaterial!=null).collect(Collectors.toList());
			bmList.add(bm);
		}
		if(bg != null) {
			bgList  =bgList.stream().filter(bioGroup -> bioGroup!=null).collect(Collectors.toList());
			bgList.add(bg);
		}
		while (bmList.remove(null)) {
        }
		while (bgList.remove(null)) {
        }
		if(bmList == null) {
			editBioFormulaForm.setBiomaterialSize(0);
		}else {
			editBioFormulaForm.setBiomaterialSize(1);
		}
		if(bgList == null) {
			editBioFormulaForm.setBioGroupSize(0);
		}else {
			editBioFormulaForm.setBioGroupSize(1);
		}

		editBioFormulaForm.setBioMaterials(bmList);
		editBioFormulaForm.setBioGrouping(bgList);
		ModelAndView  mv = new ModelAndView("materials/associateFormulaAndMaterial","editBioFormulaForm",editBioFormulaForm);
		return mv;
	}	
	
	@GetMapping("associateFormulaAndMaterial")
	public ModelAndView displayEditFormulaPage() {
		logger.info("displayBioMaterialNutrientsPage");
		EditBioFormulaForm editBioFormulaForm= new EditBioFormulaForm();
		ModelAndView  mv = new ModelAndView("materials/associateFormulaAndMaterial","editBioFormulaForm",editBioFormulaForm);
		return mv;
	}
	
	@GetMapping("updateFormulaMaterialAssociation")
	public ModelAndView displayUpdateFormulaMaterialAssociation() {
		logger.info("displayBioMaterialNutrientsPage");
		EditBioFormulaForm editBioFormulaForm= new EditBioFormulaForm();
		ModelAndView  mv = new ModelAndView("materials/associateFormulaAndMaterial","editBioFormulaForm",editBioFormulaForm);
		return mv;
	}
	
	@PostMapping("bioMaterialComposition")
	public ModelAndView getBioMaterialNutrients(@RequestParam(value="selectedBioMaterialId", required=true) Long materialId) {
		logger.info("getBioMaterialNutrients");
		BioMaterialCompositionForm bioMaterialCompositionForm = new BioMaterialCompositionForm();
		List<BioMaterialComposition> bioMaterialNutrientList = bioMaterialCompositionService.getComposition(materialId);
		logger.info("Fetched bioMaterialNutrientList {} {}", bioMaterialNutrientList.size()  );
		bioMaterialCompositionForm.setSelectedBioMaterialId(materialId);
		bioMaterialCompositionForm.setBioMaterialNutrientList(bioMaterialNutrientList);
		ModelAndView  mv = new ModelAndView("materials/bioMaterialComposition","bioMaterialCompositionForm",bioMaterialCompositionForm);
		return mv;
	}	

	
	@GetMapping("bioMaterialComposition")
	public ModelAndView displayBioMaterialNutrientsPage() {
		logger.info("Display bioMaterialComposition");
		BioMaterialCompositionForm bioMaterialCompositionForm = new BioMaterialCompositionForm();
		ModelAndView  mv = new ModelAndView("materials/bioMaterialComposition","bioMaterialCompositionForm",bioMaterialCompositionForm);
		return mv;
	}	

	
	
	@GetMapping("addBioMaterial")
	public ModelAndView displayAddBioMaterialPage() {
		logger.info("displayAddBioMaterialPage ");
		BioMaterial bioMaterial = new BioMaterial();
		ModelAndView  mv = new ModelAndView("materials/addBioMaterial","bioMaterial",bioMaterial);
		return mv;
	}	
	
	@PostMapping("addBioMaterial")
	public ModelAndView addBioMaterial(HttpServletRequest request, @Valid @ModelAttribute BioMaterial bioMaterial, BindingResult bindingResult,@AuthenticationPrincipal Principal principal) {
		logger.info("POST addBioMaterial:user {}  {}", bioMaterial , principal);
		ModelAndView  mv ;
		if(bindingResult.hasErrors()) {
			logger.info("Error in Form Submission.  NOT Updating Data. ");
			mv = new ModelAndView("materials/addBioMaterial","bioMaterial",bioMaterial);
			
		}
		else if(principal ==null) {
			throw new RuntimeException("User is not authorized to update a material");
		}
		else {
			bioMaterial.setAddedBy(principal.getName());
			BioMaterial bm = bioMaterialService.updateBioMaterial(bioMaterial,principal.getName());
			logger.info("bioMaterialService.updateBioMaterial {}", bm);		
			mv = new ModelAndView("materials/addBioMaterial","bioMaterial",bm);
			mv.addObject("successMessage", "Successfully Added Bio-Material");
			mv.addObject("successMessage", "Thanks for Contributing your Bio-Material.  A message is sent to the administrator for approval. You will get another email when administrator takes an action.");
			bioMatEmailService.emailBioMaterialAdded(principal);
		}
		return mv;	
	}	

	
	@GetMapping("updateBioMaterial")
	public ModelAndView displayUpdateBioMaterialPage(@RequestParam(value="materialId", required=true) Long materialId) {
		logger.info("updateBioMaterial {}", materialId );
		BioMaterial bioMaterial = bioMaterialService.getBioMaterial(materialId);
		BioMeasurementForm bioMeasurementForm = new BioMeasurementForm();
		logger.debug("Fetched BioMatrial {}", bioMaterial );
		
		ModelAndView  mv = new ModelAndView("materials/updateBioMaterial","bioMaterial",bioMaterial);
		mv.addObject("bioMeasurementForm", bioMeasurementForm);
		return mv;
	}	

	@PostMapping("updateBioMaterial")
	public ModelAndView updateBioMaterialPage(HttpServletRequest request, @Valid @ModelAttribute BioMaterial bioMaterial, BindingResult bindingResult,@AuthenticationPrincipal Principal principal) {
		logger.info("POST updateBioMaterial:user {}  {}", bioMaterial , principal);
		ModelAndView  mv ;
		if(bindingResult.hasErrors()) {
			logger.info("Error in Form Submission.  NOT Updating Data. ");
			mv = new ModelAndView("materials/updateBioMaterial","bioMaterial",bioMaterial);
			
		}
		else if(principal ==null) {
			throw new RuntimeException("User is not authorized to update a material");
		}
		else {
			BioMaterial bm = bioMaterialService.updateBioMaterial(bioMaterial,principal.getName());
			logger.info("bioMaterialService.updateBioMaterial {}", bm);		
			mv = new ModelAndView("materials/updateBioMaterial","bioMaterial",bm);
			mv.addObject("successMessage", "Successfully Updated Bio-Material");
		}
		return mv;
	}	

	
	@GetMapping("searchBioMaterials")
	public ModelAndView displaySearchBioMaterialsPage(@ModelAttribute BioMaterialSearchForm bioMaterialSearchForm) {
	        
		logger.info("Start searchBioMaterials {}" , bioMaterialSearchForm);
		BioMaterialSearchForm BMSF = new BioMaterialSearchForm();
		BMSF.setUsdaOnly("a");
		logger.debug("Start searchBioMaterials {}" , bioMaterialSearchForm);
		ModelAndView  mv = new ModelAndView("materials/searchBioMaterials","bioMaterialSearchForm",BMSF);
		return mv;
	}
	
	@PostMapping("searchBioMaterials")
	public ModelAndView paginatedBioMaterialsResultsPage(HttpServletRequest request, @ModelAttribute @Valid BioMaterialSearchForm bioMaterialSearchForm,BindingResult bindingResult,  @RequestParam(value="pageNumber", required=false, defaultValue = "0") Integer pageNumber) {
		logger.info("Start searchBioMaterials with params {}",bioMaterialSearchForm);;
		ModelAndView  mv =null;
		int bioMaterialCount = 0;
		if(pageNumber > 0) {
			bioMaterialCount = 10 * pageNumber;
		}
		List<BioMaterial> bmList = new ArrayList<BioMaterial>();
		Map<String,Object> map = bioMaterialService.getBioMaterial(gotoPage(pageNumber), bioMaterialSearchForm.getBioMaterialName(), bioMaterialCount);
		Page<BioMaterial> boiMaterialPage = (Page<BioMaterial>)map.get("bioMaterialPage"); 		
		for(BioMaterial bm : boiMaterialPage){
			bmList.add(bm);
		}
		if(!bmList.isEmpty()) {
			int totalBioMaterialPages= 	((int)map.get("count")) / PAGE_SIZE;
			if(((int)map.get("count")) % PAGE_SIZE > 0) totalBioMaterialPages++;
				
			BioMaterialSearchResultsForm BMSRF = new BioMaterialSearchResultsForm();
			
			if(totalBioMaterialPages <=  PAGER_ELEMENTS ) {
				BMSRF.setPagerStart(0);
				BMSRF.setPagerEnd(totalBioMaterialPages);
				
			}
			else {
				
				if(pageNumber <= 5) {
					BMSRF.setPagerStart(0);
					BMSRF.setPagerEnd(9);
				}
				else {
					
					BMSRF.setPagerStart(pageNumber-4);
					if(totalBioMaterialPages<(pageNumber+5))
						BMSRF.setPagerEnd(totalBioMaterialPages);
					else
						BMSRF.setPagerEnd(pageNumber+5);
				}
			}
			
			BMSRF.setBioMaterials(bmList);
			BMSRF.setLastPage(totalBioMaterialPages);
			BMSRF.setCurrentPage(pageNumber);
			BMSRF.setBioMaterialSearchForm(bioMaterialSearchForm);
			logger.info("Total Pages for this Search {}" , totalBioMaterialPages);
			logger.info("SearchData {} ",BMSRF);
			
			mv = new ModelAndView("materials/searchBioMaterialsResults","bioMaterialSearchResultsForm",BMSRF);
		} else {
			mv = new ModelAndView("materials/searchBioMaterials");
			mv.addObject("errorMessage", "No material Found");
		}
		return mv;
	}
	
	@GetMapping("addCustomMaterial")
	public ModelAndView displayAddCustomMaterialPage() {
		logger.info("displayAddCustomBioMaterialPage");
		BioMaterial bioMaterial = new BioMaterial();
		ModelAndView  mv = new ModelAndView("materials/addCustomMaterial","bioMaterial",bioMaterial);
		return mv;
	}
	
	@GetMapping("getSelectedUniqueMaterial")
	public ResponseEntity<String>  searchBioMaterials(@RequestParam(value="q", required=false) String q) throws Exception {
		logger.info("Start getSelectedUniqueMaterial {} " + q);
		List<BioMaterial>selectedUniqueMaterial = new ArrayList<BioMaterial>();
		if(q.length()>1)
			selectedUniqueMaterial =bioMaterialService.getSelectedUniqueMaterial(q);
		ObjectMapper mapper = new ObjectMapper();
		String jsonSelectedMaterialArray = mapper.writeValueAsString(selectedUniqueMaterial);
		
		String s="{\"msg\":\"\", \"data\":" + jsonSelectedMaterialArray +"}";
		logger.info("end getSelectedUniqueMaterial " + s);
		return ResponseEntity.ok(s);
	}
	
	@GetMapping("saveCustomMaterial")
	public ResponseEntity<String> saveCustomMaterial(
			@RequestParam(value = "shortDescName", required = false) String shortDescName,
			@RequestParam(value = "longDescName", required = false) String longDescName,
			@RequestParam(value = "componentsName", required = false) List<String> componentsNameList, 
			@RequestParam(value = "componentsValue", required = false) List<Double> componentsValue,
			@RequestParam(value = "additionalComponentsId", required = false) List<Integer> additionalComponentsId,
			HttpServletRequest request, @AuthenticationPrincipal Principal principal) throws Exception {
		String s = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> response = new HashMap<>();
		BioMaterial bioMaterial = new BioMaterial();
		List<Integer> componentsIds = new ArrayList<Integer>();
		longDescName = longDescName.replaceAll("(,)*$", "");
		List<BioMaterial> bioMaterials = bioMaterialService.getBioMaterialForCustom(shortDescName);
		if(bioMaterials.isEmpty()) {
			Integer lastId = bioMaterialService.getLastIdOfBioMaterial();
			for (String value : componentsNameList)
			{
				if(value.equals("Fat")) {
					value = "Total lipid";
				}
				BioComposition componentsId = bioCompositionService.getBioCompositionByNutrientDesc(value);
				componentsIds.add(componentsId.getId());
			}		
			int i = 1;
			int lastDigit = lastId;
			while (lastId > 999) {
					lastId = lastId / 10;  
				}		
			String id = null;
			if(lastId.equals(999)) {
				bioMaterial.setId(Long.valueOf(lastDigit+ 1));
			}else {
				id = "99999000" + i;
				bioMaterial.setId(Long.parseLong(id));
			}
			int a = 0;
			boolean bmc = false;
			bioMaterial.setAddedBy(principal.getName());
			bioMaterial.setName(shortDescName);
			bioMaterial.setShortDesc(longDescName.toUpperCase());
			bioMaterial.setLongDesc(longDescName.toLowerCase());
			BioMaterial bm = bioMaterialService.updateBioMaterial(bioMaterial,principal.getName());
			List<Integer> newComponentsIds = Stream.concat(componentsIds.stream(), additionalComponentsId.stream())
                    .collect(Collectors.toList());
			for(Integer val: newComponentsIds)	{
				bmc = bioMaterialCompositionService.saveCompositionByMaterial(val,bioMaterial.getId(), componentsValue.get(a));
				a++;
			}	
			if (bmc == true && bm.getId() != null) {
				response.put("success", "Material is Added Successfully");
			} else {
				response.put("success", "Error in Material Submission");
			}
			String str = mapper.writeValueAsString(response.get("success"));
			s = "{\"msg\":\"\", \"data\":"+ str +"}";
		} else {
			response.put("success", "Name is not unique");
			String string = mapper.writeValueAsString(response.get("success"));
			s = "{\"msg\":" + string + ", \"data\":\"\"}";
		}		
		return ResponseEntity.ok(s);
	}
	
	@GetMapping("getCustomBioMaterialUsingId")
	public ResponseEntity<String> getCustomBioMaterialUsingId(@RequestParam(value = "biomaterialId", required = false) Long biomaterialId, HttpServletResponse response)
			throws Exception {
		List<Integer> compositionId = new ArrayList<>();
		List<Double> nutrientValue = new ArrayList<>();
		List<String> compositionName = new ArrayList<>();
		List<BioMaterial> bioMaterialList = new ArrayList<>();
		BioMaterial material = null;		
			BioMaterial customBioMaterial = bioMaterialService.getCustomBioMaterialUsingId(biomaterialId);
			List<BioMaterialComposition> bioMaterialComposition = bioMaterialCompositionService.getComposition(biomaterialId);
			bioMaterialComposition.forEach(compositionid -> {
				compositionId.add(compositionid.getBioComposition().getId());
				compositionName.add(compositionid.getBioComposition().getNutrientDesc());
				nutrientValue.add(compositionid.getNutrientValue());
			});
			List<BioMaterial> bmList = Arrays.asList(customBioMaterial);
			for (BioMaterial materialName : bmList) {
				material = new BioMaterial();
				material.setId(materialName.getId());
				material.setName(materialName.getName());
				material.setShortDesc(materialName.getShortDesc());
				material.setLongDesc(materialName.getLongDesc());
				material.setAddedBy(materialName.getAddedBy());
				material.setUpdatedBy(materialName.getUpdatedBy());
				material.setCreatedAt(materialName.getCreatedAt());
				material.setUpdatedAt(materialName.getUpdatedAt());
			}
			bioMaterialList.add(material);
			ObjectMapper mapper = new ObjectMapper();
			String customBioMaterialList = mapper.writeValueAsString(bioMaterialList);
			String compositionsId = mapper.writeValueAsString(compositionId);
			String nutrientsValue = mapper.writeValueAsString(nutrientValue);
			String compositionsName = mapper.writeValueAsString(compositionName);
			String listOfCustomMaterial = "{\"customBioMaterialList\":" + customBioMaterialList + ",\"compositionsId\":" + compositionsId + ",\"nutrientsValue\":" + nutrientsValue + ",\"compositionsName\":" + compositionsName + "}";
			logger.info("Returning groupDetails {}  ", listOfCustomMaterial);
			return ResponseEntity.ok(listOfCustomMaterial);
	}
	
	@GetMapping("deleteMaterialUsingMaterialId")
	public ModelAndView deleteMaterialUsingMaterialId(@RequestParam(value = "materialId", required = false) Long materialId, @RequestParam(value = "shortDesc", required = false) String shortDesc,  @ModelAttribute BioMaterialSearchForm bioMaterialSearchForm, BindingResult result,   @RequestParam(value="pageNumber", required=false, defaultValue = "0") Integer pageNumber) {
		logger.info("deleteMaterialUsingMaterialId {}", materialId);
		ModelAndView mv = null;
		boolean flagResult = bioMaterialService.removeMaterialUsingId(materialId);
		if(flagResult == true) {
			logger.info("material is deleted {}", materialId);
			List<BioMaterial> bmList = new ArrayList<BioMaterial>();
			bioMaterialSearchForm.setBioMaterialName(shortDesc);
			int bioMaterialCount = 0;
			if(pageNumber > 1) {
				bioMaterialCount = 10 * pageNumber;
			}
			Map<String,Object> map = bioMaterialService.getBioMaterial(gotoPage(pageNumber), bioMaterialSearchForm.getBioMaterialName(), bioMaterialCount);
			Page<BioMaterial> boiMaterialPage = (Page<BioMaterial>)map.get("bioMaterialPage"); 		
			for(BioMaterial bm : boiMaterialPage){
				bmList.add(bm);
			}
			int totalBioMaterialPages= 	((int)map.get("count")) / PAGE_SIZE;
			if(((int)map.get("count")) % PAGE_SIZE > 0) totalBioMaterialPages++;
				
			BioMaterialSearchResultsForm BMSRF = new BioMaterialSearchResultsForm();
			
			if(totalBioMaterialPages <=  PAGER_ELEMENTS ) {
				BMSRF.setPagerStart(0);
				BMSRF.setPagerEnd(totalBioMaterialPages);
				
			}
			else {
				
				if(pageNumber <= 5) {
					BMSRF.setPagerStart(0);
					BMSRF.setPagerEnd(9);
				}
				else {
					
					BMSRF.setPagerStart(pageNumber-4);
					if(totalBioMaterialPages<(pageNumber+5))
						BMSRF.setPagerEnd(totalBioMaterialPages);
					else
						BMSRF.setPagerEnd(pageNumber+5);
				}
			}
			
			BMSRF.setBioMaterials(bmList);
			BMSRF.setLastPage(totalBioMaterialPages);
			BMSRF.setCurrentPage(pageNumber);
			BMSRF.setBioMaterialSearchForm(bioMaterialSearchForm);
			logger.info("Total Pages for this Search {}" , totalBioMaterialPages);
			logger.info("SearchData {} ",BMSRF);
			
			mv = new ModelAndView("materials/searchBioMaterialsResults","bioMaterialSearchResultsForm",BMSRF);
			return mv;
		}
		else {
			logger.warn("material is not deleted {}", materialId);
			mv = new ModelAndView("materials/searchBioMaterialsResults");
			return mv;
		}
	}
	
	@GetMapping("saveEditCustomMaterial")
	public ResponseEntity<String> saveEditCustomMaterial(
			@RequestParam(value = "shortDescName", required = false) String shortDescName,
			@RequestParam(value = "longDescName", required = false) String longDescName,
			@RequestParam(value = "componentsName", required = false) List<String> componentsNameList, 
			@RequestParam(value = "componentsValue", required = false) List<Double> componentsValue,
			@RequestParam(value = "materialId", required = false) Long materialId,
			@RequestParam(value = "status", required = false) int status,
			@RequestParam(value = "additionalComponentsId", required = false) List<Integer> additionalComponentsId,
			HttpServletRequest request, @AuthenticationPrincipal Principal principal) throws Exception {
		String s = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> response = new HashMap<>();
		List<Integer> componentsIds = new ArrayList<Integer>();
		String userName = principal.getName();
		longDescName = longDescName.replaceAll("(,)*$", "");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String updatedDate = dtf.format(now);
		List<BioMaterial> bioMaterials = new ArrayList<BioMaterial>();
		if(status == 0) {
			bioMaterials = bioMaterialService.getBioMaterialForCustom(shortDescName);
		}		
		if(bioMaterials.isEmpty()) {
			boolean bmc = false;
			int a = 0;
			for (String value : componentsNameList)
			{
				if(value.equals("Fat")) {
					value = "Total lipid";
				}
				BioComposition componentsId = bioCompositionService.getBioCompositionByNutrientDesc(value);
				componentsIds.add(componentsId.getId());
			}
			boolean bm = bioMaterialService.updateCustomBioMaterialUsingId(materialId, shortDescName, longDescName.toUpperCase(), longDescName.toLowerCase(), updatedDate, userName);	
			boolean bioCM = bioMaterialCompositionService.updateCompositionByMaterial(materialId);
			List<Integer> newComponentsIds = Stream.concat(componentsIds.stream(), additionalComponentsId.stream())
                    .collect(Collectors.toList());
			if(bioCM == true) {
				for(Integer val: newComponentsIds) { 				  
					 bmc = bioMaterialCompositionService.saveCompositionByMaterial(val,materialId, componentsValue.get(a)); 
					 a++; 
				}
			}
			if (bm == true && bmc == true) {
				response.put("success", "Material is Added Successfully");
			} else {
				response.put("success", "Error in Material Submission");
			}
			String str = mapper.writeValueAsString(response.get("success"));
			s = "{\"msg\":\"\", \"data\":"+ str +"}";
		} else {
			response.put("success", "Name is not unique");
			String string = mapper.writeValueAsString(response.get("success"));
			s = "{\"msg\":" + string + ", \"data\":\"\"}";
		}
		return ResponseEntity.ok(s);
	}
	
	@GetMapping("getCompositions")
	public ResponseEntity<String>  getCompositions() throws Exception {
		logger.info("Start getCompositions {} ");
		List<BioComposition> getCompositions = new ArrayList<BioComposition>();
		getCompositions = bioCompositionService.getCompositions();
		String o = "_";
		while (getCompositions.remove(o)) {
        }
		ObjectMapper mapper = new ObjectMapper();
		String jsonGetCompositions = mapper.writeValueAsString(getCompositions);
		String s="{\"msg\":\"\", \"data\":" + jsonGetCompositions +"}";
		logger.info("end getCompositions " + s);
		return ResponseEntity.ok(s);
	}
	
	@GetMapping("getBioCompositionByNutrientDescs")
	public ResponseEntity<String>  getBioCompositionByNutrientDescs(@RequestParam(value = "nutrientDesc", required = false) String value) throws Exception {
		logger.info("Start getBioCompositionByNutrientDesc {} ");
		BioComposition compositions = bioCompositionService.getBioCompositionByNutrientDesc(value);
		ObjectMapper mapper = new ObjectMapper();
		String compositionsValue = mapper.writeValueAsString(compositions);
		String s="{\"msg\":\"\", \"data\":" + compositionsValue +"}";
		logger.info("end compositions " + s);
		return ResponseEntity.ok(s);
	}
	
	@GetMapping("getBioCompositionByNutrientDesc")
	public ResponseEntity<String>  getBioCompositionByNutrientDesc(@RequestParam(value = "nutrientId", required = false) List<Integer> additionalCompositionId, @RequestParam(value = "existingCompositionValue", required = false) List<Integer> compositionValue) throws Exception {
		logger.info("Start getBioCompositionByNutrientDesc {} ");
		BioComposition compositions = null;
		List<BioComposition> compositionsName = new ArrayList<BioComposition>();
		for(Integer val: additionalCompositionId) { 
			compositions = bioCompositionService.getBioCompositionByNutrientDescAdditional(val);
			compositionsName.add(compositions);
		}
		ObjectMapper mapper = new ObjectMapper();
		String existingCompositionsName = mapper.writeValueAsString(compositionsName);
		String existingCompositionsValue = mapper.writeValueAsString(compositionValue);
		String s="{\"value\":" + existingCompositionsValue + ", \"data\":" + existingCompositionsName +"}";
		logger.info("end compositions " + s);
		return ResponseEntity.ok(s);
	}
	private PageRequest gotoPage(int pageNumber){
		PageRequest requestedPage = new PageRequest(pageNumber,PAGE_SIZE,Sort.Direction.ASC,"shortDesc");
		return requestedPage;
	}
	
	
}
