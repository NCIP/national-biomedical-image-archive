/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.griddao;

import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ImageDAOInterface {
	public List<ZippingDTO> getImagesByNthStudyTimePointForPatient(String patientId,
            Date dateForTimepoint) throws Exception;
	public Map<String, String> getImagesFiles(
			List<String> sbSOPInstanceUIDList) throws Exception;
	public Map<String, String> getImagesFilesByPatientId(String patientId) throws Exception;
	public Map<String, String> getImagesFilesByStudyInstanceUID(
			String studyInstanceUID) throws Exception;
	public Map<String, String> getImagesFilesBySeriesInstanceUID(
			String seriesInstanceUID) throws Exception;
	public List<ZippingDTO> getImageFilesByPatientIds(List<String> patientIds)throws Exception;
	public List<ZippingDTO> getImageFilesByStudiesUids(List<String> studyInstanceUids) throws Exception;
	public List<ZippingDTO> getImageFilesBySeriesUids(List<String> seriesInstanceUids) throws Exception;
	public Image getRepresentativeImageBySeries(String seriesInstanceUID)throws Exception;
	

}
