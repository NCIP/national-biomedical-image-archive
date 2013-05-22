/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.xmlobject;

import java.util.List;

public class DataSource {
	private String sourcePackageName;
	private String sourceName;
	private String sourceLabel;
	private List<SourceItem> sourceItem;
	
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName.trim();
	}
	public List<SourceItem> getSourceItem() {
		return sourceItem;
	}
	public void setSourceItem(List<SourceItem> sourceItem) {
		this.sourceItem = sourceItem;
	}
	public String getSourceLabel() {
		return sourceLabel;
	}
	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel.trim();
	}
	public String getSourcePackageName() {
		return sourcePackageName;
	}
	public void setSourcePackageName(String sourcePackageName) {
		this.sourcePackageName = sourcePackageName.trim();
	}
	

}
