/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public class AimImagingObservationCharacteristic implements Serializable {

	private static final long serialVersionUID = 1234567890L;
   
    private String codeMeaningName;
    
    private String codeValue;
       
    private String codeSchemaDesignator;

	private Integer id;

	private GeneralSeries generalSeries;

	/**
	* identifier	**/
	private Integer seriesPKId;
	/**
	* Retreives the value of seriesPKId attribute
	* @return seriesPKId
	**/

	public Integer getSeriesPKId(){
		return seriesPKId;
	}

	/**
	* Sets the value of seriesPKId attribue
	**/

	public void setSeriesPKId(Integer seriesPKId){
		this.seriesPKId = seriesPKId;
	}
	
	private Set<AimQuantification> aimQuantificationCollection;

	public Set<AimQuantification> getAimQuantificationCollection(){
		return aimQuantificationCollection;
	}

	public void setAimQuantificationCollection(Set<AimQuantification> aimQuantificationCollection){
		this.aimQuantificationCollection = aimQuantificationCollection;
	}
	
	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	/**
	* Retreives the value of generalSeries attribue
	* @return generalSeries
	**/

	public GeneralSeries getGeneralSeries(){
		return generalSeries;
	}
	/**
	* Sets the value of generalSeries attribue
	**/

	public void setGeneralSeries(GeneralSeries generalSeries){
		this.generalSeries = generalSeries;
	}

	public String getCodeMeaningName() {
		return codeMeaningName;
	}

	public void setCodeMeaningName(String codeMeaningName) {
		this.codeMeaningName = codeMeaningName;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getCodeSchemaDesignator() {
		return codeSchemaDesignator;
	}

	public void setCodeSchemaDesignator(String codeSchemaDesignator) {
		this.codeSchemaDesignator = codeSchemaDesignator;
	}
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof AimImagingObservationCharacteristic)
		{
			AimImagingObservationCharacteristic c =(AimImagingObservationCharacteristic)obj;
			if(getId() != null && getId().equals(c.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null) {
			return getId().hashCode();
		}
		return 0;
	}



}