/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion.dao;

import gov.nih.nci.nbia.exception.DataAccessException;
import gov.nih.nci.nbia.internaldomain.GeneralImage;

import java.util.List;

public interface ImageDAO {
	public List<String> removeImages(List<Integer> seriesIds)  throws DataAccessException;
	public List<GeneralImage> getImageObject(List<Integer> imageIDs);
	public void deleteImage(List<Integer> seriesIds);
	public void deleteCTImage(List<Integer> seriesIds);
}
