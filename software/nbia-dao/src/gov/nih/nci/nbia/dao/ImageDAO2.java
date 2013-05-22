/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImageDTO2;

import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 * @author lethai
 *
 */
public interface ImageDAO2 {

    /**
     * Return all the images for a given series.  Optionally exclude
     * sop instance uid's from the returned list.
     */
    public List<ImageDTO2> findImagesBySeriesUid(String seriesUid,
    		                                    String exclusionSopUidList) throws DataAccessException;

}
