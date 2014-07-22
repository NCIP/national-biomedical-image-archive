/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.PatientDTO;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.util.SiteData;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class PatientDAOImpl extends AbstractDAO
                            implements PatientDAO {

	/**
	 * Fetch Patient Object through patient PK ID
	 * @param pid patient PK id
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public PatientDTO getPatientById(Integer pid) throws DataAccessException
	{
		PatientDTO pDto = null;

        DetachedCriteria criteria = DetachedCriteria.forClass(Patient.class);
		criteria.add(Restrictions.eq("id", pid));

		List<Patient> result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0)
		{
			Patient patient = result.get(0);
			pDto = new PatientDTO();
			pDto.setProject(patient.getDataProvenance().getProject());
			pDto.setSiteName(patient.getDataProvenance().getDpSiteName());

		}

		return pDto;
	}

	/**
	 * Fetch Patient Object through project, ie. collection
	 * This method is used for NBIA Rest API.
	 * @param collection A label used to name a set of images collected for a specific trial or other reason.
	 * Assigned during the process of curating the data. The info is kept under project column
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Object[]> getPatientByCollection(String collection, List<String> authorizedProjAndSites) throws DataAccessException
	{
		StringBuffer whereCondition = new StringBuffer();

		whereCondition.append(collection == null ? "":" and UPPER(p.dataProvenance.project)=?");
		whereCondition.append(addAuthorizedProjAndSites(authorizedProjAndSites));

		String hql = "select distinct p.patientId, p.patientName, p.patientBirthDate, p.patientSex, p.ethnicGroup, p.dataProvenance.project from Patient as p, GeneralSeries as gs " +
				" where gs.visibility = '1' and p.patientId = gs.patientId "+ whereCondition;
		List<Object[]> rs = collection == null ?
				getHibernateTemplate().find(hql):
				getHibernateTemplate().find(hql, collection.toUpperCase()); // protect against sql injection

        return rs;
	}

	/**
	 * Construct the partial where clause which contains checking with authorized project and site combinations.
	 *
	 * This method is used for NBIA Rest API filter.
	 */
	private StringBuffer addAuthorizedProjAndSites(List<String> authorizedProjAndSites) {
		StringBuffer where = new StringBuffer();

		if ((authorizedProjAndSites != null) && (!authorizedProjAndSites.isEmpty())){
			where = where.append(" and gs.projAndSite in (");

			for (Iterator<String> projAndSites =  authorizedProjAndSites.iterator(); projAndSites .hasNext();) {
	    		String str = projAndSites.next();
	            where.append (str);

	            if (projAndSites.hasNext()) {
	            	where.append(",");
	            }
	        }
			where.append(")");
		}
		System.out.println("&&&&&&&&&&&&where clause for project and group=" + where.toString());
		return where;
	}
}
