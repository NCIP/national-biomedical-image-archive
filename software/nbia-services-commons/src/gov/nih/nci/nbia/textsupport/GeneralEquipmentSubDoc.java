/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.textsupport;

import java.util.Collection;
import java.io.Serializable;
	/**
	* The imaging equipment used to obtain an in vivo image	**/
public class GeneralEquipmentSubDoc  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* 	**/
	private String deviceSerialNumber;
	/**
	* Retreives the value of deviceSerialNumber attribute
	* @return deviceSerialNumber
	**/

	public String getDeviceSerialNumber(){
		return deviceSerialNumber;
	}

	/**
	* Sets the value of deviceSerialNumber attribue
	**/

	public void setDeviceSerialNumber(String deviceSerialNumber){
		this.deviceSerialNumber = deviceSerialNumber;
	}
	
		/**
	* One or more characters used to identify, name, or characterize the nature, properties, or contents of a thing.	**/
	private Integer id;
	/**
	* Retreives the value of id attribute
	* @return id
	**/

	public Integer getId(){
		return id;
	}

	/**
	* Sets the value of id attribue
	**/

	public void setId(Integer id){
		this.id = id;
	}
	
		/**
	* 	**/
	private String institutionAddress;
	/**
	* Retreives the value of institutionAddress attribute
	* @return institutionAddress
	**/

	public String getInstitutionAddress(){
		return institutionAddress;
	}

	/**
	* Sets the value of institutionAddress attribue
	**/

	public void setInstitutionAddress(String institutionAddress){
		this.institutionAddress = institutionAddress;
	}
	
		/**
	* Text to represent the name of the institution where the equipment that produced the composite instances is located.	**/
	private String institutionName;
	/**
	* Retreives the value of institutionName attribute
	* @return institutionName
	**/

	public String getInstitutionName(){
		return institutionName;
	}

	/**
	* Sets the value of institutionName attribue
	**/

	public void setInstitutionName(String institutionName){
		this.institutionName = institutionName;
	}
	
		/**
	* Text to capture the name of the manufacturer of medical imaging equipment.	**/
	private String manufacturer;
	/**
	* Retreives the value of manufacturer attribute
	* @return manufacturer
	**/

	public String getManufacturer(){
		return manufacturer;
	}

	/**
	* Sets the value of manufacturer attribue
	**/

	public void setManufacturer(String manufacturer){
		this.manufacturer = manufacturer;
	}
	
		/**
	* Text to capture the manufacturer's model names of the (imaging) equipment that produced the composite instances.	**/
	private String manufacturerModelName;
	/**
	* Retreives the value of manufacturerModelName attribute
	* @return manufacturerModelName
	**/

	public String getManufacturerModelName(){
		return manufacturerModelName;
	}

	/**
	* Sets the value of manufacturerModelName attribue
	**/

	public void setManufacturerModelName(String manufacturerModelName){
		this.manufacturerModelName = manufacturerModelName;
	}
	
		/**
	* Numeric value to designate manufacturer's software version of the equipment that produced the (imaging) composite instances.	**/
	private String softwareVersions;
	/**
	* Retreives the value of softwareVersions attribute
	* @return softwareVersions
	**/

	public String getSoftwareVersions(){
		return softwareVersions;
	}

	/**
	* Sets the value of softwareVersions attribue
	**/

	public void setSoftwareVersions(String softwareVersions){
		this.softwareVersions = softwareVersions;
	}
	
		/**
	* 	**/
	private String stationName;
	/**
	* Retreives the value of stationName attribute
	* @return stationName
	**/

	public String getStationName(){
		return stationName;
	}

	/**
	* Sets the value of stationName attribue
	**/

	public void setStationName(String stationName){
		this.stationName = stationName;
	}
	
	/**
	* An associated gov.nih.nci.ncia.domain.GeneralSeries object's collection 
	**/
			
	private Collection<GeneralSeriesSubDoc> generalSeriesCollection;
	/**
	* Retreives the value of generalSeriesCollection attribue
	* @return generalSeriesCollection
	**/

	public Collection<GeneralSeriesSubDoc> getGeneralSeriesCollection(){
		return generalSeriesCollection;
	}

	/**
	* Sets the value of generalSeriesCollection attribue
	**/

	public void setGeneralSeriesCollection(Collection<GeneralSeriesSubDoc> generalSeriesCollection){
		this.generalSeriesCollection = generalSeriesCollection;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof GeneralEquipmentSubDoc) 
		{
			GeneralEquipmentSubDoc c =(GeneralEquipmentSubDoc)obj; 			 
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}
		
	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}
	
}