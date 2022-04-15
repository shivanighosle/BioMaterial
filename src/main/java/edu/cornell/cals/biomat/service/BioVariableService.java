package edu.cornell.cals.biomat.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import edu.cornell.cals.biomat.dao.BioVariable;
import edu.cornell.cals.biomat.model.discreet.BioDiscreetDataForm;

public interface BioVariableService {
	BioVariable getBioVariable(Integer id);
	List<BioVariable> getBioVariable(String name);
	BioVariable updateBioVariable(BioVariable bioVariable,String userId);
	Map<String,Object> getBioVariable(Pageable pageable,String name);	
	List<String> getNonExistingVariables(List<String> variableList);
	BioVariable getBioVariableBySymbol(String symbol);
    List<BioVariable> getAllBioVariable();
    List<String> getAllVariableNameUsingId(BioDiscreetDataForm bioDiscreetDataForm);
    Map<Integer, String> getOnlyVariableNameAndUnit(List<String> variableList);
}
