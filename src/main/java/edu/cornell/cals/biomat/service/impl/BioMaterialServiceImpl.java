package edu.cornell.cals.biomat.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.cornell.cals.biomat.dao.BioMaterial;
import edu.cornell.cals.biomat.repository.BioMaterialRepository;
import edu.cornell.cals.biomat.service.BioMaterialService;

@Service
public class BioMaterialServiceImpl implements BioMaterialService{

	@Autowired
	BioMaterialRepository bioMaterialRepository;
		
	@Override
	public BioMaterial getBioMaterial(Long id) {
		BioMaterial bm = bioMaterialRepository.getOne(id);
		return bm;
	}
	
	@Override
	public List<BioMaterial> getBioMaterial(String shortDesc) {
		return bioMaterialRepository.getBioMaterial("%"+shortDesc+"%");
	}

	@Override
	public BioMaterial getBioMaterialByUsdaId(Long usdaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String,Object> getBioMaterial(Pageable pageable, String shortDesc, int bioMaterialCount) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<BioMaterial> bmList = new ArrayList<BioMaterial>();
		List<BioMaterial> bmList1 = new ArrayList<BioMaterial>();
		List<BioMaterial> bmList2 = new ArrayList<BioMaterial>();
		List<BioMaterial> bmList3 = new ArrayList<BioMaterial>();
		String longDesc = shortDesc;
		int count = bioMaterialRepository.getBioMaterialCount("%"+shortDesc+"%" );
		List<BioMaterial>  bioMaterialPage  = bioMaterialRepository.getBioMaterialWithPagination("%"+shortDesc+"%");
		bioMaterialPage.forEach(biomaterial -> {
            if(biomaterial.getShortDesc().startsWith(shortDesc.toUpperCase())){
            	bmList.add(biomaterial);
            }
            else if(biomaterial.getShortDesc().contains(shortDesc.toUpperCase())){
            	bmList1.add(biomaterial);
            }
        });
		bmList1.removeAll(bmList);
		bmList.sort((BioMaterial b1, BioMaterial b2) -> b1.getShortDesc().compareTo(b2.getShortDesc()));
		bmList1.sort((BioMaterial b1, BioMaterial b2) -> b1.getShortDesc().compareTo(b2.getShortDesc()));
		bmList2 = Stream.concat(bmList.stream(), bmList1.stream())
                .collect(Collectors.toList());
		for (int i = bioMaterialCount; i < bmList2.size(); i += 10) {
			   bmList3 = bmList2.subList(i, Math.min(bmList2.size(),i+10));
			   break;
		}
		Page<BioMaterial> page = new PageImpl(bmList3, pageable, count);
		map.put("count",count);
		map.put("bioMaterialPage",page);
		map.put("bioMaterialCount",bioMaterialCount);
		return map;
	}

	@Override
	public BioMaterial updateBioMaterial(BioMaterial bioMaterial, String userId) {
		bioMaterial.setUpdatedBy(userId);
		BioMaterial bm =bioMaterialRepository.save(bioMaterial);
		return bm;
	}

	
	@Override
    public List<BioMaterial> getBioMaterialWithFormula(String shortDesc){
		//List<BioMaterial> bioMaterialsWithFormula = bioMaterialRepository.getBioMaterialWithFormula("%"+shortDesc+"%");
		//return bioMaterialsWithFormula ;
		List<BioMaterial> bioMaterialsWithFormula = bioMaterialRepository.getBioMaterial(shortDesc+"%");
		// Filter All Material with Id 0 
		return bioMaterialsWithFormula.stream().filter(bm->bm.getId()!=0).collect(Collectors.toList());
	}

	@Override
	public List<BioMaterial> getSelectedUniqueMaterial(String shortDesc) {
		List<BioMaterial> bioMat = new ArrayList<BioMaterial>();
		List<BioMaterial> bioMaterialList = new ArrayList<BioMaterial>();
		List<BioMaterial>  bioMaterials = bioMaterialRepository.getBioMaterial(shortDesc+"%");
		List<String> items = null;
		for(BioMaterial bioMaterial:bioMaterials) {
			items = Arrays.asList(bioMaterial.getShortDesc().split(","));
			String name = items.get(0);
			bioMaterial.setShortDesc(name);
			bioMat.add(bioMaterial);
		}
		Set<BioMaterial> set = bioMat.stream()
	            .collect(Collectors.toCollection(() ->
	                 new TreeSet<>(Comparator.comparing(BioMaterial::getShortDesc))));
		for(BioMaterial bi:set) {
			bioMaterialList.add(bi);
		}
		return  bioMaterialList;
	}
	
	@Override
	public Integer getLastIdOfBioMaterial() {
		Integer lastId = bioMaterialRepository.getLastIdOfBioMaterial();
		return lastId;
	}
	
	@Override
	public BioMaterial getCustomBioMaterialUsingId(Long biomaterialId) {
		BioMaterial bm = bioMaterialRepository.getOne(biomaterialId);
		return bm;
	}
	
	@Override
	public boolean removeMaterialUsingId(Long materialId) {
		bioMaterialRepository.deleteBioMaterialUsingId(materialId);
		if(bioMaterialRepository.existsById(materialId)) {
			bioMaterialRepository.deleteById(materialId);
			return true;
		}
		return true;
	}
	
	@Override
	public boolean updateCustomBioMaterialUsingId(Long materialId, String name, String shortDescName, String longDescName, String updatedDate, String userName) {
		bioMaterialRepository.updateCustomBioMaterialUsingId(materialId, name, shortDescName, longDescName, updatedDate, userName);
		return true;
	}
	
	@Override
	public List<BioMaterial> getBioMaterialForCustom(String name) {
		return bioMaterialRepository.getBioMaterialForCustom("%"+name);
	}
}
