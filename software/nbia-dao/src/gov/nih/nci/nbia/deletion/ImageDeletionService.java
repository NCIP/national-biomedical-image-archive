/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

import java.util.List;
import java.util.Map;

/**
 * This is an interface for Image Deletion Service. It declares API for this service.
 * @author zhoujim
 *
 */
public interface ImageDeletionService {
	/*
	 * Remove all series which are marked as deleted
	 */
	public Map<String, List<String>> removeSeries(String str) throws Exception;

	
	/**
	 * When online deletion page displays, this list will hold all information
	 * that need to display on GUI
	 * @return
	 */
	public List<DeletionDisplayObject> getDeletionDisplayObject();
	/**
	 * Retrieve all series that will be removed in deletion process.
	 * @return List<Integer> allSeries
	 */
	public List<Integer> getAllDeletedSeries();
}
