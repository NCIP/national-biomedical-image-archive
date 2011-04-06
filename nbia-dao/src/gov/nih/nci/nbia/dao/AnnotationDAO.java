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
