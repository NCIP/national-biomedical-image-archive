/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import java.io.Serializable;

public class ManufacturerModelSoftware implements Serializable {

	public ManufacturerModelSoftware() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof ManufacturerModelSoftware) {
			ManufacturerModelSoftware c = (ManufacturerModelSoftware) obj;
			String thisId = getId();
			if (thisId != null && thisId.equals(c.getId())) {
				eq = true;
			}
		}
		return eq;
	}

	public int hashCode() {
		int h = 0;
		if (getId() != null) {
			h += getId().hashCode();
		}
		return h;
	}

	private static final long serialVersionUID = 0x499602d2L;
	private String id;
	private String manufacturer;
	private String model;
	private String softwareVersion;
}
