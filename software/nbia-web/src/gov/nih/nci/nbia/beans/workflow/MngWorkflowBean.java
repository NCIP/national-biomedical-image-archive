/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.workflow;

import gov.nih.nci.nbia.dao.WorkflowDAO;
import gov.nih.nci.nbia.dto.WorkflowDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.beans.workflow.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

public class MngWorkflowBean  implements Serializable{


	private static Logger logger = Logger.getLogger(MngWorkflowBean.class);
   // private List<ManageWorkFlowDTO> interfaceDTOs=new ArrayList<ManageWorkFlowDTO>();
    private String name="";
    private boolean errorMessage=false;
    private String message;
    private UIData table;
	private int toDelete;
	private int toEdit;
	private String myString=" ";
    private static final long serialVersionUID = 1234567890L;
	


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

	public void setTempDTOs(List<ManageWorkFlowDTO> interfaceDTOs) {
		//this.interfaceDTOs = interfaceDTOs;
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



	public UIData getTable() {
		return table;
	}

	public void setTable(UIData table) {
		this.table = table;
	}



	/**
	 * 
	 * @return
	*/
	public String performDelete() {

		logger.warn("Deleting workflow: " + toDelete);
		WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
		workflowDao.delete(new Integer(toDelete));
		refreshValues();
	    return "manageWorkflowItems";
	}
	/**
	 * 
	 * @return
	*/
	public String performEdit() {

		logger.info("Editing workflow");
	    return "createWorkflow";
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
}