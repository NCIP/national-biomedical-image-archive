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
