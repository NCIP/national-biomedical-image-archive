/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.PatientDTO;

import org.springframework.dao.DataAccessException;

public interface PatientDAO {
	 
	/**
	 * Fetch Patient Object through patient PK ID
	 * @param pid patient PK id
	 */
	public PatientDTO getPatientById(Integer pid) throws DataAccessException;
}
