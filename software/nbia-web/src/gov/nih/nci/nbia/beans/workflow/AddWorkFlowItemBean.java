package gov.nih.nci.nbia.beans.workflow;


import gov.nih.nci.nbia.dao.WorkflowDAO;
import gov.nih.nci.nbia.dto.WorkflowDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

public class AddWorkFlowItemBean implements Serializable{
	
    private Integer id;
    private int load; //used to load for editing
    private String name;
    private String url;
    private String collection;
    private String newCollection;
    private String site;
    private String newSite;
    private String type;
    private String errorMessage;
    private List<SelectItem> collections;
    private List<SelectItem> sites;
    private List<SelectItem> types;
    private static final long serialVersionUID = 1234567890L;
    
    private static Logger logger = Logger.getLogger(AddWorkFlowItemBean.class);
    
    public AddWorkFlowItemBean()
    {
    	types=new ArrayList<SelectItem>();
    	types.add(new SelectItem(WorkflowDTO.VISIBILITY_TYPE));
    	types.add(new SelectItem(WorkflowDTO.SERIES_TYPE));
    	refreshValues();
    }
    private void refreshValues()
    {
    	WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
    	List <String>dataCollections=workflowDao.getCollections();
    	if (collection!=null)
    	{
             if (!dataCollections.contains(collection))
             {
            	 dataCollections.add(collection);
             }
    	}
    	collections=new ArrayList<SelectItem>();
    	for (String collection: dataCollections)
    	{
    		collections.add(new SelectItem(collection));
    	}
    	// no collection selected, so select the first one
    	if (collection==null)
    	{
    		Collections.sort(collections, new SortSelectItemIgnoreCase());
    		if (collections.size()==0) // rare situation
    		{
    			// do nothing
    		} 
    		else
    		{
    			collection=collections.get(0).toString();
    		}
    	}
    	List <String>dataSites=null;
    	if (collection!=null)
    	{
    		dataSites=workflowDao.getSitesByCollection(collection);
    	}
    	else
    	{
    		dataSites=workflowDao.getSites();
    	}
    	if (site!=null)
    	{
             if (!dataSites.contains(site))
             {
            	 dataSites.add(site);
             }
    	}
    	sites=new ArrayList<SelectItem>();
    	for (String site: dataSites)
    	{
    		sites.add(new SelectItem(site));
    	}


    	
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
    
    public List<SelectItem> getCollections() {
    	Collections.sort(collections, new SortSelectItemIgnoreCase());
		return collections;
	}
	public void setCollections(List<SelectItem> collections) {
		this.collections = collections;
	}
	public List<SelectItem> getSites() {
		Collections.sort(sites, new SortSelectItemIgnoreCase());
		return sites;
	}
	public void setSites(List<SelectItem> sites) {
		this.sites = sites;
	}
	public List<SelectItem> getTypes() {
		return types;
	}
	public void setTypes(List<SelectItem> types) {
		this.types = types;
	}
	
	public String getNewCollection() {
		return newCollection;
	}
	public void setNewCollection(String newCollection) {
		this.newCollection = newCollection;
	}
	public String getNewSite() {
		return newSite;
	}
	public void setNewSite(String newSite) {
		this.newSite = newSite;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public void newWorkflow()
	{
	    id=null;
	    name=null;
	    url=null;
	    collection=null;
	    newCollection=null;
	    site=null;
	    newSite=null;
	    type=null;
	    errorMessage=null;
	    refreshValues();
	}
	public String submit()
    {
		errorMessage=null;
		if (url==null || !isValidURL(url))
		{
			errorMessage="The URL "+url+" is not valid";
			return "createWorkflow";
		}
    	WorkflowDTO dto = new WorkflowDTO();
    	WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
    	dto.setCollection(collection);
    	dto.setId(id);
    	dto.setName(name);
    	dto.setSite(site);
    	dto.setType(type);
    	dto.setUrl(url);
    	workflowDao.save(dto);
    	refreshValues();
    	MngWorkflowBean manageWorkflowBean = (MngWorkflowBean) FacesContext.getCurrentInstance().
		getExternalContext().getSessionMap().get("mngWorkFlowBean");
    	manageWorkflowBean.refreshValues();
    	return "manageWorkflowItems";
    }
	public void setLoad(int idIn)
    {
		errorMessage=null;
		load=idIn;
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
	public int getLoad()
	{
		return load;
	}
	public String addCollection ()
	{
		collections.add(new SelectItem(newCollection));
		collection=newCollection;
		newCollection=null;
		return "createWorkflow";
	}
	public String addSite ()
	{
		sites.add(new SelectItem(newSite));
		site=newSite;
		newSite=null;
		return "createWorkflow";
	}
	public String cancel()
	{
		newWorkflow();
		return "manageWorkflowItems";
	}
	private boolean isValidURL(String url) {  

	    URL u = null;

	    try {  
	        u = new URL(url);  
	    } catch (MalformedURLException e) {  
	        return false;  
	    }

	    try {  
	        u.toURI();  
	    } catch (URISyntaxException e) {  
	        return false;  
	    }  

	    return true;  
	} 
    public class SortSelectItemIgnoreCase implements Comparator<Object> {
        public int compare(Object o1, Object o2) {
            SelectItem s1 = (SelectItem) o1;
            SelectItem s2 = (SelectItem) o2;
            return s1.toString().toLowerCase().compareTo(s2.toString().toLowerCase());
        }
    }
}
