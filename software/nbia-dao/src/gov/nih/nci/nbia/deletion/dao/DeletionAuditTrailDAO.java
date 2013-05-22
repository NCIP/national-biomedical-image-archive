/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion.dao;

import java.util.List;

import gov.nih.nci.nbia.deletion.DeletionAuditPatientInfo;
import gov.nih.nci.nbia.deletion.DeletionAuditSeriesInfo;
import gov.nih.nci.nbia.deletion.DeletionAuditStudyInfo;
import gov.nih.nci.nbia.exception.DataAccessException;

public interface DeletionAuditTrailDAO {

	public void recordSeries(List<DeletionAuditSeriesInfo> seriesInfos, String userName) throws DataAccessException;
	public void recordStudy(DeletionAuditStudyInfo study, String userName) throws DataAccessException;
	public void recordPatient(DeletionAuditPatientInfo patient, String userName) throws DataAccessException;

}
