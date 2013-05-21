package gov.nih.nci.nbia.dynamicsearch;

/**
 * <p>this object should instead use a constructor to pass in all values
 * and then only have accessors and the object can be made immutable
 */
public class Operator {
	private String description;
	private String value;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	//the term value is ambiguous.  is this supposed to be =, >, etc.?
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
