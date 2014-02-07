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
	public List<Object[]> getPatientByCollection(String collection,List<SiteData> authorizedSites) throws DataAccessException
	{
		StringBuffer whereCondition = new StringBuffer();
		String authorisedProjectName = getProjectNames(authorizedSites);
		String authorisedSiteName = getSiteNames(authorizedSites);
		if(authorisedProjectName != null && authorisedSiteName != null) {
			whereCondition = whereCondition.append(" and UPPER(gs.project) in (").append(authorisedProjectName).append(")").append(" and UPPER(gs.site) in (" + authorisedSiteName+")");
		}
		whereCondition.append(collection == null ? "":" and UPPER(p.dataProvenance.project)=?");

		String hql = "select distinct p.patientId, p.patientName, p.patientBirthDate, p.patientSex, p.ethnicGroup, p.dataProvenance.project from Patient as p, GeneralSeries as gs " +
				" where gs.visibility = '1' and p.patientId = gs.patientId "+ whereCondition;
		List<Object[]> rs = collection == null ?
				getHibernateTemplate().find(hql):
				getHibernateTemplate().find(hql, collection.toUpperCase()); // protect against sql injection

        return rs;
	}
	private String getProjectNames(Collection<SiteData> sites) {
		if(sites == null || sites.isEmpty()) {
			return null;
		}
		String projectNameStmt = "";
    	for (Iterator<SiteData> i = sites.iterator(); i.hasNext();) {
    		SiteData str = i.next();
            projectNameStmt += ("'" + str.getCollection().toUpperCase() + "'");

            if (i.hasNext()) {
            	projectNameStmt += ",";
            }
        }
    	return projectNameStmt;
    }
	private String getSiteNames(Collection<SiteData> sites) {
		if(sites == null || sites.isEmpty()) {
			return null;
		}
		String siteWhereStmt = "";
    	for (Iterator<SiteData> i = sites.iterator(); i.hasNext();) {
    		SiteData str = i.next();
            siteWhereStmt += ("'" + str.getSiteName().toUpperCase() + "'");

            if (i.hasNext()) {
            	siteWhereStmt += ",";
            }
        }
    	return siteWhereStmt;
    }
}
