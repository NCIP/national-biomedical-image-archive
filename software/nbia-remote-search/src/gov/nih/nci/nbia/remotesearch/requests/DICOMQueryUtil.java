/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch.requests;

import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;
import gov.nih.nci.ncia.criteria.PersistentCriteria;
import gov.nih.nci.ncia.criteria.NodeCriteria;
import gov.nih.nci.ncia.search.SearchCriteriaDTO;

import java.util.ArrayList;
import java.util.List;

public class DICOMQueryUtil {
	/**
	 * Convert the DICOMQuery object into a format that is more amenable to
	 * going over the grid.
	 */
	public static SearchCriteriaDTO[] createSearchCriteria(DICOMQuery query) {
       
       List<SearchCriteriaDTO> dtos = new ArrayList<SearchCriteriaDTO>();

       for (PersistentCriteria criteria : query.getPersistentCriteria()) {
    	   if(criteria instanceof NodeCriteria) {
    		   continue;
    	   }
           for (QueryAttributeWrapper attr : criteria.getQueryAttributes()) {
        	   SearchCriteriaDTO searchCriteriaDTO = new SearchCriteriaDTO();
        	   searchCriteriaDTO.setType(attr.getCriteriaClassName());
        	   searchCriteriaDTO.setValue(attr.getAttributeValue());
        	   searchCriteriaDTO.setSubType(attr.getSubAttributeName());
               dtos.add(searchCriteriaDTO);
           }
       }
       return dtos.toArray(new SearchCriteriaDTO[]{});
   }
}
