package gov.nih.nci.ncia.dao;

import gov.nih.nci.ncia.dto.AnnotationDTO;
import gov.nih.nci.ncia.dto.AnnotationFileDTO;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface AnnotationDAO  {


    public Map<Integer, List<AnnotationFileDTO>> findKeyedAnnotationBySeriesPkId(List<Integer> seriesIds) throws DataAccessException;
       

	public List<AnnotationFileDTO> findAnnotationBySeriesPkId(List<Integer> seriesPkIds) throws DataAccessException;
       
	
	public List<AnnotationDTO> findAnnotationBySeriesUid(String seriesInstanceUid) throws DataAccessException;

}