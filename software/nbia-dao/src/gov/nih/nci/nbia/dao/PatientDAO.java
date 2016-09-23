/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import java.util.List;

import gov.nih.nci.nbia.dto.PatientDTO;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.util.SiteData;

import org.springframework.dao.DataAccessException;

public interface PatientDAO {

	/**
	 * Fetch Patient Object through patient PK ID
	 * @param pid patient PK id
	 */
	public PatientDTO getPatientById(Integer pid) throws DataAccessException;

	/**
	 * Fetch Patient Object through project, ie. collection
	 * @param collection A label used to name a set of images collected for a specific trial or other reason.
	 * Assigned during the process of curating the data. The info is kept under project column
	 * This method is used for NBIA Rest API.
	 */
	public List<Object[]> getPatientByCollection(String collection, List<String> authorizedProjAndSites) throws DataAccessException;
}
