package gov.nih.nci.nbia.beans;

import gov.nih.nci.nbia.dynamicsearch.DynamicSearchCriteria;

import java.util.List;

import javax.faces.model.SelectItem;

public class DynamicSearchCriteriaBean {
	protected DynamicSearchCriteria criteria = new DynamicSearchCriteria();
	protected List<SelectItem> permissibleData;
	
	public DynamicSearchCriteria getCriteria() {
		return criteria;
	}
	public void setCriteria(DynamicSearchCriteria criteria) {
		this.criteria = criteria;
	}
	public List<SelectItem> getPermissibleData() {
		return permissibleData;
	}
	public void setPermissibleData(List<SelectItem> permissibleData) {
		this.permissibleData = permissibleData;
	}
	
}
