/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.xmlobject;

public class Element {
	private String source;
	private String alias;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source.trim();
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String value) {
		this.alias = value.trim();
	}
}
