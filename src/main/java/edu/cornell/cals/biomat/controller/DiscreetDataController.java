package edu.cornell.cals.biomat.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.crypto.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.cornell.cals.biomat.dao.BioDiscreetData;
import edu.cornell.cals.biomat.dao.BioGrouping;
import edu.cornell.cals.biomat.dao.BioVariable;
import edu.cornell.cals.biomat.excel.ExcelPOIHelper;
import edu.cornell.cals.biomat.excel.MyCell;
import edu.cornell.cals.biomat.model.discreet.BioDiscreetDataForm;
import edu.cornell.cals.biomat.model.discreet.ShowBioDiscreetDataForm;
import edu.cornell.cals.biomat.repository.BioDiscreetDataRepository;
import edu.cornell.cals.biomat.repository.BioMaterialGroupingRepository;
import edu.cornell.cals.biomat.repository.BioVariableRepository;
import edu.cornell.cals.biomat.service.BioDiscreetDataService;
import edu.cornell.cals.biomat.service.BioFormulaMaterialService;
import edu.cornell.cals.biomat.service.BioMaterialGroupingService;
import edu.cornell.cals.biomat.service.BioMaterialService;
import edu.cornell.cals.biomat.service.BioVariableService;

@Controller
public class DiscreetDataController {
	@Autowired
	protected BioDiscreetDataService bioDiscreetDataService;
	@Autowired
	protected BioVariableService bioVariableService;
	@Autowired
	protected BioMaterialService bioMaterialService;
	@Autowired
	protected BioDiscreetDataRepository bioDiscreetDataRepository;

	@Autowired
	private BioVariableRepository bioVariableRepo;

	@Autowired
	private BioFormulaMaterialService bioFormulaMaterialService;

	@Autowired
	private BioMaterialGroupingRepository groupingRepo;

	@Resource(name = "excelPOIHelper")
	private ExcelPOIHelper excelPOIHelper;

	@Autowired
	private BioMaterialGroupingService grpService;

	Logger logger = LoggerFactory.getLogger(DiscreetDataController.class);

	@GetMapping("searchBioProperty")
	public ModelAndView searchBioProperty() {
		logger.info("search BioProperty");
		ShowBioDiscreetDataForm showBioDiscreetDataForm = new ShowBioDiscreetDataForm();
		ModelAndView mv = new ModelAndView("contribute/searchBioProperty", "showBioDiscreetDataForm",
				showBioDiscreetDataForm);
		return mv;
	}

	@PostMapping("searchBioProperty")
	private ModelAndView showBioProperty(HttpServletRequest request,
			@Valid @ModelAttribute ShowBioDiscreetDataForm showBioDiscreetDataForm, BindingResult bindingResult,
			@AuthenticationPrincipal Principal principal) {
		logger.info("Start");
		ModelAndView mv;
		if (showBioDiscreetDataForm.getDataSetName() != "") {
			List<BioDiscreetData> bioDiscreetData = bioDiscreetDataService
					.getBioDiscreetDataByDataSetNameORYVariable(showBioDiscreetDataForm.getDataSetName());
			if (bindingResult.hasErrors()) {
				logger.info("Error in Form Submission.  NOT deleting Data. ");
				mv = new ModelAndView("contribute/showDiscreetData", "showBioDiscreetDataForm",
						showBioDiscreetDataForm);
			} else if (!bioDiscreetData.isEmpty()) {
				mv = new ModelAndView("contribute/searchBioProperty", "bioDiscreetData", bioDiscreetData);
			} else {
				mv = new ModelAndView("contribute/searchBioProperty", "searchErrorMessage", "No Discreet Data found");
			}
		} else {
			mv = new ModelAndView("contribute/searchBioProperty", "searchErrorMessage",
					"Enter dataset name or Y-Variable name");
		}
		return mv;
	}

	@GetMapping("showDiscreetData")
	public ModelAndView showDiscreetDataPage(@ModelAttribute ShowBioDiscreetDataForm showBioDiscreetDataForm) {
		logger.info("Start");
		showBioDiscreetDataForm.setBioDiscreetDatas(bioDiscreetDataService.getAllBioDiscreetData());
		logger.info("Start showBioDiscreetData in get{}", showBioDiscreetDataForm);
		ModelAndView mv = new ModelAndView("contribute/showDiscreetData", "showBioDiscreetDataForm",
				showBioDiscreetDataForm);
		return mv;
	}

	@GetMapping("deleteDiscreetData")
	public ModelAndView deleteBioDiscreetDataPage(@ModelAttribute ShowBioDiscreetDataForm showBioDiscreetDataForm) {
		logger.info("Start");
		showBioDiscreetDataForm.setBioDiscreetDatas(bioDiscreetDataService.getAllBioDiscreetData());
		logger.info("Start showBioDiscreetData in get{}", showBioDiscreetDataForm);
		ModelAndView mv = new ModelAndView("contribute/showDiscreetData", "showBioDiscreetDataForm",
				showBioDiscreetDataForm);
		return mv;

	}

	@PostMapping("deleteDiscreetData")
	private ModelAndView deleteStudent(HttpServletRequest request, @RequestParam Long id,
			@Valid @ModelAttribute ShowBioDiscreetDataForm showBioDiscreetDataForm, BindingResult bindingResult,
			@AuthenticationPrincipal Principal principal) {
		logger.info("Start");
		logger.info("DiscreetData_Id : " + id);
		ModelAndView mv;
		if (bindingResult.hasErrors()) {
			logger.info("Error in Form Submission.  NOT deleting Data. ");
			mv = new ModelAndView("contribute/showDiscreetData", "showBioDiscreetDataForm", showBioDiscreetDataForm);
		} else if (principal == null) {
			throw new RuntimeException("User is not Authorized to delete DiscreetData");
		} else {
			BioDiscreetData bioDiscreetData = bioDiscreetDataService.getBioDiscreetData(id);
			bioDiscreetDataRepository.delete(bioDiscreetData);
			showBioDiscreetDataForm.setBioDiscreetDatas(bioDiscreetDataService.getAllBioDiscreetData());
			logger.info("new showBioDiscreetData after deleting{}", showBioDiscreetDataForm);
		}
		mv = new ModelAndView("contribute/showDiscreetData", "showBioDiscreetDataForm", showBioDiscreetDataForm);
		mv.addObject("successMessage", "Successfully deleted Bio-DiscreetData");

		return mv;

	}

	@GetMapping("addDiscreetData")
	public ModelAndView displayAddDiscreetDataPage() {
		logger.info("Start");
		BioDiscreetDataForm BDD = new BioDiscreetDataForm();
		ModelAndView mv = new ModelAndView("contribute/addDiscreetData", "bioDiscreetDataForm", BDD);
		return mv;
	}

	@PostMapping("addDiscreetData")
	public ResponseEntity<String> saveAddDiscreetDataPage(HttpServletRequest request,
			@Valid @ModelAttribute BioDiscreetDataForm bioDiscreetDataForm, BindingResult bindingResult,
			@AuthenticationPrincipal Principal principal) throws IOException {
		logger.info("Start ");
		ObjectMapper mapper = new ObjectMapper();
		String s = "";
		if (bindingResult.hasErrors()) {
			String jsonError = mapper.writeValueAsString("Error in Form Submission.  NOT Updating Data.");
			s = "{\"error\":" + jsonError + "}";
			logger.info("Error in Form Submission.  NOT Updating Data. " + s);
			return ResponseEntity.ok(s);
		} else if (principal == null) {
			throw new RuntimeException("User is not authorized to add discreet dataset");
		} else {
			MultipartFile file = bioDiscreetDataForm.getFile();
			InputStream in = file.getInputStream();
			File currDir = new File(".");
			String path = currDir.getAbsolutePath();
			String fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
			FileOutputStream f = new FileOutputStream(fileLocation);
			int ch = 0;
			while ((ch = in.read()) != -1) {
				f.write(ch);
			}
			f.flush();
			f.close();
			Map<Integer, List<MyCell>> data = null;
			if (fileLocation != null && (fileLocation.endsWith(".xlsx") || fileLocation.endsWith(".xls"))) {
				data = excelPOIHelper.readExcel(fileLocation);
			}

			bioDiscreetDataForm.setData(data);
			if (bioDiscreetDataForm.getMaterialId() != null) {
				for (int i = 1; i < data.size(); i++) {
					List<MyCell> row = data.get(i);
					BioDiscreetData BDD = new BioDiscreetData();
					BDD.setMaterialId(bioDiscreetDataForm.getMaterialId());
					BDD.setAuthor_name(bioDiscreetDataForm.getAuthorName());
					BDD.setYear(bioDiscreetDataForm.getYear());
					BDD.setDatasetName(bioDiscreetDataForm.getDatasetName());
					String str = String.valueOf(row.get(row.size() - 1).getContent());
					row.remove(row.size() - 1);
					BDD.setyValue(Double.parseDouble(str));
					BDD.setyVariableId(bioDiscreetDataForm.getyVariableId());
					BDD.setaVariableId(bioDiscreetDataForm.getxVariableId1());
					BDD.setbVariableId(bioDiscreetDataForm.getxVariableId2());
					BDD.setcVariableId(bioDiscreetDataForm.getxVariableId3());
					BDD.setdVariableId(bioDiscreetDataForm.getxVariableId4());
					BDD.seteVariableId(bioDiscreetDataForm.getxVariableId5());
					BDD.setfVariableId(bioDiscreetDataForm.getxVariableId6());
					BDD.setgVariableId(bioDiscreetDataForm.getxVariableId7());
					BDD.sethVariableId(bioDiscreetDataForm.getxVariableId8());
					BDD.setiVariableId(bioDiscreetDataForm.getxVariableId9());
					BDD.setjVariableId(bioDiscreetDataForm.getxVariableId10());

					int length = row.size();
					if (length > 0) {
						BDD.setaValue(Double.parseDouble(row.get(0).getContent()));
					}
					if (length > 1) {
						BDD.setbValue(Double.parseDouble(row.get(1).getContent()));
					}
					if (length > 2) {
						BDD.setcValue(Double.parseDouble(row.get(2).getContent()));
					}
					if (length > 3) {
						BDD.setdValue(Double.parseDouble(row.get(3).getContent()));
					}
					if (length > 4) {
						BDD.seteValue(Double.parseDouble(row.get(4).getContent()));
					}
					if (length > 5) {
						BDD.setfValue(Double.parseDouble(row.get(5).getContent()));
					}
					if (length > 6) {
						BDD.setgValue(Double.parseDouble(row.get(6).getContent()));
					}
					if (length > 7) {
						BDD.sethValue(Double.parseDouble(row.get(7).getContent()));
					}
					if (length > 8) {
						BDD.setiValue(Double.parseDouble(row.get(8).getContent()));
					}
					if (length > 9) {
						BDD.setjValue(Double.parseDouble(row.get(9).getContent()));
					}
					BDD.setAddedBy(principal.getName());
					BDD.setUpdatedBy(principal.getName());
					bioDiscreetDataRepository.save(BDD);
				}
			}
			if (bioDiscreetDataForm.getGroupId() != null) {
				List<BioDiscreetData> allDiscreetData = new ArrayList<BioDiscreetData>();
				BioGrouping group = grpService.getPreExstingGroupName(bioDiscreetDataForm.getGroupId());
				List<String> singleId = Arrays.asList(group.getMaterialsInGroup().split(","));
				for (int j = 0; j < singleId.size(); j++) {
					for (int i = 1; i < data.size(); i++) {
						List<MyCell> row = data.get(i);
						BioDiscreetData BDD = new BioDiscreetData();
						BDD.setMaterialId(Long.parseLong(singleId.get(j)));
						BDD.setAuthor_name(bioDiscreetDataForm.getAuthorName());
						BDD.setYear(bioDiscreetDataForm.getYear());
						BDD.setDatasetName(bioDiscreetDataForm.getDatasetName());
						BDD.setGroupId(bioDiscreetDataForm.getGroupId());
						String str = String.valueOf(row.get(row.size() - 1).getContent());
						row.remove(row.size() - 1);
						BDD.setyValue(Double.parseDouble(str));
						BDD.setyVariableId(bioDiscreetDataForm.getyVariableId());
						BDD.setaVariableId(bioDiscreetDataForm.getxVariableId1());
						BDD.setbVariableId(bioDiscreetDataForm.getxVariableId2());
						BDD.setcVariableId(bioDiscreetDataForm.getxVariableId3());
						BDD.setdVariableId(bioDiscreetDataForm.getxVariableId4());
						BDD.seteVariableId(bioDiscreetDataForm.getxVariableId5());
						BDD.setfVariableId(bioDiscreetDataForm.getxVariableId6());
						BDD.setgVariableId(bioDiscreetDataForm.getxVariableId7());
						BDD.sethVariableId(bioDiscreetDataForm.getxVariableId8());
						BDD.setiVariableId(bioDiscreetDataForm.getxVariableId9());
						BDD.setjVariableId(bioDiscreetDataForm.getxVariableId10());

						int length = row.size();
						if (length > 0) {
							BDD.setaValue(Double.parseDouble(row.get(0).getContent()));
						}
						if (length > 1) {
							BDD.setbValue(Double.parseDouble(row.get(1).getContent()));
						}
						if (length > 2) {
							BDD.setcValue(Double.parseDouble(row.get(2).getContent()));
						}
						if (length > 3) {
							BDD.setdValue(Double.parseDouble(row.get(3).getContent()));
						}
						if (length > 4) {
							BDD.seteValue(Double.parseDouble(row.get(4).getContent()));
						}
						if (length > 5) {
							BDD.setfValue(Double.parseDouble(row.get(5).getContent()));
						}
						if (length > 6) {
							BDD.setgValue(Double.parseDouble(row.get(6).getContent()));
						}
						if (length > 7) {
							BDD.sethValue(Double.parseDouble(row.get(7).getContent()));
						}
						if (length > 8) {
							BDD.setiValue(Double.parseDouble(row.get(8).getContent()));
						}
						if (length > 9) {
							BDD.setjValue(Double.parseDouble(row.get(9).getContent()));
						}
						BDD.setAddedBy(principal.getName());
						BDD.setUpdatedBy(principal.getName());
						MyCell cells = new MyCell();
						cells.setContent(str);
						row.add(cells);
						allDiscreetData.add(BDD);
					}
				}
				bioDiscreetDataRepository.saveAll(allDiscreetData);
			}
		}
		String jsonUploaded = mapper.writeValueAsString("DataSet has been uploaded successfully!");
		s = "{\"msg\":" + jsonUploaded + "}";
		logger.info("DataSet has been uploaded successfully. " + s);
		return ResponseEntity.ok(s);
	}

	/*
	 * BioDiscreetData generateNewBDD (BioDiscreetDataForm bioDiscreetDataForm) {
	 * BioDiscreetData BDD = new BioDiscreetData();
	 * BDD.setBioMaterial(bioMaterialService.getBioMaterialByUsdaId(
	 * bioDiscreetDataForm.getMaterialId()));
	 * BDD.setAuthorName(bioDiscreetDataForm.getAuthorName());
	 * BDD.setYear(bioDiscreetDataForm.getYear());
	 * BDD.setMaterialId(bioDiscreetDataForm.getMaterialId());
	 * BDD.setyVariableId(bioDiscreetDataForm.getyVariableId());
	 * //BDD.setxVariableId(bioDiscreetDataForm.getxVariableId1());
	 * BDD.setGroupId(bioDiscreetDataForm.getGroupId());
	 * BDD.setVariableId(bioDiscreetDataForm.getVariableId());
	 * BDD.setDatasetName(bioDiscreetDataForm.getDatasetName());
	 * BioVariableAndCompostionModel bioVariableAndCompostionModel=
	 * bioFormulaMaterialService.getVariablesInFormula(bioDiscreetDataForm.
	 * getMaterialId(), bioDiscreetDataForm.getyVariableId());
	 * BDD.setyBioVariable(bioVariableService.getBioVariable(bioDiscreetDataForm.
	 * getyVariableId()));
	 * 
	 * List<BioVariable> bbv = bioVariableAndCompostionModel.getBioVariables();
	 * 
	 * if(bbv.size() >= 1) { BDD.setaBioVariable(bbv.get(0));
	 * BDD.setaVariableId(bbv.get(0).getId()); } if(bbv.size() >= 2) {
	 * BDD.setbBioVariable(bbv.get(1)); BDD.setbVariableId(bbv.get(1).getId()); }
	 * if(bbv.size() >= 3) { BDD.setcBioVariable(bbv.get(2));
	 * BDD.setcVariableId(bbv.get(2).getId()); } if(bbv.size() >= 4) {
	 * BDD.setdBioVariable(bbv.get(3)); BDD.setdVariableId(bbv.get(3).getId()); }
	 * if(bbv.size() >= 5) { BDD.seteBioVariable(bbv.get(4));
	 * BDD.seteVariableId(bbv.get(4).getId()); } if(bbv.size() >= 6) {
	 * BDD.setfBioVariable(bbv.get(5)); BDD.setfVariableId(bbv.get(5).getId()); }
	 * if(bbv.size() >= 7) { BDD.setgBioVariable(bbv.get(6));
	 * BDD.setgVariableId(bbv.get(6).getId()); } if(bbv.size() >= 8) {
	 * BDD.sethBioVariable(bbv.get(7)); BDD.sethVariableId(bbv.get(7).getId()); }
	 * if(bbv.size() >= 9) { BDD.setiBioVariable(bbv.get(8));
	 * BDD.setiVariableId(bbv.get(8).getId()); } if(bbv.size() >= 10) {
	 * BDD.setjBioVariable(bbv.get(9)); BDD.setjVariableId(bbv.get(9).getId()); }
	 * 
	 * return BDD;
	 * 
	 * 
	 * }
	 */

	@GetMapping("getAllVariables")
	public ResponseEntity<String> getAllVariables() throws Exception {
		List<BioVariable> variableList = new ArrayList<BioVariable>();
		List<BioVariable> listOfVariables = bioVariableService.getAllBioVariable();
		listOfVariables.forEach(bio -> {
			if (!bio.getName().isEmpty()) {
				variableList.add(bio);
			}
		});
		ObjectMapper mapper = new ObjectMapper();
		String jsonVariableArray = mapper.writeValueAsString(variableList);

		String s = "{\"data\":" + jsonVariableArray + "}";
		logger.info("end BioVariable " + s);
		return ResponseEntity.ok(s);
	}

	@GetMapping("getDiscreeDataSetUsingName")
	public ResponseEntity<String> getDiscreeDataSetUsingName(@RequestParam(value = "dataSet", required = false) String dataSet) throws Exception {
		logger.info("start BioDiscreet Data Set ");
		List<String> discreetDataSetVariableName = new ArrayList<String>();
		List<Double> discreetDataSetVariableValue = new ArrayList<Double>();
		List<Long> discreetDataSetVariableMaterialId = new ArrayList<Long>();
		List<Long> discreetDataSetVariablesMaterialId = new ArrayList<Long>();
		List<BioDiscreetData> bddList = null;
		List<BioDiscreetData> listOfDiscreetDataMaterialId = null;
		String bioGroupName = "";
		String bioMaterialName = "";
		bddList = bioDiscreetDataService.getAllDetailsOfDiscreeDataSetUsingName(dataSet);
		listOfDiscreetDataMaterialId= bioDiscreetDataRepository.getBioDiscreetDataSetByName(dataSet);
		if(bddList.get(0).getGroupId() != null) {
			BioGrouping biogroups = groupingRepo.findByGroupNameUsingGroupId(bddList.get(0).getGroupId());
			bioGroupName = biogroups.getGroupName();
		} else {
			bioMaterialName = bddList.get(0).getBioMaterial().getShortDesc();
		}
		if (bddList.get(0).getaBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).getaBioVariable().getName());
		if (bddList.get(0).getbBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).getbBioVariable().getName());
		if (bddList.get(0).getcBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).getcBioVariable().getName());
		if (bddList.get(0).getdBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).getdBioVariable().getName());
		if (bddList.get(0).geteBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).geteBioVariable().getName());
		if (bddList.get(0).getfBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).getfBioVariable().getName());
		if (bddList.get(0).getgBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).getgBioVariable().getName());
		if (bddList.get(0).gethBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).gethBioVariable().getName());
		if (bddList.get(0).getiBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).getiBioVariable().getName());
		if (bddList.get(0).getjBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).getjBioVariable().getName());
		if (bddList.get(0).getyBioVariable().getId() != 0)
			discreetDataSetVariableName.add(bddList.get(0).getyBioVariable().getName());
		if(listOfDiscreetDataMaterialId != null) {
			listOfDiscreetDataMaterialId.forEach(materialId -> {
				discreetDataSetVariableMaterialId.add(materialId.getMaterialId());
			});
		}
		bddList.forEach(discreet -> {			
			if (discreet.getaBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.getaValue());
			}
			if (discreet.getbBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.getbValue());
			}
			if (discreet.getcBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.getcValue());
			}
			if (discreet.getdBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.getdValue());
			}
			if (discreet.geteBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.geteValue());
			}
			if (discreet.getfBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.getfValue());
			}
			if (discreet.getgBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.getgValue());
			}
			if (discreet.gethBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.gethValue());
			}
			if (discreet.getiBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.getiValue());
			}
			if (discreet.getjBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.getjValue());
			}
			if (discreet.getyBioVariable().getId() != 0) {
				discreetDataSetVariableValue.add(discreet.getyValue());
			}
		});
	discreetDataSetVariablesMaterialId = discreetDataSetVariableMaterialId.stream().distinct().collect(Collectors.toList());
	ObjectMapper mapper = new ObjectMapper();
	String DetailsDiscreet = mapper.writeValueAsString(bddList);
	String discreetDataVariableName = mapper.writeValueAsString(discreetDataSetVariableName);
	String discreetDataSetValue = mapper.writeValueAsString(discreetDataSetVariableValue);
	String materialIdCommaSeparated = mapper.writeValueAsString(discreetDataSetVariablesMaterialId);
	String discreetBioGroupName = mapper.writeValueAsString(bioGroupName);
	String discreetBioMaterialName = mapper.writeValueAsString(bioMaterialName);
	String s = "{\"data\":" + DetailsDiscreet + ",\"discreetDataVariableName\":" + discreetDataVariableName
			+ ",\"discreetDataSetValue\":" + discreetDataSetValue + ",\"materialIdCommaSeparated\":" + materialIdCommaSeparated
			+ ",\"discreetBioGroupName\":" + discreetBioGroupName + ",\"discreetBioMaterialName\":" + discreetBioMaterialName + "}";
	logger.info("end BioDiscreet Data Set "+s);
	return ResponseEntity.ok(s);
	}

	@GetMapping("deleteDiscreetDataSetUsingName")
	public ModelAndView deleteDiscreetDataSetUsingName(@RequestParam("dataSet") String dataSet,
			@RequestParam("discreteDataSetName") String discreteDataSetName,
			@Valid @ModelAttribute ShowBioDiscreetDataForm showBioDiscreetDataForm, BindingResult bindingResult,
			@AuthenticationPrincipal Principal principal) {
		bioDiscreetDataService.removeDataSetBasedOnName(dataSet);
		List<BioDiscreetData> bioDiscreetData = bioDiscreetDataService
				.getBioDiscreetDataByDataSetNameORYVariable(discreteDataSetName);
		showBioDiscreetDataForm.setDataSetName(discreteDataSetName);
		ModelAndView mv = new ModelAndView("contribute/searchBioProperty", "bioDiscreetData", bioDiscreetData);
		mv.addObject("showBioDiscreetDataForm", showBioDiscreetDataForm);
		return mv;
	}

	/*
	 * @GetMapping("editDiscreetDataSet") public ResponseEntity<String>
	 * editDiscreetDataSet(@RequestParam("dataSetId") Long dataSetId,
	 * 
	 * @Valid @ModelAttribute ShowBioDiscreetDataForm showBioDiscreetDataForm,
	 * BindingResult bindingResult,
	 * 
	 * @AuthenticationPrincipal Principal principal) { BioDiscreetDataForm bioForm =
	 * new BioDiscreetDataForm(); List<BioDiscreetData> listDataSet =
	 * bioDiscreetDataService.getAllDetailsOfDiscreeDataSetUsingId(dataSetId); //
	 * common for group and material listDataSet.forEach(data -> {
	 * bioForm.setGroupId(data.getGroupId()); });
	 * 
	 * bioForm.setGroupName(bg.getGroupName()); if (bioForm.getyVariableId() != 0) {
	 * BioVariable bv = bioVariableRepo.getVariableById(bioForm.getyVariableId());
	 * bioForm.setyVariableName(bv.getName()); } if (bioForm.getxVariableId1() != 0)
	 * { BioVariable bv =
	 * bioVariableRepo.getVariableById(bioForm.getxVariableId1());
	 * bioForm.setxVariableName1(bv.getName()); } if (bioForm.getxVariableId2() !=
	 * 0) { BioVariable bv =
	 * bioVariableRepo.getVariableById(bioForm.getxVariableId2());
	 * bioForm.setxVariableName2(bv.getName()); } if (bioForm.getxVariableId3() !=
	 * 0) { BioVariable bv =
	 * bioVariableRepo.getVariableById(bioForm.getxVariableId3());
	 * bioForm.setxVariableName3(bv.getName()); } if (bioForm.getxVariableId4() !=
	 * 0) { BioVariable bv =
	 * bioVariableRepo.getVariableById(bioForm.getxVariableId4());
	 * bioForm.setxVariableName4(bv.getName()); } if (bioForm.getxVariableId5() !=
	 * 0) { BioVariable bv =
	 * bioVariableRepo.getVariableById(bioForm.getxVariableId5());
	 * bioForm.setxVariableName5(bv.getName()); } if (bioForm.getxVariableId6() !=
	 * 0) { BioVariable bv =
	 * bioVariableRepo.getVariableById(bioForm.getxVariableId6());
	 * bioForm.setxVariableName6(bv.getName()); } if (bioForm.getxVariableId7() !=
	 * 0) { BioVariable bv =
	 * bioVariableRepo.getVariableById(bioForm.getxVariableId7());
	 * bioForm.setxVariableName7(bv.getName()); } if (bioForm.getxVariableId8() !=
	 * 0) { BioVariable bv =
	 * bioVariableRepo.getVariableById(bioForm.getxVariableId8());
	 * bioForm.setxVariableName8(bv.getName()); } if (bioForm.getxVariableId9() !=
	 * 0) { BioVariable bv =
	 * bioVariableRepo.getVariableById(bioForm.getxVariableId9());
	 * bioForm.setxVariableName9(bv.getName()); } if (bioForm.getxVariableId10() !=
	 * 0) { BioVariable bv =
	 * bioVariableRepo.getVariableById(bioForm.getxVariableId10());
	 * bioForm.setxVariableName10(bv.getName()); } ObjectMapper mapper = new
	 * ObjectMapper(); String DetailsDiscreet = mapper.writeValueAsString(bddList);
	 * ModelAndView mv = new ModelAndView("contribute/addDiscreetData",
	 * "bioDiscreetDataForm", bioForm); mv.addObject("excelFileData", listDataSet);
	 * String s = null; return ResponseEntity.ok(s); }
	 */
}
