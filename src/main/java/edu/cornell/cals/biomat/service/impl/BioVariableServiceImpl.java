package edu.cornell.cals.biomat.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.cornell.cals.biomat.dao.BioVariable;
import edu.cornell.cals.biomat.model.discreet.BioDiscreetDataForm;
import edu.cornell.cals.biomat.repository.BioVariableRepository;
import edu.cornell.cals.biomat.service.BioVariableService;

@Service
public class BioVariableServiceImpl implements BioVariableService{
	@Autowired
	BioVariableRepository bioVariableRepository;

	@Override
	public BioVariable getBioVariableBySymbol(String symbol) {
		BioVariable bv =bioVariableRepository.getVariableBySymbol(symbol);
		return bv;
	}

	@Override
	public BioVariable getBioVariable(Integer id) {
		BioVariable bv = bioVariableRepository.getVariableById(id);
		return bv;
	}

	@Override
	public List<BioVariable> getBioVariable(String name) {
		List<BioVariable> bvList = bioVariableRepository.getVariable("%"+name+"%");
		return bvList;
	}

	@Override
	public BioVariable updateBioVariable(BioVariable bioVariable, String userId) {
		bioVariable.setAddedBy(userId);
		bioVariable.setUpdatedBy(userId);
		BioVariable bv =bioVariableRepository.save(bioVariable);
		return bv;
	}


	@Override
	public Map<String,Object> getBioVariable(Pageable pageable, String name) {
		Map<String,Object> map = new HashMap<String,Object>();
		int count = bioVariableRepository.getBioVariableCount("%"+name+"%");
		Page<BioVariable>  bioVariablePage  = bioVariableRepository.getBioVariableWithPagination(pageable,"%"+name+"%");	
		map.put("count",count);
		map.put("bioVariablePage",bioVariablePage);
		return map;
	}

	public List<String> getNonExistingVariables(List<String> variableList){
		List<String> nonExistingVariables = new ArrayList();
		variableList.forEach(variable -> {
			BioVariable bv  =bioVariableRepository.getVariableBySymbol(variable);
			if(bv==null) nonExistingVariables.add(variable);
		});

		return nonExistingVariables;
	}

	@Override
	public List<BioVariable> getAllBioVariable() {
		return bioVariableRepository.getVariables();
	}

	@Override
	public List<String> getAllVariableNameUsingId(BioDiscreetDataForm bioDiscreetDataForm) {
		List <Integer> idList1 = new ArrayList <Integer> ();
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(bioDiscreetDataForm.getxVariableId1());
		idList.add(bioDiscreetDataForm.getxVariableId2());
		idList.add(bioDiscreetDataForm.getxVariableId3());
		idList.add(bioDiscreetDataForm.getxVariableId4());
		idList.add(bioDiscreetDataForm.getxVariableId5());
		idList.add(bioDiscreetDataForm.getxVariableId6());
		idList.add(bioDiscreetDataForm.getxVariableId7());
		idList.add(bioDiscreetDataForm.getxVariableId8());
		idList.add(bioDiscreetDataForm.getxVariableId9());
		idList.add(bioDiscreetDataForm.getxVariableId10());
		idList.add(bioDiscreetDataForm.getyVariableId());
		for(Integer id : idList){
			if(id !=0){
				idList1.add(id);
			}
		}
		return bioVariableRepository.getAllVariableName(idList1);
	}

	@Override
	public Map<Integer, String> getOnlyVariableNameAndUnit(List<String> variableList) {
		variableList.removeAll(Arrays.asList("", null));
		Map<Integer, String> mapOfVariable = new HashMap<>();
		List<BioVariable> variableNameList = bioVariableRepository.getVariables();
		int i = 0;
		for(String bv : variableList) {
			i++;
				BioVariable varName = variableNameList.stream().filter((variable) -> variable.getName().equals(bv)).findAny().get();
				mapOfVariable.put(i, varName.getName()+"["+varName.getUom()+"]");
		}
		return mapOfVariable;
	}
}
