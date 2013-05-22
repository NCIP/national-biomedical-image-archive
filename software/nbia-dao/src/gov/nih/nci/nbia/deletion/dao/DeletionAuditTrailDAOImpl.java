/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion.dao;

import java.util.Date;
import java.util.List;

import gov.nih.nci.nbia.deletion.DeletionAuditPatientInfo;
import gov.nih.nci.nbia.deletion.DeletionAuditSeriesInfo;
import gov.nih.nci.nbia.deletion.DeletionAuditStudyInfo;
import gov.nih.nci.nbia.exception.DataAccessException;
import gov.nih.nci.nbia.internaldomain.DeletionAuditTrail;
import gov.nih.nci.nbia.util.NCIAConstants;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DeletionAuditTrailDAOImpl extends HibernateDaoSupport implements
		DeletionAuditTrailDAO
{

	public void recordSeries(List<DeletionAuditSeriesInfo> seriesInfos, String userName) throws DataAccessException {
		Session session = getExistingSession();
		
		for (DeletionAuditSeriesInfo info : seriesInfos)
		{	
			DeletionAuditTrail deletionAuditTrail = getDeletionAuditTrail(info, userName);
			session.save(deletionAuditTrail);
		}
	}
	public void recordStudy(DeletionAuditStudyInfo study, String userName) throws DataAccessException {
		Session session = getExistingSession();
		DeletionAuditTrail deletionAuditTrail = getDeletionAuditTrail(study, userName);
		session.save(deletionAuditTrail);
	}
	public void recordPatient(DeletionAuditPatientInfo patient, String userName) throws DataAccessException {
		Session session = getExistingSession();
		DeletionAuditTrail deletionAuditTrail = getDeletionAuditTrail(patient, userName);
		session.save(deletionAuditTrail);
	}

	private Session getExistingSession() throws DataAccessException
	{
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		if (session == null)
		{
			throw new DataAccessException("Cannot obtain the session in DeletionAuditTrailDAOImpl class");
		}

		return session;
	}

	private DeletionAuditTrail getDeletionAuditTrail(Object o, String userName)
	{
		DeletionAuditTrail dat = new DeletionAuditTrail();
		if (o instanceof DeletionAuditSeriesInfo)
		{
			DeletionAuditSeriesInfo seriesInfo = (DeletionAuditSeriesInfo)o;
			dat.setDataType(NCIAConstants.GENERAL_SERIES_TYPE);
			dat.setDataId(seriesInfo.getSeriesInstanceUID());
			dat.setTotalImages(seriesInfo.getTotalImageSize());
		}
		if(o instanceof DeletionAuditStudyInfo)
		{
			DeletionAuditStudyInfo study = (DeletionAuditStudyInfo)o;
			dat.setDataType(NCIAConstants.STUDY_TYPE);
			dat.setDataId(study.getStudyInstanceUID());
			dat.setTotalImages(null);
		}
		if (o instanceof DeletionAuditPatientInfo)
		{
			DeletionAuditPatientInfo patient = (DeletionAuditPatientInfo)o;
			dat.setDataType(NCIAConstants.PATIENT_TYPE);
			dat.setDataId(patient.getPatientId());
			dat.setTotalImages(null);
		}
		dat.setTimeStamp(new Date());
		if (userName == null)
		{
			dat.setUserName("System");
		}
		else
		{
			dat.setUserName(userName);
		}

		return dat;
	}
}
