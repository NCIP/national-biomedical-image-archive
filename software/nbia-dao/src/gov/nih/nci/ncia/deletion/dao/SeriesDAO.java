package gov.nih.nci.ncia.deletion.dao;

import gov.nih.nci.ncia.deletion.DeletionAuditSeriesInfo;
import gov.nih.nci.ncia.deletion.DeletionDisplayObject;
import gov.nih.nci.ncia.exception.DataAccessException;

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
