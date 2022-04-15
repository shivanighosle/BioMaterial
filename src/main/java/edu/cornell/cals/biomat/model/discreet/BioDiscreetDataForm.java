package edu.cornell.cals.biomat.model.discreet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import edu.cornell.cals.biomat.excel.MyCell;


public class BioDiscreetDataForm implements  Serializable{
	private static final long serialVersionUID = -4L;
	
	
	
	private Long id;
	//@NotNull
	private Long materialId;
	private Long groupId;
	private String materialName;
	private String groupName;
	
	@NotNull
	private String authorName;
	@NotNull
	private String datasetName;
	@NotNull
	private String year;
	
	private int variableId;
	
	@NotNull
	private int xVariableId1;
	private String xVariableName1;
	private int xVariableId2;
	private String xVariableName2;
	private int xVariableId3;
	private String xVariableName3;
	private int xVariableId4;
	private String xVariableName4;
	private int xVariableId5;
	private String xVariableName5;
	private int xVariableId6;
	private String xVariableName6;
	private int xVariableId7;
	private String xVariableName7;
	private int xVariableId8;
	private String xVariableName8;
	private int xVariableId9;
	private String xVariableName9;
	private int xVariableId10;
	private String xVariableName10;
	@NotNull
	private int yVariableId;
	private String yVariableName;
	
	MultipartFile file;
	
	Map<Integer, List<MyCell>> data = new HashMap<>();
	
	private List<String> name;

	public String getxVariableName1() {
		return xVariableName1;
	}

	public void setxVariableName1(String xVariableName1) {
		this.xVariableName1 = xVariableName1;
	}

	public String getxVariableName2() {
		return xVariableName2;
	}

	public void setxVariableName2(String xVariableName2) {
		this.xVariableName2 = xVariableName2;
	}

	public String getxVariableName3() {
		return xVariableName3;
	}

	public void setxVariableName3(String xVariableName3) {
		this.xVariableName3 = xVariableName3;
	}

	public String getxVariableName4() {
		return xVariableName4;
	}

	public void setxVariableName4(String xVariableName4) {
		this.xVariableName4 = xVariableName4;
	}

	public String getxVariableName5() {
		return xVariableName5;
	}

	public void setxVariableName5(String xVariableName5) {
		this.xVariableName5 = xVariableName5;
	}

	public String getxVariableName6() {
		return xVariableName6;
	}

	public void setxVariableName6(String xVariableName6) {
		this.xVariableName6 = xVariableName6;
	}

	public String getxVariableName7() {
		return xVariableName7;
	}

	public void setxVariableName7(String xVariableName7) {
		this.xVariableName7 = xVariableName7;
	}

	public String getxVariableName8() {
		return xVariableName8;
	}

	public void setxVariableName8(String xVariableName8) {
		this.xVariableName8 = xVariableName8;
	}

	public String getxVariableName9() {
		return xVariableName9;
	}

	public void setxVariableName9(String xVariableName9) {
		this.xVariableName9 = xVariableName9;
	}

	public String getxVariableName10() {
		return xVariableName10;
	}

	public void setxVariableName10(String xVariableName10) {
		this.xVariableName10 = xVariableName10;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getVariableId() {
		return variableId;
	}

	public void setVariableId(int variableId) {
		this.variableId = variableId;
	}

	

	public int getxVariableId1() {
		return xVariableId1;
	}

	public void setxVariableId1(int xVariableId1) {
		this.xVariableId1 = xVariableId1;
	}

	public int getxVariableId2() {
		return xVariableId2;
	}

	public void setxVariableId2(int xVariableId2) {
		this.xVariableId2 = xVariableId2;
	}

	public int getxVariableId3() {
		return xVariableId3;
	}

	public void setxVariableId3(int xVariableId3) {
		this.xVariableId3 = xVariableId3;
	}

	public int getxVariableId4() {
		return xVariableId4;
	}

	public void setxVariableId4(int xVariableId4) {
		this.xVariableId4 = xVariableId4;
	}

	public int getxVariableId5() {
		return xVariableId5;
	}

	public void setxVariableId5(int xVariableId5) {
		this.xVariableId5 = xVariableId5;
	}

	public int getxVariableId6() {
		return xVariableId6;
	}

	public void setxVariableId6(int xVariableId6) {
		this.xVariableId6 = xVariableId6;
	}

	public int getxVariableId7() {
		return xVariableId7;
	}

	public void setxVariableId7(int xVariableId7) {
		this.xVariableId7 = xVariableId7;
	}

	public int getxVariableId8() {
		return xVariableId8;
	}

	public void setxVariableId8(int xVariableId8) {
		this.xVariableId8 = xVariableId8;
	}

	public int getxVariableId9() {
		return xVariableId9;
	}

	public void setxVariableId9(int xVariableId9) {
		this.xVariableId9 = xVariableId9;
	}

	public int getxVariableId10() {
		return xVariableId10;
	}

	public void setxVariableId10(int xVariableId10) {
		this.xVariableId10 = xVariableId10;
	}

	public int getyVariableId() {
		return yVariableId;
	}

	public void setyVariableId(int yVariableId) {
		this.yVariableId = yVariableId;
	}
	public Map<Integer, List<MyCell>> getData() {
		return data;
	}

	public void setData(Map<Integer, List<MyCell>> data) {
		this.data = data;
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getyVariableName() {
		return yVariableName;
	}

	public void setyVariableName(String yVariableName) {
		this.yVariableName = yVariableName;
	}

	@Override
	public String toString() {
		return "BioDiscreetDataForm [id=" + id + ", materialId=" + materialId + ", groupId=" + groupId
				+ ", materialName=" + materialName + ", groupName=" + groupName + ", authorName=" + authorName
				+ ", datasetName=" + datasetName + ", year=" + year + ", variableId=" + variableId + ", xVariableId1="
				+ xVariableId1 + ", xVariableName1=" + xVariableName1 + ", xVariableId2=" + xVariableId2
				+ ", xVariableName2=" + xVariableName2 + ", xVariableId3=" + xVariableId3 + ", xVariableName3="
				+ xVariableName3 + ", xVariableId4=" + xVariableId4 + ", xVariableName4=" + xVariableName4
				+ ", xVariableId5=" + xVariableId5 + ", xVariableName5=" + xVariableName5 + ", xVariableId6="
				+ xVariableId6 + ", xVariableName6=" + xVariableName6 + ", xVariableId7=" + xVariableId7
				+ ", xVariableName7=" + xVariableName7 + ", xVariableId8=" + xVariableId8 + ", xVariableName8="
				+ xVariableName8 + ", xVariableId9=" + xVariableId9 + ", xVariableName9=" + xVariableName9
				+ ", xVariableId10=" + xVariableId10 + ", xVariableName10=" + xVariableName10 + ", yVariableId="
				+ yVariableId + ", yVariableName=" + yVariableName + ", file=" + file + ", data=" + data + ", name="
				+ name + "]";
	}

}
