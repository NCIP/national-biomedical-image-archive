/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.AnnotationDTO;
import gov.nih.nci.nbia.dto.AnnotationFileDTO;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface AnnotationDAO  {


    public Map<Integer, List<AnnotationFileDTO>> findKeyedAnnotationBySeriesPkId(List<Integer> seriesIds) throws DataAccessException;
       

	public List<AnnotationFileDTO> findAnnotationBySeriesPkId(List<Integer> seriesPkIds) throws DataAccessException;
       
	
	public List<AnnotationDTO> findAnnotationBySeriesUid(String seriesInstanceUid) throws DataAccessException;

}
