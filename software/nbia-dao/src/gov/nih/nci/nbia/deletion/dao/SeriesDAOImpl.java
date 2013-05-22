/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion.dao;

import gov.nih.nci.nbia.deletion.DeletionAuditSeriesInfo;
import gov.nih.nci.nbia.deletion.DeletionDisplayObject;
import gov.nih.nci.nbia.exception.DataAccessException;
import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.util.NCIAConstants;
import gov.nih.nci.nbia.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class SeriesDAOImpl extends HibernateDaoSupport implements SeriesDAO{
	private static Logger logger = Logger.getLogger(SeriesDAOImpl.class);

	public List<Integer> listAllDeletedSeries()
	throws DataAccessException
	{
		List<Integer> result = null;

		try
		{
			Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
			if (session == null)
			{
				throw new DataAccessException("Cannot fetch Session from Session Facotry in SesiesDAOImpl");
			}
			Criteria criteria = session.createCriteria(GeneralSeries.class);
			criteria.setProjection(Projections.property("id"));

			criteria.add(Restrictions.eq("visibility", NCIAConstants.DELETED_STATUS));

			result = criteria.list();

		}catch (org.springframework.dao.DataAccessException e)
		{
			logger.error("In SeriesDAOImpl, data Access Exception: " + e.getMessage() );
			throw new DataAccessException(e.getMessage());
		}

		return result;
	}

	public void removeSeries(List<Integer> seriesIds)
	throws DataAccessException
	{
		List<List<Integer>> breakedSeriesList = Util.breakListIntoChunks(seriesIds, 900);
		
		for (List<Integer> unit : breakedSeriesList)
		{
			List<GeneralSeries> seriesList = getSeriesObject(unit);

			//Session session = getExistingSession();
			for (GeneralSeries series : seriesList)
			{
				getHibernateTemplate().delete(series);
			}
			getHibernateTemplate().flush();
		}
	}

	private List<GeneralSeries> getSeriesObject(List<Integer> seriesIds)
	throws DataAccessException
	{
		List<GeneralSeries> result = new ArrayList<GeneralSeries>();
		
		List<List<Integer>> breakdownList = Util.breakListIntoChunks(seriesIds, 900);
		for (List<Integer> unit : breakdownList)
		{
			DetachedCriteria criteria = DetachedCriteria.forClass(GeneralSeries.class);
			criteria.add(Restrictions.in("id", unit));

			List<GeneralSeries> tempHolder = getHibernateTemplate().findByCriteria(criteria);
			for (GeneralSeries ser : tempHolder)
			{
				result.add(ser);
			}
		}
		
		return result;
	}
	
	public List<DeletionAuditSeriesInfo> getDeletionAuditSeriesInfo(List<Integer> seriesList)
	{
		List<DeletionAuditSeriesInfo> deletionListInfo = new ArrayList<DeletionAuditSeriesInfo>();
		List<GeneralSeries> seriesObjs = getSeriesObject(seriesList);
		
		for (GeneralSeries series : seriesObjs)
		{
			DeletionAuditSeriesInfo dasi = new DeletionAuditSeriesInfo();
			dasi.setSeriesInstanceUID(series.getSeriesInstanceUID());
			dasi.setTotalImageSize(series.getGeneralImageCollection().size());
			deletionListInfo.add(dasi);
		}
		return deletionListInfo;
	}
	
	public Map<Integer, Integer> getStudyMap(List<Integer>seriesIds){
		
		Map<Integer, Integer> studyMap = new HashMap<Integer, Integer>();

		List<GeneralSeries> gsObject = getSeriesObject(seriesIds); 
		for (GeneralSeries series : gsObject)
		{
			if (studyMap.get(series.getStudy().getId()) == null)
			{
				int count=0;
				count++;
				studyMap.put(series.getStudy().getId(),new Integer(count));
			}else
			{
				Integer inc = studyMap.get(series.getStudy().getId());
				inc++;
				studyMap.put(series.getStudy().getId(), new Integer(inc));
			}
		}
		return studyMap;
	}	

	public Map<Integer, Integer> getPatientMap(List<Integer>seriesIds){
		
		Map<Integer, List<Integer>> patientMap = new HashMap<Integer, List<Integer>>();
		List<GeneralSeries> gsObject = getSeriesObject(seriesIds); 
		
		for (GeneralSeries series : gsObject)
		{
			if (patientMap.get(series.getPatientPkId()) == null)
			{
				List<Integer> studyPkIdList = new ArrayList<Integer>();
				studyPkIdList.add(series.getStudy().getId());
				patientMap.put(series.getPatientPkId(), studyPkIdList);
			}
			else
			{
				List<Integer> studyPkIdList  = patientMap.get(series.getPatientPkId());
				if (!studyPkIdList.contains(series.getStudy().getId()))
				{
					studyPkIdList.add(series.getStudy().getId());
					patientMap.put(series.getPatientPkId(), studyPkIdList);
				}
			}
		}
		Map<Integer,Integer> studyHasBeenChangedInpatientMap = new HashMap<Integer, Integer>();
		
		Iterator<Integer> iterator = patientMap.keySet().iterator();
		while( iterator. hasNext() )
		{
			Integer patientId =  iterator.next();
			List<Integer> studiesPkIds = patientMap.get(patientId);
			studyHasBeenChangedInpatientMap.put(patientId, studiesPkIds.size());
		}
		
		return studyHasBeenChangedInpatientMap;
	}
	
	public List<DeletionDisplayObject> getDeletionDisplayObjectDTO(List<Integer> seriesList){
		List<DeletionDisplayObject> object = new ArrayList<DeletionDisplayObject>();
		int count = 0;
		
		if (seriesList != null && seriesList.size() > 0)
		{
			List<List<Integer>> breakedSeriesList = Util.breakListIntoChunks(seriesList, 900);
			for (List<Integer> unit : breakedSeriesList)
			{
				List<GeneralSeries> seriesObjects = getSeriesObject(unit);
				for (GeneralSeries series : seriesObjects)
				{
					DeletionDisplayObject dObject = new DeletionDisplayObject();
					count++;
					dObject.setOrder(count);
					dObject.setSeriesUID(series.getSeriesInstanceUID());
					setProject(series, dObject);
					object.add(dObject);
				}
			}
		}
		
		return object;
	}
	
	private void setProject(GeneralSeries series, DeletionDisplayObject dObject)
	{
		if (series.getStudy() == null || series.getStudy().getPatient() == null ||
				series.getStudy().getPatient().getDataProvenance() == null)
		{
			dObject.setProject("");
			dObject.setSite("");
		}
		else
		{
			dObject.setProject(series.getStudy().getPatient().getDataProvenance().getProject());
			dObject.setSite(series.getStudy().getPatient().getDataProvenance().getDpSiteName());
		}
	}
}
