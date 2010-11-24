/**
 *
 */
package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImageDTO;

import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 * @author lethai
 *
 */
public interface ImageDAO {

    /**
     * Return all the images for a given series.  Optionally exclude
     * sop instance uid's from the returned list.
     */
    public List<ImageDTO> findImagesBySeriesUid(String seriesUid,
    		                                    String exclusionSopUidList) throws DataAccessException;

}
