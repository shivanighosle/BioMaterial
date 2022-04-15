package edu.cornell.cals.biomat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.cornell.cals.biomat.dao.BioDiscreetData;
import edu.cornell.cals.biomat.repository.BioDiscreetDataRepository;
import edu.cornell.cals.biomat.repository.BioVariableRepository;
import edu.cornell.cals.biomat.service.BioDiscreetDataService;

@Service
public class BioDiscreetDataServiceImpl implements BioDiscreetDataService{
	@Autowired
	private BioDiscreetDataRepository bioDiscreetDataRepository;
	
	@Override
	public List<BioDiscreetData> getAllBioDiscreetData() {
		List<BioDiscreetData> bdd = bioDiscreetDataRepository.getAllBioDiscreetData();
		return bdd;
	}

	@Override
	public BioDiscreetData getBioDiscreetData(Long id) {
		BioDiscreetData bdd = bioDiscreetDataRepository.getBioDiscreetDataById(id);
		return bdd;
	}
	
	@Override
	public List<BioDiscreetData> getBioDiscreetDataByDataSetNameORYVariable(String yVariableOrDatasetName) {
		List<BioDiscreetData> dataSet = new ArrayList<BioDiscreetData>();
		List<BioDiscreetData> bioDiscreetData = bioDiscreetDataRepository.getBioDiscreetDataByDataSetNameORYVariable("%"+yVariableOrDatasetName+"%");		
		dataSet = bioDiscreetData.stream().filter(distinctByKey(BioDiscreetData::getDatasetName) ).collect(Collectors.toList());
		return dataSet;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}

	@Override
	public List<BioDiscreetData> getAllDetailsOfDiscreeDataSetUsingName(String dataSet) {
		BioDiscreetData bdd = new BioDiscreetData();
		List<BioDiscreetData> listOfDiscreetData = null;
		listOfDiscreetData = bioDiscreetDataRepository.getBioDiscreetDataSetByName(dataSet);
		listOfDiscreetData.forEach(data->{
			bdd.setGroupId(data.getGroupId());
			bdd.setMaterialId(data.getMaterialId());
		});
        if(bdd.getGroupId() != null) {
        	listOfDiscreetData  = bioDiscreetDataRepository.getDetailsUsingMatIdAndDataSetName(bdd.getMaterialId(), dataSet);
        }
		return listOfDiscreetData;
	}

	@Override
	public boolean removeDataSetBasedOnName(String dataSetName) {
		bioDiscreetDataRepository.deleteDataSetName(dataSetName);
		return false;
	}

	@Override
	public List<BioDiscreetData> getDiscreetDataUsingMaterialIdAndDataSetName(Long materialId, String dataSetName) {
		return bioDiscreetDataRepository.getDetailsUsingMatIdAndDataSetName(materialId, dataSetName);
	}
	
	

}
