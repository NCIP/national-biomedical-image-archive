/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion.dao;

import gov.nih.nci.nbia.deletion.DeletionAuditSeriesInfo;
import gov.nih.nci.nbia.deletion.DeletionDisplayObject;
import gov.nih.nci.nbia.exception.DataAccessException;

import java.util.List;
import java.util.Map;

public interface SeriesDAO {
	public List<Integer> listAllDeletedSeries() throws DataAccessException;
	public void removeSeries(List<Integer> seriesObjects)throws DataAccessException;
	public Map<Integer, Integer> getPatientMap(List<Integer> seriesList);
	public Map<Integer, Integer> getStudyMap(List<Integer> seriesList);
	public List<DeletionDisplayObject> getDeletionDisplayObjectDTO(List<Integer> seriesList);
	public List<DeletionAuditSeriesInfo> getDeletionAuditSeriesInfo(List<Integer> seriesList);
}
