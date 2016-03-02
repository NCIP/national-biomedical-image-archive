/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.workflow;

import gov.nih.nci.nbia.dao.WorkflowDAO;
import gov.nih.nci.nbia.dto.QcSearchResultDTO;
import gov.nih.nci.nbia.dto.WorkflowDTO;
import gov.nih.nci.nbia.beans.workflow.ManageWorkFlowDTO;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.beans.workflow.*;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.UIComponent;

import org.apache.log4j.Logger;

public class MngWorkflowBean  implements Serializable{

 private static Logger logger = Logger.getLogger(MngWorkflowBean.class);
 // private List<ManageWorkFlowDTO> interfaceDTOs=new ArrayList<ManageWorkFlowDTO>();
 private String name="";
 private boolean errorMessage=false;
 private String message;
 private int toDelete;
 private int toEdit;
 private String myString=" ";
 private static final long serialVersionUID = 1234567890L;
private boolean toggleSort = true;
 
 
 public String getMyString() {
	 return myString;
 }
 public void setMyString(String myString) {
	 this.myString = myString;
 }
 public List<ManageWorkFlowDTO> getTempDTOs() {
	 List<ManageWorkFlowDTO> tempDTOs=new ArrayList<ManageWorkFlowDTO>();
	 WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
	 List<WorkflowDTO> wDTOs=workflowDao.getWorkflows();
	 for (WorkflowDTO inDTO : wDTOs)
	 {
		 ManageWorkFlowDTO newDTO = new ManageWorkFlowDTO();
		 System.out.println("Adding Temp DTO "+inDTO.getId()+" to list");
		 newDTO.setId(inDTO.getId());
		 newDTO.setName(inDTO.getName());
		 newDTO.setCollectionSite(inDTO.getCollection()+"-"+inDTO.getSite());
		 tempDTOs.add(newDTO);
		 System.out.println("Length "+tempDTOs.size());
	 }
	 
 	return tempDTOs;
 }

	public boolean isErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(boolean errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MngWorkflowBean(){
    	try {
    		setToggleSort(true);
			refreshValues();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isToggleSort() {
    	return toggleSort;
   }
    
   public void setToggleSort(boolean toggleSort) {
   		this.toggleSort = toggleSort;
   }
	/**
	 * 
	 * @return 
	 * @return
	*/
   private String hiddenDel;
   
   
    public String getHiddenDel() {
		return hiddenDel;
	}
	public void setHiddenDel(String hiddenDel) {
		this.hiddenDel = hiddenDel;
	}
	
	public String deleteWorkflow(){
		int idI = 0;
		String valueDel=null;

		valueDel =(String) FacesContext.getCurrentInstance().
				getExternalContext().getRequestParameterMap().get(String.valueOf("hiddenDel"));
		System.out.println("valueDel " +valueDel);
		idI = Integer.parseInt(valueDel);
		performDelete(idI);
		return "manageWorkflowItems";
    	
    }
    
    
	public void performDelete(int intDel) {

		
		logger.warn("Deleting workflow: " + intDel);
		WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
		workflowDao.delete(new Integer(intDel));
		intDel = 0;
		refreshValues();
		setToggleSort(true);	    
	}
	/**
	 * 
	 * @return
	*/
	
	private UIData table;
	
	public UIData getTable() {
		return table;
	}
	public void setTable(UIData table) {
		this.table = table;
	}
	
	String value="Something1";
	int val =0;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getVal() {
		return val;
	}
	public void setVal(int val) {
		this.val = val;
	}
	
	
	public String createWorkflow() {

		logger.info("Creating workflow");
		AddWorkFlowItemBean addWorkflowBean = (AddWorkFlowItemBean) FacesContext.getCurrentInstance().
		getExternalContext().getSessionMap().get("addWorkFlowItemBean");
		if (addWorkflowBean!=null) // check to see if already created
		{
			addWorkflowBean.newWorkflow();
		}
	    return "createWorkflow";
	}
	public int getToDelete() {
		return toDelete;
	}
	public void setToDelete(int toDelete) {
		this.toDelete = toDelete;
	}

    public int getToEdit() {
		return toEdit;
	}

	public void setToEdit(int toEdit) {
		this.toEdit = toEdit;
	}

	public void refreshValues()
    {

    }
	
	
	
    private static final String nameHeader = "Name";
    private static final String collectionSiteHeader = "Collection Site";
    
    private String sortColumnName= "nameHeader";
    private boolean ascending = false;
	
    private String oldSort = sortColumnName;
    private boolean oldAscending = ascending;
    
    private List<ManageWorkFlowDTO> dtoManageList = new ArrayList<ManageWorkFlowDTO>();
   
    
    public List<ManageWorkFlowDTO> getDtoManageList() {
    	
    	if(dtoManageList != null){
    		dtoManageList.clear();
    	}
    	
    	
    	for(ManageWorkFlowDTO a : getTempDTOs()){
    		dtoManageList.add(a);
    	}
    	setToggleSort(true);
    	
    	System.out.println("From getDtoManageList");
    	if (!oldSort.equals(sortColumnName) ||
                (oldAscending != ascending) || toggleSort == true){
             sort();
             oldSort = sortColumnName;
             oldAscending = ascending;
             toggleSort = false;
        }

		return dtoManageList;
	}
    
	public void setDtoManageList(List<ManageWorkFlowDTO> dtoManageList) {
		this.dtoManageList = dtoManageList;
	}
	
	public String getNameHeader() {

		return nameHeader;
	}

	public String getCollectionSiteHeader() {
		return collectionSiteHeader;
	}
    
    public String getSortColumnName() {
        return sortColumnName;
    }


    public void setSortColumnName(String sortColumnName) {
        oldSort = this.sortColumnName;
        this.sortColumnName = sortColumnName;

    }


    public boolean isAscending() {
        return ascending;
    }


    public void setAscending(boolean ascending) {
        oldAscending = this.ascending;
        this.ascending = ascending;
    }

    
    protected void sort() {
      
		Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
            	ManageWorkFlowDTO c1 = (ManageWorkFlowDTO) o1;
            	ManageWorkFlowDTO c2 = (ManageWorkFlowDTO) o2;
                if (sortColumnName == null) {
                	return 0;
                }
                if (sortColumnName.equals(nameHeader) || toggleSort == true) {

                	if (c1.getName() != null && c2.getName()!= null) {
                		System.out.println("c1.getName() "+ c1.getName());
                		System.out.println("c2.getName() "+ c2.getName());
                		return compareObject(c1.getName().compareToIgnoreCase(c2.getName()), c2.getName().compareToIgnoreCase(c1.getName()));
                	}
                	else{
                		System.out.println("Inside Sort 5");
                		return 0;
                	}
                }
                else if (sortColumnName.equals(collectionSiteHeader)) {
                    return compareObject(c1.getCollectionSite().compareToIgnoreCase(c2.getCollectionSite()),
                            c2.getCollectionSite().compareToIgnoreCase(c1.getCollectionSite()));
                }
                else {
                	return 0;
                }
                
            }
            
        
        };

        
        Collections.sort(dtoManageList, comparator);
        
    }

    private int compareObject(int result1, int result2){
    	return ascending ? result2 : result1;
    }
	
	
}