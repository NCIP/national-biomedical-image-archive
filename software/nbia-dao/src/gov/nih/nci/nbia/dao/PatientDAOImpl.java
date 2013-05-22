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

}
