/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.WorkflowDTO;
import gov.nih.nci.nbia.internaldomain.Workflow;
import gov.nih.nci.nbia.workflowsupport.RESTCaller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

 class WorkflowDAOImpl extends AbstractDAO
                                   implements WorkflowDAO {
	 
	private final static String SITES_QUERY="select distinct dp_site_name from trial_data_provenance";
	private final static String COLLECTION_QUERY="select distinct project from trial_data_provenance";
	private final static String SITE_COLLECTION_QUERY="select distinct dp_site_name from trial_data_provenance where project=:project";
	static Logger log = Logger.getLogger(WorkflowDAOImpl.class);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public long delete(Integer toRemove) throws DataAccessException {
		    Workflow existingWorkflow = (Workflow)getHibernateTemplate().load(Workflow.class, toRemove);
			getHibernateTemplate().delete(existingWorkflow);
			getHibernateTemplate().flush();
		return 1L;
	}

	
	@Transactional(propagation=Propagation.REQUIRED)
	public WorkflowDTO getWorkflowById(Integer wid) throws DataAccessException{
		WorkflowDTO wDto = null;

        DetachedCriteria criteria = DetachedCriteria.forClass(Workflow.class);
		criteria.add(Restrictions.eq("id", wid));

		List<Workflow> result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0)
		{
			Workflow workflow = result.get(0);
            wDto=convertObjectToDTO(workflow);
		}
		return wDto;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<WorkflowDTO> getWorkflows() throws DataAccessException{
		List<WorkflowDTO> wDtos = new ArrayList<WorkflowDTO>();

        DetachedCriteria criteria = DetachedCriteria.forClass(Workflow.class);
		
		List<Workflow> result = getHibernateTemplate().findByCriteria(criteria);
		for (Workflow workflow : result){
			WorkflowDTO wDto=convertObjectToDTO(workflow);
			wDtos.add(wDto);
		}
		return wDtos;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<WorkflowDTO> getNewSeriesWorkflowsByCollectionAndSite(String collection, String site) throws DataAccessException{
		List<WorkflowDTO> wDtos = new ArrayList<WorkflowDTO>();

        DetachedCriteria criteria = DetachedCriteria.forClass(Workflow.class);
        criteria.add(Restrictions.eq("collection", collection));
        criteria.add(Restrictions.eq("site", site));
        criteria.add(Restrictions.eq("type", WorkflowDTO.SERIES_TYPE));
        log.info("Searching for workflow - collection:"+collection+"; site:"+site+"; visibility type:"+WorkflowDTO.SERIES_TYPE);
		List<Workflow> result = getHibernateTemplate().findByCriteria(criteria);
		for (Workflow workflow : result){
			WorkflowDTO wDto=convertObjectToDTO(workflow);
			wDtos.add(wDto);
		}
		return wDtos;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public List<WorkflowDTO> getVisibilityWorkflowsByCollectionAndSite(String collection, String site) throws DataAccessException{
		List<WorkflowDTO> wDtos = new ArrayList<WorkflowDTO>();

        DetachedCriteria criteria = DetachedCriteria.forClass(Workflow.class);
        criteria.add(Restrictions.eq("collection", collection));
        criteria.add(Restrictions.eq("site", site));
        criteria.add(Restrictions.eq("type", WorkflowDTO.VISIBILITY_TYPE));
        log.info("Searching for workflow - collection:"+collection+"; site:"+site+"; visibility type:"+WorkflowDTO.VISIBILITY_TYPE);
		List<Workflow> result = getHibernateTemplate().findByCriteria(criteria);
		for (Workflow workflow : result){
			WorkflowDTO wDto=convertObjectToDTO(workflow);
			System.out.println("Adding workflow DTO");
			wDtos.add(wDto);
		}
		return wDtos;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public List<String> getSitesByCollection(String collection) throws DataAccessException{
		List<String> returnValue = new ArrayList<String>();
		try {
			returnValue= this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(SITE_COLLECTION_QUERY)
	    	  .setParameter("project", collection).list();
			if (returnValue.size()==0) {
				 return returnValue; //nothing to do
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnValue;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public long save(WorkflowDTO wDTO) throws DataAccessException {

		
		Integer id = wDTO.getId();

		if(id != null){
			update(wDTO);
		}else{
			insert(wDTO);
		}
		return 1L;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<String> getSites() throws DataAccessException{
		List<String> returnValue=new ArrayList<String>();

		List <String>sites= this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(SITES_QUERY)
        .list();		
		for (String site : sites )
		{
        	returnValue.add((String)site);
		}
		return returnValue;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<String> getCollections() throws DataAccessException{
		List<String> returnValue=new ArrayList<String>();

		List <String>collections= this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(COLLECTION_QUERY)
        .list();		
		for (String collection : collections )
		{
        	returnValue.add((String)collection);
		}
		return returnValue;
	}
	
	////////////////////////////////PROTECTED//////////////////////////////////////////////



	protected void update(WorkflowDTO wDTO){
		Workflow worflow = convertDTOToObject(wDTO);
		getHibernateTemplate().update(worflow);
		getHibernateTemplate().flush();

	}

	protected void insert(WorkflowDTO wDTO){
		Workflow worflow = convertDTOToObject(wDTO);
		getHibernateTemplate().saveOrUpdate(worflow);
		getHibernateTemplate().flush();
	}


	/////////////////////////////////PRIVATE/////////////////////////////////////////////
	private Workflow convertDTOToObject(WorkflowDTO wDTO){
		Workflow workflow = new Workflow();
		workflow.setId(wDTO.getId());
		workflow.setCollection(wDTO.getCollection());
		workflow.setName(wDTO.getName());
		workflow.setSite(wDTO.getSite());
		workflow.setUrl(wDTO.getUrl());
		workflow.setType(wDTO.getType());
		return workflow;
	}
	private WorkflowDTO convertObjectToDTO(Workflow workflow){
		WorkflowDTO wDto = new WorkflowDTO();
		wDto.setId(workflow.getId());
		wDto.setCollection(workflow.getCollection());
		wDto.setName(workflow.getName());
		wDto.setSite(workflow.getSite());
		wDto.setUrl(workflow.getUrl());
		wDto.setType(workflow.getType());
		return wDto;
	}
	
}
