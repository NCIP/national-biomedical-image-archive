package gov.nih.nci.nbia.beans.workflow;


import org.apache.log4j.Logger;

public class AddWorkFlowItemBean {
	
    private Integer id;
    private String name;
    private String url;
    private String collection;
    private String site;
    private String type;
    public final static String VISIBILITY_TYPE = "Visibility Change";
    public final static String SERIES_TYPE = "New Series";
    
    
    private static Logger logger = Logger.getLogger(AddWorkFlowItemBean.class);
    
    
    
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public static String getVisibilityType() {
		return VISIBILITY_TYPE;
	}
	public static String getSeriesType() {
		return SERIES_TYPE;
	}
    
    

	

}
