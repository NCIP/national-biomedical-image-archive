/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.StudyNumberDTO;

import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface StudyNumberDAO {

	public Map<Integer, StudyNumberDTO> findAllStudyNumbers() throws DataAccessException;

}
