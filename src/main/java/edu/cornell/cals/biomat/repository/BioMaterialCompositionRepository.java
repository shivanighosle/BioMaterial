package edu.cornell.cals.biomat.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.cornell.cals.biomat.dao.BioMaterialComposition;
import edu.cornell.cals.biomat.dao.BioMaterialCompositionId;

@Repository
public interface BioMaterialCompositionRepository extends JpaRepository<BioMaterialComposition, BioMaterialCompositionId> {
	
	
	List<BioMaterialComposition> findByIdMaterialId(Long materialId);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT bio_material_composition (material_id,composition_id,nutrient_value) values (:materialId, :compositionId, :nutrientValue)", nativeQuery = true)
	void saveCompositionByMaterial(@Param("compositionId") Integer compositionId, @Param("materialId") Long materialId, @Param("nutrientValue") double nutrientValue);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE bio_material_composition SET nutrient_value =:nutrientValue WHERE material_id =:materialId AND composition_id =:compositionId", nativeQuery = true)
	void updateCompositionByMaterial(@Param("compositionId") Integer compositionId, @Param("materialId") Long materialId, @Param("nutrientValue") double nutrientValue);
}
