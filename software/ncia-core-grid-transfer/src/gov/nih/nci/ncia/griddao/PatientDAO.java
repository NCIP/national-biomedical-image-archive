/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.ncia.griddao;

import gov.nih.nci.nbia.internaldomain.Study;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This object does hand-rolled data access for anything patient related in the
 * grid service.
 *
 * <p>This came from a massive object called ImageFileProcessor which was split
 * into this object and several other "DAO" objects to isolate data access according
 * to similar pattern as rest of system.
 *
 * @author lethai
 */
public class PatientDAO extends HibernateDaoSupport implements PatientDAOInterface{

	/**
	 * This method return list of study instance uid for given patientId and studyNumber
	 * @param patientId
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Date> getTimepointStudyForPatient(String patientId) throws DataAccessException{

		String hql = "SELECT DISTINCT s "+
		            " FROM Study s , Patient p, GeneralSeries gs " +
					" WHERE p.id=s.patient.id AND p.id=gs.patientPkId AND p.patientId='" +
					patientId + "' AND s.studyDate IS NOT NULL AND gs.visibility='1' " +
					" ORDER BY s.studyDate";

		List<Date> studyDates = new ArrayList<Date>();

		if (patientId == null ) {
			return null;
		}
		List<Study> rs = this.getHibernateTemplate().find(hql);
		for (Study s : rs) {
			Date sd = s.getStudyDate();
			if(sd != null && !studyDates.contains(sd)){
					studyDates.add(sd);
			}
		}
		if(studyDates.isEmpty()){
			return null;
		}

		return studyDates;
	}

}
