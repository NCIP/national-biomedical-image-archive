/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.griddao;

import gov.nih.nci.ncia.domain.TrialDataProvenance;

import java.util.Collection;
import java.util.Map;

import org.springframework.dao.DataAccessException;


public interface TrialDataProvenanceDAOInterface {
	public Map<String, TrialDataProvenance> getTDPByPatientId(Collection<String> ids) throws DataAccessException;
	public Map<String, TrialDataProvenance> getTDPByStudyInstanceUID(Collection<String> ids) throws DataAccessException;
	public Map<String, TrialDataProvenance> getTDPBySeriesInstanceUID(Collection<String> ids) throws DataAccessException;
}
