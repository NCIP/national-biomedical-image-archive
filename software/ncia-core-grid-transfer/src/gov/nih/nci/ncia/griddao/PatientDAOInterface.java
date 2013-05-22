/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.griddao;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

public interface PatientDAOInterface {
	public List<Date> getTimepointStudyForPatient(String patientId) throws DataAccessException;
}
