package edu.cornell.cals.biomat.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.cornell.cals.biomat.dao.BioMaterial;
@Repository
public interface BioMaterialRepository extends JpaRepository<BioMaterial, Long> {


		@Query(value="SELECT bm FROM BioMaterial bm where bm.shortDesc like :shortDesc")
	    List<BioMaterial> getBioMaterial(@Param("shortDesc") String shortDesc);

		@Query(value="SELECT bm FROM BioMaterial bm where bm.shortDesc like :shortDesc")
		List<BioMaterial> getBioMaterialWithPagination(@Param("shortDesc") String shortDesc);
		 
		@Query(value="SELECT count(bm) FROM BioMaterial bm where bm.shortDesc like :shortDesc")
	    Integer getBioMaterialCount(@Param("shortDesc") String shortDesc);
		
		@Query(value="SELECT DISTINCT bm FROM BioMaterial bm INNER join BioFormulaMaterial bfm on bfm.materialId=bm.id where bm.shortDesc like :shortDesc ")
	    List<BioMaterial> getBioMaterialWithFormula(@Param("shortDesc") String shortDesc);
		
		@Query(value="SELECT bm FROM BioMaterial bm")
	    List<BioMaterial> getAllBioMaterial();
		
		@Query(value = "SELECT bm FROM BioMaterial bm WHERE bm.longDesc LIKE :bioMaterialName AND (bm.longDesc LIKE :process OR bm.longDesc LIKE :form)")
		List<BioMaterial> getBioMaterialNameByNameAndProcessAndForm(@Param("bioMaterialName") String bioMaterialName, @Param("process") String process, @Param("form") String form);
		
		@Query(value="SELECT bm FROM BioMaterial bm WHERE bm.shortDesc like :bioMaterialName")
		List<BioMaterial> getBioMaterialNameByName(@Param("bioMaterialName") String bioMaterialName);
		
		@Query(value="SELECT bm FROM BioMaterial bm WHERE bm.id IN(:commaSeperatedId)")
		List<BioMaterial> passCommaSeperatedId(@Param("commaSeperatedId") List<Long> commaSeperatedId);
		
		//For Process and Form
		@Query(value = "SELECT bm FROM BioMaterial bm WHERE bm.longDesc LIKE :bioMaterialName AND (bm.longDesc LIKE :process AND bm.longDesc LIKE :form)")
		List<BioMaterial> getBioMaterialNameByNameAndProcessAndFormNotNull(@Param("bioMaterialName") String bioMaterialName, @Param("process") String process, @Param("form") String form);
		
		//For Process and Form null
		@Query(value = "SELECT bm FROM BioMaterial bm WHERE bm.longDesc LIKE :bioMaterialName OR (bm.longDesc LIKE :process AND bm.longDesc LIKE :form)")
		List<BioMaterial> getBioMaterialNameByNameAndProcessAndFormNull(@Param("bioMaterialName") String bioMaterialName, @Param("process") String process, @Param("form") String form);
		
		@Query(value = "SELECT max(bm.id) FROM BioMaterial bm")
		Integer getLastIdOfBioMaterial();
		
		@Transactional
		@Modifying
		@Query(value = "DELETE bio_material_composition FROM bio_material_composition WHERE bio_material_composition.material_id =:materialId", nativeQuery = true)
		void deleteBioMaterialUsingId(@Param("materialId") Long materialId);
		
		@Transactional
		@Modifying
		@Query(value = "UPDATE bio_material SET name =:name, short_desc =:shortDescName, long_desc=:longDescName, updated_by=:userName, updated_at=:updatedDate WHERE id=:materialId", nativeQuery = true)
		void updateCustomBioMaterialUsingId(@Param("materialId") Long materialId, @Param("name") String name, @Param("shortDescName") String shortDescName, @Param("longDescName") String longDescName, @Param("updatedDate") String updatedDate, @Param("userName") String userName);
		
		@Query(value="SELECT bm FROM BioMaterial bm where bm.name like :name")
	    List<BioMaterial> getBioMaterialForCustom(@Param("name") String name);
}
