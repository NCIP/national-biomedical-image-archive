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
	 

	public List<WorkflowDTO> getNewSeriesWorkflowsByCollectionAndSite(String collection, String site) throws DataAccessException;
	
	public List<WorkflowDTO> getVisibilityWorkflowsByCollectionAndSite(String collection, String site) throws DataAccessException;
	
	public WorkflowDTO getWorkflowById(Integer wid) throws DataAccessException;
	
	public List<WorkflowDTO> getWorkflows() throws DataAccessException ;
	
	public long delete(Integer toRemove) throws DataAccessException ;
	
	public long save(WorkflowDTO wDTO) throws DataAccessException;
	
	public List<String> getSites() throws DataAccessException;
	
	public List<String> getCollections() throws DataAccessException;
	
	public List<String> getSitesByCollection(String collection);
	

}
