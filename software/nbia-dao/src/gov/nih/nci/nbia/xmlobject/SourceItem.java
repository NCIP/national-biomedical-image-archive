/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.xmlobject;

public class SourceItem {
	
	private String itemLabel;
	private String itemName;
	private String itemType;
	private String itemPrimaryValue;
	private String actualSource;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName.trim();
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType.trim();
	}
	public String getItemPrimaryValue() {
		return itemPrimaryValue;
	}
	public void setItemPrimaryValue(String itemPrimaryValue) {
		this.itemPrimaryValue = itemPrimaryValue.trim();
	}
	public String getItemLabel() {
		return itemLabel;
	}
	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel.trim();
	}
	public String getActualSource() {
		return actualSource;
	}
	public void setActualSource(String actualSource) {
		this.actualSource = actualSource;
	}
	

}
