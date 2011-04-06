package gov.nih.nci.ncia.deletion.dao;

import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.ncia.exception.DataAccessException;

import java.util.List;

public interface ImageDAO {
	public List<String> removeImages(List<Integer> seriesIds)  throws DataAccessException;
	public List<GeneralImage> getImageObject(List<Integer> imageIDs);
	public void deleteImage(List<Integer> seriesIds);
	public void deleteCTImage(List<Integer> seriesIds);
}
