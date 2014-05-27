package gov.nih.nci.nbia.beans.workflow;


import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.*;
import gov.nih.nci.nbia.dao.*;
import gov.nih.nci.nbia.dto.*;
import gov.nih.nci.nbia.util.SpringApplicationContext;
public class AddWorkFlowItemBean implements Serializable{
	
    private Integer id;
    private String name;
    private String url;
    private String collection;
    private String site;
    private String type;
    private List<String> collections;
    private List<String> sites;
    private List<String> types;
    private static final long serialVersionUID = 1234567890L;

    
    
    private static Logger logger = Logger.getLogger(AddWorkFlowItemBean.class);
    
    public AddWorkFlowItemBean()
    {
    	types=new ArrayList<String>();
    	types.add(WorkflowDTO.VISIBILITY_TYPE);
    	types.add(WorkflowDTO.SERIES_TYPE);
    	refreshValues();
    }
    private void refreshValues()
    {
    	WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
    	sites=workflowDao.getSites();
    	collections=workflowDao.getCollections();
    }
    
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
    
    public List<String> getCollections() {
		return collections;
	}
	public void setCollections(List<String> collections) {
		this.collections = collections;
	}
	public List<String> getSites() {
		return sites;
	}
	public void setSites(List<String> sites) {
		this.sites = sites;
	}
	public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
	}
	public void submit()
    {
    	WorkflowDTO dto = new WorkflowDTO();
    	WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
    	dto.setCollection(collection);
    	dto.setId(null);
    	dto.setName(name);
    	dto.setSite(site);
    	dto.setType(type);
    	dto.setUrl(url);
    	workflowDao.save(dto);
    	refreshValues();
    }
	public void load(int idIn)
    {
    	WorkflowDTO dto = new WorkflowDTO();
    	WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
        dto=workflowDao.getWorkflowById(new Integer(idIn));
        id=dto.getId();
        name=dto.getName();
        collection=dto.getCollection();
        site=dto.getSite();
        type=dto.getType();
        url=dto.getUrl();
    	refreshValues();
    }
	

}
