/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import java.util.List;

import gov.nih.nci.nbia.dto.WorkflowDTO;
import gov.nih.nci.nbia.internaldomain.Workflow;


import org.springframework.dao.DataAccessException;

public interface WorkflowDAO {
	 

	public WorkflowDTO getWorkflowById(Integer wid) throws DataAccessException;
	

	public List<WorkflowDTO> getWorkflows() throws DataAccessException ;
	
	public long delete(WorkflowDTO toRemove) throws DataAccessException ;
}
