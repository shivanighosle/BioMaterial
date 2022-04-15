package edu.cornell.cals.biomat.model.discreet;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import edu.cornell.cals.biomat.dao.BioDiscreetData;


public class ShowBioDiscreetDataForm implements  Serializable{
	private static final long serialVersionUID = -4L;
	

	protected List<BioDiscreetData> bioDiscreetDatas;
	
	private String dataSetName;
	
	public List<BioDiscreetData> getBioDiscreetDatas() {
		return bioDiscreetDatas;
	}
	public void setBioDiscreetDatas(List<BioDiscreetData> bioDiscreetDatas) {
		this.bioDiscreetDatas = bioDiscreetDatas;
	}
	
	public String getDataSetName() {
		return dataSetName;
	}
	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}
	@Override
	public String toString() {
		return "ShowBioDiscreetDataForm [bioDiscreetDatas=" + bioDiscreetDatas + ", dataSetName=" + dataSetName + "]";
	}

}
