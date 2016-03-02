/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.dto.ImageDTO;

/**
 * This object encapsulates the act of computing the thumbnail URL for a given
 * DICOM image.
 * 
 * <p>Previously, this URL was computed as a side effect of data access..... now
 * it is a side effect of searching.... but because the old implementation depended
 * upon a security flaw in the ThumbnailServer, we didn't need anything like this.
 * 
 * <p>So the remote and local thumbnail resolution can be different without
 * coding this logical in inconvenient places in the search logic.... this
 * interface should hopefully clean things up a little and push the decision
 * making up to a concrete impl in presentation (for local) or grid service (for remote).
 *
 */
public interface ThumbnailURLResolver {
	/**
	 * For a given local DICOM image, return a URL (String) to get to a thumbnail of it.	 
	 */	
	public String resolveThumbnailUrl(ImageDTO imageDto);
}
