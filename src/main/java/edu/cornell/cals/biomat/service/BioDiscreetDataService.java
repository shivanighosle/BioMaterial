package edu.cornell.cals.biomat.service;

import java.util.List;
import edu.cornell.cals.biomat.dao.BioDiscreetData;

public interface BioDiscreetDataService {
	
	List<BioDiscreetData> getAllBioDiscreetData();

	BioDiscreetData getBioDiscreetData(Long id);
	
	List<BioDiscreetData> getBioDiscreetDataByDataSetNameORYVariable(String yVariableOrDatasetName);
	
	public List<BioDiscreetData> getAllDetailsOfDiscreeDataSetUsingName(String dataSet);
	
	public boolean removeDataSetBasedOnName(String dataSetName);
	
	public List<BioDiscreetData> getDiscreetDataUsingMaterialIdAndDataSetName(Long materialId, String dataSetName);
}
