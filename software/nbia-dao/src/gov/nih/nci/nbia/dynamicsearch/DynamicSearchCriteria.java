/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

/**
 * This object encapsulates a criteria for what kind of results
 * to return from a query.
 * 
 * <p>This object matches up a DataField with an actual choice
 * for a field value made by a client/user.  
 * 
 * <p>Should this object work with DataField instead of breaking
 * things down into String objects?
 * 
 * <p>this object should instead use a constructor to pass in all values
 * and then only have accessors and the object can be made immutable
 */
public class DynamicSearchCriteria {
	private String field = "";
	private Operator operator;
	private String value = "";
	private String dataGroup="";
	private String label = "";
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operand) {
		this.operator = operand;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * This would be a target object like Patient, Study, etc.
	 */
	public String getDataGroup() {
		return dataGroup;
	}
	public void setDataGroup(String dataGroup) {
		this.dataGroup = dataGroup;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
