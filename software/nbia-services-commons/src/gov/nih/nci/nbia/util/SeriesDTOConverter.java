/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.search.LocalNode;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.ArrayList;
import java.util.List;

public class SeriesDTOConverter {
    public static List<SeriesSearchResult> convert(List<SeriesDTO> dtos) {
    	List<SeriesSearchResult> results = new ArrayList<SeriesSearchResult>();
    	for(SeriesDTO dto : dtos) {
    		SeriesSearchResult result = new SeriesSearchResult();

    		result.setId(dto.getSeriesPkId());
    		result.setSeriesInstanceUid(dto.getSeriesUID());
    		result.setStudyId(dto.getStudyPkId());
    		result.setStudyInstanceUid(dto.getStudyId());
    		result.setNumberImages(dto.getNumberImages());
    		result.setSeriesNumber(dto.getSeriesNumber());
    		result.setManufacturer(dto.getManufacturer());
    		result.setModality(dto.getModality());
    		result.setAnnotated(dto.isAnnotationsFlag());
    		result.setProject(dto.getProject());


    		result.setPatientId(dto.getPatientId());
    		result.setAnnotationsSize(dto.getAnnotationsSize());
    		result.setDescription(dto.getDescription());
    		result.setTotalSizeForAllImagesInSeries(dto.getTotalSizeForAllImagesInSeries());

    		result.associateLocation(LocalNode.getLocalNode());
    		result.setMaxFrameCount(dto.getMaxFrameCount());
    		results.add(result);
    	}
    	return results;
    }

    public static SeriesSearchResult convert(SeriesDTO dto) {
    	List<SeriesDTO> dtos = new ArrayList<SeriesDTO>();
    	dtos.add(dto);
    	List<SeriesSearchResult> results = convert(dtos);
    	return results.get(0);
    }
}
