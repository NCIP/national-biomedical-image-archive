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

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIData;

import org.apache.log4j.Logger;

public class ManageWorkflowBean {


	private static Logger logger = Logger.getLogger(ManageWorkflowBean.class);
    private List<ManageWorkFlowDTO> interfaceDTOs;
    private String name="";
    private boolean errorMessage=false;
    private String message;
    private UIData table;
	private int toDelete;
	private int toEdit;


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

    public ManageWorkflowBean() throws Exception{
    	refreshValues();
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
	    return null;
	}
	/**
	 * 
	 * @return
	*/
	public String performEdit() {

		logger.warn("Editing workflow");
	    return "createWorkflow";
	}
	public String createWorkflow() {

		logger.warn("Creating workflow");
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

	private void refreshValues()
    {
    	interfaceDTOs=new ArrayList<ManageWorkFlowDTO>();
    	WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
    	List<WorkflowDTO> wDTOs=workflowDao.getWorkflows();
    	for (WorkflowDTO inDTO : wDTOs)
    	{
    		ManageWorkFlowDTO newDTO = new ManageWorkFlowDTO();
    		newDTO.setId(inDTO.getId());
    		newDTO.setName(inDTO.getName());
    		newDTO.setCollectionSite(inDTO.getCollection()+"-"+inDTO.getSite());
    		interfaceDTOs.add(newDTO);
    	}
    }
}