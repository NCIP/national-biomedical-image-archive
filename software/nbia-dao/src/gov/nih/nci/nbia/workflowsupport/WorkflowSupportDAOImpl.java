/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.workflowsupport;

import gov.nih.nci.nbia.dao.AbstractDAO;
import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dao.WorkflowDAO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.dto.WorkflowDTO;
import gov.nih.nci.nbia.qctool.VisibilityStatus;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



public class WorkflowSupportDAOImpl extends AbstractDAO
                               implements WorkflowSupportDAO 
{
	static Logger log = Logger.getLogger(WorkflowSupportDAOImpl.class);
    
    private final static String UPDATED_VISIBILITY_QUERY="select distinct series_instance_uid, new_value from qc_status_history where history_timestamp between :low and :high";
    private final static String SERIES_ADDED_QUERY="select distinct series_instance_uid from submission_history where submission_timestamp between :low and :high";
    private final static String SERIES_EXISTS_QUERY="select distinct series_instance_uid from submission_history where submission_timestamp < :low and series_instance_uid = :series";


@Transactional(propagation=Propagation.REQUIRED)
public List<WorkflowVisibilityUpdateDTO> getVisibilityUpdated(Date high, Date low)
{
	List<WorkflowVisibilityUpdateDTO> returnValue = new ArrayList<WorkflowVisibilityUpdateDTO>();
	log.info("high-"+high+" low-"+low);
	try {
		List <Object[]>visbilities= this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(UPDATED_VISIBILITY_QUERY)
		  .setTimestamp("low", low)
		  .setTimestamp("high", high).list();
		if (visbilities.size()==0) {
			   log.info("No updated visibilities");
			   return returnValue; //nothing to do
		}
		// first get all the series dto's associated with the new visibilities
		List<String>seriesUIDs=new ArrayList<String>();
		for (Object[] visibilityRecord : visbilities )
		{
			seriesUIDs.add((String)visibilityRecord[0]);
		}
		GeneralSeriesDAO generalSeriesDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
		WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
		List<SeriesDTO> seriesList= generalSeriesDao.findSeriesBySeriesInstanceUIDAnyVisibility(seriesUIDs);
		// now go through the list again find the series in the series dto list and then check if there are associated workflows.
		for (Object[] visibilityRecord : visbilities )
		{
			
			SeriesDTO foundSeries=findSeriesInList(seriesList, visibilityRecord[0].toString());
			if (foundSeries==null)
			{
				log.error("series not found in second loop");
				continue;
			}
			// now lets see if there are associated work flows
			List<WorkflowDTO> wDtos = workflowDao.getVisibilityWorkflowsByCollectionAndSite
			              (foundSeries.getProject(), foundSeries.getDataProvenanceSiteName());
			for (WorkflowDTO wDTO: wDtos)
			{
			    Integer i = new Integer((String)visibilityRecord[1]);
			    String visibility=VisibilityStatus.statusFactory(i).getText();
			    WorkflowVisibilityUpdateDTO vuDTO = new WorkflowVisibilityUpdateDTO();
                vuDTO.setNewVisibility(visibility);
                vuDTO.setPatientUID(foundSeries.getPatientId());
                vuDTO.setSeriesUID(foundSeries.getSeriesUID());;
                vuDTO.setwDTO(wDTO);
                log.info("vuDTO: v-"+vuDTO.getNewVisibility()+" p-"+vuDTO.getPatientUID()+" s-"+vuDTO.getSeriesUID());
                returnValue.add(vuDTO);
			}
			
		}	
		
	} catch (HibernateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return returnValue;
}
@Transactional(propagation=Propagation.REQUIRED)
public List<WorkflowNewSeriesDTO> getNewSeries(Date high, Date low)
{
	List<WorkflowNewSeriesDTO> returnValue = new ArrayList<WorkflowNewSeriesDTO>();
	log.info("high-"+high+" low-"+low);
	try {
		List<String> seriesAdded= this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(SERIES_ADDED_QUERY)
		  .setTimestamp("low", low)
		  .setTimestamp("high", high).list();
		if (seriesAdded.size()==0) {
			   log.info("No new series");
			   return returnValue; //nothing to do
		}
		// look for the series which previously did not exist
		List <String> seriesUIDs=new ArrayList<String>();
		for (String series: seriesAdded)
		{
			List test = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(SERIES_EXISTS_QUERY)
			  .setTimestamp("low", low)
			  .setString("series", series).list();
			if (test.size()<1) // series did not previously exist
			{
				seriesUIDs.add(series);
			}
		}
		GeneralSeriesDAO generalSeriesDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
		WorkflowDAO workflowDao = (WorkflowDAO)SpringApplicationContext.getBean("workflowDAO");
		List<SeriesDTO> seriesList= generalSeriesDao.findSeriesBySeriesInstanceUIDAnyVisibility(seriesUIDs);
		if (seriesList==null){
			return returnValue;
		}
		for (SeriesDTO sDTO: seriesList)
		{
			// now lets see if there are associated work flows
			List<WorkflowDTO> wDtos = workflowDao.getNewSeriesWorkflowsByCollectionAndSite
			              (sDTO.getProject(), sDTO.getDataProvenanceSiteName());
			for (WorkflowDTO wDTO: wDtos)
			{

			    WorkflowNewSeriesDTO nsDTO = new WorkflowNewSeriesDTO();
			    nsDTO.setPatientUID(sDTO.getPatientId());
			    nsDTO.setSeriesUID(sDTO.getSeriesUID());
			    nsDTO.setwDTO(wDTO);
                returnValue.add(nsDTO);
			}
			
		}
	} catch (HibernateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return returnValue;
}
private SeriesDTO findSeriesInList(List<SeriesDTO> seriesList, String series)
{
	SeriesDTO returnValue = null;
	for (SeriesDTO seriesDTO: seriesList)
	{
		System.out.println("Series:"+series);
		System.out.println("SeriesId:"+seriesDTO.getSeriesId());
		if (seriesDTO.getSeriesId().equals(series))
		{
			return seriesDTO;
		}
	}
	return returnValue;
}
}
