/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

public class EquipmentDTO {

	public EquipmentDTO(String manufacturer, 
			            String model,
			            String version) {
		this.manufacturer = manufacturer;
		this.model = model;
		this.version = version;
	}
	

	public String getManufacturer() {
		return manufacturer;
	}
	
	
	public String getModel() {
		return model;
	}
	
	
	public String getVersion() {
		return version;
	}
	
	private String manufacturer; 
	private String model;
	private String version;	
}
