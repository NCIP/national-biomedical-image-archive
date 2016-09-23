/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import gov.nih.nci.nbia.deletion.DeletionAuditPatientInfo;
import gov.nih.nci.nbia.exception.DataAccessException;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;

public class PatientDAOImpl extends HibernateDaoSupport implements PatientDAO {


	public boolean checkPatientNeedToBeRemoved(Integer patientId, Integer count)
	{
		boolean needToRemove = false;
		if (patientId == null || count == 0)
		{
			return false;
		}

		int size = getTotalStudiesInPatient(patientId);
		needToRemove  = (size > 0 ? false : true);

		return needToRemove;
	}

	private Patient getPatientObject(Integer patientId)
			throws DataAccessException {
		Patient patient = null;

		Session session = getExistingSession();
		Criteria criteria = session.createCriteria(Patient.class);
		criteria.add(Restrictions.eq("id", patientId));

		List<Patient> result = criteria.list();
		if (result != null && result.size() > 0)
		{
			patient = result.get(0);
		}
		return patient;
	}
	
	public DeletionAuditPatientInfo getDeletionAuditPatientInfo(Integer patientId)
	{
		Patient patient = getPatientObject(patientId);
		DeletionAuditPatientInfo dapi = new DeletionAuditPatientInfo();
		dapi.setPatientId(patient.getPatientId());
		dapi.setTotalImage(null);
		
		return dapi;
	}
	
	public int getTotalStudiesInPatient(Integer patientId)
	throws DataAccessException {
		int total = 0;

		Session session = getExistingSession();
		Criteria criteria = session.createCriteria(Study.class);
		criteria.add(Restrictions.eq("patient.id", patientId));

		List<Study> result = criteria.list();
		if (result != null && result.size() > 0)
		{
			total = result.size();
		}
		return total;
	}

	public void removePatient(Integer patientId) throws DataAccessException {
		Session session = getExistingSession();
		Patient patient = getPatientObject(patientId);
		session.delete(patient);
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
