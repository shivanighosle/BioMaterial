package edu.cornell.cals.biomat.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.cornell.cals.biomat.dao.BioComposition;
@Repository
public interface BioCompositionRepository extends JpaRepository<BioComposition, Integer> {


		@Query(value="SELECT bc FROM BioComposition bc where bc.tagName in (:tagNameList)")
	    List<BioComposition> getBioCompositionByTagNameList(@Param("tagNameList") List tagNameList);

		@Query(value="SELECT bc FROM BioComposition bc where bc.tagName = :tagName")
		BioComposition getBioCompositionByTagName(@Param("tagName") String tagName);
		
		@Query(value="SELECT bc FROM BioComposition bc where bc.nutrientDesc like :componentsName")
		BioComposition getBioCompositionByNutrientDesc(@Param("componentsName") String componentsName);
		
		@Query(value="SELECT * FROM bio_composition", nativeQuery = true)
		List<BioComposition> getCompositions();
		
		@Query(value="SELECT bc FROM BioComposition bc where bc.id = :additionalCompositionId")
		BioComposition getBioCompositionByNutrientDescAdditional(@Param("additionalCompositionId") Integer additionalCompositionId);

}
