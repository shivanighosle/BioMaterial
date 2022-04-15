package edu.cornell.cals.biomat.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.cornell.cals.biomat.dao.BioDiscreetData;

@Repository
public interface BioDiscreetDataRepository extends JpaRepository<BioDiscreetData, Integer> {
	@Query(value = "SELECT bm FROM BioDiscreetData bm")
	List<BioDiscreetData> getAllBioDiscreetData();
	
	@Query(value = "SELECT bd FROM BioDiscreetData bd where bd.id=:id")
	BioDiscreetData getBioDiscreetDataById(Long id);
	
	@Query(value = "SELECT * FROM bio_discreet_data bdd JOIN bio_variable bv on bdd.y_variable_id = bv.id WHERE bv.name LIKE :yVariableOrDatasetName or bdd.dataset_name LIKE :yVariableOrDatasetName", nativeQuery = true)
	List<BioDiscreetData> getBioDiscreetDataByDataSetNameORYVariable(@Param("yVariableOrDatasetName") String yVariableOrDatasetName);
	
	@Query(value = "SELECT bd FROM BioDiscreetData bd where bd.datasetName=:dataSet")
	List<BioDiscreetData> getBioDiscreetDataSetByName(String dataSet);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM BioDiscreetData bdd WHERE bdd.datasetName = :dataSetName")
	public void deleteDataSetName(@Param("dataSetName") String dataSetName);
	
	@Query(value = "SELECT * FROM bio_discreet_data bdd WHERE material_id =:matId AND dataset_name = :dataSetName", nativeQuery = true)
	public List<BioDiscreetData>getDetailsUsingMatIdAndDataSetName(@Param("matId") Long matId, @Param("dataSetName") String dataSetName);

}
