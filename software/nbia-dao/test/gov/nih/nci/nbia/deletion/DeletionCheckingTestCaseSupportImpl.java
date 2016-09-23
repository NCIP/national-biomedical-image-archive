/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

import java.util.List;

import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.exception.DataAccessException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class DeletionCheckingTestCaseSupportImpl extends HibernateDaoSupport 
					implements DeletionCheckingTestCaseSupport
{
	@Transactional
	public GeneralSeries getSeries(Integer id) throws DataAccessException
	{
		Session session = getExistingSession();

		Criteria criteria = session.createCriteria(GeneralSeries.class);
		criteria.add(Restrictions.eq("id", id));

		List<GeneralSeries> result = criteria.list();
		if (result != null && result.size() > 0)
		{
			GeneralSeries series = (GeneralSeries)result.get(0);
			return series;
		}
		else
		{
			return null;
		}
	}
	
	@Transactional
	public Study getStudy(Integer id) throws DataAccessException
	{
		Session session = getExistingSession();

		Criteria criteria = session.createCriteria(Study.class);
		criteria.add(Restrictions.eq("id", id));

		List<Study> result = criteria.list();
		if (result != null && result.size() > 0)
		{
			Study study = (Study)result.get(0);
			return study;
		}
		else
		{
			return null;
		}
	}
	
	@Transactional
	public Patient getPatient(Integer id) throws DataAccessException
	{
		Session session = getExistingSession();

		Criteria criteria = session.createCriteria(Patient.class);
		criteria.add(Restrictions.eq("id", id));

		List<Patient> result = criteria.list();
		if (result != null && result.size() > 0)
		{
			Patient patient = (Patient)result.get(0);
			return patient;
		}
		else
		{
			return null;
		}
	}
	
	private Session getExistingSession() throws DataAccessException
	{
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		if (session == null)
		{
			throw new DataAccessException("Cannot fetch Session from Session Facotry in SesiesDAOImpl");
		}
		return session;
	}
}
