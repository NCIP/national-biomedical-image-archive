package gov.nih.nci.nbia.beans.workflow;

public class ManageWorkFlowDTO {
	private Integer id;
	private String name;
	private String collectionSite;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCollectionSite() {
		return collectionSite;
	}
	public void setCollectionSite(String collectionSite) {
		this.collectionSite = collectionSite;
	}

}
