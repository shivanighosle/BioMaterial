package edu.cornell.cals.biomat.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.cornell.cals.biomat.dao.BioComposition;
import edu.cornell.cals.biomat.repository.BioCompositionRepository;
import edu.cornell.cals.biomat.service.BioCompositionService;

@Service
public class BioCompositionServiceImpl implements BioCompositionService {

	@Autowired
	BioCompositionRepository bioCompositionRepository; 
	@Override
	public BioComposition getBioCompositionByTagName(String tagName) {
		return bioCompositionRepository.getBioCompositionByTagName(tagName);
	}
	@Override
	public List<String> getNonExistingTagNames(List<String> tagNameList) {
		List<String> nonExistingTagNames = new ArrayList();
		
		tagNameList.forEach(tagName -> {
			BioComposition bc  =bioCompositionRepository.getBioCompositionByTagName(tagName);
			if(bc==null) nonExistingTagNames.add(tagName);
		});

		
		return nonExistingTagNames;
	}

	@Override
	public List<String> getExistingTagNames(List<String> tagNameList) {
		List<String> existingTagNames = new ArrayList();
		
		tagNameList.forEach(tagName -> {
			BioComposition bc  =bioCompositionRepository.getBioCompositionByTagName(tagName);
			if(bc!=null) existingTagNames.add(tagName);
		});

		
		return existingTagNames;
	}

	public BioComposition getBioCompositionByNutrientDesc(String componentsName) {
		return bioCompositionRepository.getBioCompositionByNutrientDesc(componentsName + "%");
	}
	
	@Override
	public List<BioComposition> getCompositions() {
		List<BioComposition>  getCompositions = bioCompositionRepository.getCompositions();
		getCompositions = getCompositions.stream().distinct().collect(Collectors.toList());
		getCompositions.removeIf(composition -> {
			if(composition.getNutrientDesc().contains("Protein")){
				return true;
			}
			if(composition.getNutrientDesc().contains("Total lipid (fat)")){
				return true;
			}
			if(composition.getNutrientDesc().contains("Water")){
				return true;
			}
			if(composition.getNutrientDesc().contains("Carbohydrate, by difference")){
				return true;
			}
			if(composition.getNutrientDesc().contains("Ash")){
				return true;
			}
			if(composition.getNutrientDesc().contains("Fiber, total dietary")){
				return true;
			}
			return false;			
		});
		return  getCompositions;
	}
	
	public BioComposition getBioCompositionByNutrientDescAdditional(Integer additionalCompositionId) {
		return bioCompositionRepository.getBioCompositionByNutrientDescAdditional(additionalCompositionId);
	}
}
