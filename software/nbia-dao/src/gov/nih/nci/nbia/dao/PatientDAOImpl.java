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
	public List<Object[]> getPatientByCollection(String collection) throws DataAccessException 
	{
		String where = collection == null ? "":" where UPPER(p.dataProvenance.project)=?";
		String hql = "select p.patientId, p.patientName, p.patientBirthDate, p.patientSex, p.ethnicGroup, p.dataProvenance.project from Patient as p"+where;
		List<Object[]> rs = collection == null ? 
				getHibernateTemplate().find(hql):
				getHibernateTemplate().find(hql, collection.toUpperCase()); // protect against sql injection

		//for testing
		if(rs != null && rs.size() > 0) {
			for (Object[] objects : rs) {
				for (Object obj: objects) {
					if (obj != null)
						System.out.println("obj name=" + obj.toString());
				}
			}
	    }
		// for testing
        return rs;
	}
}
