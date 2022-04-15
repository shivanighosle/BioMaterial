package edu.cornell.cals.biomat.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.cornell.cals.biomat.dao.BioMaterial;

public interface BioMaterialService {
	BioMaterial getBioMaterial(Long id);
	List<BioMaterial> getBioMaterial(String desc);
	List<BioMaterial> getBioMaterialForCustom(String desc);
	Map<String,Object> getBioMaterial(Pageable pageable,String desc, int bioMaterialCount);
	BioMaterial getBioMaterialByUsdaId(Long usdaId);
	
	BioMaterial updateBioMaterial(BioMaterial bioMaterial,String userId);
	Integer getLastIdOfBioMaterial();
	

    List<BioMaterial> getBioMaterialWithFormula( String shortDesc);
    List<BioMaterial> getSelectedUniqueMaterial(String desc);
    BioMaterial getCustomBioMaterialUsingId(Long biomaterialId);
    public boolean removeMaterialUsingId(Long materialId);
    boolean updateCustomBioMaterialUsingId(Long materialId, String name, String shortDescName, String longDescName, String updatedDate, String userName);
}
