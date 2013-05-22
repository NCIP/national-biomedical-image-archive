/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.dto.ImageSecurityDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface ImageDAO {
	
	/**
	 * Copied down from ImageServlet in ncia-web and made generic
	 */
    public Collection<ImageSecurityDTO> findImageSecurityBySeriesInstanceUID(String seriesInstanceUid) throws DataAccessException; 

    
	public String findImagePath(Integer imagePkId) throws DataAccessException; 

	
	public ImageSecurityDTO findImageSecurity(String imageSopInstanceUid) throws DataAccessException; 

	public Collection<String> findDistinctConvolutionKernels() throws DataAccessException; 

	public Collection<String> findAllImageType();
    public Map<Integer, List<ImageDTO>> findKeyedImagesBySeriesPkId(List<Integer> seriesIds)  throws DataAccessException; 


    /**
     * Retrieve the maximum curation timestamp from the database
     */
    public Date findLastCurationDate()  throws DataAccessException; 


	public List<ImageDTO> findImagesbySeriesDTO(SeriesDTO seriesDTO) throws DataAccessException; 


	public List<ImageDTO> findImagesbySeriesPkID(Integer seriesPkId) throws DataAccessException; 

	public List<ImageDTO> findImagesbySeriesPkID(Collection<Integer> seriesPkIds) throws DataAccessException; 
	
	public List<ImageDTO> findImagesbySeriesInstandUid(Collection<String> seriesInstanceUids) throws DataAccessException; 
	
	
	public ImageDTO getGeneralImageByImagePkId(Integer imagePkId)  throws DataAccessException;

}
