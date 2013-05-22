/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion.dao;

import gov.nih.nci.nbia.deletion.DeletionAuditStudyInfo;
import gov.nih.nci.nbia.exception.DataAccessException;
import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.Study;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class StudyDAOImpl extends HibernateDaoSupport implements StudyDAO {

	public void removeStudy(Integer studyId) throws DataAccessException
	{
		Study study = getStudyObject(studyId);
		
		if (study != null)
		{
			Session session = getExistingSession();
			session.delete(study);
		}

	}

	public boolean checkStudyNeedToBeRemoved(Integer studyId, Integer count) throws DataAccessException
	{
		boolean needToRemove = false;
	
		if (studyId == null || count == 0)
		{
			return false;
		}

		//get total studies by a given patient
		int size = getTotalSeriesNumber(studyId) ;
		needToRemove  = (size > 0 ? false : true);

		return needToRemove;
	}
	
	public Integer getPatientId(Integer studyId)
	{
		Study study = getStudyObject(studyId);
		if (study != null){
			return study.getPatient().getId();
		}else{
			return null;
		}
	}

	private Study getStudyObject(Integer studyId)throws DataAccessException
	{
		Study study = null;

		Session session = getExistingSession();
		Criteria criteria = session.createCriteria(Study.class);
		criteria.add(Restrictions.eq("id", studyId));

		List<Study> result = criteria.list();
		if (result != null && result.size() > 0)
		{
			study = result.get(0);
		}
		return study;
	}
	
	public DeletionAuditStudyInfo getDeletionAuditStudyInfo(Integer studyId)
	{
		Study study = getStudyObject(studyId);
		DeletionAuditStudyInfo dasi = new DeletionAuditStudyInfo();
		dasi.setStudyInstanceUID(study.getStudyInstanceUID());
		dasi.setTotalImage(null);
		
		return dasi;
	}
	
	public int getTotalSeriesNumber(Integer studyId)throws DataAccessException
	{
		int total = 0;

		Session session = getExistingSession();
		Criteria criteria = session.createCriteria(GeneralSeries.class);
		criteria.add(Restrictions.eq("study.id", studyId));

		List<GeneralSeries> result = criteria.list();
		if (result != null && result.size() > 0)
		{
			total = result.size();
		}
		
		return total;
	}
	private Session getExistingSession()
	{
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		if (session == null)
		{
			throw new DataAccessException("Cannot fetch Session from StudyDAOImpl");
		}

		return session;
	}

}
