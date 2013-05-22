/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import java.io.Serializable;

public class AimQuantification implements Serializable {

	private static final long serialVersionUID = 1234567890L;
   
    private String name;
    
    private String value;
       
    private String type;

	private Integer id;

	private AimImagingObservationCharacteristic aimImagingObservationCharacteristic;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public AimImagingObservationCharacteristic getAimImagingObservationCharacteristic(){
		return aimImagingObservationCharacteristic;
	}


	public void setAimImagingObservationCharacteristic(AimImagingObservationCharacteristic aimImagingObservationCharacteristic){
		this.aimImagingObservationCharacteristic = aimImagingObservationCharacteristic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof AimQuantification)
		{
			AimQuantification c =(AimQuantification)obj;
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