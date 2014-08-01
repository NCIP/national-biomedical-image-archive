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
package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImageDTO2;

import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 * @author lethai
 *
 */
public interface InstanceDAO {

    /**
     * Return all the images matching criteria.  
     */
	public List<Object[]> getImages(String sOPInstanceUID, String patientId, String studyInstanceUid, String seriesInstanceUid, List<String> authorizedProjAndSites);
}
