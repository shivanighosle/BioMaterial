package edu.cornell.cals.biomat.service;

import java.util.List;

import edu.cornell.cals.biomat.dao.BioComposition;

public interface BioCompositionService {
	List<String> getNonExistingTagNames(List<String> tagNameList);
	List<String> getExistingTagNames(List<String> tagNameList);
	BioComposition getBioCompositionByTagName(String tagName);
	BioComposition getBioCompositionByNutrientDesc(String componentsName);
	List<BioComposition> getCompositions();
	BioComposition getBioCompositionByNutrientDescAdditional(Integer additionalCompositionId);
}
